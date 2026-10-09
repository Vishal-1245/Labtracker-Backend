package com.college.labtracker.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.college.labtracker.entity.Equipment;
import com.college.labtracker.entity.EquipmentCategory;
import com.college.labtracker.entity.EquipmentStatus;
import com.college.labtracker.entity.Experiment;
import com.college.labtracker.entity.ExperimentComponent;
import com.college.labtracker.entity.ExperimentStep;
import com.college.labtracker.entity.Lab;
import com.college.labtracker.entity.Role;
import com.college.labtracker.entity.User;
import com.college.labtracker.repository.ComponentRepository;
import com.college.labtracker.repository.EquipmentRepository;
import com.college.labtracker.repository.ExperimentComponentRepository;
import com.college.labtracker.repository.ExperimentRepository;
import com.college.labtracker.repository.ExperimentStepRepository;
import com.college.labtracker.repository.LabRepository;
import com.college.labtracker.entity.ExperimentFormula;
import com.college.labtracker.repository.ExperimentFormulaRepository;
import com.college.labtracker.repository.UserRepository;

// CommandLineRunner seeding database from plain CSV files located in classpath:/seed/
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final LabRepository labRepository;
    private final ComponentRepository componentRepository;
    private final EquipmentRepository equipmentRepository;
    private final ExperimentRepository experimentRepository;
    private final ExperimentStepRepository stepRepository;
    private final ExperimentComponentRepository expComponentRepository;
    private final ExperimentFormulaRepository formulaRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            LabRepository labRepository,
            ComponentRepository componentRepository,
            EquipmentRepository equipmentRepository,
            ExperimentRepository experimentRepository,
            ExperimentStepRepository stepRepository,
            ExperimentComponentRepository expComponentRepository,
            ExperimentFormulaRepository formulaRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.labRepository = labRepository;
        this.componentRepository = componentRepository;
        this.equipmentRepository = equipmentRepository;
        this.experimentRepository = experimentRepository;
        this.stepRepository = stepRepository;
        this.expComponentRepository = expComponentRepository;
        this.formulaRepository = formulaRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedLabs();
        seedComponents();
        seedEquipment();
        seedExperiments();
        seedExperimentSteps();
        seedExperimentComponents();
        seedFormulas();
        seedUsers();
    }

    // Helper: read and parse CSV lines with plain Java (respecting quotes and commas)
    private List<List<String>> readCsv(String resourcePath) {
        List<List<String>> rows = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                log.warn("CSV seed resource not found: {}", resourcePath);
                return rows;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                boolean firstLine = true;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    if (firstLine) {
                        firstLine = false; // skip header line
                        continue;
                    }
                    List<String> values = parseCsvLine(line);
                    if (!values.isEmpty()) {
                        rows.add(values);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse CSV file: " + resourcePath, e);
        }
        return rows;
    }

    // Plain Java CSV parser that supports quotes containing commas
    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++; // skip escaped quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                values.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        values.add(sb.toString().trim());
        return values;
    }

    private void seedLabs() {
        if (labRepository.count() == 0) {
            log.info("Labs table is empty. Seeding from seed/labs.csv...");
            List<List<String>> rows = readCsv("seed/labs.csv");
            for (List<String> row : rows) {
                if (row.size() >= 3) {
                    String name = row.get(0);
                    String roomNo = row.get(1);
                    int slotCapacity = Integer.parseInt(row.get(2));
                    labRepository.save(new Lab(name, roomNo, slotCapacity));
                }
            }
            log.info("Seeded {} departmental labs.", labRepository.count());
        }
    }

    private void seedComponents() {
        if (componentRepository.count() == 0) {
            log.info("Components table is empty. Seeding from seed/components.csv...");
            List<List<String>> rows = readCsv("seed/components.csv");
            for (List<String> row : rows) {
                if (row.size() >= 5) {
                    String roomNo = row.get(0);
                    String name = row.get(1);
                    String category = row.get(2);
                    int total = Integer.parseInt(row.get(3));
                    int available = Integer.parseInt(row.get(4));

                    labRepository.findByRoomNo(roomNo).ifPresent(lab -> {
                        componentRepository.save(new com.college.labtracker.entity.Component(lab, name, category, total, available));
                    });
                }
            }
            log.info("Seeded {} electronic components.", componentRepository.count());
        }
    }

    private void seedEquipment() {
        if (equipmentRepository.count() == 0) {
            log.info("Equipment table is empty. Seeding from seed/equipment.csv...");
            List<List<String>> rows = readCsv("seed/equipment.csv");
            for (List<String> row : rows) {
                if (row.size() >= 6) {
                    String assetTag = row.get(0);
                    String name = row.get(1);
                    EquipmentCategory category = EquipmentCategory.valueOf(row.get(2));
                    String roomNo = row.get(3);
                    EquipmentStatus status = EquipmentStatus.valueOf(row.get(4));
                    LocalDate calibDate = LocalDate.parse(row.get(5));

                    labRepository.findByRoomNo(roomNo).ifPresent(lab -> {
                        equipmentRepository.save(new Equipment(assetTag, name, category, lab, status, calibDate));
                    });
                }
            }
            log.info("Seeded {} laboratory equipment assets.", equipmentRepository.count());
        }
    }

    private void seedExperiments() {
        List<List<String>> rows = readCsv("seed/experiments.csv");
        for (List<String> row : rows) {
            if (row.size() >= 8) {
                String roomNo = row.get(0);
                String code = row.get(1);
                String title = row.get(2);
                String aim = row.get(3);
                String theory = row.get(4);
                String observationFormat = row.get(5);
                String conclusionNote = row.get(6);
                String safetyNotes = row.get(7);

                if (!experimentRepository.existsByCode(code)) {
                    labRepository.findByRoomNo(roomNo).ifPresent(lab -> {
                        experimentRepository.save(new Experiment(lab, code, title, aim, theory, observationFormat, conclusionNote, safetyNotes));
                    });
                }
            }
        }
    }

    private void seedExperimentSteps() {
        List<List<String>> rows = readCsv("seed/experiment_steps.csv");
        for (List<String> row : rows) {
            if (row.size() >= 3) {
                String expCode = row.get(0);
                int stepNo = Integer.parseInt(row.get(1));
                String instruction = row.get(2);

                experimentRepository.findByCode(expCode).ifPresent(exp -> {
                    boolean exists = stepRepository.findByExperimentIdOrderByStepNoAsc(exp.getId()).stream()
                            .anyMatch(s -> s.getStepNo() == stepNo);
                    if (!exists) {
                        stepRepository.save(new ExperimentStep(exp, stepNo, instruction));
                    }
                });
            }
        }
    }

    private void seedExperimentComponents() {
        List<List<String>> rows = readCsv("seed/experiment_components.csv");
        for (List<String> row : rows) {
            if (row.size() >= 3) {
                String expCode = row.get(0);
                String compName = row.get(1);
                int qty = Integer.parseInt(row.get(2));

                experimentRepository.findByCode(expCode).ifPresent(exp -> {
                    componentRepository.findFirstByName(compName).ifPresent(comp -> {
                        boolean exists = expComponentRepository.findByExperimentId(exp.getId()).stream()
                                .anyMatch(ec -> ec.getComponent().getId().equals(comp.getId()));
                        if (!exists) {
                            expComponentRepository.save(new ExperimentComponent(exp, comp, qty));
                        }
                    });
                });
            }
        }
    }

    private void seedFormulas() {
        if (formulaRepository.count() == 0) {
            log.info("Experiment formulas table is empty. Seeding analytical formulas...");
            experimentRepository.findByCode("ECE-EDC-01").ifPresent(exp -> {
                formulaRepository.save(new ExperimentFormula(exp, "Dynamic Resistance", "(deltaV) / (deltaI)", "deltaV,deltaI", "Ω"));
                formulaRepository.save(new ExperimentFormula(exp, "Static Resistance", "V / I", "V,I", "Ω"));
            });
            experimentRepository.findByCode("ECE-EDC-02").ifPresent(exp -> {
                formulaRepository.save(new ExperimentFormula(exp, "Ripple Factor", "sqrt((Vrms / Vdc)^2 - 1)", "Vrms,Vdc", ""));
                formulaRepository.save(new ExperimentFormula(exp, "Rectifier Efficiency", "(Pdc / Pac) * 100", "Pdc,Pac", "%"));
            });
            experimentRepository.findByCode("ECE-DSP-01").ifPresent(exp -> {
                formulaRepository.save(new ExperimentFormula(exp, "Linear Convolution Length", "N1 + N2 - 1", "N1,N2", "samples"));
            });
            experimentRepository.findByCode("ECE-VLSI-01").ifPresent(exp -> {
                formulaRepository.save(new ExperimentFormula(exp, "Average Propagation Delay", "(tpHL + tpLH) / 2", "tpHL,tpLH", "ns"));
                formulaRepository.save(new ExperimentFormula(exp, "Static Power Dissipation", "Vdd * Istatic", "Vdd,Istatic", "µW"));
            });
            experimentRepository.findByCode("ECE-VLSI-02").ifPresent(exp -> {
                formulaRepository.save(new ExperimentFormula(exp, "Critical Path Delay", "4 * tgate", "tgate", "ns"));
            });
            log.info("Seeded {} experiment formulas.", formulaRepository.count());
        }
    }

    private void seedUsers() {
        List<List<String>> rows = readCsv("seed/users.csv");
        for (List<String> row : rows) {
            if (row.size() >= 7) {
                String username = row.get(0);
                String password = row.get(1);
                String fullName = row.get(2);
                String email = row.get(3);
                Role role = Role.valueOf(row.get(4));
                boolean active = Boolean.parseBoolean(row.get(5));
                boolean mustChangePassword = Boolean.parseBoolean(row.get(6));

                if (!userRepository.existsByUsername(username)) {
                    userRepository.save(new User(
                            username,
                            passwordEncoder.encode(password),
                            fullName,
                            email,
                            role,
                            active,
                            mustChangePassword
                    ));
                }
            }
        }
    }
}

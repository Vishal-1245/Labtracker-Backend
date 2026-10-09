package com.college.labtracker;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.jayway.jsonpath.JsonPath;

// Comprehensive integration test suite validating Phase 2 backend features and failure scenarios
@SpringBootTest
public class Phase2BackendIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private String adminToken;
    private String inchargeToken;
    private String studentToken;

    @BeforeEach
    void setup() throws Exception {
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .apply(springSecurity())
                    .build();
        }

        if (adminToken == null) {
            adminToken = obtainToken("admin", "admin123");
            inchargeToken = obtainToken("incharge_edc", "incharge123");
            studentToken = obtainToken("24eg104e01", "student123");
        }
    }

    // Helper: Obtain JWT token for given credentials
    private String obtainToken(String username, String password) throws Exception {
        String payload = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    // 1. College Context Tests
    @Test
    @DisplayName("GET /api/config/public - Public institutional configuration accessible without token")
    void testPublicConfigEndpoint() throws Exception {
        mockMvc.perform(get("/api/config/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collegeName", is("Anurag University")))
                .andExpect(jsonPath("$.departmentName", is("Department of Electronics and Communication Engineering")))
                .andExpect(jsonPath("$.rollNoPattern").exists());
    }

    // 2. Auth & Password Change Tests
    @Test
    @DisplayName("POST /api/auth/login - Returns mustChangePassword flag for newly provisioned student")
    void testLoginMustChangePasswordFlag() throws Exception {
        String payload = "{\"username\":\"24eg104e29\",\"password\":\"Student@123\"}";
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("24eg104e29")))
                .andExpect(jsonPath("$.mustChangePassword", is(true)));
    }

    @Test
    @DisplayName("POST /api/auth/change-password - Failure on incorrect current password")
    void testChangePasswordFailureInvalidOldPassword() throws Exception {
        String token = obtainToken("24eg104e29", "Student@123");
        String payload = "{\"oldPassword\":\"WrongPassword\",\"newPassword\":\"NewStudent@456\"}";

        mockMvc.perform(post("/api/auth/change-password")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/change-password - Success updates password and clears mustChangePassword")
    void testChangePasswordSuccess() throws Exception {
        String token = obtainToken("24eg104e29", "Student@123");
        String payload = "{\"oldPassword\":\"Student@123\",\"newPassword\":\"NewStudent@456\"}";

        mockMvc.perform(post("/api/auth/change-password")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Password changed successfully")));

        // Verify login with new password and flag reset
        String verifyPayload = "{\"username\":\"24eg104e29\",\"password\":\"NewStudent@456\"}";
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(verifyPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mustChangePassword", is(false)));

        // Reset password back for subsequent runs
        String newToken = obtainToken("24eg104e29", "NewStudent@456");
        String resetPayload = "{\"oldPassword\":\"NewStudent@456\",\"newPassword\":\"Student@123\"}";
        mockMvc.perform(post("/api/auth/change-password")
                .header("Authorization", "Bearer " + newToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(resetPayload))
                .andExpect(status().isOk());
    }

    // 3. Roll Number Pattern & Bulk Import Tests
    @Test
    @DisplayName("POST /api/users - Reject student user with invalid roll number format")
    void testCreateStudentInvalidRollNo() throws Exception {
        String payload = "{"
                + "\"username\":\"INVALID_ROLL_123\","
                + "\"password\":\"pass123\","
                + "\"fullName\":\"Test Student\","
                + "\"email\":\"invalid@anurag.edu.in\","
                + "\"role\":\"STUDENT\","
                + "\"active\":true"
                + "}";

        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/users/import - Admin bulk import CSV with duplicate skipping")
    void testBulkUserImport() throws Exception {
        String csvContent = "rollNo,fullName,email\n"
                + "24eg104e01,A. Sai Teja Duplicate,existing@anurag.edu.in\n"
                + "24eg104e50,D. Harish Kumar,24eg104e50@anurag.edu.in\n"
                + "INVALID_ROLL,Bad Roll Student,bad@anurag.edu.in\n";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "students.csv",
                "text/csv",
                csvContent.getBytes()
        );

        mockMvc.perform(multipart("/api/users/import")
                .file(file)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.created", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.skipped", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("POST /api/users/import - Forbidden for STUDENT role")
    void testBulkUserImportForbiddenForStudent() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "rollNo,fullName,email\n".getBytes());
        mockMvc.perform(multipart("/api/users/import")
                .file(file)
                .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isForbidden());
    }

    // 4. Departmental Labs Tests
    @Test
    @DisplayName("GET /api/labs - Authenticated user can view all seeded laboratories")
    void testGetAllLabs() throws Exception {
        mockMvc.perform(get("/api/labs")
                .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].roomNo").exists());
    }

    // 5. Experiments & Curriculum Manuals Tests
    @Test
    @DisplayName("GET /api/experiments - Retrieve experiments with steps and component requirements")
    void testGetExperimentsWithSteps() throws Exception {
        mockMvc.perform(get("/api/experiments/1")
                .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("ECE-EDC-01")))
                .andExpect(jsonPath("$.steps", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.components", hasSize(greaterThanOrEqualTo(1))));
    }

    // 6. Equipment Inventory & Maintenance Lifecycle Tests
    @Test
    @DisplayName("POST /api/equipment/{id}/maintenance and resolve workflow")
    void testEquipmentMaintenanceFlow() throws Exception {
        // Report fault on equipment asset 1
        String reportPayload = "{\"issue\":\"Trace flicker on channel 1 of DSO\"}";
        MvcResult reportResult = mockMvc.perform(post("/api/equipment/1/maintenance")
                .header("Authorization", "Bearer " + studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reportPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resolved", is(false)))
                .andReturn();

        Long logId = ((Number) JsonPath.read(reportResult.getResponse().getContentAsString(), "$.id")).longValue();

        // Verify equipment status changed to UNDER_MAINTENANCE
        mockMvc.perform(get("/api/equipment/1")
                .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UNDER_MAINTENANCE")));

        // Resolve fault by lab incharge
        mockMvc.perform(put("/api/equipment/maintenance/" + logId + "/resolve")
                .header("Authorization", "Bearer " + inchargeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resolved", is(true)));

        // Verify equipment restored to AVAILABLE
        mockMvc.perform(get("/api/equipment/1")
                .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("AVAILABLE")));
    }

    // 7. Component Stock, Issue, and Return Reconciliation Tests
    @Test
    @DisplayName("POST /api/components/issue - Fails with 400 InsufficientStockException when quantity exceeds stock")
    void testComponentIssueInsufficientStock() throws Exception {
        // Request 9999 units
        String payload = "{\"teamId\":1,\"componentId\":1,\"quantity\":99999}";
        mockMvc.perform(post("/api/components/issue")
                .header("Authorization", "Bearer " + inchargeToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    // 8. Slot Booking Validation & Conflict Checking Tests
    @Test
    @DisplayName("POST /api/bookings - Fails with 400 when start time is not before end time")
    void testBookingInvalidTimeBounds() throws Exception {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String payload = "{"
                + "\"labId\":1,"
                + "\"bookingDate\":\"" + tomorrow + "\","
                + "\"startTime\":\"14:00:00\","
                + "\"endTime\":\"12:00:00\","
                + "\"purpose\":\"Testing time bounds failure\""
                + "}";

        mockMvc.perform(post("/api/bookings")
                .header("Authorization", "Bearer " + studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/bookings - Fails with 400 when booking date is in the past")
    void testBookingPastDate() throws Exception {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String payload = "{"
                + "\"labId\":1,"
                + "\"bookingDate\":\"" + yesterday + "\","
                + "\"startTime\":\"10:00:00\","
                + "\"endTime\":\"12:00:00\","
                + "\"purpose\":\"Testing past date failure\""
                + "}";

        mockMvc.perform(post("/api/bookings")
                .header("Authorization", "Bearer " + studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/bookings - Fails with 409 SlotAlreadyBookedException when equipment is double-booked")
    void testEquipmentDoubleBookingConflict() throws Exception {
        LocalDate targetDate = LocalDate.now().plusDays(5);
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(12, 0);

        // First student booking equipment 2
        String payload1 = "{"
                + "\"labId\":1,"
                + "\"equipmentId\":2,"
                + "\"bookingDate\":\"" + targetDate + "\","
                + "\"startTime\":\"" + start + "\","
                + "\"endTime\":\"" + end + "\","
                + "\"purpose\":\"First reservation\""
                + "}";

        mockMvc.perform(post("/api/bookings")
                .header("Authorization", "Bearer " + studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload1))
                .andExpect(status().isCreated());

        // Second student attempting to book same equipment 2 for overlapping slot
        String payload2 = "{"
                + "\"labId\":1,"
                + "\"equipmentId\":2,"
                + "\"bookingDate\":\"" + targetDate + "\","
                + "\"startTime\":\"11:00:00\","
                + "\"endTime\":\"13:00:00\","
                + "\"purpose\":\"Conflicting reservation\""
                + "}";

        mockMvc.perform(post("/api/bookings")
                .header("Authorization", "Bearer " + studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload2))
                .andExpect(status().isConflict());
    }
}

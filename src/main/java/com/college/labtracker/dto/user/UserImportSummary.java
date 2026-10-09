package com.college.labtracker.dto.user;

import java.util.ArrayList;
import java.util.List;

// Summary DTO returned after CSV bulk user import
public class UserImportSummary {

    private int created;
    private int skipped;
    private List<String> errors;

    public UserImportSummary() {
        this.errors = new ArrayList<>();
    }

    public UserImportSummary(int created, int skipped, List<String> errors) {
        this.created = created;
        this.skipped = skipped;
        this.errors = errors != null ? errors : new ArrayList<>();
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

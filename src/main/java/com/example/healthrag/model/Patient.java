package com.example.healthrag.model;

import java.util.List;

public record Patient(
        String patientId,
        String name,
        int age,
        List<String> conditions,
        List<String> medications,
        List<Visit> visits
) {
    public record Visit(
            String date,
            String reason,
            String notes
    ) {}
}

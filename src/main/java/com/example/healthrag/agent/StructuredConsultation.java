package com.example.healthrag.agent;

import java.util.List;

public record StructuredConsultation(
        String patientSummary,
        List<String> activeConditions,
        List<String> currentMedications,
        List<String> relevantFindings,
        List<String> clinicalRecommendations,
        String riskLevel
) {}

package com.example.healthrag.agent;

import com.example.healthrag.retrieval.PatientContextService;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PatientTools {

    private final PatientContextService contextService;

    public PatientTools(PatientContextService contextService) {
        this.contextService = contextService;
    }

    @Tool(description = "Retrieves the complete medical history for a patient. Use this when you need a full overview of a patient's profile, conditions, medications, and all past visits.")
    public String getPatientHistory(String patientId) {
        List<Document> documents = contextService.getPatientHistory(patientId);
        return contextService.assembleContext(documents);
    }

    @Tool(description = "Finds patient history chunks that are semantically relevant to specific symptoms or clinical concerns. Use this when you need focused context for a specific complaint rather than the full history.")
    public String findRelevantHistory(String patientId, String symptoms) {
        List<Document> documents = contextService.findRelevantHistory(patientId, symptoms);
        return contextService.assembleContext(documents);
    }
}

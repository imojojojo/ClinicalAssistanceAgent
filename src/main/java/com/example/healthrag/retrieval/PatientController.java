package com.example.healthrag.retrieval;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientContextService contextService;

    public PatientController(PatientContextService contextService) {
        this.contextService = contextService;
    }

    @GetMapping("/{patientId}/history")
    public PatientHistoryResponse getHistory(@PathVariable String patientId) {
        List<Document> documents = contextService.getPatientHistory(patientId);
        return new PatientHistoryResponse(
                patientId,
                documents.size(),
                documents.stream().map(Document::getText).toList()
        );
    }

    @GetMapping("/{patientId}/context")
    public PatientContextResponse getContext(
            @PathVariable String patientId,
            @RequestParam String symptoms) {

        List<Document> documents = contextService.findRelevantHistory(patientId, symptoms);
        return new PatientContextResponse(
                patientId,
                symptoms,
                documents.size(),
                contextService.assembleContext(documents)
        );
    }
}

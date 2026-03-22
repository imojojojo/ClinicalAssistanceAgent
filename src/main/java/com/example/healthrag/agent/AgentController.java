package com.example.healthrag.agent;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultation")
public class AgentController {

    private final ClinicalAgent clinicalAgent;

    public AgentController(ClinicalAgent clinicalAgent) {
        this.clinicalAgent = clinicalAgent;
    }

    @PostMapping
    public ConsultationResponse consult(@RequestBody ConsultationRequest request) {
        if (request.patientId() == null || request.patientId().isBlank()) {
            throw new IllegalArgumentException("patientId must not be blank");
        }
        
        if (request.question() == null || request.question().isBlank()) {
            throw new IllegalArgumentException("question must not be blank");
        }
        StructuredConsultation consultation = clinicalAgent.consult(request.patientId(), request.question());
        return new ConsultationResponse(request.patientId(), request.question(), consultation);
    }
}

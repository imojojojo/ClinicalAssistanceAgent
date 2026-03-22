package com.example.healthrag.agent;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final ClinicalAgent clinicalAgent;

    public AgentController(ClinicalAgent clinicalAgent) {
        this.clinicalAgent = clinicalAgent;
    }

    @PostMapping("/consult")
    public ConsultationResponse consult(@RequestBody ConsultationRequest request) {
        StructuredConsultation consultation = clinicalAgent.consult(request.patientId(), request.question());
        return new ConsultationResponse(request.patientId(), request.question(), consultation);
    }
}

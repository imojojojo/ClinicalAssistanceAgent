package com.example.healthrag.agent;

public record ConsultationResponse(String patientId, String question, StructuredConsultation consultation) {}

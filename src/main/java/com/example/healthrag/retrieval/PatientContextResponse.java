package com.example.healthrag.retrieval;

public record PatientContextResponse(String patientId, String symptoms, int chunksFound, String assembledContext) {}

package com.example.healthrag.retrieval;

import java.util.List;

public record PatientHistoryResponse(String patientId, int chunksFound, List<String> chunks) {}

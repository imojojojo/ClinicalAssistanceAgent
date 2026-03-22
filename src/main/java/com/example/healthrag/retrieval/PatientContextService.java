package com.example.healthrag.retrieval;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientContextService {

    private final VectorStore vectorStore;

    public PatientContextService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    // Method A: Fetch ALL chunks for a patient (by metadata filter, no semantic query)
    // Use this to get a complete picture of a patient's history
    public List<Document> getPatientHistory(String patientId) {
        var filter = new FilterExpressionBuilder()
                .eq("patientId", patientId)
                .build();

        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(patientId)          // dummy query — filter does the work
                        .topK(20)                  // retrieve up to 20 chunks
                        .filterExpression(filter)
                        .similarityThreshold(0.0)  // no threshold — return all matches
                        .build()
        );
    }

    // Method B: Semantic search — find history relevant to specific symptoms
    // Combines a patientId filter with a semantic query on symptom text
    public List<Document> findRelevantHistory(String patientId, String symptoms) {
        var filter = new FilterExpressionBuilder()
                .eq("patientId", patientId)
                .build();

        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(symptoms)           // semantic query on symptom description
                        .topK(5)                   // top 5 most relevant chunks
                        .filterExpression(filter)
                        .similarityThreshold(0.5)  // only return reasonably relevant results
                        .build()
        );
    }

    // Assembles retrieved documents into a clean context string for the LLM
    public String assembleContext(List<Document> documents) {
        if (documents.isEmpty()) {
            return "No relevant patient history found.";
        }

        StringBuilder sb = new StringBuilder();
        for (Document doc : documents) {
            sb.append(doc.getText()).append("\n---\n");
        }
        return sb.toString().trim();
    }
}

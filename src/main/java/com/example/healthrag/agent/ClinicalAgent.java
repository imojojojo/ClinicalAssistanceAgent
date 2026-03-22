package com.example.healthrag.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ClinicalAgent {

    private final ChatClient chatClient;

    public ClinicalAgent(ChatClient.Builder builder, PatientTools patientTools) {
        this.chatClient = builder
                .defaultTools(patientTools)
                .defaultSystem("""
                        You are a clinical decision support assistant.
                        You have access to tools that retrieve patient medical history from a vector database.
                        Always use the appropriate tool to fetch patient data before answering.
                        Be concise, medically precise, and cite the patient data you retrieved.
                        """)
                .build();
    }

    public StructuredConsultation consult(String patientId, String question) {
        String prompt = String.format("Patient ID: %s\n\nQuestion: %s", patientId, question);
        return chatClient.prompt(prompt).call().entity(StructuredConsultation.class);
    }
}

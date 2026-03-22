package com.example.healthrag.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ClinicalAgent {

    private static final Logger log = LoggerFactory.getLogger(ClinicalAgent.class);

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
        log.info("Consulting agent for patient {} — question: {}", patientId, question);
        String prompt = String.format("Patient ID: %s\n\nQuestion: %s", patientId, question);
        StructuredConsultation result = chatClient.prompt(prompt).call().entity(StructuredConsultation.class);
        log.info("Agent consultation complete for patient {}", patientId);
        return result;
    }
}

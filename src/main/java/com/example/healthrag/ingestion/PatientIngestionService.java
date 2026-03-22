package com.example.healthrag.ingestion;

import com.example.healthrag.model.Patient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PatientIngestionService {

    private static final Logger log = LoggerFactory.getLogger(PatientIngestionService.class);

    private final VectorStore vectorStore;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PatientIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int ingestAll() throws Exception {
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:patients/*.json");

        List<Document> documents = new ArrayList<>();

        for (Resource resource : resources) {
            Patient patient = objectMapper.readValue(resource.getInputStream(), Patient.class);
            documents.addAll(toDocuments(patient));
            log.info("Prepared documents for patient {}", patient.patientId());
        }

        vectorStore.add(documents);
        log.info("Ingested {} document chunks into ChromaDB", documents.size());

        return documents.size();
    }

    // Each patient is split into multiple focused chunks:
    // 1. One "profile" chunk: demographics + conditions + medications
    // 2. One chunk per visit
    // Smaller, focused chunks → better retrieval precision
    private List<Document> toDocuments(Patient patient) {
        List<Document> docs = new ArrayList<>();

        // Chunk 1: Patient profile
        String profileText = """
                Patient: %s (ID: %s), Age: %d
                Chronic Conditions: %s
                Current Medications: %s
                """.formatted(
                patient.name(),
                patient.patientId(),
                patient.age(),
                String.join(", ", patient.conditions()),
                String.join(", ", patient.medications())
        );

        docs.add(new Document(profileText, Map.of(
                "patientId", patient.patientId(),
                "chunkType", "profile"
        )));

        // Chunk 2+: One document per visit
        for (Patient.Visit visit : patient.visits()) {
            String visitText = """
                    Patient: %s (ID: %s)
                    Visit Date: %s | Reason: %s
                    Clinical Notes: %s
                    """.formatted(
                    patient.name(),
                    patient.patientId(),
                    visit.date(),
                    visit.reason(),
                    visit.notes()
            );

            docs.add(new Document(visitText, Map.of(
                    "patientId", patient.patientId(),
                    "chunkType", "visit",
                    "visitDate", visit.date(),
                    "visitReason", visit.reason()
            )));
        }

        return docs;
    }
}

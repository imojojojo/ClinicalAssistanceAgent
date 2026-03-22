package com.example.healthrag.ingestion;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final PatientIngestionService ingestionService;

    public AdminController(PatientIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    public IngestionResponse ingest() throws Exception {
        int count = ingestionService.ingestAll();
        return new IngestionResponse("success", count);
    }
}

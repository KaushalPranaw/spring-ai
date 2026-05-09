package com.spring.multimodel.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface DataLoader {
    List<Document> loadDocumentFromJson();
    List<Document> loadDocumentFromPdf();

}

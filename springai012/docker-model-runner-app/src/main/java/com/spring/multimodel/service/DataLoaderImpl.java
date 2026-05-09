package com.spring.multimodel.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoaderImpl implements DataLoader{

    @Value("classpath:simple_data.json")
    private Resource jsonResource;

    @Value("classpath:cricket_rules.pdf")
    private Resource pdfResource;

    @Override
    public List<Document> loadDocumentFromJson() {
        System.out.println("started loading json");

        //reading json from resource
//        var jsonReader=new JsonReader(jsonResource);

        //chaho to keys b pas kr skte h
        var jsonReader=new JsonReader(jsonResource, "projects");

        //now json we are having
        //now using document reader we will read this json
        List<Document> listDocuments= jsonReader.read();
        return listDocuments;
    }

    @Override
    public List<Document> loadDocumentFromPdf() {
        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build()
        );

        return pagePdfDocumentReader.read();
    }
}

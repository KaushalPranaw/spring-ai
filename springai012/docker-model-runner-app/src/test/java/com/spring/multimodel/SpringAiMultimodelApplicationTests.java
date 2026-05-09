package com.spring.multimodel;

import com.spring.multimodel.helper.Helper;
import com.spring.multimodel.service.ChatService;
import com.spring.multimodel.service.DataLoader;
import com.spring.multimodel.service.DataTransformer;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringAiMultimodelApplicationTests {

    @Autowired
    private DataLoader  dataLoader;

    @Autowired
    private DataTransformer dataTransformer;

    @Autowired
    private VectorStore vectorStore;

    /*@Test
    void testDataLoader(){
        List<Document> documents = dataLoader.loadDocumentFromJson();
        System.out.println(documents.size());
        //let check what document comes
        documents.forEach(System.out::println);


    }*/
    @Test
    void testPdfDataLoader(){
        List<Document> documents = dataLoader.loadDocumentFromPdf();
        System.out.println(documents.size());
        //let check what document comes
        documents.forEach(item->{
            System.out.println(item);
            System.out.println("---------------------");
        });

        System.out.println("read..... now going to transform");

        List<Document> transformDocument = dataTransformer.transform(documents);
        System.out.println(transformDocument.size());

        System.out.println("going to save data into databse");
        this.vectorStore.add(transformDocument);
        System.out.println("DONE");


    }

}

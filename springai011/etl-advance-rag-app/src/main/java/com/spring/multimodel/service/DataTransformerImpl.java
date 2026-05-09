package com.spring.multimodel.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTransformerImpl implements DataTransformer{
    @Override
    public List<Document> transform(List<Document> documents) {
//        var splitter=TokenTextSplitter.builder().build();
        var splitter=TokenTextSplitter.builder()
                .withChunkSize(300)
                .withMinChunkSizeChars(400)
                .withMinChunkLengthToEmbed(10)
                .withMaxNumChunks(5000)
                .withKeepSeparator(true)
                .build();
        //List<Document> transform = splitter.transform(documents);
        //return transform;

        return splitter.apply(documents);
    }
}

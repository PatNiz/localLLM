package com.niziolekp.controller;

import com.lowagie.text.DocumentException;
import com.niziolekp.model.Book;
import com.niziolekp.model.Chapter;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@RestController
public class OllamaController {
    OllamaApi ollamaApi = new OllamaApi();
    private final ChatClient chatClient;
    private final ChatClient chatClientHtmlCreator;

    public OllamaController(){
        ChatModel chatModel = new OllamaChatModel(ollamaApi, OllamaOptions.create()
                        .withTemperature(0.7)
                        .withModel("llama3.2:latest")); //phi3.5:latest
        this.chatClient = ChatClient.create(chatModel);
        ChatModel htmlChatModel = new OllamaChatModel(this.ollamaApi,
                OllamaOptions.create()
                        .withTemperature(0.5)
                        .withModel("llama3.2:latest"));
        this.chatClientHtmlCreator = ChatClient.create(htmlChatModel);
    }
    @GetMapping("/hello")
    public ResponseEntity<byte[]> get(){
/*        BeanOutputConverter<Book> converter = new BeanOutputConverter<Book>(new ParameterizedTypeReference<Book>() {

        });
        Book book =  chatClient.prompt()
                .system("Jestes pisarzem,piszesz, ksiazki romantyczne")
                .user(u-> u.text("napisz ksiazke (romans) w języku polskim na przynajmniej 5 rozdziałów ").param("format",converter.getFormat()))
                .call()
                .entity(converter);*/

        Book book = new Book();
        List<Chapter> chapterList = new ArrayList<>();

                String chapterContent = chatClient.prompt()
                .system("You are an experienced java programmer")
                .user("Write the first chapter of the book which will be called inheritance in java. Describe inheritance in words only")
                        .call().content();
                Chapter chapter = new Chapter();
                chapter.setChapterContent(chapterContent);
                chapterList.add(chapter);


        String nextChapterContent = chatClient.prompt()
                .system("You are an experienced java programmer")
                .user("Write the first chapter of the book called polymorphism in java. Describe polymorphism in words only")
                .call().content();
        Chapter chapter2 = new Chapter();
        chapter2.setChapterContent(nextChapterContent);
        chapterList.add(chapter2);

        book.setChapterList(chapterList);


        //Structured AI output
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=ebook.pdf");

        String html = chatClientHtmlCreator.prompt()
                .user("modify the body section in this html: " + """
                        <html>
                            <head>
                                <style>
                                    h1 { color: blue, font-size:24px; }
                                    p { font-size:12px; line-height: 1.5; }
                                </style>
                            </head>
                            <body>
                            <h1></h1>
                            <p></p>
                            <p style="color: red;"></p>
                            </body>
                        <html>
                        """ + "to include this content: " + book.toString() + ". Provide Full static HTML output only")
                .call().content();

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            System.out.println("PDF created");
        }catch (DocumentException e){
                e.printStackTrace();
        }
        byte[] pdf = outputStream.toByteArray();

    return ResponseEntity.ok()
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}

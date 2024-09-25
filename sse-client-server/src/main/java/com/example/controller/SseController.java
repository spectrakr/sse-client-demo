package com.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;

import com.example.SseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SseController {

    @PostMapping(value = "/sseClient", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void requestSse(
        @RequestBody SseRequest request,
        HttpServletResponse response
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(request.getPayload());
        StringEntity entity = new StringEntity(payload, StandardCharsets.UTF_8);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost http = new HttpPost(request.getUrl());
            http.setEntity(entity);
            http.setHeader("Content-Type", "application/json");

            response.setContentType("text/event-stream; charset=UTF-8");
            try (CloseableHttpResponse httpResponse = httpClient.execute(http)) {
                try (PrintWriter writer = response.getWriter()) {
                    String line;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                        writer.println(line);
                        writer.flush();
                    }
                }
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
        }
    }
}

package com.example.controller;

import java.util.Map;
import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("webflux")
@RequiredArgsConstructor
public class WebFluxSseController {

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @PostMapping(value = "/sseClient", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> connect(
        @RequestBody SseRequest request
    ) {
        String payload = toJson(request.getPayload());
        Flux<Map<String, Object>> map = webClient.post()
            .uri(request.getUrl())
            .bodyValue(payload)
            .accept(MediaType.TEXT_EVENT_STREAM)
            .retrieve()
            .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorResume(error -> {
                if (error.getMessage().contains("JsonToken.START_ARRAY")) {
                    return Flux.empty();
                } else {
                    return Flux.error(error);
                }
            });

        map.subscribe(System.out::println);
        return map;
    }

    private String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

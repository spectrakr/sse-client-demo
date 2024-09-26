package com.example.controller;


import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SseRequest {

    private String url;

    private Map payload;
}

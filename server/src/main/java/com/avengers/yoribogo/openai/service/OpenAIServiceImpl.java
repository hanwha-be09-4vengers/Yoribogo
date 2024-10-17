package com.avengers.yoribogo.openai.service;

import com.avengers.yoribogo.openai.dto.RequestChatDTO;
import com.avengers.yoribogo.openai.dto.ResponseChatDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    private final RestTemplate restTemplate;

    // 설명. OpenAI와 통신을 위한 RestTemplate의 빈 이름으로 설정
    //  (시큐리티 RestTemplate은 AppConfiguration에 설정되어있음)
    @Autowired
    public OpenAIServiceImpl(@Qualifier("openAIRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseChatDTO getRecommend(String prompt) {
        RequestChatDTO req = new RequestChatDTO(model, prompt);
        return restTemplate.postForObject(url, req, ResponseChatDTO.class);
    }

}

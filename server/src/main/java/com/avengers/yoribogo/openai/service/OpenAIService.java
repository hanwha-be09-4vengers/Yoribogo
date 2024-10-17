package com.avengers.yoribogo.openai.service;

import com.avengers.yoribogo.openai.dto.ResponseChatDTO;

public interface OpenAIService {

    ResponseChatDTO getRecommend(String prompt);

}

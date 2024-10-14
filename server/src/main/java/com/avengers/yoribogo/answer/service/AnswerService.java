package com.avengers.yoribogo.answer.service;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.answer.dto.AnswerDTO;

import java.util.List;

public interface AnswerService {
    List<Answer> findAnswer(int inquiryId);

    Answer insertAnswer(AnswerDTO newAnswer);

    Answer updateAnswer(AnswerDTO modifyAnswer);

    boolean removeAnswer(int id);
}

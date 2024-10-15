package com.avengers.yoribogo.answer.service;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.answer.dto.AnswerDTO;
import com.avengers.yoribogo.answer.repository.AnswerRepository;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.common.exception.ExceptionDTO;
import com.avengers.yoribogo.inquiry.service.InquiryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final InquiryService inquiryService;
    private final ModelMapper modelMapper;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository,
                             @Lazy InquiryService inquiryService,
                             ModelMapper modelMapper) {
        this.answerRepository = answerRepository;
        this.inquiryService = inquiryService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);
    }

    @Override
    public List<Answer> findAnswer(int inquiryId) {
        try {
            return answerRepository.findAll()
                    .stream()
                    .filter(answer -> answer.getInquiryId() == inquiryId)
                    .toList();
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return null;
        }
    }

    @Override
    public Answer insertAnswer(AnswerDTO newAnswer) {
        try {
            newAnswer.setAnswerCreatedAt(LocalDateTime.now());
            Answer result = answerRepository.save(modelMapper.map(newAnswer, Answer.class));
            inquiryService.plusAnswers(newAnswer.getInquiryId());
            return result;
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return null;
        }
    }

    @Override
    public Answer updateAnswer(AnswerDTO modifyAnswer) {
        try {
            modifyAnswer.setAnswerCreatedAt(LocalDateTime.now());
            return answerRepository.saveAndFlush(modelMapper.map(modifyAnswer, Answer.class));
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return null;
        }
    }

    @Override
    public boolean removeAnswer(int id) {
        try {
            int inquiryId = answerRepository.findById(id).get().getInquiryId();
            inquiryService.minusAnswers(inquiryId);
            answerRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return false;
        }
    }
}

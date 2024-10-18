package com.avengers.yoribogo.inquiry.service;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.common.Role;
import com.avengers.yoribogo.common.Status;
import com.avengers.yoribogo.common.Visibility;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.common.exception.ExceptionDTO;
import com.avengers.yoribogo.inquiry.domain.Inquiry;
import com.avengers.yoribogo.inquiry.dto.InquiryDTO;
import com.avengers.yoribogo.inquiry.dto.InquiryOnlyDTO;
import com.avengers.yoribogo.inquiry.repository.InquiryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.Comparator.comparing;

@Service
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public InquiryServiceImpl(InquiryRepository inquiryRepository,
                              ModelMapper modelMapper) {
        this.inquiryRepository = inquiryRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);
    }

    @Override
    public List<InquiryOnlyDTO> findInquiryOnly(Integer userId, String status) {
        try {
            List<InquiryOnlyDTO> result = modelMapper.map(inquiryRepository.findAll()
                    .stream()
                    .filter(row -> (row.getInquiryStatus()==Status.ACTIVE) &&
                            (status == null || status.toUpperCase().equals(row.getAnswerStatus().name())) &&
                            (userId == null || row.getUser().getUserId().toString().equals(userId.toString())))
                    .toList(), new TypeToken<List<InquiryOnlyDTO>>() {}.getType());
            return result;
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public List<Inquiry> findInquiry(Integer userId, String status) {
        try {
            List<Inquiry> result = inquiryRepository.findAll()
                    .stream()
                    .filter(row -> (row.getInquiryStatus()==Status.ACTIVE) &&
                            (status == null || status.toUpperCase().equals(row.getAnswerStatus().name())) &&
                            (userId == null || row.getUser().getUserId().toString().equals(userId.toString())))
                    .toList();

            return result;
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public Inquiry findInquiryById(Integer inquiryId) {
        try {
            return inquiryRepository.findById(inquiryId).get();
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public Inquiry insertInquiry(InquiryDTO newInquiry) {
        try {
            newInquiry.setInquiryCreatedAt(LocalDateTime.now());
            Inquiry created = modelMapper.map(newInquiry, Inquiry.class);
            if (created.getInquiryVisibility() == null) created.setInquiryVisibility(Visibility.PUBLIC);
            created.setInquiryStatus(Status.ACTIVE);
            created.setInquiryCreatedAt(LocalDateTime.now().withNano(0));
            created.setAnswers(0);
            created.setAnswerStatus(Status.PENDING);
            return inquiryRepository.save(created);
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public Inquiry updateInquiry(InquiryDTO modifyInquiry) {
        try {
            modifyInquiry.setInquiryCreatedAt(LocalDateTime.now().withNano(0));
            return inquiryRepository.saveAndFlush(modelMapper.map(modifyInquiry, Inquiry.class));
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public Inquiry removeInquiry(int id) {
        try {
            Inquiry del = inquiryRepository.findById(id).get();
            del.setInquiryStatus(Status.INACTIVE);
            return inquiryRepository.saveAndFlush(del);
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public void changeStatus(int inquiryId) {
        try {
            Inquiry tmp = inquiryRepository.findById(inquiryId).get();
            List<Answer> answers =  tmp.getAnswer()
                    .stream()
                    .sorted(comparing(Answer::getAnswerId).reversed())
                    .toList();

            tmp.setAnswers(answers.size());

            Role writer = answers.get(0).getWriterType();

            if (writer.equals(Role.ADMIN)) tmp.setAnswerStatus(Status.ANSWERED);
            else tmp.setAnswerStatus(Status.PENDING);

            inquiryRepository.saveAndFlush(tmp);
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
        }
    }
}

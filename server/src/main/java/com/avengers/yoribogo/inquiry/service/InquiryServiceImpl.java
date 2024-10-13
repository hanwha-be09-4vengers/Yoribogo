package com.avengers.yoribogo.inquiry.service;

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
    public List<InquiryOnlyDTO> findInquiryOnly(Integer userId) {
        try {
            if (userId == null) {
                return  modelMapper.map(inquiryRepository.findAll()
                        .stream()
                        .filter(row -> row.getInquiryStatus().equals("ACTIVE"))
                        .toList(), new TypeToken<List<InquiryOnlyDTO>>() {}.getType());

            } else {
                return modelMapper.map(inquiryRepository.findAll()
                        .stream()
                        .filter(row -> row.getUserId() == userId &&
                                row.getInquiryStatus().equals("ACTIVE"))
                        .toList(), new TypeToken<List<InquiryOnlyDTO>>() {}.getType());
            }
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return null;
        }
    }

    @Override
    public List<Inquiry> findInquiry(Integer userId) {
        try {
            if (userId == null) {
                return inquiryRepository.findAll()
                        .stream()
                        .filter(row -> row.getInquiryStatus().equals("ACTIVE"))
                        .toList();
            } else {
                return inquiryRepository.findAll()
                        .stream()
                        .filter(row -> row.getUserId() == userId &&
                                row.getInquiryStatus().equals("ACTIVE"))
                        .toList();
            }
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return null;
        }
    }

    @Override
    public Inquiry findInquiryById(Integer id) {
        try {
            return inquiryRepository.findById(id).get();
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return null;
        }
    }

    @Override
    public Inquiry insertInquiry(InquiryDTO newInquiry) {
        try {
            newInquiry.setInquiryStatus("ACTIVE");
            newInquiry.setInquiryCreatedAt(LocalDateTime.now());
            newInquiry.setAnswers(0);
            return inquiryRepository.save(modelMapper.map(newInquiry, Inquiry.class));
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return null;
        }
    }

    @Override
    public Inquiry updateInquiry(InquiryDTO modifyInquiry) {
        try {
            modifyInquiry.setInquiryCreatedAt(LocalDateTime.now());
            return inquiryRepository.saveAndFlush(modelMapper.map(modifyInquiry, Inquiry.class));
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return null;
        }
    }

    @Override
    public boolean removeInquiry(int id) {
        try {
            Inquiry del = inquiryRepository.findById(id).get();
            del.setInquiryStatus("INACTIVE");
            inquiryRepository.saveAndFlush(del);
            return true;
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);
            return false;
        }
    }
}

package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardManual;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardManualDTO;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardManualRepository;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RecipeBoardManualServiceImpl implements RecipeBoardManualService{

    private final ModelMapper modelMapper;
    private final RecipeBoardManualRepository recipeBoardManualRepository;
    private final RecipeBoardRepository recipeBoardRepository;

    @Autowired
    public RecipeBoardManualServiceImpl(ModelMapper modelMapper, RecipeBoardManualRepository recipeBoardManualRepository, RecipeBoardRepository recipeBoardRepository) {
        this.modelMapper = modelMapper;
        this.recipeBoardManualRepository = recipeBoardManualRepository;
        this.recipeBoardRepository = recipeBoardRepository;
    }

    @Override
    public List<RecipeBoardManualDTO> registRecipeBoardManual(Long recipeBoardId, List<RecipeBoardManualDTO> recipeBoardManualDTOList) {
        // 게시글 유효성 검사
        if (!recipeBoardRepository.existsById(recipeBoardId)){
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
        }

        List<RecipeBoardManual> recipeBoardManualList = new ArrayList<>();

        for (RecipeBoardManualDTO manualDTO : recipeBoardManualDTOList) {
            RecipeBoardManual manual = modelMapper.map(manualDTO, RecipeBoardManual.class);
            manual.setRecipeBoardId(recipeBoardId);
            recipeBoardManualList.add(manual);
        }

        recipeBoardManualRepository.saveAll(recipeBoardManualList);

        return recipeBoardManualDTOList;
    }
}

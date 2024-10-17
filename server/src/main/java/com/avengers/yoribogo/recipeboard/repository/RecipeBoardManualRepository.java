package com.avengers.yoribogo.recipeboard.repository;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardManual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RecipeBoardManualRepository extends JpaRepository<RecipeBoardManual, Long> {

    @Transactional
    void deleteByRecipeBoardRecipeBoardId(Long recipeBoardId);
}

package com.avengers.yoribogo.recipeboard.recipeboard.repository;

import com.avengers.yoribogo.recipeboard.recipeboard.dto.RecipeBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeBoardRepository extends JpaRepository<RecipeBoardEntity, Long> {
}

package com.avengers.yoribogo.recipe.repository;

import com.avengers.yoribogo.recipe.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    // 요리 이름이 포함된 레시피 목록을 페이지네이션 처리하여 반환
    Page<Recipe> findByMenuNameContaining(String menuName, Pageable pageable);

}

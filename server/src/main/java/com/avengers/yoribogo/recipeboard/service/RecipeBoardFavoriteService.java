package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.dto.MyFavoriteBoardDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardFavoriteDTO;
import com.avengers.yoribogo.recipeboard.dto.ResponseFavoriteDTO;
import org.springframework.data.domain.Page;

public interface RecipeBoardFavoriteService {

    ResponseFavoriteDTO addFavorite(RecipeBoardFavoriteDTO recipeBoardFavoriteDTO);

    Page<MyFavoriteBoardDTO> findFavoriteBoardsByUserId(Long userId, Integer pageNo);

    void removeFavorite(Long userId, Long recipeBoardId);
}

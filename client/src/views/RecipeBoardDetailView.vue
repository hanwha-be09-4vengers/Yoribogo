<template>
    <div class="recipe-board-detail-view">
      <header>
        <ProfileButton></ProfileButton>
        <HomeButton></HomeButton>
      </header>
      <MainBoard :cur="'recipe-board'">
        <BackButton></BackButton>
        <div class="menu-name-wrapper">
          <span class="menu-name">{{ menuInfo.menu_name }}</span>
        </div>
        <div class="menu-img-wrapper">
          <img v-show="isImageLoading" :src="defaultImage" alt="MenuImage" />
          <img
            v-show="!isImageLoading"
            :src="menuImageSrc"
            alt="MenuImage"
            @load="handleImageLoad"
            @error="handleImageError"
          />
        </div>
        <div class="menu-ingredient-container">
          <div class="info">재료</div>
          <span class="menu-ingredient">{{ menuInfo.ingredients }}</span>
        </div>
        <div class="menu-manual-container">
          <div class="info">조리 순서</div>
          <!-- manuals 필드에서 매뉴얼 리스트를 바인딩 -->
          <RecipeManual
            v-for="manual in menuInfo.manuals"
            :key="manual.manual_id"
            :manualContent="manual.manual_content"
            :manualImage="manual.manual_image"
          ></RecipeManual>
        </div>

        <!-- 즐겨찾기 버튼 -->
        <div class="favorite-button-container">
            <button @click="toggleFavorite" class="favorite-button">
                {{ isFavorited ? '즐겨찾기 취소' : '즐겨찾기' }}
            </button>
        </div>
      </MainBoard>
      <aside>
        <GoTopButton></GoTopButton>
      </aside>
    </div>
  </template>
  
  <script setup>
  import HomeButton from '@/components/common/HomeButton.vue'
  import ProfileButton from '@/components/common/ProfileButton.vue'
  import MainBoard from '@/components/common/MainBoard.vue'
  import BackButton from '@/components/common/BackButton.vue'
  import RecipeManual from '@/components/recipe/RecipeManual.vue'
  import GoTopButton from '@/components/common/GoTopButton.vue'
  import axios from 'axios'
  import { ref, onMounted } from 'vue'
  import { useRoute } from 'vue-router'
  
  const isImageLoading = ref(true)
  const isImageError = ref(false)
  
  const route = useRoute()
  
  const menuInfo = ref({})
  const menuImageSrc = ref('')

  const isFavorited = ref(false)
  
  const defaultImage = ref(
    'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728804967802_a4720492-2dd2-4e59-8f31-79b55e6a169e_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.svg'
  )
  
  const fetchData = async () => {
    try {
      const recipeResponse = (await axios.get(`/api/recipe-board/detail/${route.params.board_id}`)).data
      if (recipeResponse.success) {
        menuInfo.value = recipeResponse.data
        menuImageSrc.value = menuInfo.value.board_image || defaultImage.value
        checkIfFavorited() // 즐겨찾기 상태 확인
      }
    } catch (error) {
      console.error('Failed to fetch data:', error)
    }
  }

  // 즐겨찾기 여부 확인 로직 호출
  const checkIfFavorited = async () => {
    try {
        const response = await axios.get(`/api/recipe-board/favorites/users/${tokenStore.token.userId}/boards/${route.params.recipeBoardId}`)
        isFavorited.value = response.data.isFavorited
    } catch (error) {
        console.error('Failed to check favorite status: ', error)
    }
  }

  // 즐겨찾기 토글
  const toggleFavorite = async () => {
  try {
    if (isFavorited.value) {
      // 즐겨찾기 취소 API 호출
      await axios.delete(`/api/recipe-board/favorites/users/${tokenStore.token.userId}/boards/${route.params.recipeBoardId}`)
    } else {
      // 즐겨찾기 등록 API 호출
      await axios.post(`/api/recipe-board/favorites`, {
        userId: tokenStore.token.userId,
        recipeBoardId: route.params.recipeBoardId
      })
    }
    isFavorited.value = !isFavorited.value
  } catch (error) {
    console.error('Failed to toggle favorite:', error)
  }
}
  
  // 이미지 로딩 오류 처리 함수
  const handleImageError = () => {
    menuImageSrc.value = defaultImage.value
    isImageLoading.value = false // 로딩 중지
    isImageError.value = true
  }
  
  // 이미지 로드 완료 처리 함수
  const handleImageLoad = () => {
    isImageLoading.value = false // 로딩 중지
    isImageError.value = false
  }
  
  onMounted(() => {
    fetchData()
  })
  </script>
  
  <style scoped>
  .recipe-board-detail-view {
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    min-height: 100vh;
    background-color: var(--yellow-color);
  }
  
  .profile-btn {
    position: absolute;
    top: 7rem;
    right: 12rem;
  }
  
  .home-btn {
    position: absolute;
    top: 7rem;
    right: 20rem;
  }
  
  .back-btn {
    position: absolute;
    top: 14rem;
    left: 12rem;
  }
  
  .go-top-btn {
    position: fixed;
    top: 84%;
    right: 8rem;
  }
  
  .menu-name-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 18rem;
  }
  
  .menu-name {
    font-size: 4rem;
    font-weight: 500;
    color: var(--black-color);
  }
  
  .menu-img-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 72rem;
    height: 54rem;
    overflow: hidden;
    margin-top: 4rem;
    border-radius: 1.4rem;
  }
  
  .menu-img-wrapper img {
    height: 100%;
    width: 100%;
    object-fit: cover;
  }
  
  .menu-ingredient-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    gap: 4rem;
    margin-top: 6rem;
  }
  
  .menu-ingredient {
    text-align: center;
    width: 68%;
    font-size: 2.4rem;
    font-weight: 500;
    line-height: 4rem;
    word-wrap: break-word;
  }
  
  .menu-manual-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    gap: 4rem;
    margin-top: 4rem;
    margin-bottom: 12rem;
  }
  
  .info {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 1rem 2rem;
    font-size: 2.4rem;
    font-weight: 350;
    color: var(--white-color);
    background-color: var(--black-color);
    border-radius: 3rem;
  }
  
  @media screen and (max-width: 1440px) {
    .menu-img-wrapper {
      width: 68rem;
      height: 51rem;
    }
  
    .back-btn {
      left: 8rem;
    }
  }
  
  @media screen and (max-width: 1280px) {
    .menu-img-wrapper {
      width: 52rem;
      height: 39rem;
    }
  
    .back-btn {
      left: 6rem;
    }
  }
  
  @media screen and (max-width: 1024px) {
    .go-top-btn {
      right: 5rem;
    }
  }
  
  @media screen and (max-width: 600px) {
    .menu-img-wrapper {
      width: 40rem;
      height: 30rem;
    }
  
    .back-btn {
      left: 5rem;
    }
  
    .go-top-btn {
      right: 4rem;
    }
  
    .menu-name {
      font-size: 3rem;
    }
  }
  
  @media screen and (max-width: 480px) {
    .profile-btn {
      right: 10rem;
    }
  
    .home-btn {
      right: 18rem;
    }
  
    .go-top-btn {
      right: 3rem;
    }
  }
  
  @media screen and (max-width: 425px) {
    .menu-img-wrapper {
      width: 36rem;
      height: 27rem;
    }
  
    .profile-btn {
      right: 9rem;
    }
  
    .home-btn {
      right: 17rem;
    }
  
    .go-top-btn {
      right: 2rem;
    }
  }
  
  @media screen and (max-width: 375px) {
    .profile-btn {
      right: 8rem;
    }
  
    .home-btn {
      right: 16rem;
    }
  
    .go-top-btn {
      right: 1.5rem;
    }
  }
  </style>
  
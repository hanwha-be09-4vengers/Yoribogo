<template>
  <div class="result-view">
    <ProfileButton></ProfileButton>
    <HomeButton></HomeButton>
    <header>
      <ResultNav></ResultNav>
    </header>
    <main>
      <div class="result-container">
        <p v-show="!isLoading">요리보고가 추천하는 요리는!</p>
        <p v-show="isLoading">요리보고가 추천 중입니다...</p>
        <div class="result-info" v-show="!isLoading">
          <span :style="{ opacity: isFlipped ? 1 : 0 }">{{ menuName }}</span>
        </div>
        <div class="card-board-container" v-show="!isLoading">
          <ResultBoard :img="menuImage" :text="menuName" @flipped="isFlipped = !isFlipped">
          </ResultBoard>
        </div>
        <LoadingSpinner v-show="isLoading"></LoadingSpinner>
      </div>
    </main>
  </div>
</template>

<script setup>
import ResultNav from '../components/ResultNav.vue'
import ResultBoard from '../components/ResultBoard.vue'
import LoadingSpinner from '@/components/LoadingSpinner.vue'
import HomeButton from '@/components/HomeButton.vue'
import ProfileButton from '@/components/ProfileButton.vue'
import { ref, onMounted } from 'vue'
import axios from 'axios'

const isFlipped = ref(false)
const isLoading = ref(true)

const menuName = ref('')
const menuImage = ref(
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728804967802_a4720492-2dd2-4e59-8f31-79b55e6a169e_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.svg'
)
const recipeId = ref()

const fetchRecommendedMenu = async () => {
  try {
    const questionResponse = JSON.parse(localStorage.getItem('question_responses'))
    const requestData = {
      first: questionResponse.question_1, // 질문 1의 응답
      second: questionResponse.question_2, // 질문 2의 응답
      third: questionResponse.question_3, // 질문 3의 응답
      fourth: questionResponse.question_4, // 질문 4의 응답
      fifth: questionResponse.question_5 // 질문 5의 응답
    }
    const response = (await axios.post('/api/recipes/recommend', requestData)).data
    if (response.success) {
      menuName.value = response.data.menu_name
      if (response.data.menu_image) menuImage.value = response.data.menu_image
      recipeId.value = response.data.recipe_id
      isLoading.value = false
      localStorage.removeItem('question_responses')
    }
  } catch (error) {
    console.error('요리를 추천받는데 실패했습니다.', error)
  }
}

onMounted(() => {
  fetchRecommendedMenu()
})
</script>

<style scoped>
.result-view {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.profile-btn {
  position: absolute;
  top: 14rem;
  right: 20rem;
}

.home-btn {
  position: absolute;
  top: 14rem;
  right: 12rem;
}

main {
  justify-content: center;
  align-items: center;
}

.result-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 120rem;
  gap: 3rem;
  margin-top: 5rem;
  font-size: 4rem;
  font-weight: 700;
}

.result-container p {
  font-size: 3rem;
  font-weight: 700;
}

.result-info {
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 15rem; /* 최소 너비 설정 */
  padding: 1rem 2rem; /* 내부 여백 추가 */
  height: 8rem;
  background-color: var(--light-yellow-color);
  border-radius: 5rem;
}

.result-info span {
  transition: opacity 0.7s ease;
  white-space: nowrap;
}

.card-board-container {
  display: flex;
  flex-direction: row;
}

@media screen and (max-width: 768px) {
  .profile-btn {
    right: 14rem;
  }

  .home-btn {
    right: 6rem;
  }
}

@media screen and (max-width: 480px) {
  .profile-btn {
    top: 14rem;
    right: 3rem;
  }

  .home-btn {
    top: 22rem;
    right: 3rem;
  }
}
</style>

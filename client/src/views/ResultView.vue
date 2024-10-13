<template>
  <div class="result-view">
    <header>
      <ResultNav></ResultNav>
    </header>
    <main>
      <div class="result-container">
        <p v-if="!isLoading">요리보고가 추천하는 요리는!</p>
        <p v-if="isLoading">요리보고가 추천 중입니다...</p>
        <div class="result-info" v-if="!isLoading">
          <span :style="{ opacity: isFlipped ? 1 : 0 }">{{ menuName }}</span>
        </div>
        <div class="card-board-container" v-if="!isLoading">
          <ResultBoard :img="menuImage" :text="menuName" @flipped="isFlipped = !isFlipped">
          </ResultBoard>
        </div>
        <Loading v-if="isLoading"></Loading>
      </div>
    </main>
  </div>
</template>

<script setup>
import ResultNav from '../components/ResultNav.vue'
import ResultBoard from '../components/ResultBoard.vue'
import Loading from '@/components/Loading.vue'
import { ref, onMounted} from 'vue'
import axios from 'axios'

const isFlipped = ref(false)
const isLoading = ref(true)

const menuName = ref('')
const menuImage = ref('https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728804967802_a4720492-2dd2-4e59-8f31-79b55e6a169e_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.svg')
const recipeId = ref()

const fetchRecommendedMenu = async () => {
  const questionResponse = JSON.parse(localStorage.getItem('question_responses'));
  const requestData = {
    first: questionResponse[0].response, 
    second: questionResponse[1].response,
    third: questionResponse[2].response,
    fourth: questionResponse[3].response,
    fifth: questionResponse[4].response,
  };
  try {
    const response = (await axios.post('/api/recipes/recommend',requestData)).data;
    if(response.success) {
      menuName.value = response.data.menu_name;
      if(response.data.menu_image) menuImage.value = response.data.menu_image;
      recipeId.value = response.data.recipe_id; 
      if(response.data.menu_image) menuImage.value = response.data.menu_image;
      isLoading.value = false;
      localStorage.removeItem("question_responses");
    }
  } catch (error) {
    console.error('요리를 추천받는데 실패했습니다.', error)
  }
}

onMounted(() => {
  fetchRecommendedMenu();  
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
</style>

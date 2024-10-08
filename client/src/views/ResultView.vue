<template>
  <div class="result-view">
    <header>
      <ResultNav></ResultNav>
    </header>
    <main>
      <div class="result-container">
        <p>요리보고가 추천하는 음식은!</p>
        <div class="result-info">
          <span :style="{ opacity: isFlipped ? 1 : 0 }">{{ result.label }}</span>
        </div>
        <div class="card-board-container">
          <ResultBoard :img="result.img" :text="result.label" @flipped="isFlipped = !isFlipped">
          </ResultBoard>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import ResultNav from '../components/ResultNav.vue'
import ResultBoard from '../components/ResultBoard.vue'
import bibimbapImg from '../assets/bibimbap.svg'
import { useRoute, useRouter } from 'vue-router'
import { ref } from 'vue'

const route = useRoute()
const router = useRouter()

const isFlipped = ref(false)

const result = {
  rid: 1,
  label: '비빔밥',
  img: bibimbapImg
}
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
  width: 22.5rem;
  height: 8rem;
  background-color: var(--light-yellow-color);
  border-radius: 5rem;
}

.result-info span {
  transition: opacity 0.5s ease;
}

.card-board-container {
  display: flex;
  flex-direction: row;
}
</style>

<template>
  <div class="question-view">
    <header>
      <QuestionNav :questions="questions"></QuestionNav>
    </header>
    <main>
      <div class="question-container">
        <div class="question-info">
          <span>{{ questions[qid - 1].label }}</span>
        </div>
        <div class="card-board-container" v-if="qid<5">
          <CardBoard
            :img="questions[qid - 1].leftImg"
            :text="questions[qid - 1].leftText"
            @click="goNext(questions[qid - 1].leftText, qid + 1)"
          >
          </CardBoard>
          <CardBoard
            :img="questions[qid - 1].rightImg"
            :text="questions[qid - 1].rightText"
            @click="goNext(questions[qid - 1].rightText, qid + 1)"
          >
          </CardBoard>
        </div>
        <div class="input-container" v-if="qid===5">
          <input type="text" placeholder="답변:" v-model="inputText">
          <button class="submit-btn" @click="goNext(null, qid + 1)">추천 결과 보기</button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import QuestionNav from '../components/QuestionNav.vue'
import CardBoard from '../components/CardBoard.vue'
import sunnyImg from '../assets/sunny.svg'
import cloudyImg from '../assets/cloudy.svg'
import { useRoute, useRouter } from 'vue-router'
import { ref, watch } from 'vue'

const route = useRoute()
const router = useRouter()

const qid = ref(parseInt(route.params.qid))

const inputText = ref("");

watch(
  () => route.params.qid,
  (newQid) => {
    qid.value = parseInt(newQid)
  }
)

const questions = [
  {
    qid: 1,
    label: '오늘의 날씨는 어떤가요?',
    leftImg: sunnyImg,
    leftText: '맑아요',
    rightImg: cloudyImg,
    rightText: '흐려요'
  },
  {
    qid: 2,
    label: '오늘의 기분은 어떤가요?',
    leftImg: sunnyImg,
    leftText: '좋아요!',
    rightImg: cloudyImg,
    rightText: '나빠요!'
  },
  {
    qid: 3,
    label: '몇 명이 먹는 음식인가요?',
    leftImg: sunnyImg,
    leftText: '혼자 먹어요',
    rightImg: cloudyImg,
    rightText: '여러명이 먹어요'
  },
  {
    qid: 4,
    label: '채식 또는 비건 식단을 따르시나요?',
    leftImg: sunnyImg,
    leftText: '네',
    rightImg: cloudyImg,
    rightText: '아니요'
  },
  {
    qid: 5,
    label: '제가 또 알아야 하는게 있을까요?',
  }
]

const goNext = (selectedText, nextQid) => {
  let responses = JSON.parse(localStorage.getItem('question_responses')) || [];

  if (nextQid <= 5) responses.push({ qid: qid.value, response: selectedText });
  else responses.push({ qid: qid.value, response: inputText.value });

  localStorage.setItem('question_responses', JSON.stringify(responses));

  nextQid <= 5 ? router.push(`/question/${nextQid}`) : router.push(`/question/result`)
}
</script>

<style scoped>
.question-view {
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

.question-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 120rem;
  margin-top: 11rem;
  font-size: 4rem;
  font-weight: 700;
}

.question-info {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 72rem;
  height: 8rem;
  background-color: var(--white-color);
  border-radius: 5rem;
  color: var(--black-color)
}

.card-board-container {
  display: flex;
  flex-direction: row;
  gap: 4rem;
  margin-top: 5.4rem;
}

.input-container {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  gap: 6rem;
  margin-top: 16rem;
}

.input-container input {
  width: 59rem;
  height: 10rem;
  padding-left: 4rem;
  border: none;
  border-radius: 1.4rem;
  font-size: 3.8rem;
  font-weight: 700;
  color: var(--black-color);
  cursor: pointer;
}

.submit-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 34rem;
  height: 9rem;
  background-color: var(--light-yellow-color);
  border: none;
  border-radius: 1.4rem;
  box-shadow: 0.5rem 0.3rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  font-size: 3.5rem;
  font-weight: 700;
  color: var(--black-color);
  cursor: pointer;
  transition: 0.3s ease-out;
}

.submit-btn:hover {
  transform: translateY(-0.1rem);
  box-shadow: 0.5rem 0.3rem 0.3rem 0.05rem rgba(60, 60, 60, 0.5);
}
</style>

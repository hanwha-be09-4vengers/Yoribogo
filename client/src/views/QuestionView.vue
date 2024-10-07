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
        <div class="card-board-container">
          <CardBoard
            :img="questions[qid - 1].leftImg"
            :text="questions[qid - 1].leftText"
            @click="goNext(qid + 1)"
          >
          </CardBoard>
          <CardBoard
            :img="questions[qid - 1].rightImg"
            :text="questions[qid - 1].rightText"
            @click="goNext(qid + 1)"
          >
          </CardBoard>
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

watch(
  () => route.params.qid,
  (newQid) => {
    qid.value = parseInt(newQid)
  }
)

const questions = [
  {
    qid: 1,
    label: '질문1',
    leftImg: sunnyImg,
    leftText: '맑아요',
    rightImg: cloudyImg,
    rightText: '흐려요'
  },
  {
    qid: 2,
    label: '질문2',
    leftImg: sunnyImg,
    leftText: '맑아요',
    rightImg: cloudyImg,
    rightText: '흐려요'
  },
  {
    qid: 3,
    label: '질문3',
    leftImg: sunnyImg,
    leftText: '맑아요',
    rightImg: cloudyImg,
    rightText: '흐려요'
  },
  {
    qid: 4,
    label: '질문4',
    leftImg: sunnyImg,
    leftText: '맑아요',
    rightImg: cloudyImg,
    rightText: '흐려요'
  },
  {
    qid: 5,
    label: '질문5',
    leftImg: sunnyImg,
    leftText: '맑아요',
    rightImg: cloudyImg,
    rightText: '흐려요'
  }
]

const goNext = (nextQid) => {
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
  width: 58.5rem;
  height: 8rem;
  background-color: var(--white-color);
  border-radius: 5rem;
}

.card-board-container {
  display: flex;
  flex-direction: row;
  gap: 4rem;
  margin-top: 5.4rem;
}
</style>

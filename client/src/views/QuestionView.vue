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
import { useRoute, useRouter } from 'vue-router'
import { ref, watch } from 'vue'

const route = useRoute()
const router = useRouter()

const qid = ref(parseInt(route.params.qid))

const inputText = ref("");

// 얘는 나중에 저희 s3쓰면 바뀔 예정
const sunnyImg = 'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728802076203_597f003c-9c70-44a5-a0a5-17b41acc8ecc_%E1%84%82%E1%85%A1%E1%86%AF%E1%84%8A%E1%85%B5-%E1%84%8C%E1%85%A9%E1%87%82%E1%84%8B%E1%85%A1%E1%84%8B%E1%85%AD.svg'
const cloudyImg = 'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728802076202_42e590b1-4ad9-43b8-a265-f5bb3c90a4aa_%E1%84%82%E1%85%A1%E1%86%AF%E1%84%8A%E1%85%B5-%E1%84%92%E1%85%B3%E1%84%85%E1%85%A7%E1%84%8B%E1%85%AD.svg'
const feelGoodImg = 'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728802376035_35580f1b-82dd-45eb-af7b-846808f78d65_%E1%84%80%E1%85%B5%E1%84%87%E1%85%AE%E1%86%AB-%E1%84%8C%E1%85%A9%E1%87%82%E1%84%8B%E1%85%A1%E1%84%8B%E1%85%AD.svg'
const feelBadImg = 'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728802376035_7e441d8a-2f4e-4318-8466-81bf2c79b086_%E1%84%80%E1%85%B5%E1%84%87%E1%85%AE%E1%86%AB-%E1%84%82%E1%85%A1%E1%84%88%E1%85%A1%E1%84%8B%E1%85%AD.svg'
const eatAloneImg = 'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728808005528_d69f3de9-43a1-49e1-9695-0e8f8434546c_%E1%84%80%E1%85%B3%E1%84%85%E1%85%B3%E1%86%BA.svg'
const eatTogetherImg = 'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728802401230_1100e968-5f72-4e80-b96a-911d6098d3bb_%E1%84%80%E1%85%A1%E1%87%80%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A5%E1%86%A8%E1%84%8B%E1%85%A5%E1%84%8B%E1%85%AD.svg'
const vegeImg = 'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728802376019_b65e001e-3788-4f9f-904a-9378aa94b498_%E1%84%8E%E1%85%A2%E1%84%89%E1%85%B5%E1%86%A8.svg'
const meatImg = 'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728806853872_2b46dd9d-f7c5-49bf-b67c-fbfb7d2d2529_%E1%84%80%E1%85%A9%E1%84%80%E1%85%B5.svg'

watch(
  () => route.params.qid,
  (newQid) => {
    qid.value = parseInt(newQid)
  }
)

// 얘는 이제 db의 질문과 선지로 바뀔 예정
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
    leftImg: feelGoodImg,
    leftText: '좋아요!',
    rightImg: feelBadImg,
    rightText: '나빠요!'
  },
  {
    qid: 3,
    label: '몇 명이 먹는 음식인가요?',
    leftImg: eatAloneImg,
    leftText: '혼자 먹어요',
    rightImg: eatTogetherImg,
    rightText: '여러명이 먹어요'
  },
  {
    qid: 4,
    label: '채식 또는 비건 식단을 따르시나요?',
    leftImg: vegeImg,
    leftText: '네',
    rightImg: meatImg,
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
  min-width: 50rem;
  padding: 1rem 3rem;
  height: 8rem;
  background-color: var(--white-color);
  border-radius: 5rem;
  color: var(--black-color)
}

.question-info span {
  white-space: nowrap;
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
  transform: translateY(-0.2rem);
  box-shadow: 0.5rem 0.3rem 0.3rem 0.1rem rgba(60, 60, 60, 0.5);
}
</style>

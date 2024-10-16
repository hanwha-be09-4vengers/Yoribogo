<template>
  <div id="home-view" class="home-view">
    <img class="bg-circle" src="/src/assets/Intersect.png" alt="Background Circle" />
    <img class="chicken" src="/src/assets/chicken.png" alt="Chicken" />
    <header>
      <MainNav></MainNav>
    </header>
    <main>
      <section id="start-section" class="start-section">
        <div class="catchphrase">
          <span class="emphasize">맛있는 선택의 시작.</span>
        </div>
        <div class="catchphrase">
          <span class="normal">여러분의 <span class="f-red">취향</span>에</span>
          <span class="indent normal">맞춘 <span class="f-red">음식 레시피.</span></span>
        </div>
        <button id="start-btn" class="start-btn" @click="goQuestion">시작하기</button>
      </section>

      <div>
    <button @click="likePost">좋아요 추가</button>
  </div>

    </main>
  </div>
</template>

<script setup>
import MainNav from '@/components/MainNav.vue'
import { useRouter } from 'vue-router'

import axios from 'axios';  // 추후 삭제 요망

const router = useRouter()

const goQuestion = () => {
  router.push('/question/1')
}

// 이하 JS 코드 추후 삭제 - 로그인 후 반응형으로 수정해야함 
const likePost = async () => {
  try {
    const likeRequestDTO = {
      userId: 1,
      postId: 4
    };

    const response = await axios.post('http://localhost:8080/api/likes/like', likeRequestDTO);

    console.log(response.data);
    alert("좋아요가 추가되었습니다!");

  } catch (error) {
    console.error(error);
    alert("좋아요 추가 중 오류가 발생했습니다.");
  }
}

// SSE 연결 코드
console.log("eventsource 연결 전");
const eventSource = new EventSource('http://localhost:8080/notifications');
console.log("eventsource 연결 후");

eventSource.onmessage = function(event) {
    console.log('알림:', event.data);
};

eventSource.addEventListener('notification', function(event) {
    console.log('이벤트 알림:', event.data);
});


</script>

<style scoped>
.home-view {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.bg-circle {
  position: absolute;
  top: 0;
  right: 0;
  width: 74rem;
  height: 66rem;
  z-index: 0;
}

.chicken {
  position: absolute;
  top: 30rem;
  right: 20rem;
  width: 80rem;
  height: 40rem;
}

.start-section {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-top: 12rem;
  margin-left: 15rem;
  margin-bottom: 10rem;
  gap: 9.6rem;
}

.catchphrase {
  display: flex;
  flex-direction: column;
  font-weight: 700;
  color: var(--black-color);
}

.emphasize {
  font-size: 8rem;
}

.normal {
  font-size: 6rem;
}

.f-red {
  color: var(--red-color);
}

.normal div span {
  color: var(--red-color);
}

.indent {
  margin-left: 12rem;
}

.start-btn {
  height: 10rem;
  width: 34rem;
  background-color: var(--white-color);
  border: none;
  border-radius: 3.5rem;
  box-shadow: 0.5rem 0.5rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  font-size: 3.5rem;
  font-weight: 700;
  color: var(--black-color);
  cursor: pointer;
  transition: 0.3s ease-out;
}

.start-btn:hover {
  transform: translateY(-0.3rem);
  box-shadow: 0.5rem 0.5rem 0.3rem 0.2rem rgba(60, 60, 60, 0.5);
}

/* 노트북 */
@media screen and (max-width: 1683px) {
  .start-section {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-top: 12rem;
    margin-left: 15rem;
    gap: 9.6rem;
  }
}

@media screen and (max-width: 1439px) {
  .chicken {
    top: 22rem;
    right: 15rem;
    width: 80rem;
    height: 40rem;
  }

  .bg-circle {
    width: 60rem;
    height: 54rem;
  }
}

/* 태블릿 */
@media screen and (max-width: 1023px) {
  .chicken {
    top: 22rem;
    right: 15rem;
    width: 80rem;
    height: 40rem;
  }

  .bg-circle {
    width: 60rem;
    height: 54rem;
  }

  .start-section {
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    margin-top: 60rem;
    margin-left: 0px;
  }
}

/* 모바일 */
@media screen and (max-width: 767px) {
  .chicken {
    left: 50%;
    transform: translateX(-50%);
    width: 60rem;
    height: 30rem;
  }

  .bg-circle {
    width: 54rem;
    height: 48rem;
  }

  .emphasize {
    font-size: 6.4rem;
  }

  .normal {
    font-size: 4.8rem;
  }
}
</style>

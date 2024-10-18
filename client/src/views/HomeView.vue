<template>
  <div id="home-view" class="home-view">
    <!-- 기존 코드 유지 -->
    <img class="bg-circle" src="/src/assets/Intersect.png" alt="Background Circle" />
    <img class="chicken" src="/src/assets/chicken.png" alt="Chicken" />
    <header>
      <HomeNav @open-login-modal="openLoginModal"></HomeNav>
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

      <!-- 새로 추가된 좋아요 버튼 -->
      <div>
        <button @click="likePost">좋아요 추가</button>
      </div>

    </main>
    <LoginModal
      v-if="isLoginModalVisible"
      @close="closeLoginModal"
      @goToStep1="openRegisterModal"
      @openPasswordReset="openPasswordResetModal"
      @openFindId="openFindIdModal"
    />
  </div>
</template>

<script setup>
import LoginModal from '@/components/user/login/LoginModal.vue';
import HomeNav from '@/components/user/HomeNav.vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue'

// 새로 추가된 import
import { useTokenStore } from '@/stores/tokenStore';
import axios from 'axios';
import { addLike } from '@/api/like';

const router = useRouter()

const isLoginModalVisible = ref(false); // 로그인 모달 상태

// 로그인 모달 열기
const openLoginModal = () => {
  isLoginModalVisible.value = true; // 로그인 모달 열기
};

// 로그인 모달 닫기
const closeLoginModal = () => {
  isLoginModalVisible.value = false; // 로그인 모달 닫기
};

const goQuestion = () => {
  router.push('/question/1')
}

// 새로 추가된 likePost 함수
const tokenStore = useTokenStore();

const likePost = async () => {
  try {
    const postId = 1; 
    const userId = tokenStore.token.userId;
    console.log('Attempting to like post:', { userId, postId });

    if (!tokenStore.token.userId) {
      alert("로그인이 필요합니다.");
      openLoginModal();
      return;
    }
    
    const response = await addLike(userId, postId);

    console.log('Response data:', response.data); // 응답 확인

    if (response.data.success) {
      if (response.data.isLiked) {
        alert("좋아요가 추가되었습니다!");
      } else {
        alert("좋아요가 취소되었습니다.");
      }
    } else {
      alert("좋아요 처리에 실패했습니다.");
    }
  } catch (error) {
    console.error(error);
    if (error.response && error.response.status === 401) {
      alert("세션이 만료되었습니다. 다시 로그인해주세요.");
      tokenStore.logout();
      openLoginModal();
    } else {
      alert("좋아요 처리 중 오류가 발생했습니다.");
    }
  }
};

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
  margin-top: 18rem;
  margin-left: 20rem;
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
  color: var(--text-red-color);
}

.normal div span {
  color: var(--text-red-color);
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
@media screen and (max-width: 1684px) {
  .start-section {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-top: 14rem;
    margin-left: 20rem;
    gap: 9.6rem;
  }
}

@media screen and (max-width: 1440px) {
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

@media screen and (max-width: 1280px) {
  .chicken {
    top: 22rem;
    right: 15rem;
    width: 60rem;
    height: 30rem;
  }

  .bg-circle {
    width: 50rem;
    height: 45rem;
  }
}

/* 태블릿 */
@media screen and (max-width: 1024px) {
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
@media screen and (max-width: 768px) {
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

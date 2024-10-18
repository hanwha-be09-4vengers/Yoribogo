<template>
  <div class="login-page">
    <!-- 왼쪽 상단 로고 -->
    <div class="logo-wrapper" @click="goHome">
      <img src="/src/assets/logo.svg" alt="Yoribogo Logo" />
      <span>요리보고</span>
    </div>

    <!-- 오른쪽 요리사 이미지 및 텍스트 -->
    <div class="chef-wrapper">
      <img src="/src/assets/images/basic_profile.png" alt="Chef" />
      <div class="speech-bubble">
        <p>만나서 반가워요!</p>
      </div>
    </div>

    <!-- Login Modal -->
    <LoginModal v-if="isLoginModalVisible" @close="closeLoginModal" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import LoginModal from '@/components/user/login/LoginModal.vue'; // 모달 컴포넌트 임포트
import { useRouter } from 'vue-router';

const router = useRouter();
const isLoginModalVisible = ref(false);

// 홈으로 돌아가기
const goHome = () => {
  router.push('/');
};

// 로그인 모달 열기
const openLoginModal = () => {
  isLoginModalVisible.value = true;
};

// 로그인 모달 닫기
const closeLoginModal = () => {
  isLoginModalVisible.value = false;
};

// 페이지가 로드되면 자동으로 모달 열기
onMounted(() => {
  openLoginModal(); // 페이지가 로드되면 모달을 자동으로 열기
});
</script>

<style scoped>
/* 전체 페이지 스타일 */
.login-page {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100vh;
  background-color: #ffda14;
  position: relative;
}

/* 왼쪽 상단 로고 스타일 */
.logo-wrapper {
  display: flex;
  align-items: center;
  position: absolute;
  top: 2rem;
  left: 2rem;
  cursor: pointer;
}

.logo-wrapper img {
  width: 4rem;
  height: 4rem;
}

.logo-wrapper span {
  font-size: 2rem;
  font-weight: bold;
  margin-left: 1rem;
  color: #333;
}

/* 요리사 이미지 및 텍스트 */
.chef-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: absolute;
  right: 13%;; /* 오른쪽에서 20rem 떨어지게 설정 */
  top: 50%; /* 화면의 세로 중앙에 배치 */
  transform: translateY(-50%); /* 세로 중앙 정렬을 위해 요소를 위로 50% 이동 */
}

.chef-wrapper img {
  width: 18rem;
  height: auto;
  position: relative;
}

/* 말풍선 스타일 */
.speech-bubble {
  position: absolute;
  bottom: 105%; /* 이미지 위에 나타나게 하기 위해 위치 조정 */
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  padding: 1rem 2rem;
  border-radius: 10px;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
  font-size: 1.6rem;
  font-weight: bold;
  color: #000;
  text-align: center;
  width: max-content;
  max-width: 20rem;
}

.speech-bubble::after {
  content: '';
  position: absolute;
  bottom: -50%; /* 이미지 위에 나타나게 하기 위해 위치 조정 */
  left: 50%;
  transform: translateX(-50%);
  border-width: 10px;
  border-style: solid;
  border-color: white transparent transparent transparent; /* 꼬리 부분을 삼각형으로 만듦 */
}
</style>

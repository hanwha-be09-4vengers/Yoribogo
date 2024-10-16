<template>
  <nav id="main-nav" class="main-nav">
    <div id="logo" class="logo" @click="goHome">
      <img src="https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/128icon.jpg" alt="Logo" />
      <span>Yoribogo</span>
    </div>
    
    <ul class="menu">
      <li class="search-recipe" @click="goWiki">
        <div class="search-bar">
          <span>레시피 검색</span>
          <i class="fa-solid fa-magnifying-glass"></i>
        </div>
      </li>

      <!-- 로그인 상태에 따라 다른 UI 표시 -->
      <li v-if="isLoggedIn" @click="logout">로그아웃</li>
      <li v-if="isLoggedIn" @click="toggleProfileDropdown">프로필</li>
      <li v-else class="signup">회원가입</li>
      <li v-else class="login" @click="openLoginModal">로그인</li>
    </ul>
    
    <!-- 로그인 모달 창 -->
    <LoginModal
      v-if="isLoginModalVisible"
      @close="closeLoginModal"
      @goToStep1="openRegisterModal"
      @openPasswordReset="openPasswordResetModal"
      @openFindId="openFindIdModal"
    />
  </nav>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useTokenStore } from '@/stores/tokenStore'; // Pinia 스토어 임포트
import LoginModal from '@/components/top_nav/login/LoginModal.vue';

const router = useRouter();
const tokenStore = useTokenStore(); // Pinia 스토어 사용

const isLoggedIn = ref(false); // 로그인 상태
const isLoginModalVisible = ref(false); // 로그인 모달 상태
const isDropdownVisible = ref(false); // 드롭다운 메뉴 상태

// 로그인 상태 확인
const checkLoginStatus = () => {
  isLoggedIn.value = !!tokenStore.token.accessToken; // accessToken이 존재하면 로그인 상태
};

// 로그인 상태 변화 감시
watch(
  () => tokenStore.token.accessToken,
  () => {
    checkLoginStatus(); // accessToken 변경 시 로그인 상태 확인
  },
  { immediate: true } // 초기에도 바로 실행
);

// 로그아웃 처리
const logout = () => {
  tokenStore.logout(); // Pinia 스토어의 로그아웃 함수 호출
  router.push('/'); // 로그아웃 후 홈으로 이동
};

// 로그인 모달 열기
const openLoginModal = () => {
  isLoginModalVisible.value = true; // 로그인 모달 열기
};

// 로그인 모달 닫기
const closeLoginModal = () => {
  isLoginModalVisible.value = false; // 로그인 모달 닫기
};

// 프로필 드롭다운 메뉴 토글
const toggleProfileDropdown = () => {
  isDropdownVisible.value = !isDropdownVisible.value;
};

// 위키 페이지로 이동
const goWiki = () => {
  router.push('/wiki');
};

// 로고 클릭 시 홈으로 이동
const goHome = () => {
  router.push('/');
};
</script>

<style scoped>
.main-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: calc(100% - 8rem);
  height: 100%;
  z-index: 9999;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo img {
  width: 6rem;
  height: 6rem;
}

.logo span {
  font-size: 2rem;
  font-weight: 600;
  color: var(--black-color);
}

.menu {
  display: flex;
  align-items: center;
  gap: 4rem;
  font-size: 2rem;
  font-weight: 600;
  color: var(--black-color);
}

ul li {
  cursor: pointer;
}

.search-recipe {
  display: flex;
  background-color: var(--blended-light-color);
  padding: 1rem 3.6rem;
  border: none;
  border-radius: 2rem;
}

.search-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.search-bar span {
  line-height: 2rem;
  font-weight: 400;
}

.search-bar i {
  color: var(--yellow-color);
}

@media screen and (max-width: 425px) {
  .menu {
    font-size: 1.9rem;
    gap: 2.5rem;
  }
}
</style>

<template>
  <nav id="main-nav" class="main-nav">
    <div id="logo" class="logo">
      <img src="../assets/logo.svg" alt="Logo" />
      <span>Yoribogo</span>
    </div>
    <ul class="menu">
      <li class="search-recipe" @click="goWiki">
        <div class="search-bar">
          <span>레시피 검색</span>
          <i class="fa-solid fa-magnifying-glass"></i>
        </div>
      </li>
      <li class="signup">회원가입</li>
      <li class="login" @click="openLoginModal">로그인</li>
    </ul>
  </nav>


    <!-- 로그인 모달 창 -->
	  <LoginModal
      v-if="isLoginModalVisible"
      @close="closeLoginModal"
      @goToStep1="openRegisterModal"
      @openPasswordReset="openPasswordResetModal"
      @openFindId="openFindIdModal" 
	  />
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, inject, watch } from 'vue';
import LoginModal from '@/components/top_nav/login/LoginModal.vue';

const router = useRouter()


  // 사용자 정보 및 로그인 상태 주입받기
  const token = inject('token');
  const setTokenData = inject('setTokenData');
  
  const isLoggedIn = ref(false); // 로그인 상태 확인
  const isDropdownVisible = ref(false); // 드롭다운 메뉴 상태

  const isAccountReactivationModalVisible = ref(false);
  const userAuthId = ref('');

  // 로그인 상태 확인
  const checkLoginStatus = () => {
	  isLoggedIn.value = !!token.accessToken; // accessToken이 존재하면 로그인된 것으로 간주
  };
  

    // 로그인 상태 변화 감시
    watch(
      () => token.accessToken,
      () => {
        checkLoginStatus(); // accessToken 변경 시 로그인 상태 확인
      },
      { immediate: true }
  );


// 로그아웃 처리
const logout = () => {
	// 토큰과 사용자 정보를 초기화
	setTokenData({
	  user_identifier: null,
	  access_token: null,
	  access_token_expiry: null,
	  refresh_token: null,
	  refresh_token_expiry: null,
	});
  
	// localStorage 초기화
	localStorage.removeItem('token');
	localStorage.removeItem('userId');
	localStorage.removeItem('groupData');
  
	// 로그인 상태 갱신
	isLoggedIn.value = false;
  
	// 로그아웃 후 홈으로 이동
	router.push('/');
  };

  // 모달 상태 변수
  const isLoginModalVisible = ref(false);
  const isRegisterModalVisible = ref(false);
  const currentSignupStep = ref(1); // 회원가입 단계를 1로 초기화
  const isPrivacyPolicyModalVisible = ref(false); // 개인정보 처리방침 모달 상태

    // 로그인 모달 열기
    const openLoginModal = () => {
    isLoginModalVisible.value = true; // 로그인 모달 열기
    isRegisterModalVisible.value = false; // 회원가입 모달 닫기
    };
    
    // 로그인 모달 닫기
    const closeLoginModal = () => {
    isLoginModalVisible.value = false; // 로그인 모달 닫기
    };

    // 드롭다운 메뉴 외부 클릭 시 닫기
    document.addEventListener('click', (event) => {
      const dropdownElement = document.querySelector('.user-profile');
      if (dropdownElement && !dropdownElement.contains(event.target)) {
        isDropdownVisible.value = false;
      }
    });

    //위키로 가기
    const goWiki = () => {
      router.push('/wiki')
    }
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

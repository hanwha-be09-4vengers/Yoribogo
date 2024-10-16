<template>
    <div class="modal-overlay">
      <div class="modal-content">
        <button class="close-btn" @click="closeModal">×</button>
        <div class="modal-header">
          <h2>Yoribogo</h2>
        </div>
        <div class="modal-body">
          <input
            type="text"
            placeholder="아이디"
            v-model="username"
            @keyup.enter="login"
          />
          <!-- 아이디 입력 에러 메시지 -->
          <span v-if="usernameError" class="error-message">{{ usernameError }}</span>
  
          <div class="password-input-container">
            <input
              :type="passwordVisible ? 'text' : 'password'"
              placeholder="비밀번호 입력"
              v-model="password"
              maxlength="24"
              @keyup.enter="login"
            />
            <i class="eye-icon" @click="togglePasswordVisibility">
              <img
                :src="passwordVisible ? eyeOpenIcon : eyeClosedIcon"
                alt="eye icon"
              />
            </i>
          </div>
  
          <!-- 비밀번호 입력 에러 메시지 -->
          <span v-if="passwordError" class="error-message">{{ passwordError }}</span>
  
          <button class="login-btn" @click="login">로그인</button>
          <div class="sns-login">
            <hr />
            <span>SNS LOGIN</span>
            <hr />
          </div>
          <div class="sns-buttons">
            <button class="sns-btn kakao" @click="navigateToKakaoLogin"></button>
            <button class="sns-btn naver" @click="navigateToNaverLogin"></button>
          </div>
          <div class="login-options">
            <a href="#" @click.prevent="openFindId">아이디 찾기</a>
            <span class="divider">|</span>
            <a href="#" @click.prevent="openPasswordReset">비밀번호 찾기</a>
            <span class="divider">|</span>
            <a href="#" @click.prevent="goToRegister">회원가입</a>
          </div>
        </div>
  
        <!-- 계정 재활성화 모달 추가 -->
        <AccountReactivationModal
          v-if="isAccountReactivationModalVisible"
          @close="closeAccountReactivationModal"
          :userAuthId="username"
        />
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, inject } from 'vue';
  import { useRouter } from 'vue-router';
  import axios from 'axios';
  import eyeOpenIcon from '@/assets/images/eye_open.png';
  import eyeClosedIcon from '@/assets/images/eye_closed.png';
  import AccountReactivationModal from '@/components/top_nav/login/AccountReactivationModal.vue';
  
  // useRouter를 최상위 레벨에서 호출
  const router = useRouter();
  
  // 환경 변수로부터 카카오 로그인 관련 정보 불러오기
  const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${import.meta.env.VITE_KAKAO_REST_API_KEY}&redirect_uri=${import.meta.env.VITE_KAKAO_REDIRECT_URI}&response_type=code`;
  const NAVER_AUTH_URL = `https://nid.naver.com/oauth2.0/authorize?client_id=${import.meta.env.VITE_NAVER_CLIENT_ID}&redirect_uri=${import.meta.env.VITE_NAVER_REDIRECT_URI}&response_type=code&state=STATE_STRING`;
  
  // 외부에서 받아온 이벤트 정의
  const emit = defineEmits(['close', 'goToStep1', 'openPasswordReset', 'openFindId']);
  
  // 상태와 메서드 `inject`로 받아오기
  const token = inject('token'); // 토큰 상태
  const setTokenData = inject('setTokenData'); // 토큰 데이터 설정 함수
  
  const username = ref('');
  const password = ref('');
  
  // 에러 메시지 상태 변수 정의
  const usernameError = ref(''); // 아이디 에러 메시지 상태
  const passwordError = ref(''); // 비밀번호 에러 메시지 상태
  
  // 비밀번호 표시 여부 상태 관리
  const passwordVisible = ref(false);
  
  // 계정 재활성화 모달 상태 관리
  const isAccountReactivationModalVisible = ref(false);
  
  // 계정 재활성화 모달 열기 함수
  const openAccountReactivationModal = () => {
    isAccountReactivationModalVisible.value = true;
  };
  
  // 계정 재활성화 모달 닫기 함수
  const closeAccountReactivationModal = () => {
    isAccountReactivationModalVisible.value = false;
  };
  
  // 모달 닫기 함수
  const closeModal = () => {
    emit('close');
  };
  
  // 비밀번호 표시/숨기기 토글 함수
  const togglePasswordVisibility = () => {
    passwordVisible.value = !passwordVisible.value;
  };
  
  // 로그인 처리 함수
  const login = async () => {
    usernameError.value = '';
    passwordError.value = '';
  
    // 입력값 유효성 검사
    if (!username.value) {
      usernameError.value = '아이디를 입력하세요.';
      return;
    }
    if (!password.value) {
      passwordError.value = '비밀번호를 입력하세요.';
      return;
    }
  
    try {
      const response = await axios.post('/login', {
        user_auth_id: username.value,
        password: password.value,
        signup_path: 'NORMAL',
      });
  
      if (response.data.success) {
        const tokenData = {
          user_identifier: response.data.data.user_identifier,
          access_token: response.data.data.access_token,
          access_token_expiry: response.data.data.access_token_expiry,
          refresh_token: response.data.data.refresh_token,
          refresh_token_expiry: response.data.data.refresh_token_expiry,
        };
  
        setTokenData(tokenData); // 토큰 정보 설정
        closeModal(); // 모달 닫기
      } else {
        passwordError.value = response.data.error.message || '로그인에 실패했습니다.';
      }
    } catch (error) {
      if (error.response) {
        if (error.response.data.error.code === 40320) {
          openAccountReactivationModal(); // 계정 재활성화 모달 열기
        } else {
          passwordError.value = error.response.data.error.message || '로그인 요청이 거부되었습니다. 서버 상태를 확인해주세요.';
        }
      } else {
        passwordError.value = '로그인 요청이 실패했습니다. 서버 상태를 확인해주세요.';
      }
    }
  };
  
  // 아이디 찾기 모달 열기
  const openFindId = () => {
    emit('openFindId');
  };
  
  // 비밀번호 찾기 모달 열기
  const openPasswordReset = () => {
    emit('openPasswordReset');
  };
  
  // 회원가입 모달로 이동
  const goToRegister = () => {
    emit('goToStep1');
  };
  
  // 카카오 로그인 페이지로 리다이렉트
  const navigateToKakaoLogin = () => {
    window.location.href = KAKAO_AUTH_URL;
  };
  
  // 네이버 로그인 페이지로 리다이렉트
  const navigateToNaverLogin = () => {
    window.location.href = NAVER_AUTH_URL;
  };
  
  </script>
  
  <style scoped>
  /* 스타일 동일하게 유지 */
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 2000;
  }
  
  .modal-content {
    display: flex; /* Flexbox 레이아웃 사용 */
    flex-direction: column; /* 수직 정렬 */
    align-items: center; /* 수평 가운데 정렬 */
    justify-content: center;
  
    background-color: white;
    border-radius: 10px;
    width: 400px;
    height: 480px;
    padding: 2rem;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    position: relative;
    text-align: center;
    animation: slide-up 0.3s ease-out; /* 애니메이션 효과 추가 */
  }
  
  /* 슬라이드 애니메이션 */
  @keyframes slide-up {
    from {
      transform: translateY(50px);
      opacity: 0;
    }
    to {
      transform: translateY(0);
      opacity: 1;
    }
  }
  
  .close-btn {
    position: absolute;
    top: 1rem;
    right: 1rem;
    font-size: 3.2rem;
    opacity: 50%;
    background: none;
    border: none;
    cursor: pointer;
  }
  
  .modal-header h2 {
    margin: 2rem;
    font-size: 5rem;
    color: #a1b872;
  }
  
  .modal-body input {
    width: 360px;
    height: 40px;
    padding: 1rem;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 1.6rem;
    margin: 1rem;
  }
  
  /* 비밀번호 입력 필드 컨테이너 */
  .password-input-container {
    position: relative;
    display: flex;
    align-items: center;
  }
  
  /* 눈 아이콘 스타일링 */
  .eye-icon {
    position: absolute;
    right: 20px; /* 아이콘을 오른쪽 끝에 배치 */
    cursor: pointer;
  }
  
  .eye-icon img {
    width: 16px; /* 아이콘 너비 조정 */
    height: 16px; /* 아이콘 높이 조정 */
    opacity: 0.5;
  }
  
  .login-btn {
    width: 360px;
    height: 40px;
    border: none;
    background-color: #3E3E3E;
    color: #ffffff;
    text-align: center;
    font-size: 1.8rem;
    font-weight: bold;
    border-radius: 5px;
    cursor: pointer;
    margin-top: 6rem;
    margin-bottom: 2rem;
  }
  
  .sns-login {
    font-size: 1.5rem;
    display: flex;
    width: auto;
    align-items: center;
    margin: 2rem 0;
  }
  
  .sns-login hr {
    flex: 1;
    border: none;
    border-top: 1px solid #ddd;
  }
  
  .sns-login span {
    margin: 0 1.4rem;
    color: #888;
  }
  
  .sns-buttons {
    display: flex;
    justify-content: center;
    gap: 6.5rem;
    margin-bottom: 6rem;
  }
  
  .sns-btn {
    width: 5rem;
    height: 5rem;
    border-radius: 50%;
    border: none;
    cursor: pointer;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
  }
  
  .kakao {
    background-image: url('@/assets/images/btn_kakao.png');
  }
  
  .naver {
    background-image: url('@/assets/images/btn_naver.png');
  }
  
  .login-options {
    margin-top: 1.5rem;
    font-size: 1.4rem;
    color: #888;
    display: flex;
    justify-content: center;
    gap: 1.6rem;
  }
  
  .divider {
    color: #888;
  }
  
  .login-options a {
    color: #888888;
    text-decoration: none;
  }
  
  /* 컴포넌트 내에만 적용되는 에러 메시지 스타일 */
  .error-message {
    color: #E1523A;
    font-size: 1.4rem;  
    margin-top: 0.5rem; /* 위 여백 */
    margin-left: 1.5rem; /* 위 여백 */
    display: block; /* 블록 요소로 설정 */
    text-align: left;
  }
  
  </style>
  
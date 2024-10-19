<template>
    <div class="modal-overlay">
      <div class="modal-content">
        <button class="close-btn" @click="closeModal">×</button>
        
        <!-- 페이지 번호 표시 -->
        <div class="page-indicator">
          <span>1</span>
          <span>2</span>
        </div>
        <div class="modal-header">
          <h2>SGMA</h2>
        </div>
        <div class="modal-body">
          <div class="message-container"> <!-- 1번: 인사말 -->
            <p class="first-text">비밀번호를 찾으시나요?</p>
            <p class="second-text">아이디와 이메일을 입력해주세요.</p> <!-- 이름에서 아이디로 변경 -->
          </div>
          
          <div class="input-container"> <!-- 아이디 입력 필드로 변경 -->
            <input type="text" placeholder="아이디 입력" v-model="localPasswordResetData.user_auth_id" maxlength="24" />
            <span v-if="userAuthIdError" class="error-text">{{ userAuthIdError }}</span>
            
            <input type="email" placeholder="이메일 입력" v-model="localPasswordResetData.email" maxlength="24" />
            <!-- 이메일 유효성 검사 메시지 -->
            <span v-if="emailError" class="error-text">{{ emailError }}</span>
          </div>
          
          <div class="verification-container"> <!-- 인증번호 발송 버튼 -->
            <div class="checkbox-container">
              <div class="email-checkbox" :class="{ 'verified': isVerified }"></div>
              <span class="checkbox-text">이메일 인증</span>
            </div>
            <button class="verify-btn" @click="handleSendVerification">{{ verificationButtonText }}</button>
          </div>
          <!-- 인증 에러 및 성공 메시지 표시 -->
          <span v-if="verificationMessage" class="message-text">{{ verificationMessage }}</span>
          <span v-if="verificationError" class="error-text">{{ verificationError }}</span>
              
          <div class="code-input-container"> <!-- 인증코드 입력과 타이머 -->
            <div class="input-with-timer">
              <input type="text" placeholder="문자 6자리 입력" v-model="verificationCode" maxlength="6" />
              <span class="timer">{{ formattedTime }}</span>
            </div>
            <button class="confirm-btn" @click="confirmVerificationCode">확인</button>
          </div>
        </div>  
  
        <!-- 하단 버튼들 -->
        <div class="modal-footer">
          <YesNoButton
            type="cancel"
            label="취소"
            @click="goToLogin"
          />
          <YesNoButton
            type="next"
            label="다음"
            @click="goToPasswordResetStep2"
            :disabled="!canProceed"
          />
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, defineEmits, defineProps, watch } from 'vue';
  import axios from 'axios';
  import YesNoButton from '@/components/common/YesNoButton.vue'; // YesNoButton 컴포넌트 임포트
  
  // 이벤트 정의
  const emit = defineEmits(['close', 'goToPasswordResetStep2', 'openLogin', 'update']);
  
  // Navigation.vue에서 관리하는 데이터 받아오기
  const props = defineProps(['passwordResetData']);

  const localPasswordResetData = ref({ ...props.passwordResetData });
  
  const verificationCode = ref('');
  const verificationButtonText = ref('인증번호 발송');
  const timeRemaining = ref(0); // 타이머 설정
  const verificationMessage = ref('');
  const verificationError = ref('');
  const userAuthIdError = ref(''); // 아이디 입력 오류 메시지
  const emailError = ref(''); // 이메일 입력 오류 메시지
  const isVerified = ref(false);
  let timerInterval = null;
  
  // 입력값 변경 시 부모 컴포넌트로 업데이트
  watch(
    () => localPasswordResetData.value,
    (newData) => {
        console.log("Updated localPasswordResetData:", newData); // localPasswordResetData 로그 추가
      emit('update', newData);
    },
    { deep: true }
  );
  
  // 이메일 유효성 검사 정규 표현식
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const isEmailValid = computed(() => emailPattern.test(props.passwordResetData.email));
  
    // canProceed 계산된 속성: 인증이 완료되었을 때만 "다음" 버튼 활성화
    const canProceed = computed(() => {
    // return isVerified.value; // 인증이 완료되었을 때만 true 반환
    return true;
    });


  // 인증번호 발송 API 호출 함수 (비밀번호 찾기)
  const sendVerificationCode = async () => {
    try {
      const response = await axios.post('/user-service/api/users/verification-email/user-password', {
        user_auth_id: props.passwordResetData.user_auth_id,  // 아이디 및 이메일 발송
        email: props.passwordResetData.email,
      });
  
      if (response.data.success) {
        verificationMessage.value = '인증번호가 이메일로 발송되었습니다.';
        verificationError.value = '';
      } else {
        verificationError.value = response.data.error.message || '인증번호 발송에 실패했습니다.';
        verificationMessage.value = '';
      }
    } catch (error) {
      verificationError.value = error.response?.data?.message || '서버 오류로 인해 인증번호 발송에 실패했습니다.';
      verificationMessage.value = '';
    }
  };
  
  // 발송 버튼 클릭 시 호출
  const handleSendVerification = () => {
    userAuthIdError.value = '';
    emailError.value = '';
    verificationError.value = '';
    verificationMessage.value = '';
  
    if (props.passwordResetData.user_auth_id === '') {
      userAuthIdError.value = '아이디를 입력해주세요.';
      return;
    }
    
    if (!isEmailValid.value) {
      emailError.value = '유효한 이메일을 입력해주세요.';
      return;
    }
  
    verificationMessage.value = '인증번호를 전송 중입니다...';
    timeRemaining.value = 300; // 5분 타이머 설정
    sendVerificationCode();
  
    if (!timerInterval) {
      startTimer();
    }
  };
  
  // 타이머 함수
  const startTimer = () => {
    clearTimer();
    timerInterval = setInterval(() => {
      if (timeRemaining.value > 0) {
        timeRemaining.value--;
      } else {
        clearTimer();
      }
    }, 1000);
  };
  
  // 타이머 초기화 함수
  const clearTimer = () => {
    if (timerInterval) {
      clearInterval(timerInterval);
      timerInterval = null;
    }
  };
  
  // 인증번호 확인 API 호출
  const confirmVerificationCode = async () => {
    try {
      const response = await axios.post('/user-service/api/users/verification-email/confirmation', {
        email: props.passwordResetData.email,
        code: verificationCode.value,
      });
      if (response.data.success) {
        verificationMessage.value = '인증되었습니다.';
        verificationError.value = '';
        isVerified.value = true;
      } else {
        verificationError.value = response.data.message || '인증번호가 올바르지 않습니다.';
        verificationMessage.value = '';
      }
    } catch (error) {
      verificationError.value = error.response?.data?.message || '인증에 실패했습니다. 다시 시도해주세요.';
      verificationMessage.value = '';
    }
  };
  
  // 다음 단계로 이동
 // PasswordResetStep2로 이동하는 함수
const goToPasswordResetStep2 = () => {
    console.log("goToPasswordResetStep2 triggered!"); // 추가된 로그
    emit('goToPasswordResetStep2', localPasswordResetData.value);

};
  
  // 로그인 페이지로 이동
  const goToLogin = () => {
    emit('openLogin'); // openLogin 이벤트 방출 // 부모 컴포넌트에게 로그인 모달을 열라는 이벤트 전달
    emit('close'); // 비밀번호 모달 닫기
  };
  
  // 타이머 형식 변경
  const formattedTime = computed(() => {
    const minutes = String(Math.floor(timeRemaining.value / 60)).padStart(2, '0');
    const seconds = String(timeRemaining.value % 60).padStart(2, '0');
    return `${minutes}:${seconds}`;
  });
  </script>

  
<style scoped>
/* 오류 텍스트 스타일 */
.error-text {
  color: #E1523A;
  font-size: 1.4rem;
  margin-top: 0.2rem;
}

/* 성공 텍스트 스타일 */
.message-text {
  color: #02B853;
  font-size: 1.4rem;
  margin-top: 0.5rem;
}

/* 모달 오버레이 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5); /* 어두운 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

/* 모달 콘텐츠 */
.modal-content {
  background-color: white;
  border-radius: 10px;
  width: 400px;
  height: 480px; /* 모달의 높이를 고정 */
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  text-align: left; /* 왼쪽 정렬 */
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

/* 페이지 번호 표시 */
.page-indicator {
  position: absolute; /* modal-content를 기준으로 절대 위치 */
  top: 20px; /* 모달 콘텐츠 위쪽에 배치 */
  left: 20px; /* 오른쪽 끝에서 10px 떨어짐 */
  display: flex; /* 숫자들을 수평 배치 */
  align-items: center; /* 세로 가운데 정렬 */
  background-color: #E0E9C8; /* 배경색 */
  padding: 5px 10px; /* 안쪽 여백 */
  border-radius: 15px; /* 둥근 모서리 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); /* 그림자 */
}

.page-indicator span {
  font-size: 1.4rem; /* 숫자 크기 */
  color: #525150; /* 숫자 색상 */
  margin: 0 5px; /* 숫자 사이 간격 */
}

.page-indicator span:nth-child(1) {
  font-weight: 700; /* 현재 페이지 강조 */
}

.page-indicator span:nth-child(2) {
  color: #525150; /* 총 페이지 수 흐리게 */
}

/* 모달 닫기 버튼 */
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

/* 모달 헤더 */
.modal-header {
  text-align: center;
}

.modal-header h2 {
  margin: 2rem;
  font-size: 5rem;
  color: #a1b872;
}

/* 모달 바디 */
.modal-body {
  display: flex; /* 플렉스박스 사용 */
  flex-direction: column; /* 수직 정렬 */
  justify-content: space-between; /* 각 행 간격 조정 */
  align-items: flex-start; /* 모든 요소들을 왼쪽 정렬 */
  gap: 1rem; /* 각 요소들 사이의 간격 */
  margin: 0.5rem;
}

/* 인사말 텍스트 */
.message-container {
  display: flex;
  flex-direction: column;
}

.first-text {
  font-size: 2.2rem;
  color: #525150;
}
.second-text {
  font-size: 2.2rem;
  font-weight: 700;
  color: #525150;
  margin-bottom: 1rem;
}

/* 입력 필드 */
.input-container {
  display: flex;
  flex-direction: column;
  gap: 1rem; /* 입력 필드 간격 */
  margin-top: 2rem;
}

.modal-body input {
  width: 360px;
  height: 40px;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1.6rem;
}

/* 인증번호 발송 */
.verification-container {
  display: flex;
  justify-content: space-between; /* 요소들을 양쪽에 배치 */
  align-items: center;
  width: 100%;
}

.checkbox-container {
  display: flex;
  align-items: center;
}

/* 인증 전 원형 스타일 */
.email-checkbox {
  width: 18px; /* 체크박스 크기 조정 */
  height: 18px;
  border-radius: 50%;
  margin-right: 10px; /* 체크박스와 텍스트 사이 간격 */
  cursor: pointer;
  background-color: #d5d5d5; /* 인증 전 기본 회색 */
}

/* 인증 후 체크박스 스타일 */
.email-checkbox.verified {
  background-image: url('@/assets/images/checkbox_checked.png'); /* 인증 성공 체크박스 이미지 */
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  background-color: transparent; /* 배경 투명 */
}

.checkbox-text {
  font-size: 1.8rem; /* 텍스트 크기 */
  font-weight: 600;
  color: #525150; /* 텍스트 색상 */
}

/* 인증번호 발송 버튼 */
.verify-btn {
  width: 100px;
  height: 40px;
  border: 1px solid #525150;
  border-radius: 5px;
  background-color: #fff;
  font-size: 1.6rem;
  cursor: pointer;
  color: #525150;
}

/* 인증코드 입력 필드 및 타이머 */
.code-input-container {
  display: flex;
  justify-content: space-between; /* 좌우 공간 배분 */
  align-items: center;
  width: 100%;
  margin-top: 2rem;
}

.input-with-timer {
  position: relative;
  width: 70%; /* 입력 필드 넓이 설정 */
}

.input-with-timer input {
  width: 100%;
  padding-right: 60px; /* 타이머 공간 확보 */
}

.input-with-timer .timer {
  position: absolute;
  right: 10px; /* 타이머 위치 조정 */
  top: 50%;
  transform: translateY(-50%);
  color: #e99a24;
  font-size: 1.4rem;
}

/* 확인 버튼 */
.confirm-btn {
  width: 100px;
  height: 40px;
  padding: 0.8rem 1.5rem;
  background-color: #a1b872;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 2rem;
  margin-left: 10px; /* 간격 추가 */
}

/* 모달 푸터 */
.modal-footer {
  position: absolute; /* 모달 콘텐츠 내에서 절대 위치 지정 */
  bottom: 10px; /* 모달 콘텐츠의 하단에 배치 */
  width: 360px; /* 푸터의 너비 */
  justify-content: space-between; /* 요소들 간의 공간을 균등 분배 */
  display: flex; /* 플렉스 박스 사용 */
  background-color: white; /* 배경색이 바디와 같게 */
  margin: 0 auto; /* 좌우 가운데 정렬 */
  left: 0; /* 부모 요소 기준 왼쪽 기준점 */
  right: 0; /* 부모 요소 기준 오른쪽 기준점 */
}
</style>
  
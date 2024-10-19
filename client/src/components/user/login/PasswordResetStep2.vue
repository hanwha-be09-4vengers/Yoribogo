<template>
    <div class="modal-overlay">
      <div class="modal-content">
        <button class="close-btn" @click="closeModal">×</button>
  
        <!-- 페이지 번호 표시 -->
        <div class="page-indicator">
          <span>2</span>
          <span>2</span>
        </div>
  
        <div class="modal-header">
          <h2>SGMA</h2>
        </div>
  
        <div class="modal-body">
          <div class="message-container">
            <p class="first-text">{{ passwordResetData.user_auth_id }}님</p>
            <p class="second-text">비밀번호를 변경해주세요.</p>
          </div>
  
          <!-- 새 비밀번호 입력 -->
          <div class="input-container">
            <input
              :type="newPasswordVisible ? 'text' : 'password'"
              placeholder="새 비밀번호 입력"
              v-model="newPassword"
              maxlength="24"
            />
            <!-- 눈 아이콘 -->
            <i class="eye-icon" @click="toggleNewPasswordVisibility">
              <img :src="newPasswordVisible ? eyeOpenIcon : eyeClosedIcon" alt="eye icon" />
            </i>
          </div>
          <!-- 비밀번호 길이 에러 -->
          <span v-if="passwordError" class="error-text">{{ passwordError }}</span>
  
          <!-- 새 비밀번호 확인 -->
          <div class="input-container">
            <input
              :type="confirmPasswordVisible ? 'text' : 'password'"
              placeholder="새 비밀번호 확인"
              v-model="confirmPassword"
              maxlength="24"
            />
            <!-- 눈 아이콘 -->
            <i class="eye-icon" @click="toggleConfirmPasswordVisibility">
              <img :src="confirmPasswordVisible ? eyeOpenIcon : eyeClosedIcon" alt="eye icon" />
            </i>
          </div>
          <!-- 비밀번호 일치 여부 -->
          <span v-if="passwordMatchMessage" :class="passwordMatchClass">{{ passwordMatchMessage }}</span>
        </div>
  
        <!-- 하단 버튼들 -->
        <div class="modal-footer">
          <YesNoButton
            type="cancel"
            label="취소"
            @click="goToStep1"
          />
          <YesNoButton
            type="next"
            label="비밀번호 변경"
            @click="resetPassword"
            :disabled="!canSubmit"
          />
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, defineProps, defineEmits, watch } from 'vue';
  import axios from 'axios';
  import YesNoButton from '@/components/common/YesNoButton.vue';
  import eyeOpenIcon from '@/assets/images/eye_open.png';
  import eyeClosedIcon from '@/assets/images/eye_closed.png';
  
  const emit = defineEmits(['close', 'openPasswordReset']);
  
  // Navigation.vue에서 전달받은 passwordResetData
  const props = defineProps({
    passwordResetData: {
      type: Object,
      required: true,
      default: () => ({
        user_auth_id: '', // 기본값 설정
      }),
    },
  });
  
  // 새 비밀번호, 비밀번호 확인 관련 변수들
  const newPassword = ref('');
  const confirmPassword = ref('');
  const passwordError = ref(''); // 비밀번호 길이 에러 메시지
  const passwordMatchMessage = ref(''); // 비밀번호 일치 여부 메시지
  const passwordMatchClass = ref(''); // 에러 혹은 성공 스타일 클래스
  const newPasswordVisible = ref(false); // 새 비밀번호 표시 여부
  const confirmPasswordVisible = ref(false); // 비밀번호 확인 표시 여부
  
  // 눈 아이콘 토글 함수
  const toggleNewPasswordVisibility = () => {
    newPasswordVisible.value = !newPasswordVisible.value;
  };
  
  const toggleConfirmPasswordVisibility = () => {
    confirmPasswordVisible.value = !confirmPasswordVisible.value;
  };
  
  // 비밀번호 유효성 검사 함수
  const isPasswordValid = computed(() => {
    return newPassword.value.length >= 6;
  });
  
  // 제출 가능 여부 (비밀번호 확인이 일치하는지 체크)
  const canSubmit = computed(() => {
    // return (
    //   newPassword.value.length > 0 &&
    //   newPassword.value === confirmPassword.value &&
    //   isPasswordValid.value
    // );
    return true;
  });
  
  // 비밀번호 재설정 API 호출 함수
  const resetPassword = async () => {
    // 오류 메시지 초기화
    passwordError.value = '';
    passwordMatchMessage.value = '';
  
    // 유효성 검사
    if (!isPasswordValid.value) {
      passwordError.value = '비밀번호는 6자 이상이어야 합니다.';
      return;
    }
  
    if (newPassword.value !== confirmPassword.value) {
      passwordMatchMessage.value = '비밀번호를 다시 확인해주세요.';
      passwordMatchClass.value = 'error-text'; // 빨간색 스타일
      return;
    }
  
    // 비밀번호 재설정 API 호출 (POST)
    try {
      const response = await axios.post(`/user-service/api/users/re-password`, {
        user_auth_id: props.passwordResetData.user_auth_id,
        password: newPassword.value,
      });
  
      if (response.data.success) {
        alert('비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요!');
        closeModal();
      } else {
        alert('비밀번호 변경에 실패했습니다.');
      }
    } catch (error) {
      alert('서버 오류가 발생했습니다.');
      console.error(error);
    }
  };
  
  // Step1로 돌아가기
  const goToStep1 = () => {
    emit('openPasswordReset');
  };
  
  // 모달 닫기 함수
  const closeModal = () => {
    emit('close');
  };
  
  // 비밀번호 일치 여부 및 길이 검사
  watch([newPassword, confirmPassword], () => {
    if (newPassword.value.length > 0 && confirmPassword.value.length > 0) {
      if (newPassword.value === confirmPassword.value) {
        passwordMatchMessage.value = '비밀번호가 일치합니다!';
        passwordMatchClass.value = 'success-text'; // 초록색 스타일
      } else {
        passwordMatchMessage.value = '비밀번호를 다시 확인해주세요.';
        passwordMatchClass.value = 'error-text'; // 빨간색 스타일
      }
    } else {
      passwordMatchMessage.value = '';
    }
  
    if (newPassword.value.length > 0 && !isPasswordValid.value) {
      passwordError.value = '비밀번호는 6자 이상이어야 합니다.';
    } else {
      passwordError.value = '';
    }
  });
  </script>
  
  <style scoped>
  /* 오류 텍스트 스타일 */
  .error-text {
    color: #e1523a;
    font-size: 1.4rem;
    margin-top: 0.2rem;
  }
  
  /* 성공 텍스트 스타일 */
  .success-text {
    color: #02b853;
    font-size: 1.4rem;
    margin-top: 0.2rem;
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
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: flex-start;
    gap: 1rem;
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
  
  /* 입력 필드 스타일 */
  .input-container {
    position: relative;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    margin-top: 2rem;
  }
  
  .input-container input {
    width: 360px;
    height: 40px;
    padding: 1rem;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 1.6rem;
  }
  
  /* 눈 아이콘 스타일링 */
  .eye-icon {
    position: absolute;
    right: 10px;
    top: 30%;
    cursor: pointer;
  }
  
  .eye-icon img {
    width: 16px;
    height: 16px;
    opacity: 0.5;
  }
  
  /* 모달 오버레이 */
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
  
  /* 모달 콘텐츠 */
  .modal-content {
    background-color: white;
    border-radius: 10px;
    width: 400px;
    height: 480px;
    padding: 2rem;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    position: relative;
    text-align: left;
    animation: slide-up 0.3s ease-out;
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
    position: absolute;
    top: 20px;
    left: 20px;
    display: flex;
    align-items: center;
    background-color: #e0e9c8;
    padding: 5px 10px;
    border-radius: 15px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  }
  
  .page-indicator span {
    font-size: 1.4rem;
    color: #525150;
    margin: 0 5px;
  }
  
  .page-indicator span:nth-child(1) {
    font-weight: 700;
  }
  
  .page-indicator span:nth-child(2) {
    color: #525150;
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
  
  /* 모달 푸터 */
  .modal-footer {
    position: absolute;
    bottom: 10px;
    width: 360px;
    justify-content: space-between;
    display: flex;
    background-color: white;
    margin: 0 auto;
    left: 0;
    right: 0;
  }
  </style>
  
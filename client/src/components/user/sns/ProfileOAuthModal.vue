<template>
    <div class="modal-overlay" v-if="showProfileModal">
      <div class="modal-content">
        <button class="close-btn" @click="closeModal">×</button>
  
        <div class="modal-header">
          <h2>SGMA</h2>
        </div>
  
        <div class="modal-body">
          <div class="message-container">
            <p class="first-text">어서와요!</p>
            <p class="second-text">프로필 사진과 닉네임을 설정해주세요.<br>닉네임을 설정하셔야 해요!</p>
          </div>
  
          <div class="profile-picture-container">
            <label class="profile-picture-label">
              <div class="profile-picture">
                <input type="file" @change="handleFileChange" accept="image/*" hidden />
                <span v-if="!profilePicture">+</span>
                <img v-if="profilePicture" :src="profilePicture" alt="Profile Picture" />
              </div>
            </label>
  
            <div class="nickname-container">
              <input type="text" placeholder="닉네임 입력" v-model="nickname" maxlength="30" />
              <button class="check-btn" @click="checkNicknameDuplication">중복 확인</button>
            </div>
  
            <span v-if="nicknameError" class="error-text">{{ nicknameError }}</span>
            <span v-if="nicknameDuplicationStatus" :class="{'success-text': !nicknameError, 'error-text': nicknameError}">
              {{ nicknameDuplicationStatus }}
            </span>
          </div>
        </div>
  
        <div class="modal-footer">
          <YesNoButton type="cancel" label="로그아웃" @click="logout"/>
          <YesNoButton type="complete" 
           label="완료"
          @click="completeUpdateProfile" :disabled="!canComplete"
          />
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, inject , onMounted, onUnmounted} from 'vue';
  import YesNoButton from '@/components/common/YesNoButton.vue';
  import { useRouter, onBeforeRouteLeave  } from 'vue-router';
  import axios from 'axios';
  
  // 상태와 메서드 `inject`로 받아오기
  const token = inject('token');
  const setTokenData = inject('setTokenData');
  
  // 모달 가시성을 제어하는 변수
  const showProfileModal = ref(true); // true로 설정하여 모달을 처음에 표시함
  
  // 닉네임과 프로필 사진을 위한 데이터
  const nickname = ref('');
  const profilePicture = ref('');
  const nicknameError = ref('');
  const isNicknameAvailable = ref(false);
  const nicknameDuplicationStatus = ref('');

  // 닉네임만 입력되어야 완료 버튼이 활성화됨
  const canComplete = computed(
    () => nickname.value !== '' && isNicknameAvailable.value);
  
  // 라우터 사용
  const router = useRouter();
  
  // 파일 선택 시 호출되는 함수
  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        profilePicture.value = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  };
  
// 닉네임 중복 확인 함수
const checkNicknameDuplication = async () => {
  nicknameError.value = '';
  nicknameDuplicationStatus.value = '';
  isNicknameAvailable.value = false;

  if (!nickname.value) {
    nicknameError.value = '닉네임을 입력해주세요.';
    return;
  }

  try {
    const response = await axios.post('/api/users/nickname/validate', { nickname: nickname.value });
    console.log('닉네임 중복 검증 응답',response); // 응답 구조 확인
    if (response.data.success) {
  
      if (response.data.data.exist) {
        console.log('response.data.data.exist:', response.data.data.exist);
        // exist가 true인 경우, 이미 존재하는 닉네임임
        nicknameError.value = '이미 사용 중인 닉네임입니다.';
      } else {
        console.log('response.data.data.exist:', response.data.data.exist);
        // exist가 false인 경우, 사용 가능한 닉네임임
        nicknameDuplicationStatus.value = '사용 가능한 닉네임입니다.';
        isNicknameAvailable.value = true;
      }
    } else {
      nicknameError.value = '닉네임 중복 확인 중 오류가 발생했습니다.';
    }
  } catch (error) {
    nicknameError.value = '닉네임 중복 확인 중 오류가 발생했습니다.';
    console.error('checkNicknameDuplication 에러:', error);
  }
};


  // 프로필 업데이트 완료 함수
  const completeUpdateProfile = async () => {
    if (!canComplete.value) return;
  
    try {
      const formData = new FormData();
      formData.append('nickname', nickname.value);
  
      if (profilePicture.value) {
        formData.append('profile_image', profilePicture.value);
      }
  
      const response = await axios.patch(`/api/users/${token.userId}/profile`, formData, {
        headers: { Authorization: `Bearer ${token.accessToken}` }
      });
  
      if (response.data.success) {
        alert('프로필이 성공적으로 업데이트되었습니다!');
        closeModal();
        router.push('/'); // 홈 페이지로 이동
      } else {
        alert(`프로필 수정 중 오류가 발생했습니다: ${response.data.message}`);
      }
    } catch (error) {
      alert('프로필 수정 중 오류가 발생했습니다. 다시 시도해주세요.');
      console.error('completeUpdateProfile 에러:', error);
    }
  };
  
  // 로그아웃 함수
  const logout = () => {
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
    router.push('/'); // 홈 페이지로 이동
  };
  
  // 모달 닫기 함수 - 닉네임을 입력하지 않았을 경우 경고창을 표시
  const closeModal = () => {
    if (!nickname.value || !isNicknameAvailable.value) {
      alert('닉네임을 설정하셔야 합니다!');
    } else {
      showProfileModal.value = false; // 모달 창 닫기
    }
  };

// 사용자가 페이지를 떠나려고 할 때 막음
const handlePopState = (event) => {
  if (showProfileModal.value && (!nickname.value || !isNicknameAvailable.value)) {
    const leave = confirm('닉네임 설정이 완료되지 않았습니다. 정말로 페이지를 떠나시겠습니까?');
    if (!leave) {
      event.preventDefault();
      window.history.pushState(null, null, window.location.href); // 뒤로가기를 막음
    } else {
      router.go(-1);  // 정상적으로 페이지를 이동
    }
  }
};
onMounted(() => {
  window.addEventListener('popstate', handlePopState);  // 브라우저의 뒤로가기를 감지
});

onUnmounted(() => {
  window.removeEventListener('popstate', handlePopState);  // 이벤트 제거
});

  </script>

  <style scoped>
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
    height: 480px; /* 높이를 살짝 늘려줌 */
    padding: 2rem;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 그림자 효과 강화 */
    position: relative;
    text-align: left;
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
    display: flex;
    flex-direction: column;
    justify-content: flex-start; /* 상단부터 배치 */
    gap: 1.5rem; /* 간격을 더 넓게 */
    margin: 0.5rem;
    padding: 1rem;
  }
  
  /* 인사말 텍스트 */
  .message-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start; /* 인사말은 왼쪽 정렬 */
  }
  
  .first-text {
    font-size: 2.4rem; /* 조금 더 크고 선명하게 */
    color: #525150;
  }
  
  .second-text {
    font-size: 2.2rem;
    font-weight: 700;
    color: #525150;
    margin-bottom: 1rem;
  }
  
  /* 프로필 사진과 닉네임 입력 */
  .profile-picture-container {
    display: flex;
    flex-direction: column;
    align-items: center; /* 프로필 사진과 닉네임을 가운데 정렬 */
    gap: 2rem;
    margin-top: 2rem;
    padding: 1rem;
  }
  
  .profile-picture-label {
    cursor: pointer;
  }
  
  .profile-picture {
    width: 140px;
    height: 140px;
    background-color: #f5f5f5;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    border: 2px dashed #a1b872; /* 두께를 늘리고 선명한 색상 */
    position: relative;
    overflow: hidden;
    transition: border 0.3s ease-in-out; /* 호버 시 부드럽게 전환 */
  }
  
  .profile-picture:hover {
    border: 2px solid #a1b872; /* 호버 시 실선으로 변경 */
  }
  
  .profile-picture span {
    font-size: 4rem; /* 폰트 크기 증가 */
    color: #a1b872;
  }
  
  .profile-picture img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    position: absolute;
    top: 0;
    left: 0;
  }
  
  /* 닉네임 입력 및 중복 확인 */
  .nickname-container {
    display: flex;
    align-items: center;
    gap: 1rem; /* 닉네임 필드와 버튼 간의 간격 */
  }
  
  input[type="text"] {
    width: 140px;
    height: 40px;
    padding: 0.8rem;
    border: 1.5px solid #c5ccd2;
    border-radius: 5px;
    font-size: 1.6rem;
    text-align: center; /* 텍스트 가운데 정렬 */
    transition: border-color 0.3s ease-in-out; /* 포커스 전환 효과 */
  }
  
  input[type="text"]:focus {
    border-color: #a1b872; /* 포커스 시 색상 변경 */
    outline: none;
  }
  
  /* 중복 확인 버튼 */
  .check-btn {
    padding: 0.6rem 1rem;
    height: 40px;
    font-size: 1.6rem;
    border: none;
    border-radius: 5px;
    background-color: #a1b872;
    color: white;
    cursor: pointer;
  }
  
  .check-btn:hover {
    background-color: #8aa15e;
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
  
  /* 오류 텍스트 스타일 */
  .error-text {
    color: red;
    font-size: 1.4rem;
    margin-top: 0.2rem;
  }
  
  /* 성공 텍스트 스타일 */
  .success-text {
    color: green;
    font-size: 1.4rem;
    margin-top: 0.2rem;
  }
  </style>
  
  
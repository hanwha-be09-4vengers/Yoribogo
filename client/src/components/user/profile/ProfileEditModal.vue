  <template>
      <div class="modal-overlay" @click.self="closeModal">
        <div class="modal-content">
          <button class="close-btn" @click="closeModal">×</button>
    
          <div class="modal-header">
            <h2>프로필 수정</h2>
          </div>
    
          <div class="modal-body">
            <div class="message-container">
              <p class="first-text">안녕하세요!</p>
              <p class="second-text">프로필 사진과 닉네임을 설정해주세요.</p>
            </div>
    
            <div class="profile-picture-container">
              <label class="profile-picture-label">
                <div class="profile-picture">
                  <input type="file" @change="handleFileChange" accept="image/*" hidden />
                  <span v-if="!profilePicture">+</span>
                  <img v-if="profilePicture" :src="profilePicture" alt="Profile Picture" />
                </div>
              </label>
    
              <!-- 닉네임 입력 및 중복 확인 버튼 -->
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
            <YesNoButton type="cancel" label="취소" @click="closeModal" />
            <YesNoButton type="complete" label="완료" @click="completeUpdateProfile" :disabled="!canComplete" />
          </div>
        </div>
      </div>
    </template>
    
    <script setup>
    import { ref, computed, inject } from 'vue';
    import YesNoButton from '@/components/common/YesNoButton.vue'; // YesNoButton 컴포넌트 임포트
    import { updateUserProfile, validateNickname } from '@/api/user'; // 필요한 API 함수들 임포트
    import { useRouter } from 'vue-router'; // vue-router 임포트
    
    // 이벤트 방출 함수
    const emit = defineEmits(['close']);
    
    // 라우터 사용 설정
    const router = useRouter();
    
    // 토큰 정보 주입
    const token = inject('token');
    
    // 닉네임과 프로필 사진을 위한 데이터
    const nickname = ref('');
    const profilePicture = ref('');
    const nicknameError = ref('');
    const isNicknameAvailable = ref(false); // 닉네임 사용 가능 여부
    const nicknameDuplicationStatus = ref(''); // 닉네임 중복 검증 결과 메시지
    
    // 닉네임만 입력되어야만 완료 버튼이 활성화됨
    const canComplete = computed(() => {
      return nickname.value !== '' && isNicknameAvailable.value;
    });
    
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
        const response = await validateNickname(nickname.value);
        if (response && response.success) {
          if (!response.data.exist) {
            nicknameDuplicationStatus.value = '사용 가능한 닉네임입니다.';
            isNicknameAvailable.value = true;
          } else {
            nicknameError.value = '이미 사용 중인 닉네임입니다.';
          }
        } else {
          nicknameError.value = '닉네임 중복 확인 중 오류가 발생했습니다.';
        }
      } catch (error) {
        nicknameError.value = '닉네임 중복 확인 중 오류가 발생했습니다.';
        console.error('checkNicknameDuplication 에러:', error);
      }
    };
    
    // completeUpdateProfile 함수
    const completeUpdateProfile = async () => {
      if (!canComplete.value) return;
    
      try {
        // FormData 객체 생성
        const formData = new FormData();
        formData.append('nickname', nickname.value);
    
        // profilePicture가 있을 때는 파일을, 없을 때는 null을 추가
        if (profilePicture.value) {
          formData.append('profile_image', profilePicture.value);
        } else {
          formData.append('profile_image', null); // 파일이 선택되지 않은 경우 null로 추가
        }
    
        console.log("userId:", token.userId);
        console.log("accessToken:", token.accessToken); // accessToken 값을 출력하여 확인
    
        const response = await updateUserProfile(token.userId, formData, token.accessToken);
    
        if (response.success) {
          alert('프로필이 성공적으로 업데이트되었습니다!');
          closeModal();
          router.push('/mypage'); // 마이페이지로 이동
        } else {
          alert(`프로필 수정 중 오류가 발생했습니다: ${response.message}`);
        }
      } catch (error) {
        if (error.response && error.response.status === 403) {
          alert('접근 권한이 없습니다. 다시 로그인해주세요.');
        } else {
          alert('프로필 수정 중 오류가 발생했습니다. 다시 시도해주세요.');
        }
        console.error('completeUpdateProfile 에러:', error);
      }
    };
    
    // 모달 닫기 함수
    const closeModal = () => {
      emit('close');
    };
    </script>
    
    <style scoped>
    /* 모달 오버레이 */
    .modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999; /* 화면 최상위에 오도록 */
}

.modal-content {
  background-color: white;
  border-radius: 10px;
  width: 400px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

    
    /* 슬라이드 애니메이션 */
    @keyframes slide-up {
      from {
        transform: translate(-50%, -40%);
        opacity: 0;
      }
      to {
        transform: translate(-50%, -50%);
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
      font-size: 3rem;
      color: #a1b872;
    }
    
    /* 모달 바디 */
    .modal-body {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      gap: 1.5rem;
      margin: 0.5rem;
      padding: 1rem;
    }
    
    /* 프로필 사진과 닉네임 입력 */
    .profile-picture-container {
      display: flex;
      flex-direction: column;
      align-items: center;
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
      border: 2px dashed #a1b872;
      position: relative;
      overflow: hidden;
      transition: border 0.3s ease-in-out;
    }
    
    .profile-picture:hover {
      border: 2px solid #a1b872;
    }
    
    .profile-picture span {
      font-size: 4rem;
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
      gap: 1rem;
    }
    
    input[type="text"] {
      width: 140px;
      height: 40px;
      padding: 0.8rem;
      border: 1.5px solid #c5ccd2;
      border-radius: 5px;
      font-size: 1.6rem;
      text-align: center;
      transition: border-color 0.3s ease-in-out;
    }
    
    input[type="text"]:focus {
      border-color: #a1b872;
      outline: none;
    }
    
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
      display: flex;
      justify-content: space-between;
      width: 100%;
      padding: 1rem 0;
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
    
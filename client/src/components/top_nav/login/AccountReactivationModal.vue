<template>
    <div class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h2>휴먼 계정 안내</h2>
        </div>
        <div class="modal-body">
          <div class="message-container">
            <p class="first-text">오랜만이에요.</p>
            <p class="second-text">계정을 재활성화해주세요!</p>
          </div>
          <div class="modal-footer">
            <YesNoButton type="cancel" label="취소" @click="closeModal" />
            <YesNoButton type="confirm" label="확인" @click="reactivateUser" />
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import axios from 'axios';
  import YesNoButton from '@/components/common/YesNoButton.vue';
  
  const props = defineProps({
    userAuthId: String,
  });
  
  const emit = defineEmits(['close']);
  
  const reactivateUser = async () => {
    try {
      const response = await axios.post('/user-service/api/users/activate', null, {
        params: {
          userAuthId: props.userAuthId,
        },
      });
  
      if (response.data.success) {
        alert('계정이 재활성화되었습니다. 다시 로그인 해주세요.');
        closeModal();
      } else {
        alert('재활성화에 실패했습니다.');
      }
    } catch (error) {
      alert('서버 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };
  
  const closeModal = () => {
    emit('close');
  };
  </script>
  
  <style scoped>
  /* 모달 헤더 */
  .modal-header {
    text-align: center;
  }
  
  .modal-header h2 {
    margin: 2rem;
    font-size: 3rem;
    color: #a1b872;
  }
  
  /* 모달 오버레이 */
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  /* 모달 콘텐츠 */
  .modal-content {
    background-color: white;
    border-radius: 10px;
    width: 360px;
    height: 260px;
    padding: 20px;
    text-align: center;
  }
  
  /* 모달 바디 */
  .modal-body {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 1rem;
    margin: 0.5rem;
    text-align: center; /* 모달 바디 내 텍스트 가운데 정렬 */
  }
  
  /* 인사말 텍스트 */
  .message-container {
    display: flex;
    flex-direction: column;
    align-items: center; /* 텍스트 가운데 정렬 */
    text-align: center;  /* 텍스트 가운데 정렬 */
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
  
  /* 모달 푸터 */
  .modal-footer {
    position: relative;
    width: 320px;
    
    display: flex;
    justify-content: space-between;
    margin-top: 30px;
  }
  </style>
  
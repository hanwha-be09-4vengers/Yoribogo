<template>
    <div class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <button class="close-btn" @click="closeModal">×</button>
        <div class="modal-header">
          <h2>계정 설정</h2>
        </div>
        <div class="modal-body">
          <button class="modal-btn" @click="openChangePasswordModal">비밀번호 변경</button>
          <button class="modal-btn" @click="openAccountDeactivationModal">회원탈퇴</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  const emit = defineEmits(['close', 'openChangePasswordModal', 'openAccountDeactivationModal']);
  
  const closeModal = () => {
    emit('close');
  };
  
  const openChangePasswordModal = () => {
    emit('openChangePasswordModal');
  };
  
  const openAccountDeactivationModal = () => {
    emit('openAccountDeactivationModal');
  };
  </script>
  
  <style scoped>
  /* 모달 전체 오버레이 */
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
    z-index: 9999; /* 최상단으로 위치 */
  }
  
  /* 모달 콘텐츠 */
  .modal-content {
    background-color: white;
    padding: 2rem;
    border-radius: 10px;
    width: 400px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 부드러운 그림자 효과 */
    text-align: center;
  }
  
  /* 모달 닫기 버튼 */
  .close-btn {
    position: absolute;
    top: 1rem;
    right: 1rem;
    font-size: 2rem;
    background: none;
    border: none;
    cursor: pointer;
  }
  
  /* 모달 제목 스타일 */
  .modal-header h2 {
    margin: 0;
    font-size: 2rem;
    color: #333;
  }
  
  /* 모달 본문 버튼 스타일 */
  .modal-body {
    margin-top: 2rem;
  }
  
  .modal-btn {
    display: block;
    width: 100%;
    padding: 1rem;
    margin: 1rem 0;
    background-color: #a1b872;
    color: white;
    border: none;
    border-radius: 5px;
    font-size: 1.2rem;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  .modal-btn:hover {
    background-color: #8aa15e; /* 호버 시 살짝 어두운 색으로 변경 */
  }
  
  </style>
  
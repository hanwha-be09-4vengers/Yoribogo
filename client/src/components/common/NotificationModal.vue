<template>
    <div class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <h2>알림</h2>
        <ul class="notification-list">
          <li class="notification" @click="goMyPage">
            <span>알림: 회원가입을 축하합니다!</span>
          </li>
          <li class="notification">
            <span>알림: 요리보고에 오신것을 환영합니다~~</span>
          </li>
          <li class="notification">
            <span>알림: 요리보고에 오신것을 환영합니다~~</span>
          </li>
          <li class="notification">
            <span>알림: 요리보고에 오신것을 환영합니다~~</span>
          </li>
        </ul>
      </div>
    </div>
  </template>
  
  <script setup>
  const emit = defineEmits(['close']); // 부모 컴포넌트로 close 이벤트 전달
  
  const closeModal = () => {
    emit('close'); // 부모 컴포넌트로 close 이벤트를 전달
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
  }
  
  .modal-content {
    background-color: white;
    padding: 2rem;
    border-radius: 10px;
    width: 40rem;
    max-width: 90%;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .notification-list {
    list-style: none;
    padding: 0;
  }
  
  .notification {
    padding: 1rem;
    border-bottom: 1px solid #eee;
    cursor: pointer;
  }
  
  .notification:hover {
    background-color: var(--light-pink-color);
  }
  
  </style>
  
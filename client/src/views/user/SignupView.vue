<template>
    <div class="signup-container">
      <h1>회원가입</h1>
      <form @submit.prevent="handleSignup">
        <div class="form-group">
          <label for="email">이메일</label>
          <input v-model="email" type="email" id="email" required />
        </div>
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input v-model="password" type="password" id="password" required />
        </div>
        <div class="form-group">
          <label for="confirm-password">비밀번호 확인</label>
          <input v-model="confirmPassword" type="password" id="confirm-password" required />
        </div>
        <button type="submit">회원가입</button>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  
  const email = ref('');
  const password = ref('');
  const confirmPassword = ref('');
  const router = useRouter();
  
  const handleSignup = () => {
    // 회원가입 로직 구현
    if (password.value === confirmPassword.value) {
      // 여기에서 API 호출 또는 회원가입 로직 처리
      alert('회원가입 성공');
      router.push('/login');  // 회원가입 성공 후 로그인 페이지로 이동
    } else {
      alert('비밀번호가 일치하지 않습니다.');
    }
  };
  </script>
  
  <style scoped>
  .signup-container {
    max-width: 400px;
    margin: 2rem auto;
    padding: 2rem;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #fff;
  }
  
  .form-group {
    margin-bottom: 1.5rem;
  }
  
  label {
    display: block;
    font-size: 1.2rem;
    margin-bottom: 0.5rem;
  }
  
  input {
    width: 100%;
    padding: 0.8rem;
    font-size: 1rem;
    border-radius: 4px;
    border: 1px solid #ccc;
  }
  
  button {
    width: 100%;
    padding: 1rem;
    font-size: 1.2rem;
    color: #fff;
    background-color: #007bff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #0056b3;
  }
  </style>
  
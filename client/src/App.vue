<template>
  <router-view /> <!-- 여기에 라우터가 활성화된 컴포넌트를 렌더링 -->
</template>

<script setup>
import { onMounted } from 'vue';
import { useTokenStore } from '@/stores/tokenStore'; // 스토어 임포트

// Pinia 스토어 사용
const tokenStore = useTokenStore();

// 컴포넌트가 마운트될 때 초기화 함수 호출 및 SSE 연결 테스트
onMounted(() => {
  // 로그인 상태 초기화
  tokenStore.initializeState();

});
</script>

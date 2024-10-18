import apiClient from '@/api/axios';

export const addLike = (userId, postId) => {
    console.log('Sending like request:', { userId, postId });       // 테스트 로그
    return apiClient.post('/likes/like', { userId, postId });
  };
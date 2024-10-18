import apiClient from '@/api/axios';  // Axios 설정이 적용된 apiClient 사용

/**
 * 문의 조회 함수
 */
export const getInquiries = async () => {
  try {
    const response = await apiClient.get('/inquiry/get-only');
    return response.data;
  } catch (error) {
    console.error('getInquiries 에러:', error);
    throw error;
  }
};

/**
 * 문의 등록 함수
 */
export const addInquiry = async (inquiry) => {
  try {
    const response = await apiClient.post('/inquiry/add', inquiry);
    return response.data;
  } catch (error) {
    console.error('postInquiry 에러:', error);
    throw error;
  }
};

/**
 * 문의 수정 함수
 */
export const modifyInquiry = async (inquiry) => {
  try {
    const response = await apiClient.post('/inquiry/modify', inquiry);
    return response.data;
  } catch (error) {
    console.error('modifyInquiry 에러:', error);
    throw error;
  }
};

/**
 * 문의 삭제 함수(ACTIVE -> INACTIVE)
 */
export const deleteModify = async (id) => {
  try {
    const response = await apiClient.delete('/inquiry/delete', { 
      params:{id} 
    });
    return response.data;
  } catch (error) {
    console.error('sendPasswordResetVerificationEmail 에러:', error);
    throw error;
  }
};

/**
 * 문의의 답변(or 재문의) 조회 함수
 */
export const getAnswers = async (id) => {
  try {
    const response = await apiClient.get(`/inquiry/${id}`);
    return response.data;
  } catch (error) {
    console.error('getAnswers 에러:', error);
    throw error;
  }
};

/**
 * 답변(or 재문의) 등록 함수
 */
export const addAnswer = async (answer) => {
  try {
    const response = await apiClient.post('/answer/add', answer);
    return response.data;
  } catch (error) {
    console.error('addAnswer 에러:', error);
    throw error;
  }
};

/**
 *  답변(or 재문의) 삭제 함수
 */
export const deleteAnswer = async (id) => {
  try {
    const response = await apiClient.delete('/answer/delete', {
      params: {id} 
    });
    return response.data;
  } catch (error) {
    console.error('deleteAnswer:', error);
    throw error;
  }
};
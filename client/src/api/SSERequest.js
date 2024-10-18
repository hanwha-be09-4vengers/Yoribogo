import { useTokenStore } from '@/stores/tokenStore';  // Pinia 스토어에서 토큰 가져오기
import { EventSource, EventSourcePolyfill } from 'event-source-polyfill';

export function connectSSE() {
  const tokenStore = useTokenStore();
  const accessToken = tokenStore.token.accessToken;
  console.log('진짜 받아온 JWT 토큰:', accessToken);

  if (!accessToken) {
    console.error('토큰이 없습니다. SSE 연결을 할 수 없습니다.');
    return;
  }

 // 백엔드 서버의 전체 URL을 사용하여 SSE 연결
 // EventSourcePolyfill을 사용하여 JWT 토큰을 Authorization 헤더에 담아서 전송
 const eventSource = new EventSource('http://localhost:8080/api/notifications/sseconnect', {
  // headers: {
  //   Authorization: `Bearer ${accessToken}`
  // },
  withCredentials: true,
});

  // 연결 완료 로그
  eventSource.onopen = () => {
    console.log('SSE 연결이 완료되었습니다.');
  };

  // 서버에서 스케쥴러를 통해 이벤트 객체 생성 시 자동으로 호출
  eventSource.onmessage = (event) => {
    console.log('알림 수신(추후 삭제):', event.data);
    // 여기서 수신한 메시지(event.data)를 UI를 고려하여 처리
  };


  // 오류 처리
  eventSource.onerror = (error) => {
    console.error('SSE 연결 오류 발생:', error);

    if (eventSource.readyState === EventSource.CONNECTING) {
      console.log('SSE 연결이 끊어졌습니다. 재연결 시도 중...');
    } 
  };

  return eventSource;
}

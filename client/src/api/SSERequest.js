let eventSource = null;
// import { useTokenStore } from '@/stores/tokenStore'; // Pinia 스토어 임포트
// const tokenStore = useTokenStore(); // Pinia 스토어 사용


export function connectSSE() {
  // console.log('로그인 토큰1: ', tokenStore.token.accessToken);
  console.log('로그인 토큰2: ', JSON.parse(localStorage.getItem('token')).accessToken);
  if (!eventSource) {
    // eventSource = new EventSource('/api/notifications/sseconnect');
    eventSource = new EventSource(
                    `/boot/api/notifications/sseconnect`,
                    {
                      headers: {
                        Authorization: `Bearer ${JSON.parse(localStorage.getItem('token')).accessToken}`,
                      },
                      withCredentials: true,
                    }
                  );
    // SSE 연결 열림
    eventSource.onopen = () => {
      console.log('SSE 연결이 열렸습니다.');
    };

    // 서버에서 보내는 connect 이벤트
    eventSource.addEventListener('connect', (event) => {
      console.log('서버로부터 연결 메시지:', event.data);
    });

    // 알림 이벤트 수신
    eventSource.addEventListener('notification', (event) => {
      console.log('새로운 알림:', event.data);
      // 추가적인 알림 처리 로직
    });

    // 기본 메시지 수신
    eventSource.onmessage = (event) => {
      console.log('새로운 메시지:', event.data);
    };

    // 에러 처리
    eventSource.onerror = (error) => {
      console.error('SSE 연결 오류:', error);
    };
  }

  return eventSource;
}

export function closeSSE() {
  if (eventSource) {
    eventSource.close();
    eventSource = null;
    console.log('SSE 연결이 닫혔습니다.');
  }
}
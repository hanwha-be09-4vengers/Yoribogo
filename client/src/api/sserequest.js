export function connectSSE() {
  const eventSource = new EventSource('/api/notification/sseconnect');

  // SSE 연결 시 나오는 메시지 
  eventSource.onopen = () => {
      console.log('SSE 연결이 열렸습니다.');
  };

  // 서버가 최초로 보내는 'connect' 이벤트 메시지 수신
  eventSource.addEventListener('connect', (event) => {
      console.log('서버로부터 연결 메시지:', event.data);
  });

  // 'notification' 이벤트를 별도로 수신하는 이벤트 리스너
  eventSource.addEventListener('notification', (event) => {
      console.log('새로운 알림 (notification 이벤트):', event.data);
      // 알림을 처리하는 로직을 추가할 수 있습니다.
  });

  // 일반 메시지 수신 (이벤트 이름이 없는 메시지 처리)
  eventSource.onmessage = (event) => {
      console.log('새로운 메시지:', event.data);
  };

  // 에러 처리
  eventSource.onerror = (error) => {
      console.error('SSE 연결 오류:', error);
      eventSource.close();
  };

  return eventSource;
}

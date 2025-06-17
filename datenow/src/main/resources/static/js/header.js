(() => {
  const logout = document.querySelector('#logout');
  if (!logout) return;

  logout.addEventListener('click', async ev => {
    ev.preventDefault();

    const response = await fetch("/auth/logout", {
      method: "POST"
    });

    if (!response.ok) {
      alert("로그아웃에 실패했습니다.")
      return;
    }

    alert("로그아웃 되었습니다.");

    window.location.href = "/member/signin";
  });
  let stompClient = null;

  function connectStomp(userId) {
    const socket = new SockJS("/ws/connect");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
      stompClient.subscribe("/topic/match." + userId, (message) => {
        const data = JSON.parse(message.body);
        const roomId = data.roomId;
        document.getElementById("status").textContent = `매칭 완료! 채팅방으로 이동합니다...`;
        window.location.href = `/chatList/${roomId}`;
      });
    });
  }


  function startRandomChat() {
    fetch("/api/randomChat", { method: "POST" })
    .then(res => res.text())
    .then(text => text ? JSON.parse(text) : {})
    .then(data => {
      const roomId = typeof data === 'object' ? data.roomId : data;
      if (roomId) {
        window.location.href = `/chatList/${roomId}`;
      } else {
        document.getElementById("status").textContent = "상대방을 기다리는 중입니다...";
        fetch("/api/userId")
        .then(res => res.json())
        .then(data => connectStomp(data.userId));
      }
    })
    .catch(err => {
      console.error(err);
      document.getElementById("status").textContent = "에러 발생";
    });
  }

// 👇 여기 추가!
  window.startRandomChat = startRandomChat;
  })();

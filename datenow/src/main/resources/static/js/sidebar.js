document.addEventListener("DOMContentLoaded", () => {
  fetch("/api/members/info", {
    method: "GET",
    credentials: "include"
  })
  .then(res => res.json())
  .then(data => {
    document.querySelector(".user-name").textContent = "닉네임: " + data.nickname;
    document.querySelector(".user-email").textContent = "이메일: " + data.email;
  })
  .catch(err => {
    console.error("회원 정보 조회 실패", err);
  });
});
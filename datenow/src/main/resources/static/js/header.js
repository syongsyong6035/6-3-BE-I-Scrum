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
})();

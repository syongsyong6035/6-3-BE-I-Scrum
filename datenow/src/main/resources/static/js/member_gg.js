function deactivateAccount() {
  if (confirm("정말로 회원 탈퇴하시겠습니까?")) {
    fetch('/api/members/deactivate', {
      method: 'PATCH'
    })
    .then(() => {
      alert("탈퇴되었습니다.");
      window.location.href = "/member/signin";
    })
    .catch(error => {
      alert('탈퇴 처리 중 오류가 발생했습니다.');
      console.error(error);
    });
  }
}

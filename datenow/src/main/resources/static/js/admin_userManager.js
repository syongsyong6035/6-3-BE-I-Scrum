
// 1. 유저 목록 불러오기
function fetchMembers() {
  fetch('/api/admin/users')
  .then(res => res.json())
  .then(data => {
    members = data.map(user => ({
      id: user.userId,
      name: user.name,
      nickname: user.nickName,
      date: user.created_at.split('T')[0],

    }));
    renderMembers();
  })
  .catch(err => {
    console.error('유저 목록을 불러오지 못했습니다:', err);
  });
}

// 2. 테이블 렌더링
function renderMembers() {
  const tbody = document.getElementById('memberTableBody');
  tbody.innerHTML = '';
  members.forEach((member, idx) => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${idx + 1}</td>
      <td>${member.name}</td>
      <td>${member.id}</td>
      <td>${member.nickname}</td>
      <td>${member.date}</td>

    `;
    tbody.appendChild(tr);
  });
}

// 3. 삭제 요청
function deleteMember(userId) {
  if (!confirm('정말 삭제하시겠습니까?')) return;

  fetch(`/api/admin/users/${userId}`, {
    method: 'POST'
  })
  .then(response => {
    if (response.ok) {
      alert('삭제되었습니다.');
      members = members.filter(m => m.id !== userId);
      renderMembers();
    } else {
      alert('삭제 실패: ' + response.status);
    }
  })
  .catch(error => {
    console.error('삭제 중 오류 발생:', error);
    alert('삭제 중 오류가 발생했습니다.');
  });
}

function editMember(id) {
  alert(`회원 수정 기능은 아직 미구현 (ID: ${id})`);
}

// 4. 로드 시 실행
window.onload = fetchMembers;

const isValid = {
  email: false,
  nickname: false,
  birth: false,
  phone: false
};

function updateSubmitButton() {
  const allValid = Object.values(isValid).every(Boolean);
  document.getElementById('submitBtn').disabled = !allValid;
}

function checkDuplication(field) {
  const value = document.getElementById(field).value.trim();
  const msgElement = document.getElementById(`${field}-msg`);

  if (!value) {
    msgElement.textContent = '값을 입력하세요.';
    msgElement.className = 'error-field';
    isValid[field] = false;
    updateSubmitButton();
    return;
  }

  if (field === 'email') {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(value)) {
      msgElement.textContent = '이메일 형식이 올바르지 않습니다.';
      msgElement.className = 'error-field';
      isValid.email = false;
      updateSubmitButton();
      return;
    }
  }

  fetch(`/api/members/check/${field}?${field}=${encodeURIComponent(value)}`)
  .then(res => res.json())
  .then(json => {
    if (!json.data) {
      msgElement.textContent = '사용 가능한 값입니다.';
      msgElement.className = 'success-field';
      isValid[field] = true;
    } else {
      msgElement.textContent = '이미 사용 중인 값입니다.';
      msgElement.className = 'error-field';
      isValid[field] = false;
    }
    updateSubmitButton();
  })
  .catch(() => {
    msgElement.textContent = '중복 검사 중 오류 발생';
    msgElement.className = 'error-field';
    isValid[field] = false;
    updateSubmitButton();
  });
}

function checkBirth() {
  const birth = document.getElementById('birth').value;
  const birthMsg = document.getElementById('birth-msg');
  const birthRegex = /^(19|20)\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/;

  if (birthRegex.test(birth)) {
    birthMsg.textContent = '';
    birthMsg.className = '';
    isValid.birth = true;
  } else {
    birthMsg.textContent = '생년월일은 YYYY-MM-DD 형식으로 입력하세요.';
    birthMsg.className = 'error-field';
    isValid.birth = false;
  }

  updateSubmitButton();
}

function checkPhone() {
  const phone = document.getElementById('phone').value;
  const phoneMsg = document.getElementById('phone-msg');
  const phoneRegex = /^\d{3}-\d{3,4}-\d{4}$/;

  if (phoneRegex.test(phone)) {
    phoneMsg.textContent = '';
    phoneMsg.className = '';
    isValid.phone = true;
  } else {
    phoneMsg.textContent = '전화번호 형식을 확인하세요. 예: 010-1234-5678';
    phoneMsg.className = 'error-field';
    isValid.phone = false;
  }

  updateSubmitButton();
}

// 이벤트 리스너 등록
document.getElementById('email').addEventListener('input', () => checkDuplication('email'));
document.getElementById('nickname').addEventListener('input', () => checkDuplication('nickname'));
document.getElementById('birth').addEventListener('input', checkBirth);
document.getElementById('phone').addEventListener('input', checkPhone);

// 제출 이벤트 처리
document.getElementById('signupForm').addEventListener('submit', function (e) {
  e.preventDefault();

  const name = document.getElementById('name').value.trim();
  if (!name) {
    alert('이름을 입력하세요.');
    return;
  }

  const data = {
    email: document.getElementById('email').value,
    name: name,
    nickname: document.getElementById('nickname').value,
    birth: document.getElementById('birth').value,
    phone: document.getElementById('phone').value
  };

  fetch('/api/members/signup', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(data)
  })
  .then(res => {
    if (!res.ok) throw new Error('회원가입 실패');
    return res.json();
  })
  .then(json => {
    alert(json.data.message || '회원가입이 완료되었습니다.');
    window.location.href = '/';
  })
  .catch(err => {
    alert('회원가입 중 오류가 발생했습니다.');
    console.error(err);
  });
});
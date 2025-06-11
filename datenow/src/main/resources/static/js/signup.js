const isValid = {
  userId: false,
  email: false,
  nickname: false,
  password: false,
  birth: false,
  phone: false
};

const apiMap = {
  userId: '/api/members/exists/userId?userId=',
  email: '/api/members/check/email?email=',
  nickname: '/api/members/check/nickname?nickname='
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

  if (field === 'userId') {
    const idRegex = /^[a-z0-9]{4,10}$/;
    if (!idRegex.test(value)) {
      msgElement.textContent = '아이디는 영어 소문자 4~10자여야 합니다.';
      msgElement.className = 'error-field';
      isValid.userId = false;
      updateSubmitButton();
      return;
    }
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

  fetch(apiMap[field] + encodeURIComponent(value))
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

function checkPasswordMatch() {
  const pw = document.getElementById('password').value;
  const confirm = document.getElementById('confirmPassword').value;
  const msg = document.getElementById('password-msg');

  if (!pw || pw.length < 8 || pw.length > 20) {
    msg.textContent = '비밀번호는 8자 이상 20자 이하로 입력해야 합니다.';
    msg.className = 'error-field';
    isValid.password = false;
  } else if (pw === confirm) {
    msg.textContent = '비밀번호가 일치합니다.';
    msg.className = 'success-field';
    isValid.password = true;
  } else {
    msg.textContent = '비밀번호가 일치하지 않습니다.';
    msg.className = 'error-field';
    isValid.password = false;
  }
  updateSubmitButton();
}

function checkBirth() {
  const birth = document.getElementById('birth').value;
  const birthMsg = document.getElementById('birth-msg');
  const birthRegex = /^(19|20)\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/;

  if (birthRegex.test(birth)) {
    birthMsg.textContent = '';
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
    isValid.phone = true;
  } else {
    phoneMsg.textContent = '전화번호 형식을 확인하세요. 예: 010-1234-5678';
    phoneMsg.className = 'error-field';
    isValid.phone = false;
  }

  updateSubmitButton();
}


document.getElementById('userId').addEventListener('input', () => checkDuplication('userId'));
document.getElementById('email').addEventListener('input', () => checkDuplication('email'));
document.getElementById('nickname').addEventListener('input', () => checkDuplication('nickname'));
document.getElementById('password').addEventListener('input', checkPasswordMatch);
document.getElementById('confirmPassword').addEventListener('input', checkPasswordMatch);
document.getElementById('birth').addEventListener('input', checkBirth);
document.getElementById('phone').addEventListener('input', checkPhone);

document.getElementById('signupForm').addEventListener('submit', function (e) {
  e.preventDefault();

  const data = {
    userId: document.getElementById('userId').value,
    password: document.getElementById('password').value,
    email: document.getElementById('email').value,
    name: document.getElementById('name').value,
    nickname: document.getElementById('nickname').value,
    birth: document.getElementById('birth').value,
    phone: document.getElementById('phone').value
  };

  fetch('/api/members/signup', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  })
  .then(res => {
    if (!res.ok) throw new Error('회원가입 실패');
    return res.json();
  })
  .then(json => {
    alert(json.data.message || '회원가입이 완료되었습니다.');
    window.location.href = '/signin';
  })
  .catch(err => {
    alert('회원가입 중 오류가 발생했습니다.');
    console.error(err);
  });
});
document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("editForm");
  const password = document.getElementById("password");
  const passwordConfirm = document.getElementById("passwordConfirm");
  const phone = document.getElementById("phone");
  const email = document.getElementById("email");
  const nickname = document.getElementById("nickname");
  const errorMsg = document.getElementById("error-message");

  const originalEmail = email.getAttribute("data-original");
  const originalNickname = nickname.getAttribute("data-original");

  const isValid = {
    password: false,
    phone: true,
    email: true,
    nickname: true
  };

  const isChanged = {
    password: false,
    phone: false,
    email: false,
    nickname: false
  };

  function updateSubmitButton() {
    const changedFields = Object.values(isChanged).some(v => v);
    const allValid = Object.entries(isValid)
    .filter(([key]) => isChanged[key])
    .every(([_, v]) => v);

    const submitEnabled = isValid.password && changedFields && allValid;
    document.getElementById("submitBtn").disabled = !submitEnabled;
  }

  function validatePassword() {
    const pw = password.value;
    const confirm = passwordConfirm.value;
    const msg = document.getElementById("password-msg");

    isChanged.password = !!pw || !!confirm;

    if (!pw && !confirm) {
      msg.textContent = '';
      isValid.password = false;
    } else if (pw.length < 8 || pw.length > 20) {
      msg.textContent = '비밀번호는 8자 이상 20자 이하로 입력해야 합니다.';
      msg.className = 'error-field';
      isValid.password = false;
    } else if (pw !== confirm) {
      msg.textContent = '비밀번호가 일치하지 않습니다.';
      msg.className = 'error-field';
      isValid.password = false;
    } else {
      msg.textContent = '비밀번호가 일치합니다.';
      msg.className = 'success-field';
      isValid.password = true;
    }
    updateSubmitButton();
  }

  function validatePhone() {
    const val = phone.value.trim();
    const msg = document.getElementById("phone-msg");
    const regex = /^\d{3}-\d{3,4}-\d{4}$/;

    isChanged.phone = true;

    if (!val) {
      msg.textContent = '';
      isValid.phone = true;
    } else if (regex.test(val)) {
      msg.textContent = '';
      isValid.phone = true;
    } else {
      msg.textContent = '전화번호 형식을 확인하세요. 예: 010-1234-5678';
      msg.className = 'error-field';
      isValid.phone = false;
    }
    updateSubmitButton();
  }

  async function validateField(field, originalValue) {
    const input = document.getElementById(field);
    const value = input.value.trim();
    const msg = document.getElementById(`${field}-msg`);

    isChanged[field] = value !== originalValue;

    if (!isChanged[field]) {
      msg.textContent = '';
      isValid[field] = true;
      updateSubmitButton();
      return;
    }

    if (!value) {
      msg.textContent = '값을 입력하세요.';
      msg.className = 'error-field';
      isValid[field] = false;
      updateSubmitButton();
      return;
    }

    if (field === 'email') {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(value)) {
        msg.textContent = '이메일 형식이 올바르지 않습니다.';
        msg.className = 'error-field';
        isValid.email = false;
        updateSubmitButton();
        return;
      }
    }

    try {
      const res = await fetch(`/api/members/check/${field}?${field}=` + encodeURIComponent(value));
      const result = await res.json();

      if (!result.data) {
        msg.textContent = '사용 가능한 값입니다.';
        msg.className = 'success-field';
        isValid[field] = true;
      } else {
        msg.textContent = '이미 사용 중인 값입니다.';
        msg.className = 'error-field';
        isValid[field] = false;
      }
    } catch (err) {
      console.error(err);
      msg.textContent = '중복 검사 중 오류 발생';
      msg.className = 'error-field';
      isValid[field] = false;
    }

    updateSubmitButton();
  }

  password.addEventListener("input", validatePassword);
  passwordConfirm.addEventListener("input", validatePassword);
  phone.addEventListener("input", validatePhone);
  email.addEventListener("input", () => validateField('email', originalEmail));
  nickname.addEventListener("input", () => validateField('nickname', originalNickname));

  form.addEventListener("submit", async function (e) {
    e.preventDefault();

    if (!isValid.password) {
      errorMsg.textContent = "비밀번호를 올바르게 입력하세요.";
      errorMsg.style.display = "block";
      return;
    }
    if (isChanged.email && !isValid.email) {
      errorMsg.textContent = "이메일 중복 확인을 통과하지 않았습니다.";
      errorMsg.style.display = "block";
      return;
    }
    if (isChanged.nickname && !isValid.nickname) {
      errorMsg.textContent = "닉네임 중복 확인을 통과하지 않았습니다.";
      errorMsg.style.display = "block";
      return;
    }
    if (isChanged.phone && !isValid.phone) {
      errorMsg.textContent = "전화번호 형식을 확인하세요.";
      errorMsg.style.display = "block";
      return;
    }

    const userId = form.dataset.userid;
    const payload = {};

    if (isValid.password && isChanged.password) {
      payload.password = password.value;
    }

    if (isChanged.email && isValid.email) {
      payload.email = email.value;
    } else {
      payload.email = email.getAttribute("data-original");
    }

    if (isChanged.nickname && isValid.nickname) {
      payload.nickname = nickname.value;
    } else {
      payload.nickname = nickname.getAttribute("data-original");
    }

    if (isChanged.phone && isValid.phone) {
      payload.phone = phone.value;
    } else {
      payload.phone = phone.getAttribute("data-original");
    }

    try {
      const response = await fetch(`/api/members/edit/${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        alert("회원정보가 수정되었습니다.");
        window.location.href = "/my-page";
      } else {
        const res = await response.json();
        errorMsg.textContent = res.message || "수정에 실패했습니다.";
        errorMsg.style.display = "block";
      }
    } catch (err) {
      console.error(err);
      errorMsg.textContent = "서버와의 통신에 실패했습니다.";
      errorMsg.style.display = "block";
    }
  });
});
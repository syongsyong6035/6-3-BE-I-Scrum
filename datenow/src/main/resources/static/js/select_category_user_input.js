document.addEventListener('DOMContentLoaded', () => {
  // 1) 날짜 입력
  const dateInput = document.getElementById('dateInput');
  if (dateInput) {
    dateInput.addEventListener('click', () => {
      const picker = document.createElement('input');
      picker.type = 'date';
      picker.style.position = 'absolute';
      picker.style.opacity = 0;
      document.body.appendChild(picker);
      picker.focus();
      picker.onchange = function() {
        dateInput.value = this.value;
        document.body.removeChild(picker);
      };
      picker.onblur = function() {
        setTimeout(() => {
          if (document.body.contains(picker)) document.body.removeChild(picker);
        }, 200);
      };
    });
  }

  // 2) 카테고리(최대 3개) 선택 로직
  const MAX_CAT = 3;
  const selectedCats = new Set();
  document.querySelectorAll('.category-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const code = btn.dataset.code;
      if (selectedCats.has(code)) {
        // 이미 선택된 항목은 토글 해제
        selectedCats.delete(code);
        btn.classList.remove('selected');
      } else {
        // 최대 개수 체크
        if (selectedCats.size >= MAX_CAT) {
          alert(`카테고리는 최대 ${MAX_CAT}개까지 선택할 수 있어요.`);
          return;
        }
        selectedCats.add(code);
        btn.classList.add('selected');
      }
    });
  });

  document.querySelector('.ai-btn')?.addEventListener('click', () => {
    const date = document.getElementById('dateInput')?.value;
    if (!date) return alert('날짜를 선택해주세요.');
    if (selectedCats.size === 0) return alert('카테고리를 선택해주세요!');

    const moods = [...selectedCats];
    const moodDescriptions = {
      ROMANTIC: "로맨틱한",
      COZY: "아늑한",
      TRENDY: "트렌디한",
      COMFORTABLE: "편안한",
      QUIET: "조용한",
      LIVELY: "활기찬",
      LUXURIOUS: "고급스러운",
      UNIQUE: "독특한"
    };

    const moodText = (() => {
      const descs = moods.map(code => moodDescriptions[code]);
      if (descs.length === 1) return descs[0];
      if (descs.length === 2) return descs.join(" 그리고 ");
      return descs.slice(0, -1).join(", ") + " 그리고 " + descs.at(-1);
    })();

    // 👉 오버레이 표시
    const overlay = document.getElementById('loadingOverlay');
    const aiBtn = document.querySelector('.ai-btn');
    aiBtn.disabled = true;
    overlay.style.display = 'block';

    fetch('/api/recommend/course', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ mood: moodText })
    })
    .then(res => res.text())
    .then(result => {
      const clean = result.trim().startsWith('```json')
          ? result.replace(/^```json\s*/, '').replace(/```$/, '').trim()
          : result;
      const places = JSON.parse(clean);

      sessionStorage.setItem('recommendedPlaces', JSON.stringify(places));
      sessionStorage.setItem('selectedDate', date);

      window.location.href = `/course-composition`;
    })
    .catch(err => {
      console.error('추천 실패:', err);
      alert('추천 코스를 받아오지 못했습니다.');
      overlay.style.display = 'none';
      aiBtn.disabled = false;
    });
  });
});
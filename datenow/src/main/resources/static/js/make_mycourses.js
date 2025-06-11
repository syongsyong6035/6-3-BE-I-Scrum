// 사진 등록 버튼 클릭 시 파일 선택창
const photoBtn = document.querySelector('.photo-btn');
if (photoBtn) {
  photoBtn.addEventListener('click', () => {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.style.display = 'none';
    document.body.appendChild(fileInput);
    fileInput.click();
    fileInput.onchange = function() {
      setTimeout(() => document.body.removeChild(fileInput), 500);
    };
  });
}

document.addEventListener('DOMContentLoaded', function() {
    // 세션 스토리지에서 선택된 장소들을 가져옴
    const selectedPlaces = JSON.parse(sessionStorage.getItem('selectedPlaces') || '[]');
    
    // 선택된 장소들을 화면에 표시
    const mycourseList = document.querySelector('.mycourse-list');
    selectedPlaces.forEach((place, index) => {
        const placeCard = document.createElement('div');
        placeCard.className = 'mycourse-card';
        placeCard.innerHTML = `
            <div class="place-rank">${index + 1}st Place</div>
            <div class="place-img"></div>
            <div class="place-info">
                <span class="place-name">${place.name}</span>
                <p class="place-address">${place.address}</p>
            </div>
        `;
        mycourseList.appendChild(placeCard);
    });

    // 폼 제출 시 처리
    const courseForm = document.getElementById('courseForm');
    if (courseForm) {
        courseForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            if (confirm('이 코스를 추천 코스로 등록하시겠습니까?')) {
                this.submit();
            }
        });
    }

    // 파일 업로드 관련 코드
    const photoInput = document.getElementById('photoInput');
    const fileList = document.getElementById('fileList');

    if (photoBtn && photoInput && fileList) {
    photoBtn.addEventListener('click', function() {
        photoInput.click();
    });

    photoInput.addEventListener('change', function(e) {
            const files = e.target.files;
            fileList.innerHTML = '';
            
            for (let file of files) {
            const fileItem = document.createElement('div');
            fileItem.className = 'file-item';
                fileItem.textContent = file.name;
            fileList.appendChild(fileItem);
        }
    });
    }
});

// 카테고리/지역 버튼 토글
function toggleBtnGroup(selector) {
  document.querySelectorAll(selector).forEach(btn => {
    btn.addEventListener('click', function() {
      this.classList.toggle('selected');
    });
  });
}
toggleBtnGroup('.category-btn');
toggleBtnGroup('.region-btn');

// 저장 버튼 클릭 시 예시
const saveBtn = document.querySelector('.save-btn');
if (saveBtn) {
  saveBtn.addEventListener('click', function(e) {
    e.preventDefault();
    alert('내 데이트 코스가 저장되었습니다!');
  });
} 
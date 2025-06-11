function renderCards(cardData, containerId, type) {
  const cardList = document.getElementById(containerId);
  if (!cardList) return;  // ✅ 존재하지 않으면 종료

  cardList.innerHTML = '';

  cardData.forEach(card => {
    const title = card.title;
    const author = type === 'admin' ? card.editorNickname : card.creatorNickname;
    const imageUrl = card.imageurl || 'https://via.placeholder.com/260x160?text=No+Image';
    const courseId = card.courseId;
    const favoriteCount = card.favoriteCnt || 0;
    const commentCount = card.reviewCnt || 0;

    const cardDiv = document.createElement('div');
    cardDiv.className = 'course-card';
    cardDiv.style.cursor = 'pointer';
    cardDiv.addEventListener('click', () => {
      const url = type === 'admin'
          ? `/editor-recommend-courses/${courseId}`
          : `/recommend-courses/${courseId}`;
      window.location.href = url;
    });

    let extraInfo = `<div class="card-stats">❤️ ${favoriteCount}</div>`;
    if (type === 'user') {
      extraInfo += `<div class="card-stats">💬 ${commentCount}</div>`;
    }

    cardDiv.innerHTML = `
      <div class="card-image" style="background-image:url('${imageUrl}')"></div>
      <div class="card-title">${title}</div>
      <div class="card-author">
        <div class="author-avatar" style="background-image:url('/images/user.jpg')"></div>
        <div class="author-name">${author}</div>
      </div>
      <div class="card-footer">${extraInfo}</div>
    `;

    cardList.appendChild(cardDiv);
  });
}

function renderCarousel(data) {
  const carousel = document.getElementById('editor-carousel');
  if (!carousel) return;

  carousel.innerHTML = '';

  data.forEach(course => {
    const card = document.createElement('div');
    card.className = 'course-card';
    card.onclick = () => {
      window.location.href = `/editor-recommend-courses/${course.courseId}`;
    };
    card.innerHTML = `
      <img src="${course.imageurl}" alt="${course.title} 썸네일" />
      <div class="course-card-content">
        <div class="course-title">${course.title}</div>
        <div class="card-author">
          <div class="author-avatar" style="background-image:url('/images/user.jpg');"></div>
          <div class="course-instructor">${course.editorNickname} 에디터</div>
        </div>
      </div>
    `;
    carousel.appendChild(card);
  });

  // 자동 슬라이드
  let index = 0;
  const total = data.length;
  setInterval(() => {
    index = (index + 1) % total;
    carousel.style.transform = `translateX(-${index * 100}%)`;
  }, 2500);
}

document.addEventListener('DOMContentLoaded', function () {
  fetch('/api')
  .then(response => response.json())
  .then(data => {
    const adminCourses = data.adminlist;
    const userCourses = data.userlist;

    // 혼용 렌더링
    renderCards(adminCourses, 'editor-pick-list', 'admin');
    renderCards(userCourses, 'recommend-list', 'user');
    renderCarousel(adminCourses);
  })
  .catch(error => {
    console.error('데이터 불러오기 실패:', error);
  });

  // 더보기 버튼
  document.getElementById('editor-more')?.addEventListener('click', e => {
    e.preventDefault();
    window.location.href = '/editor-recommend-courses';
  });

  document.getElementById('user-more')?.addEventListener('click', e => {
    e.preventDefault();
    window.location.href = '/recommend-courses';
  });

  // 인기 코스 하나 가져오기
  fetch('/api/top-liked-course')
  .then(res => res.json())
  .then(course => {
    const banner = document.getElementById('top-liked-card');
    banner.innerHTML = `
      <div class="banner-card-content">
        <div class="banner-card-title">${course.title} by ${course.creatorNickname}</div>
      </div>
    `;
    banner.addEventListener('click', () => {
      window.location.href = `/recommend-courses/${course.courseId}`;
    });
  })
  .catch(err => {
    console.error("인기 코스를 불러오지 못했습니다:", err);
  });
});


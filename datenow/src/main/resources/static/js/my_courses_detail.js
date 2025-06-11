document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const courseId = urlParams.get('courseId');

    if (!courseId) {
        alert('코스 ID가 없습니다.');
        return;
    }

    // Ensure your fetch URL uses the PathVariable format
    fetch(`/api/course/my-course/${courseId}`, {
        method: 'GET',
        headers: { 'Accept': 'application/json' },
        credentials: 'include'
    })
    .then(res => res.json())
    .then(data => { // 이 'data'는 ApiResponse 객체 전체입니다.
        if (data.code === "0000") { // 성공적으로 응답을 받았는지 확인
            const course = data.data; // 🎉 여기가 핵심입니다! ApiResponse 내부의 'data' 필드를 추출

            // 이제 'course' 변수에 CourseDetailDto 객체가 직접 담겨 있습니다.
            document.getElementById('detail-course-title').textContent = course.title;
            document.getElementById('course-description').textContent = course.description;

            const placeContainer = document.querySelector('.place-container');
            placeContainer.innerHTML = '';
            course.places.forEach(place => {
                const card = document.createElement('div');
                card.className = 'place-card';
                card.innerHTML = `
                <h3>${place.title}</h3>
                <p>${place.address}</p>
            `;
                placeContainer.appendChild(card);
            });
        } else {
            // 에러 처리: ApiResponse의 메시지를 사용
            alert('코스 정보를 불러오는 데 실패했습니다: ' + data.message);
        }
    })
    .catch(err => {
        console.error(err);
        alert('코스 정보를 불러오는 데 실패했습니다.');
    });

    document.getElementById('recommend-button').addEventListener('click', () => {
        const courseId = new URLSearchParams(window.location.search).get('courseId');
        if (!courseId) {
            alert('코스 ID가 없습니다.');
            return;
        }

        // This part is already correct for PathVariable
        window.location.href = `/recommend-course/register/${courseId}`;
    });
});
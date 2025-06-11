document.addEventListener("DOMContentLoaded", () => {
    const mycourseList = document.getElementById("mycourse-list");

    fetch("/api/course/my-course", {
        credentials: "include"
    })
    .then((res) => res.json())
    .then((response) => {
        const courses = response.data;

        if (!courses || courses.length === 0) {
            mycourseList.innerHTML = "<p>등록한 코스가 없습니다.</p>";
            return;
        }

        courses.forEach((course) => {
            const card = document.createElement("div");
            card.className = "course-card";
            card.innerHTML = `<h3>${course.title}</h3>`;
            card.addEventListener("click", () => {
                window.location.href = `/my-courses-detail?courseId=${course.coursesId}`;
            });
            mycourseList.appendChild(card);
        });
    })
    .catch((error) => {
        console.error("코스 목록 로딩 실패", error);
    });
});
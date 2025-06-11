// AI 추천 코스 관련 로직
document.addEventListener("DOMContentLoaded", () => {
    const saved = sessionStorage.getItem('recommendedPlaces');
    if (saved) {
        renderRecommendation(JSON.parse(saved));
    } else {
        alert("추천받은 코스가 없습니다. 이전 페이지에서 추천을 먼저 받아주세요.");
        window.location.href = '/select-category'; // 적절한 이전 페이지 URL
        return;
    }

    const selectedDate = sessionStorage.getItem('selectedDate');
    if (selectedDate) {
        document.getElementById('selectedDateText').textContent = selectedDate;
    }
});

function renderRecommendation(places) {
    const container = document.getElementById('recommendResults');
    if (!container) return;

    container.innerHTML = '';

    places.forEach(place => {
        const card = document.createElement('div');
        card.className = 'recommend-card';

        card.innerHTML = `
  <h4>📍 ${place.placeName}</h4>
  <p><strong>주소:</strong> ${place.address}</p>
  <p><strong>이유:</strong> ${place.reason}</p>
  <button class="add-btn" data-name="${place.placeName}" data-address="${place.address}" style="background-color: #ff9eaa; cursor: pointer;">
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align: middle;">
      <line x1="12" y1="5" x2="12" y2="19"></line>
      <line x1="5" y1="12" x2="19" y2="12"></line>
    </svg>
    <span style="margin-left: 6px;">장소 추가</span>
  </button>
`;

        container.appendChild(card);
    });

    // 각 버튼에 이벤트 연결
    container.querySelectorAll('.add-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const name = btn.dataset.name;
            const address = btn.dataset.address;
            addPlaceToCourse(name, address);  // → 아래에서 정의
        });
    });
}

// 내 데이트 코스 카트에 추가하는 로직
function addPlaceToCourse(name, address) {
    const courseList = document.getElementById('courseList');

    // 중복 확인: 이미 같은 장소와 주소가 있는지 검사. 근데 좀 아쉽네 위치 정보로 하면 좋을거같기도
    const existingItems = courseList.querySelectorAll('.course-item');
    for (const item of existingItems) {
        const existingName = item.querySelector('h4').textContent.trim();
        const existingAddress = item.querySelector('p').textContent.trim();

        if (existingName === name && existingAddress === address) {
            alert('이미 추가된 장소입니다.');
            return;
        }
    }

    // 항목 추가
    const item = document.createElement('div');
    item.className = 'course-item';

    item.innerHTML = `
      <div class="place-info">
        <h4>${name}</h4>
        <p>${address}</p>
      </div>
      <button class="delete-btn">×</button>
    `;

    // 삭제 버튼 이벤트
    item.querySelector('.delete-btn').addEventListener('click', () => {
        item.remove();

        // 👉 삭제하면 다시 버튼 활성화
        const allButtons = document.querySelectorAll('.add-btn');
        allButtons.forEach(btn => {
            if (btn.dataset.name === name && btn.dataset.address === address) {
                btn.disabled = false;
                btn.style.backgroundColor = "#ff9eaa";
                btn.style.cursor = "pointer";

                btn.innerHTML = `
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align: middle;">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <line x1="5" y1="12" x2="19" y2="12"></line>
              </svg>
              <span style="margin-left: 6px;">장소 추가</span>
            `;
            }
        });
    });


    courseList.appendChild(item);

    // 👉 추가된 후 버튼 비활성화 처리
    const allButtons = document.querySelectorAll('.add-btn');
    allButtons.forEach(btn => {
        if (btn.dataset.name === name && btn.dataset.address === address) {
            btn.disabled = true;
            btn.textContent = "추가됨";
            btn.style.backgroundColor = "#ccc";
            btn.style.cursor = "not-allowed";
        }
    });
}

// 여기까지 AI 추천 코스관련.


// 마커를 담을 배열입니다
var markers = [];

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };

// 지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();

// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
var infowindow = new kakao.maps.InfoWindow({zIndex:1});

// 키워드로 장소를 검색합니다
searchPlaces();

// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {
    var keyword = document.getElementById('keyword').value;

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch(keyword, placesSearchCB);
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        displayPlaces(data);

        // 페이지 번호를 표출합니다
        displayPagination(pagination);

    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');
        return;

    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 결과 중 오류가 발생했습니다.');
        return;
    }
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {
    var listEl = document.getElementById('placesList'),
        menuEl = document.getElementById('menu_wrap'),
        fragment = document.createDocumentFragment(),
        bounds = new kakao.maps.LatLngBounds(),
        listStr = '';

    // 검색 결과 목록에 추가된 항목들을 제거합니다
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();

    for (var i=0; i<places.length; i++) {
        // 마커를 생성하고 지도에 표시합니다
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i),
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);

        // 마커와 검색결과 항목에 mouseover 했을때
        // 해당 장소에 인포윈도우에 장소명을 표시합니다
        // mouseout 했을 때는 인포윈도우를 닫습니다
        (function(marker, title) {
            kakao.maps.event.addListener(marker, 'mouseover', function() {
                displayInfowindow(marker, title);
            });

            kakao.maps.event.addListener(marker, 'mouseout', function() {
                infowindow.close();
            });

            itemEl.onmouseover = function() {
                displayInfowindow(marker, title);
            };

            itemEl.onmouseout = function() {
                infowindow.close();
            };
        })(marker, places[i].place_name);

        fragment.appendChild(itemEl);
    }

    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
    listEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {
    var el = document.createElement('li'),
        itemStr = '<div class="info">' +
            '   <h5>' + places.place_name + '</h5>';

    var address = places.road_address_name ? places.road_address_name : places.address_name;
    // 작은따옴표, 큰따옴표 이스케이프 처리
    var safePlaceName = places.place_name.replace(/'/g, "\\'");
    var safeAddress = address.replace(/'/g, "\\'");

    itemStr += '    <span>' + address + '</span>';
    if (places.phone) {
        itemStr += '    <span class="tel">' + places.phone + '</span>';
    }

    itemStr += '</div>' +
        '<button class="add-btn" onclick="addPlace(\'' + safePlaceName + '\', \'' + safeAddress + '\')">' +
        '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">' +
        '<line x1="12" y1="5" x2="12" y2="19"></line>' +
        '<line x1="5" y1="12" x2="19" y2="12"></line>' +
        '</svg>' +
        '장소 추가</button>';

    el.innerHTML = itemStr;
    el.className = 'item';

    return el;
}

// 장소를 추가하는 함수입니다
function addPlace(placeName, address) {
    var courseList = document.getElementById('courseList');

    // 이미 추가된 장소인지 확인
    var existingPlaces = courseList.getElementsByClassName('place-info');
    for (var i = 0; i < existingPlaces.length; i++) {
        var existingName = existingPlaces[i].getElementsByTagName('h4')[0].textContent;
        var existingAddress = existingPlaces[i].getElementsByTagName('p')[0].textContent;

        if (existingName === placeName && existingAddress === address) {
            alert('이미 추가된 장소입니다.');
            return;
        }
    }

    // 새로운 장소 항목 생성
    var placeItem = document.createElement('div');
    placeItem.className = 'course-item';

    // 장소 정보와 삭제 버튼을 포함한 HTML 생성
    placeItem.innerHTML = `
        <div class="place-info">
            <h4>${placeName}</h4>
            <p>${address}</p>
        </div>
        <button class="delete-btn" onclick="removePlace(this, '${placeName}', '${address}')">×</button>
    `;

    // 생성한 항목을 코스 리스트에 추가
    courseList.appendChild(placeItem);

    // 추가된 장소의 '장소 추가' 버튼 비활성화
    updateAddButton(placeName, address, true);
}

// 장소를 제거하는 함수입니다
function removePlace(button, placeName, address) {
    // 항목 삭제
    button.parentElement.remove();

    // 삭제된 장소의 '장소 추가' 버튼 다시 활성화
    updateAddButton(placeName, address, false);
}

// 장소 추가 버튼의 상태를 업데이트하는 함수입니다
function updateAddButton(placeName, address, disable) {
    var searchResults = document.getElementById('placesList');
    var items = searchResults.getElementsByClassName('item');

    for (var i = 0; i < items.length; i++) {
        var itemName = items[i].querySelector('h5').textContent;
        var itemAddress = items[i].querySelector('.info span').textContent;

        if (itemName === placeName && itemAddress === address) {
            var addBtn = items[i].querySelector('.add-btn');
            if (disable) {
                // 버튼 비활성화
                addBtn.disabled = true;
                addBtn.style.backgroundColor = '#cccccc';
                addBtn.style.cursor = 'not-allowed';
                addBtn.innerHTML = '추가됨';
            } else {
                // 버튼 활성화
                addBtn.disabled = false;
                addBtn.style.backgroundColor = '#ff9eaa';
                addBtn.style.cursor = 'pointer';
                addBtn.innerHTML = `
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <line x1="12" y1="5" x2="12" y2="19"></line>
                        <line x1="5" y1="12" x2="19" y2="12"></line>
                    </svg>
                    장소 추가`;
            }
            break;
        }
    }
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions = {
            spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
        marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i;

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild(paginationEl.lastChild);
    }

    for (i = 1; i <= pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i === pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
function displayInfowindow(marker, title) {
    var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';
    infowindow.setContent(content);
    infowindow.open(map, marker);
}

// 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}

// 코스 등록 버튼 클릭 이벤트 처리
document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('.register-btn').addEventListener('click', function() {
        // 선택된 장소들의 정보를 가져옴
        const selectedPlaces = [];
        const courseItems = document.querySelectorAll('.course-item');
        const title = document.getElementById('courseTitle')?.value.trim();
        const description = document.getElementById('courseDescription')?.value.trim();
        const date = sessionStorage.getItem('selectedDate');

        if (!title || !description) {
            alert("제목과 소개글을 모두 입력해주세요.");
            return;
        }

        if (courseItems.length === 0) {
            alert('최소 한 개 이상의 장소를 선택해주세요.');
            return;
        }

        courseItems.forEach((item, index) => {
            selectedPlaces.push({
                placeName: item.querySelector('.place-info h4').textContent,
                address: item.querySelector('.place-info p').textContent,
                rank: index + 1
            });
        });

        const payload = {
            title,
            description,
            date,
            places: selectedPlaces
        };

        fetch('/api/course/save', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
        .then(res => res.ok ? res.text() : Promise.reject(res))
        .then(msg => {
            // 코스 저장 성공 시 바로 메인 페이지로 이동
            alert('나의 데이트 코스가 등록되었습니다!');
            window.location.href = '/';
        })
        .catch(err => {
            console.error('코스 저장 실패:', err);
            alert('코스 저장 중 오류가 발생했습니다.');
        });

        if (selectedPlaces.length === 0) {
            alert('최소 한 개 이상의 장소를 선택해주세요.');
            return;
        }
    });
});
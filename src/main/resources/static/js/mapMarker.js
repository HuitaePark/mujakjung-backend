/* ---------------------------------------------------
 * mapMarker.js  (지도·카드 담당)
 * - Kakao Maps SDK는 autoload=false 옵션으로 먼저 로드
 * ------------------------------------------------- */
(() => {

    // 전역 변수 초기화는 유지
    window.lastRegion            = null;
    window.lastRestaurantList    = null;
    window.lastAccommodationList = null;

    // LEVEL 수치가 클수록 멀리서 전체 영역을 볼 수 있습니다
    const INITIAL_LEVEL = 13;
    const BLINK_INTERVAL = 200; // 깜빡임 주기 (ms)
    const BLINK_DURATION = 3000; // 깜빡임 지속 시간 (ms)
    const koreaBounds   = { latMin: 33, latMax: 38.5, lngMin: 124.5, lngMax: 131.5 };

    // 전역 변수
    let map, marker = null, defaultCenter, defaultLevel;
    let koreaPolygon;  // optional: GeoJSON으로 내부 검사용

    // DOM 캐시
    const themeSelect  = document.getElementById('theme-select');
    const regionSelect = document.getElementById('region-select');
    const seasonSelect = document.getElementById('season-select');
    const mbtiSelect   = document.getElementById('mbti-select');
    const recommendBtn = document.getElementById('recommend-btn');

    // 초기 안내문
    function showInitialGuide(targetId, message) {
        const box = document.getElementById(targetId);
        if (!box) return;
        box.innerHTML = `
      <div class="text-center text-sm text-gray-500 py-6">
        ${message}<br>
        <span class="font-medium text-blue-600">[ 랜덤 여행지 추천 ]</span> 버튼을 눌러<br>
        여행 정보를 받아보세요!
      </div>`;
    }

    // GeoJSON 로드 (optional: 폴리곤 검사용)
    fetch('https://raw.githubusercontent.com/johan/world.geo.json/master/countries/KOR.geo.json')
        .then(r => r.json())
        .then(j => { koreaPolygon = (j.type === 'FeatureCollection') ? j.features[0] : j; })
        .catch(console.error);

    // 랜덤 좌표 생성 (폴리곤 내부인지 검사)
    function getRandomLatLng() {
        let lat, lng, pt;
        if (koreaPolygon) {
            do {
                lat = Math.random() * (koreaBounds.latMax - koreaBounds.latMin) + koreaBounds.latMin;
                lng = Math.random() * (koreaBounds.lngMax - koreaBounds.lngMin) + koreaBounds.lngMin;
                pt  = turf.point([lng, lat]);
            } while (!turf.booleanPointInPolygon(pt, koreaPolygon));
        } else {
            lat = (koreaBounds.latMin + koreaBounds.latMax) / 2;
            lng = (koreaBounds.lngMin + koreaBounds.lngMax) / 2;
        }
        return { lat, lng };
    }

    // 마커 표시 (이전 마커 제거)
    function placeMarker(lat, lng) {
        if (marker) marker.setMap(null);
        marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(lat, lng),
            map: map
        });
    }

    // --- 수정된 부분 ---
    // 지도 초기화
    function initMap() {
        map = new kakao.maps.Map(document.getElementById('vmap'), {
            center: new kakao.maps.LatLng(35.7, 127.5),
            level: INITIAL_LEVEL,
            draggable: false // 지도 드래그 이동을 비활성화합니다.
        });
        defaultCenter = map.getCenter();
        defaultLevel  = map.getLevel();

        recommendBtn.addEventListener('click', onClickRecommend);
    }
    // --- 수정 끝 ---

    // ‘랜덤 추첨’ 버튼 클릭 핸들러
    function onClickRecommend() {
        // 0) 지도 리셋 (대한민국 전체)
        map.panTo(defaultCenter);                     // 부드러운 패닝
        map.setLevel(defaultLevel, { animate: true }); // 애니메이션 줌

        // 초기 안내문도 재생성
        showInitialGuide('course-list',        '아직 추천받은 여행 코스가 없어요.');
        showInitialGuide('restaurant-list',    '아직 추천받은 식당이 없어요.');
        showInitialGuide('accommodation-list', '아직 추천받은 숙소가 없어요.');

        // 1) 깜빡임 애니메이션
        const blink = setInterval(() => {
            const { lat, lng } = getRandomLatLng();
            placeMarker(lat, lng);
        }, BLINK_INTERVAL);

        // 2) BLINK_DURATION 후 깜빡임 중단 → 최종 위치로 확대
        setTimeout(() => {
            clearInterval(blink);
            const { lat, lng } = getRandomLatLng();
            const target = new kakao.maps.LatLng(lat, lng);

            placeMarker(lat, lng);
            fetchCourseAndRender(); // 코스 정보 가져오기 및 렌더링
        }, BLINK_DURATION);
    }

    // API 호출 & 카드 렌더 (기존 로직 그대로)
    async function fetchCourseAndRender() {
        const theme = themeSelect.value;
        let url = `/course/${theme}`;
        const params = new URLSearchParams();
        if (theme === 'region') params.set('region', regionSelect.value);
        if (theme === 'season') params.set('season', seasonSelect.value);
        if (theme === 'mbti')   params.set('type',   mbtiSelect.value);
        if ([...params].length)  url += `?${params}`;

        try {
            const res = await fetch(url);
            if (!res.ok) throw new Error(res.statusText);
            const data = await res.json();
            renderCourseCards(data); // 코스 카드 렌더링

            // 식당·숙소 API 병렬 호출 (async/await으로 순서 보장)
            const [restaurantRes, accommodationRes] = await Promise.all([
                fetch(`/restaurant/region?region=${encodeURIComponent(window.lastRegion)}`),
                fetch(`/accommodation/region?region=${encodeURIComponent(window.lastRegion)}`)
            ]);

            if (!restaurantRes.ok) throw new Error(restaurantRes.statusText);
            if (!accommodationRes.ok) throw new Error(accommodationRes.statusText);

            const restaurantData    = await restaurantRes.json();
            window.lastRestaurantList    = restaurantData.list;
            const accommodationData = await accommodationRes.json();
            window.lastAccommodationList = accommodationData.list;


            // ★ 중요: 식당, 숙소 데이터를 가져온 후 바로 렌더링 함수 호출
            // resultTap.js의 renderList 함수를 호출
            if (typeof window.renderList === 'function') {
                window.renderList(window.lastRestaurantList, 'restaurant');
                window.renderList(window.lastAccommodationList, 'accommodation');
            } else {
                console.error("renderList 함수를 찾을 수 없습니다. resultTap.js 로드를 확인하세요.");
            }

        } catch (e) {
            console.error(e);
            alert('추천 정보를 가져오는 중 오류가 발생했습니다.');
            // 오류 발생 시에도 안내문 표시
            showInitialGuide('restaurant-list', '식당 정보를 불러오는 중 오류가 발생했습니다.');
            showInitialGuide('accommodation-list', '숙소 정보를 불러오는 중 오류가 발생했습니다.');
        }
    }

    // 카드 렌더링 분리
    function renderCourseCards(data) {
        window.lastRegion = data.region;
        document.getElementById('result-section').style.display = 'block';
        const themeLabel = themeSelect.options[themeSelect.selectedIndex].text;
        document.getElementById('result-course').textContent = data.courseName;
        const parentCourseId = data.id;
        const list = document.getElementById('course-list');
        list.innerHTML = '';

        data.list.forEach(item => {
            const showLink = ['restaurant','accommodation'].includes(item.dtoType);
            const linkHtml = showLink && item.websiteLink
                ? `<a href="${item.websiteLink}" target="_blank" class="text-xs text-blue-500 mt-1 block">웹사이트</a>`
                : '';

            const card = document.createElement('div');
            card.className = 'card flex overflow-hidden relative';

            const itemId = item.id ?? item.contentId ?? item.attractionId;
            const itemType = item.dtoType || item.type || 'course';

            if (itemId != null) card.dataset.id = String(itemId);
            card.dataset.type = String(itemType);

            card.innerHTML = `
        <img src="${item.imgPath}" alt="${item.name}" class="w-32 h-32 object-cover flex-shrink-0"/>
        <div class="p-3 flex flex-col justify-between flex-1">
            <div>
                <h4 class="font-medium text-black">${item.name}</h4>
                ${linkHtml}
            </div>
            <div class="text-right space-x-2">
                <button class="view-detail-btn text-xs px-2 py-1 bg-gray-200 rounded">상세보기</button>
                <button class="like-btn text-xs px-2 py-1 bg-red-200 rounded">
                    <i class="fa-solid fa-heart"></i> <span class="like-count">${item.likeCount ?? 0}</span>
                </button>
                <button class="share-btn text-xs px-2 py-1 bg-yellow-400 rounded">
                    <i class="fa-solid fa-share-nodes"></i>
                </button>
            </div>
        </div>`;

            list.appendChild(card);

            // 좋아요 초기 상태 조회 후 스타일 반영
            // getLikeStatus 엔드포인트에서 { "liked": boolean, "totalLikes": long } 응답을 받음
            if (itemId != null && itemType === 'course') {
                fetch(`/course/${itemId}/like-status`)
                    .then(res => res.json())
                    .then(status => {
                        const likeBtn = card.querySelector('.like-btn');
                        const countSpan = likeBtn.querySelector('.like-count');

                        // --- 디버깅용 로그 ---
                        console.log(`[초기 상태] Course ID: ${itemId}, API 응답 liked: ${status.liked}, totalLikes: ${status.totalLikes}`);
                        console.log(`[초기 상태] 현재 span 내용: ${countSpan.textContent}`);

                        countSpan.textContent = status.totalLikes; // 초기 좋아요 수도 정확히 반영

                        // --- 디버깅용 로그 ---
                        console.log(`[초기 상태] 업데이트 후 span 내용: ${countSpan.textContent}`);


                        if (status.liked) { // 이미 좋아요가 눌러져 있다면
                            likeBtn.classList.remove('bg-red-200');
                            likeBtn.classList.add('bg-red-500', 'text-white');
                            likeBtn.disabled = true; // 이미 좋아요 눌렀으면 비활성화
                            // --- 디버깅용 로그 ---
                            console.log(`[초기 상태] 버튼 비활성화 및 색상 변경됨.`);
                        }
                    })
                    .catch(e => {
                        console.warn(`[초기 상태] 좋아요 상태 조회 실패 (ID: ${itemId}):`, e);
                    });
            }

            card.querySelector('.like-btn').addEventListener('click', async e => {
                e.stopPropagation();
                const btn = e.currentTarget;
                const countSpan = btn.querySelector('.like-count');

                if (btn.disabled) {
                    console.log(`[클릭] 버튼이 이미 비활성화되어 있습니다.`);
                    return;
                }

                try {
                    // 1. 로그인 여부 확인 (200 외에는 전부 로그인 필요로 간주)
                    const sessionCheck = await fetch('/my');
                    if (sessionCheck.status !== 200) {
                        alert('좋아요를 누르려면 먼저 로그인해주세요.');
                        return;
                    }

                    // 2. 좋아요 API 호출
                    const res = await fetch(`/course/${itemId}/like`, { method: 'POST' });
                    if (!res.ok) {
                        const errorText = await res.text();
                        throw new Error(`좋아요 요청 실패: ${res.status} - ${errorText}`);
                    }

                    const result = await res.json();
                    countSpan.textContent = result.totalLikes;

                    btn.classList.remove('bg-red-200');
                    btn.classList.add('bg-red-500', 'text-white');
                    btn.disabled = true;

                } catch (err) {
                    console.error(err);
                    alert('좋아요 처리 중 오류가 발생했습니다: ' + err.message);
                }
            });

            // 공유 버튼 이벤트
            card.querySelector('.share-btn').addEventListener('click', e => {
                e.stopPropagation();
                shareItem(card);
            });

            // 상세보기 버튼 이벤트
            card.querySelector('.view-detail-btn').addEventListener('click', e => {
                e.stopPropagation();
                openModal({ ...item, likeCount: item.likeCount ?? 0 });
            });
        });
    }

    // 페이지 로드되면 Kakao SDK 초기화
    document.addEventListener('DOMContentLoaded', () => {
        kakao.maps.load(initMap);
        initThemeSelectUI();
        // 초기 안내문은 유지
        showInitialGuide('course-list',        '아직 추천받은 여행 코스가 없어요.');
        showInitialGuide('restaurant-list',    '아직 추천받은 식당이 없어요.');
        showInitialGuide('accommodation-list', '아직 추천받은 숙소가 없어요.');
    });

    // 테마 UI 표시 로직 (기존)
    function initThemeSelectUI() {
        const wrap = document.getElementById('theme-options');
        function update() {
            const v = themeSelect.value;
            if (v === 'random') wrap.classList.add('hidden');
            else {
                wrap.classList.remove('hidden');
                regionSelect.classList.toggle('hidden', v !== 'region');
                seasonSelect.classList.toggle('hidden', v !== 'season');
                mbtiSelect.classList.toggle('hidden', v !== 'mbti');
            }
        }
        themeSelect.onchange = update;
        update();
    }

})();

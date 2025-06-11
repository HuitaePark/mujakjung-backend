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
    // let lastRegion, lastRestaurantList, lastAccommodationList; // 이 전역 변수들은 window 객체로 관리되므로 중복 선언 제거

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

    // 지도 초기화
    function initMap() {
        map = new kakao.maps.Map(document.getElementById('vmap'), {
            center: new kakao.maps.LatLng(36.5, 127.5),
            level: INITIAL_LEVEL
        });
        defaultCenter = map.getCenter();
        defaultLevel  = map.getLevel();

        recommendBtn.addEventListener('click', onClickRecommend);
    }

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

            map.panTo(target);
            map.setLevel(3, { animate: true, anchor: target });

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
            const accommodationData = await accommodationRes.json();

            window.lastRestaurantList    = restaurantData.list;
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

            // ✅ ID 유효성 검사 및 기본값 설정
            const itemId = item.id ?? item.contentId ?? item.attractionId;
            const itemType = item.dtoType || item.type || 'course';

            // dataset에 안전하게 설정
            if (itemId != null) {card.dataset.id = String(itemId);}

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
                    <button class="share-btn text-xs px-2 py-1 bg-yellow-400 rounded">
                        <i class="fa-solid fa-share-nodes"></i>
                    </button>
                </div>
            </div>`;

            list.appendChild(card);

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
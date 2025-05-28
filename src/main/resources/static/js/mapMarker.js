//— 기존에 선언해 두신 맵/폴리곤/마커 관련 함수들 —//

let koreaPolygon;
let defaultCenter, defaultZoom;
let lastRegion         = null;
let lastRestaurantList = null;
let lastAccommodationList = null;
let modalDetailList = [];
let modalCurrentIndex = 0;
let currentDetail = null;
let currentCourseId = null;
const YOUR_KEY     = '44ADFAEA-B1BF-3BC2-8036-0F5B19914FF1';
const INITIAL_ZOOM = 6;
const koreaBounds  = { latMin:33.0, latMax:38.5, lngMin:124.5, lngMax:131.5 };


const markerLayer = new ol.layer.Vector({ source: new ol.source.Vector() });
const vmap = new ol.Map({
    target: 'vmap',
    layers: [
        new ol.layer.Tile({ source: new ol.source.XYZ({
                url: `https://api.vworld.kr/req/wmts/1.0.0/${YOUR_KEY}/Base/{z}/{y}/{x}.png`,
                attributions: '© VWorld'
            }) }),
        markerLayer
    ],
    view: new ol.View({
        projection: 'EPSG:3857',
        center: ol.proj.fromLonLat([127.5,36.5]),
        zoom: INITIAL_ZOOM
    })
});
defaultCenter = vmap.getView().getCenter();
defaultZoom   = INITIAL_ZOOM;

// GeoJSON 폴리곤 로드
fetch('https://raw.githubusercontent.com/johan/world.geo.json/master/countries/KOR.geo.json')
    .then(res => res.json())
    .then(json => {
        koreaPolygon = (json.type === 'FeatureCollection') ? json.features[0] : json;
    })
    .catch(console.error);

function getRandomLatLng() {
    if (!koreaPolygon) {
        return {
            lat: (koreaBounds.latMin + koreaBounds.latMax)/2,
            lng: (koreaBounds.lngMin + koreaBounds.lngMax)/2
        };
    }
    let pt, lat, lng;
    do {
        lat = Math.random()*(koreaBounds.latMax-koreaBounds.latMin)+koreaBounds.latMin;
        lng = Math.random()*(koreaBounds.lngMax-koreaBounds.lngMin)+koreaBounds.lngMin;
        pt  = turf.point([lng, lat]);
    } while (!turf.booleanPointInPolygon(pt, koreaPolygon));
    return { lat, lng };
}

function setMarker(lat, lng) {
    markerLayer.getSource().clear();
    const feature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([lng, lat]))
    });
    feature.setStyle(new ol.style.Style({
        image: new ol.style.Circle({
            radius: 10,
            fill:   new ol.style.Fill({  color:'rgba(255,0,0,0.8)' }),
            stroke: new ol.style.Stroke({color:'#fff', width:2})
        })
    }));
    markerLayer.getSource().addFeature(feature);
}

//— 여기서부터 버튼 핸들러에 통합 —//

const themeSelect  = document.getElementById('theme-select');
const regionSelect = document.getElementById('region-select');
const seasonSelect = document.getElementById('season-select');
const mbtiSelect   = document.getElementById('mbti-select');

document.getElementById('recommend-btn').addEventListener('click', () => {
    // 0) 맵 즉시 리셋
    vmap.getView().setCenter(defaultCenter);
    vmap.getView().setZoom(defaultZoom);

    // 1) 깜빡임 애니메이션
    const animInterval = setInterval(() => {
        const { lat, lng } = getRandomLatLng();
        setMarker(lat, lng);
    }, 200);

    // 2) 3초 뒤 애니 종료 & 최종 애니메이션 없이 마커 고정
    setTimeout(() => {
        clearInterval(animInterval);
        const { lat, lng } = getRandomLatLng();
        vmap.getView().setCenter(ol.proj.fromLonLat([lng, lat]));
        vmap.getView().setZoom(17);
        setMarker(lat, lng);

        // 3) 코스 API 호출
        const theme = themeSelect.value;
        let url = `/course/${theme}`;
        const params = new URLSearchParams();
        if (theme === 'region') params.set('region', regionSelect.value);
        if (theme === 'season') params.set('season', seasonSelect.value);
        if (theme === 'mbti')   params.set('type',   mbtiSelect.value);
        if ([...params].length)  url += `?${params}`;

        fetch(url)
            .then(res => {
                if (!res.ok) throw new Error(res.statusText);
                return res.json();
            })
            .then(data => {
                // --- 1) 추천 결과 저장 및 화면 표시 ---
                lastRegion = data.region;
                document.getElementById('result-section').style.display = 'block';
                const themeLabel = themeSelect.options[themeSelect.selectedIndex].text;
                document.getElementById('result-text').innerHTML =
                    `${themeLabel} 추천받은 결과는 <br><span class="text-blue-700">${data.region}</span>입니다.`;
                document.getElementById('result-course').textContent = data.courseName;

                // --- 2) 코스 리스트 렌더링 ---
                const listContainer = document.getElementById('course-list');
                listContainer.innerHTML = '';

                // 🔥 디버깅: API 응답 데이터 확인
                console.log('API 응답 data:', data);
                console.log('data.list:', data.list);

                data.list.forEach((item, index) => {
                    // 🔥 디버깅: 각 아이템의 likeCount 확인
                    console.log(`Item ${index}:`, item);
                    console.log(`Item ${index} likeCount:`, item.likeCount);

                    const card = document.createElement('div');
                    card.className = 'card flex overflow-hidden';
                    card.innerHTML = `
      <img src="${item.imgPath}" alt="${item.name}" class="w-32 h-32 object-cover flex-shrink-0"/>
      <div class="p-3 flex flex-col justify-between flex-1">
        <div>
          <h4 class="font-medium text-black">${item.name}</h4>
          <a href="${item.websiteLink || '#'}" target="_blank" class="text-xs text-blue-500 mt-1 block">
            웹사이트
          </a>
        </div>
        <div class="text-right">
          <button class="view-detail-btn">상세보기</button>
        </div>
      </div>
    `;

                    // 🔑 appendChild 먼저! DOM에 넣고 나서 querySelector 해야 함
                    listContainer.appendChild(card);

                    // ✅ DOM 삽입 후에 버튼 찾아야 정확히 잡힘
                    const btn = card.querySelector('.view-detail-btn');
                    if (btn) {
                        btn.addEventListener('mousedown', (e) => {
                            // 🔥 수정: 원본 item을 그대로 전달하고, 디버깅 로그 추가
                            e.stopPropagation();
                            console.log('버튼 클릭 시 item:', item);
                            console.log('모달에 전달할 likeCount:', item.likeCount);

                            // likeCount가 undefined이거나 null인 경우에만 0으로 설정
                            const itemToPass = {
                                ...item,
                                likeCount: item.likeCount !== undefined && item.likeCount !== null ? item.likeCount : 0
                            };

                            console.log('최종 전달 데이터:', itemToPass);
                            setTimeout(() => {                // ✔ 다음 이벤트 루프로 미룸
                                openModal(itemToPass);
                                }, 0);
                        });
                    } else {
                        console.warn('버튼 못 찾음 😥', card.innerHTML);
                    }
                });

                // --- 3) 식당·숙소 API 병렬 호출 (한 번만) ---
                const restUrl  = `/restaurant/region?region=${encodeURIComponent(lastRegion)}`;
                const accomUrl = `/accommodation/region?region=${encodeURIComponent(lastRegion)}`;
                Promise.all([
                    fetch(restUrl).then(r => {
                        if (!r.ok) throw new Error('식당 API 오류');
                        return r.json();
                    }),
                    fetch(accomUrl).then(r => {
                        if (!r.ok) throw new Error('숙소 API 오류');
                        return r.json();
                    })
                ])
                    .then(([restData, accomData]) => {
                        lastRestaurantList    = restData.list;
                        lastAccommodationList = accomData.list;
                    })
                    .catch(err => {
                        console.error('식당/숙소 로드 중 오류:', err);
                    });
            })
            .catch(err => {
                console.error(err);
                alert('추천 정보를 가져오는 중 오류가 발생했습니다.');
            });

    }, 3000);


});

function openModalList(detailList) {
    if (!detailList || !detailList.length) return;

    modalDetailList  = detailList;
    modalCurrentIndex = 0;

    // ① 첫 항목을 openModal 로 띄워서 like 로직 재사용
    openModal(detailList[0]);

    // ② 페이지 네비게이션 버튼만 따로 보여주기
    document.getElementById('modal-prev-btn').classList.remove('hidden');
    document.getElementById('modal-next-btn').classList.remove('hidden');
    document.getElementById('modal-page').classList.remove('hidden');
    updateModalPageText();
}


document.getElementById('close-modal').addEventListener('click', () => {
    document.getElementById('detail-modal').style.display = 'none';  // hide
});

function renderModal(detail) {
    document.getElementById('modal-img').src = detail.imgPath;
    document.getElementById('modal-title').textContent = detail.name;
    document.getElementById('modal-like').textContent = detail.likeCount || 0;
    document.getElementById('modal-desc').textContent = detail.description;
     // ← 추가: 페이지 바뀔 때 like 버튼 상태도 맞춰주기
    currentDetail   = detail;
    currentCourseId = detail.id;
    updateLikeButtonState();
}

function updateModalPageText() {
    const page = document.getElementById('modal-page');
    page.textContent = `${modalCurrentIndex + 1} / ${modalDetailList.length}`;
    document.getElementById('modal-prev-btn').disabled = modalCurrentIndex === 0;
    document.getElementById('modal-next-btn').disabled = modalCurrentIndex === modalDetailList.length - 1;
}

document.getElementById('modal-prev-btn').addEventListener('click', () => {
    if (modalCurrentIndex > 0) {
        modalCurrentIndex--;
        renderModal(modalDetailList[modalCurrentIndex]);
        updateModalPageText();
    }
});

document.getElementById('modal-next-btn').addEventListener('click', () => {
    if (modalCurrentIndex < modalDetailList.length - 1) {
        modalCurrentIndex++;
        renderModal(modalDetailList[modalCurrentIndex]);
        updateModalPageText();
    }
});

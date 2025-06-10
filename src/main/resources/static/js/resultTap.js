document.addEventListener('DOMContentLoaded', () => {
  // 탭 버튼과 컨텐츠 영역 참조
  const tabs = {
    course:        document.getElementById('tab-course'),
    restaurant:    document.getElementById('tab-restaurant'),
    accommodation: document.getElementById('tab-accommodation')
  };
  const contents = {
    course:        document.getElementById('content-course'),
    restaurant:    document.getElementById('content-restaurant'),
    accommodation: document.getElementById('content-accommodation')
  };

  // 1) 추천된 region을 저장할 전역 변수(다른 스크립트에서 설정됨)
  //    window.lastRegion
  //    window.lastRestaurantList
  //    window.lastAccommodationList

  // 2) 식당/숙소 리스트 렌더 함수 (전역으로 노출)
  // mapMarker.js에서 호출할 수 있도록 window 객체에 할당
  window.renderList = function(list, type) {
    const container = document.getElementById(`${type}-list`);
    container.innerHTML = ''; // 초기화

    if (!list || list.length === 0) {
      container.innerHTML = `
            <div class="text-center text-sm text-gray-500 py-6">
                추천할 ${type==='restaurant'?'식당':'숙소'} 정보가 없습니다.<br>
            </div>`;
      return;
    }

    list.forEach(item => {
      let imgSrc = item.imgPath || '/images/default.jpg';
      if (!/^https?:\/\//.test(imgSrc)) {
        imgSrc = window.location.origin + imgSrc;
      }
      const detailLink = item.websiteLink
          ? item.websiteLink
          : `https://map.naver.com/search/${encodeURIComponent(item.name)}`;

      const card = document.createElement('div');
      card.className = 'card flex overflow-hidden relative'; // 기존 클래스 유지
      card.innerHTML = `
        <img src="${imgSrc}" alt="${item.name}"
             class="w-32 h-32 object-cover flex-shrink-0"/> <div class="p-3 flex flex-col justify-between flex-1">
          <div>
            <h4 class="font-medium text-black">${item.name}</h4>
            ${item.address ? `<p class="text-xs text-gray-500">${item.address}</p>` : ''}
            <a href="${detailLink}" target="_blank"
               class="text-xs text-blue-500 mt-1 block">웹사이트</a>
          </div>
          <div class="text-right space-x-2 mt-2">
            <button class="share-btn text-xs px-2 py-1 bg-yellow-400 rounded">
              <i class="fa-solid fa-share-nodes"></i>
            </button>
          </div>
        </div>`;

      const shareBtn = card.querySelector('.share-btn');
      shareBtn.addEventListener('click', e => {
        e.stopPropagation();
        if (typeof window.shareItem === 'function') {
          window.shareItem({ ...item, dtoType: type });
        } else {
          console.error("shareItem 함수를 찾을 수 없습니다.");
        }
      });

      container.appendChild(card);
    });
  };


  // 3) 리스트 로드 함수 (캐시 활용 + API 호출 방지)
  function loadList(type) {
    const container = document.getElementById(`${type}-list`);
    // 추천이 없으면 안내
    if (!window.lastRegion) {
      container.innerHTML = `
        <div class="text-center text-sm text-gray-500 py-6">
          아직 추천받은 ${type==='restaurant'?'식당':'숙소'}가 없어요.<br>
          <span class="font-medium text-blue-600">[ 랜덤 여행지 추천 ]</span> 버튼을 눌러<br>여행 정보를 받아보세요!
        </div>`;
      return;
    }

    // 캐시된 리스트가 있으면 바로 렌더
    const cacheKey = type === 'restaurant' ? 'lastRestaurantList' : 'lastAccommodationList';
    const cached = window[cacheKey];

    if (Array.isArray(cached) && cached.length > 0) {
      window.renderList(cached, type); // window.renderList 사용
      return;
    }

    // ★ 이 부분은 이제 mapMarker.js에서 데이터를 불러와 캐시하므로,
    //    resultTap.js에서는 탭 클릭 시 중복 API 호출을 하지 않습니다.
    //    만약 mapMarker.js에서 데이터를 불러오지 못했거나,
    //    사용자가 새로고침 없이 탭만 클릭한 경우를 대비하여
    //    이 로직을 유지할 수 있지만, 현재 의도상으로는 불필요합니다.
    //    만약 '랜덤 여행지 추천' 버튼을 누르지 않고 바로 식당/숙소 탭을 클릭하는 경우를 처리하려면 필요.
    //    현재 문제 상황에서는 '코스 조회 후 식당과 숙소 조회가 안 됨' 이므로
    //    mapMarker.js에서 한번에 처리하는 것이 더 적절합니다.

    // 데이터를 찾을 수 없는 경우 (예: 페이지 로드 후 '랜덤 여행지 추천' 버튼 누르지 않은 경우)
    container.innerHTML = `<p class="text-center text-gray-500">데이터를 불러오는 중입니다. 잠시만 기다려주세요.</p>`;
  }

  // 4) 탭 활성화 함수
  function activateTab(name) {
    Object.keys(tabs).forEach(key => {
      if (tabs[key]) {
        tabs[key].classList.toggle('active', key === name);
      }
      if (contents[key]) {
        contents[key].classList.toggle('hidden', key !== name);
      }
    });
    // '여행코스' 탭은 mapMarker.js에서 이미 렌더링하므로,
    // '식당' 또는 '숙소' 탭이 활성화될 때만 loadList 호출
    if (name === 'restaurant' || name === 'accommodation') {
      loadList(name);
    }
  }

  // 5) 이벤트 리스너 등록 & 초기 탭 활성화
  if (tabs.course) {
    tabs.course.addEventListener('click',        () => activateTab('course'));
  }
  if (tabs.restaurant) {
    tabs.restaurant.addEventListener('click',    () => activateTab('restaurant'));
  }
  if (tabs.accommodation) {
    tabs.accommodation.addEventListener('click', () => activateTab('accommodation'));
  }

  // 화면 로드 시 기본 탭 활성화
  activateTab('course');
});
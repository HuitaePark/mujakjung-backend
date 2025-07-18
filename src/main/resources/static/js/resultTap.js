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
      card.className = 'card flex overflow-hidden relative';

      const itemId = item.id ?? item.contentId ?? item.attractionId;
      const itemType = type;

      if (itemId != null) card.dataset.id = String(itemId);
      card.dataset.type = String(itemType);

      card.innerHTML = `
        <img src="${imgSrc}" alt="${item.name}"
             class="w-32 h-32 object-cover flex-shrink-0" onerror="this.onerror=null;this.src='/images/default.png';"/>
        <div class="p-3 flex flex-col justify-between flex-1">
          <div>
            <h4 class="font-medium text-black">${item.name}</h4>
            ${item.address ? `<p class="text-xs text-gray-500">${item.address}</p>` : ''}
            <a href="${detailLink}" target="_blank"
               class="text-xs text-blue-500 mt-1 block">웹사이트</a>
          </div>
          <div class="text-right space-x-2 mt-2">
            <button class="like-btn text-xs px-2 py-1 bg-red-200 rounded">
              <i class="fa-solid fa-heart"></i> <span class="like-count">${item.likeCount ?? 0}</span>
            </button>
            <button class="share-btn text-xs px-2 py-1 bg-yellow-400 rounded">
              <i class="fa-solid fa-share-nodes"></i>
            </button>
          </div>
        </div>`;

      container.appendChild(card);

      // --- 수정된 부분 ---
      // 좋아요 초기 상태 조회 후 스타일과 카운트 반영
      if (itemId != null) {
        fetch(`/${itemType}/${itemId}/like-status`)
            .then(res => {
              if (!res.ok) throw new Error('Like status fetch failed');
              return res.json();
            })
            .then(dto => {
              const likeBtn = card.querySelector('.like-btn');
              const countSpan = card.querySelector('.like-count');
              // API 응답 필드명 'totalLikes'로 변경, null일 경우 0으로 표시
              countSpan.textContent = dto.totalLikes ?? 0;
              // API 응답 필드명 'liked'로 변경
              if (dto.liked) {
                likeBtn.classList.remove('bg-red-200');
                likeBtn.classList.add('bg-red-500', 'text-white');
                likeBtn.disabled = true;
              }
            })
            .catch(err => console.warn(`Could not fetch like status for ${itemType} ${itemId}:`, err));
      }

      // 좋아요 클릭 이벤트
      const likeBtn = card.querySelector('.like-btn');
      likeBtn.addEventListener('click', async e => {
        e.stopPropagation();
        const btn = e.currentTarget;
        const countSpan = btn.querySelector('.like-count');

        if (btn.disabled || !itemId) return;

        try {
          // 1. 로그인 여부 확인
          const sessionCheck = await fetch('/my');
          if (sessionCheck.status !== 200) {
            alert('좋아요를 누르려면 먼저 로그인해주세요.');
            return;
          }

          // 2. 좋아요 API 호출
          const res = await fetch(`/${itemType}/${itemId}/like`, { method: 'POST' });
          if (!res.ok) {
            const errorText = await res.text();
            throw new Error(`좋아요 요청 실패: ${res.status} - ${errorText}`);
          }

          const resultDto = await res.json();

          // 3. UI 업데이트
          // API 응답 필드명 'totalLikes'로 변경, null일 경우 0으로 표시
          countSpan.textContent = resultDto.totalLikes ?? 0;

          // 버튼의 스타일을 변경하고 비활성화합니다.
          btn.classList.remove('bg-red-200');
          btn.classList.add('bg-red-500', 'text-white');
          btn.disabled = true;

        } catch (err) {
          console.error(err);
          alert('좋아요 처리 중 오류가 발생했습니다: ' + err.message);
        }
      });
      // --- 수정 끝 ---


      // 공유 버튼 이벤트
      const shareBtn = card.querySelector('.share-btn');
      shareBtn.addEventListener('click', e => {
        e.stopPropagation();
        if (typeof window.shareItem === 'function') {
          window.shareItem({ ...item, dtoType: type });
        } else {
          console.error("shareItem 함수를 찾을 수 없습니다.");
          alert('공유 기능은 아직 개발 중입니다!');
        }
      });
    });
  };


  // 3) 리스트 로드 함수 (캐시 활용 + API 호출 방지)
  function loadList(type) {
    const container = document.getElementById(`${type}-list`);
    if (!window.lastRegion) {
      container.innerHTML = `
        <div class="text-center text-sm text-gray-500 py-6">
          아직 추천받은 ${type==='restaurant'?'식당':'숙소'}가 없어요.<br>
          <span class="font-medium text-blue-600">[ 랜덤 여행지 추천 ]</span> 버튼을 눌러<br>
          여행 정보를 받아보세요!
        </div>`;
      return;
    }

    const cacheKey = type === 'restaurant' ? 'lastRestaurantList' : 'lastAccommodationList';
    const cached = window[cacheKey];

    if (Array.isArray(cached) && cached.length > 0) {
      window.renderList(cached, type);
      return;
    }

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

  activateTab('course');
});

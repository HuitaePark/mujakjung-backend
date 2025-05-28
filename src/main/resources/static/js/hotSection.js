document.addEventListener('DOMContentLoaded', () => {
    // 1. /share/hot 호출
    fetch('/share/hot')
        .then(res => {
            if (!res.ok) throw new Error(res.statusText);
            return res.json();
        })
        .then(data => {
            // API 응답은 [코스, 식당, 숙소] 순서이므로
            const [courseObj, restaurantObj, accommodationObj] = data;

            renderHot('course',      courseObj      ? [courseObj]      : []);
            renderHot('restaurant',  restaurantObj  ? [restaurantObj]  : []);
            renderHot('accommodation', accommodationObj ? [accommodationObj] : []);
        })
        .catch(err => console.error('[/share/hot] error:', err));

    // 2. type별로 카드 생성
    function renderHot(type, items) {
        const container = document.getElementById(`Hotcontent-${type}`);
        if (!container) return;
        container.innerHTML = '';

        items.forEach(item => {
            // 1) rawImg 에서 null을 방지하고 기본 이미지 지정
            let rawImg;
            if (type === 'course') {
                rawImg = item.list?.[0]?.imgPath;
            } else {
                rawImg = item.imgPath;
            }
            // rawImg 가 falsy(null/undefined/빈문자열)이면 기본 이미지 URL 사용
            let imgSrc = rawImg || '/images/default.jpg';

            // 2) 절대 URL이 아니면 origin 붙이기
            if (!/^https?:\/\//.test(imgSrc)) {
                imgSrc = window.location.origin + imgSrc;
            }

            // 3) 제목·부제목
            const title    = type === 'course' ? item.courseName : item.name;
            const subtitle = type === 'course' ? item.region     : item.address;

            // 4) 링크
            const linkUrl = item.websiteLink
                ? item.websiteLink
                : `https://map.naver.com/search/${encodeURIComponent(title)}`;
            const likeCount = item.likeCount ?? 0;
            const likeHtml = `
              <div class="flex items-center gap-1 text-xs text-gray-500 mt-2">
                <i class="fa-solid fa-thumbs-up"></i>
                <span>${likeCount}</span>
              </div>
                `;
            // 5) 카드 마크업
            const card = document.createElement('div');
            card.className = 'card-hot bg-white rounded-lg shadow-md overflow-hidden flex flex-col';
            card.innerHTML = `
      <img src="${imgSrc}" alt="${title}" class="w-full aspect-[16/9] object-cover flex-shrink-0">
      <div class="card-content p-3 flex-1 flex flex-col justify-between">
        <div>
          <h4 class="font-bold text-black text-sm mb-1">${title}</h4>
          <div class="text-xs text-gray-500 flex items-center">
            <i class="fas fa-map-marker-alt mr-1"></i>
            <span>${subtitle}</span>
          </div>
        </div>
        <div class="mt-2 flex justify-between items-center">
          <a href="${linkUrl}" target="_blank" class="text-xs text-blue-500 truncate">${linkUrl}</a>
          <button class="view-detail-btn text-xs text-gray-600">상세보기</button>
        </div>
      </div>
    `;


            container.appendChild(card);

            // ✅ [코스일 때만] 상세보기 버튼 모달 열기
            if (type === 'course') {
                const btn = card.querySelector('.view-detail-btn');
                const detail = item.list?.[0];
                if (btn && detail) {
                    detail.likeCount = item.likeCount;
                    btn.addEventListener('mousedown', (e) => {
                        e.stopPropagation();
                        console.log('핫 코스 상세보기 클릭됨');
                        setTimeout(() => openModalList(item.list), 0);
                    });
                }
            }
        });
    }
});
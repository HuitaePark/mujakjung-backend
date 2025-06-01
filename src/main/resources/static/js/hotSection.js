// hotSection.js (수정 후)

document.addEventListener('DOMContentLoaded', () => {
    /* 1. /share/hot 호출 */
    fetch('/share/hot')
        .then(res => {
            if (!res.ok) throw new Error(res.statusText);
            return res.json();
        })
        .then(data => {
            const [courseObj, restaurantObj, accommodationObj] = data;

            renderHot('course',        courseObj        ? [courseObj]        : []);
            renderHot('restaurant',    restaurantObj    ? [restaurantObj]    : []);
            renderHot('accommodation', accommodationObj ? [accommodationObj] : []);
        })
        .catch(err => console.error('[/share/hot] error:', err));

    /* 2. 항목별 카드 생성 */
    function renderHot(type, items) {
        const container = document.getElementById(`Hotcontent-${type}`);
        if (!container) return;
        container.innerHTML = '';

        items.forEach(item => {
            /* 1) 이미지 */
            const rawImg = type === 'course'
                ? item.list?.[0]?.imgPath
                : item.imgPath;
            let imgSrc   = rawImg || '/images/default.jpg';
            if (!/^https?:\/\//.test(imgSrc)) {
                imgSrc = window.location.origin + imgSrc;
            }

            /* 2) 텍스트 */
            const title    = type === 'course' ? item.courseName : item.name;
            const subtitle = type === 'course' ? item.region     : item.address;

            /* 3) 링크 */
            const linkUrl = item.websiteLink
                ? item.websiteLink
                : `https://map.naver.com/search/${encodeURIComponent(title)}`;

            /* 4) 카드 마크업 */
            const card = document.createElement('div');
            card.className =
                'card-hot bg-white rounded-lg shadow-md overflow-hidden flex flex-col';

            card.innerHTML = `
        <img src="${imgSrc}" alt="${title}"
             class="w-full aspect-[16/9] object-cover flex-shrink-0">
        <div class="card-content p-3 flex-1 flex flex-col justify-between">
          <div>
            <h4 class="font-bold text-black text-sm mb-1">${title}</h4>
            <div class="text-xs text-gray-500 flex items-center">
              <i class="fas fa-map-marker-alt mr-1"></i>
              <span>${subtitle}</span>
            </div>
          </div>
          <div class="mt-2 flex justify-between items-center space-x-2">
            <a href="${linkUrl}" target="_blank"
               class="text-xs text-blue-500 truncate flex-1">${linkUrl}</a>

            ${type === 'course'
                ? '<button class="view-detail-btn text-xs px-2 py-1 bg-gray-200 rounded">상세보기</button>'
                : ''}

            <button class="share-btn text-xs px-2 py-1 bg-yellow-400 rounded">
              <i class="fa-solid fa-share-nodes"></i>
            </button>
          </div>
        </div>
      `;

            container.appendChild(card);

            /* 상세보기(코스 전용) */
            if (type === 'course') {
                const btn = card.querySelector('.view-detail-btn');
                const detail = item.list?.[0];
                if (btn && detail) {
                    detail.likeCount = item.likeCount;
                    btn.addEventListener('mousedown', e => {
                        e.stopPropagation();
                        setTimeout(() => openModalList(item.list), 0);
                    });
                }
            }

            /* 공유 버튼 */
            card.querySelector('.share-btn').addEventListener('click', e => {
                e.stopPropagation();
                const shareTarget =
                    type === 'course' ? item.list?.[0] ?? item : item;
                shareItem(shareTarget);
            });
        });
    }

    /* 3. 카카오톡 공유 유틸 */
    function shareItem(item) {
        if (!item) return;

        const imageUrl = /^https?:\/\//.test(item.imgPath)
            ? item.imgPath
            : window.location.origin + (item.imgPath || '/images/default.jpg');

        const linkUrl = item.websiteLink
            ? item.websiteLink
            : `https://map.naver.com/search/${encodeURIComponent(item.name)}`;

        Kakao.Share.sendDefault({
            objectType: 'feed',
            content: {
                title: item.name,
                description:
                    item.address ||
                    item.region ||
                    '무작정 추천 여행지 정보를 확인해 보세요.',
                imageUrl,
                link: { mobileWebUrl: linkUrl, webUrl: linkUrl },
            },
            buttons: [
                {
                    title: '웹에서 보기',
                    link: { mobileWebUrl: linkUrl, webUrl: linkUrl },
                },
            ],
        });
    }
});

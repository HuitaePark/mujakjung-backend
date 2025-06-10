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
                ? item.list?.[0]?.imgPath // 코스일 경우 list의 첫 번째 항목 이미지 사용
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
            const linkHtml = (type !== 'course' && item.websiteLink)
                ? `<a href="${linkUrl}" target="_blank" class="text-xs text-blue-500 mt-1 block">웹사이트</a>`
                : '';

            /* 4) 카드 마크업 (mapMarker.js와 거의 동일한 구조, 너비 제한 없음) */
            const card = document.createElement('div');
            // 'w-full'은 유지하여 작은 화면에서 부모 너비를 채우도록 하고,
            // 별도의 'max-w' 클래스는 제거합니다.
            // 이렇게 하면 카드는 Flexbox의 동작에 따라 유동적으로 넓어집니다.
            card.className = 'card flex overflow-hidden relative w-full'; // max-w-[400px] 제거
            card.innerHTML = `
        <img src="${imgSrc}" alt="${title}" class="w-32 h-32 object-cover flex-shrink-0"/>
        <div class="p-3 flex flex-col justify-between flex-1"> <div>
            <h4 class="font-medium text-black">${title}</h4>
            ${subtitle ? `<p class="text-xs text-gray-500">${subtitle}</p>` : ''}
            ${linkHtml}
          </div>
          <div class="text-right space-x-2 mt-2">
            ${type === 'course'
                ? '<button class="view-detail-btn text-xs px-2 py-1 bg-gray-200 rounded">상세보기</button>'
                : ''}
            <button class="share-btn text-xs px-2 py-1 bg-yellow-400 rounded">
              <i class="fa-solid fa-share-nodes"></i>
            </button>
          </div>
        </div>`;

            container.appendChild(card);

            /* 상세보기(코스 전용) */
            if (type === 'course') {
                const btn = card.querySelector('.view-detail-btn');
                const detail = item.list?.[0];
                if (btn && detail) {
                    detail.likeCount = item.likeCount;
                    btn.addEventListener('click', e => {
                        e.stopPropagation();
                        window.openModal({ ...detail, likeCount: item.likeCount ?? 0 });
                    });
                }
            }

            /* 공유 버튼 */
            card.querySelector('.share-btn').addEventListener('click', e => {
                e.stopPropagation();
                const shareTarget = type === 'course'
                    ? { ...item.list?.[0], dtoType: type }
                    : { ...item, dtoType: type };
                shareItem(shareTarget);
            });
        });
    }

    /* 3. 카카오톡 공유 유틸 */
    function shareItem(item) {
        if (!item) return;

        const isCourseItem = item.dtoType === 'course';
        const title = isCourseItem ? (item.courseName || item.name) : item.name;
        const description = isCourseItem ? item.region : (item.address || '무작정 추천 여행지 정보를 확인해 보세요.');
        const imageUrl = /^https?:\/\//.test(item.imgPath)
            ? item.imgPath
            : window.location.origin + (item.imgPath || '/images/default.jpg');

        const linkUrl = item.websiteLink
            ? item.websiteLink
            : `https://map.naver.com/search/${encodeURIComponent(title)}`;

        Kakao.Share.sendDefault({
            objectType: 'feed',
            content: {
                title: title,
                description: description,
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
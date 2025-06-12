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
            const imgSrc = (() => {
                const rawImg = item.imgPath;
                if (!rawImg) return '/images/default.jpg';
                return /^https?:\/\//.test(rawImg) ? rawImg : window.location.origin + rawImg;
            })();

            const title = type === 'course' ? item.courseName : item.name;
            const subtitle = type === 'course'
                ? item.detailName || item.region
                : (item.address || ''); // null 제거


            const linkUrl = item.websiteLink
                ? item.websiteLink
                : `https://map.naver.com/search/${encodeURIComponent(title)}`;
            const linkHtml = (type !== 'course' && item.websiteLink)
                ? `<a href="${linkUrl}" target="_blank" class="text-xs text-blue-500 mt-1 block">웹사이트</a>`
                : '';

            const card = document.createElement('div');
            card.className = 'card flex overflow-hidden relative w-full';
            card.innerHTML = `
            <img src="${imgSrc}" alt="${title}" class="w-32 h-32 object-cover flex-shrink-0"/>
            <div class="p-3 flex flex-col justify-between flex-1">
              <div>
                <h4 class="font-medium text-black">${title}</h4>
                <p class="text-xs text-gray-500">${subtitle}</p>
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

            /* 상세보기 버튼 */
            if (type === 'course') {
                const btn = card.querySelector('.view-detail-btn');
                if (btn) {
                    btn.addEventListener('click', e => {
                        e.stopPropagation();
                        window.openModal({
                            name: item.courseName,
                            detailName: item.detailName ?? '',
                            description: item.description ?? '',
                            imgPath: item.imgPath,
                            likeCount: item.likeCount ?? 0,
                            id: item.id ?? item.attractionId ?? null,
                            dtoType: 'course'
                        });
                    });
                }
            }

            /* 공유 버튼 */
            card.querySelector('.share-btn').addEventListener('click', e => {
                e.stopPropagation();
                const shareTarget = {
                    ...item,
                    name: item.courseName || item.name,
                    dtoType: type
                };
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
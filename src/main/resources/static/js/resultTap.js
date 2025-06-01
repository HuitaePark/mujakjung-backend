// 탭 버튼과 컨텐츠 영역 참조
  const tabs = {
    course: document.getElementById('tab-course'),
    restaurant: document.getElementById('tab-restaurant'),
    accommodation: document.getElementById('tab-accommodation')
  };
  const contents = {
    course: document.getElementById('content-course'),
    restaurant: document.getElementById('content-restaurant'),
    accommodation: document.getElementById('content-accommodation')
  };
// 1) 추천된 region을 저장할 전역 변수


// 2) 식당/숙소 리스트를 불러오는 함수
// 2) 식당/숙소 리스트를 불러오는 함수
function loadList(type) {
  let list;
  if (type === 'restaurant')      list = lastRestaurantList;
  else if (type === 'accommodation') list = lastAccommodationList;
  else return;

  if (!list) return;   // 아직 추천을 안 받은 경우

  const listContainer = document.getElementById(`${type}-list`);
  listContainer.innerHTML = '';

  list.forEach(item => {
    const card = document.createElement('div');
    card.className = 'card flex overflow-hidden relative';

    /* 절대경로 이미지 */
    let imgSrc = item.imgPath || '/images/default.jpg';
    if (!/^https?:\/\//.test(imgSrc)) {
      imgSrc = window.location.origin + imgSrc;
    }

    /* 세부정보(식당·숙소) 버튼 → 웹사이트 없으면 네이버지도 */
    const detailLink =
        item.websiteLink ||
        `https://map.naver.com/search/${encodeURIComponent(item.name)}`;

    card.innerHTML = `
      <img src="${imgSrc}" alt="${item.name}"
           class="w-24 h-auto object-cover flex-shrink-0"/>
      <div class="p-3 flex flex-col justify-between flex-1">
        <div>
          <h4 class="font-medium text-black">${item.name}</h4>
          <p class="text-xs text-gray-500">${item.address}</p>
          <a href="${detailLink}" target="_blank"
             class="text-xs text-blue-500 mt-1 block">웹사이트</a>
        </div>
        <div class="text-right space-x-2 mt-2">

          <!-- ✈️ 공유 버튼 -->
          <button class="share-btn text-xs px-2 py-1 bg-yellow-400 rounded">
            <i class="fa-solid fa-share-nodes"></i>
          </button>
        </div>
      </div>
    `;

    /* 공유 버튼 핸들러 */
    card.querySelector('.share-btn').addEventListener('click', e => {
      e.stopPropagation();
      shareItem({         // shareItem 재사용
        ...item,
        dtoType: type      // restaurant | accommodation
      });
    });
    listContainer.appendChild(card);
  });
}
  // 활성화 함수
  function activateTab(name) {
    // 1) 탭 버튼 스타일 토글
    Object.keys(tabs).forEach(key => {
      tabs[key].classList.toggle('active', key === name);
      contents[key].classList.toggle('hidden', key !== name);
    });

    if (name === 'restaurant')   loadList('restaurant');
    if (name === 'accommodation') loadList('accommodation');
  }

// 4) 이벤트 리스너 등록 & 초기 탭 활성화
tabs.course.addEventListener('click',        () => activateTab('course'));
tabs.restaurant.addEventListener('click',    () => activateTab('restaurant'));
tabs.accommodation.addEventListener('click', () => activateTab('accommodation'));
document.addEventListener('DOMContentLoaded', () => activateTab('course'));
// Kakao JavaScript SDK를 사용하여 카카오톡 공유하기 기능을 구현하는 코드입니다.

document.addEventListener('DOMContentLoaded', () => {
  // 1) SDK 초기화
  Kakao.init('2e571aff5d4d7d129d6b79700f15b495');
  console.log('Kakao initialized:', Kakao.isInitialized());

  // 2) 탭별 첫 번째 카드 정보 읽어오는 헬퍼
  function getShareData() {
    const activeTabElement = document.querySelector('.tab.active');
    if (!activeTabElement) {
      console.warn('활성화된 탭을 찾을 수 없습니다. 기본 공유 데이터를 반환합니다.');
      return {
        title: '무작정 랜덤여행하기!',
        description: '먼저 [랜덤 여행지 추천] 버튼을 눌러 코스를 받아보세요.',
        imageUrl: 'https://via.placeholder.com/200',
        linkUrl: window.location.href
      };
    }

    const activeTab = activeTabElement.id;

    const section = {
      'tab-course': document.querySelector('#course-list .card'),
      'tab-restaurant': document.querySelector('#restaurant-list .card'),
      'tab-accommodation': document.querySelector('#accommodation-list .card')
    }[activeTab];

    if (!section) {
      console.warn(`현재 활성화된 탭(${activeTab})에 카드를 찾을 수 없습니다. 기본 공유 데이터를 반환합니다.`);
      return {
        title: '무작정 랜덤여행하기!',
        description: '먼저 [랜덤 여행지 추천] 버튼을 눌러 코스를 받아보세요.',
        imageUrl: 'https://via.placeholder.com/200',
        linkUrl: window.location.href
      };
    }

    const titleElement = section.querySelector('h4');
    const descriptionElement = section.querySelector('p.text-xs.text-gray-500');
    const imageElement = section.querySelector('img');

    const title = titleElement ? titleElement.innerText : '무작정 랜덤여행하기!';
    const description = descriptionElement ? descriptionElement.innerText : '추천된 여행지를 확인해 보세요.';
    const imageUrl = imageElement ? imageElement.src : 'https://via.placeholder.com/200';
    const linkUrl = window.location.href + `?tab=${activeTab.replace('tab-', '')}`;

    return { title, description, imageUrl, linkUrl };
  }

  // 3) 전역 공유 버튼 핸들러
  const kakaoSharingBtn = document.getElementById('kakaotalk-sharing-btn');
  if (kakaoSharingBtn) {
    kakaoSharingBtn.addEventListener('click', () => {
      const activeTabEl = document.querySelector('.tab.active');
      const activeTab   = activeTabEl?.id;
      const selectorMap = {
        'tab-course':        '#course-list .card',
        'tab-restaurant':    '#restaurant-list .card',
        'tab-accommodation': '#accommodation-list .card'
      };
      const card = activeTab ? document.querySelector(selectorMap[activeTab]) : null;

      if (!card) {
        console.warn('공유할 카드가 없습니다.');
        return;
      }
      shareItem(card);
    });
  } else {
    console.error('카카오톡 공유 버튼을 찾을 수 없습니다.');
  }
});

// 3) 카드 단위 공유 처리 함수 - 수정된 버전
window.shareItem = async function (raw) {
  const isEl = raw instanceof HTMLElement;

  /* 1️⃣ ID 선택 로직  (부모 → 디테일 순) */
  const parentId = isEl
      ? raw.dataset.parentId
      : raw.parentId ?? raw.courseId;        // 모달 객체에는 parentId 넣어뒀음

  const fallbackId = isEl
      ? raw.dataset.id
      : raw.id ?? raw.contentId ?? raw.courseId;

  const shareId = parentId || fallbackId;      // ★ 서버로 보낼 최종 ID
  const type    = (isEl ? raw.dataset.type : raw.dtoType || raw.type || 'course').toUpperCase();

  /* ── 2️⃣ 통계 전송 ───────────────── */
  if (shareId) {
    await fetch('/share', {
      method : 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body   : `type=${encodeURIComponent(type)}&id=${encodeURIComponent(shareId)}`
    }).catch(console.warn);
  } else {
    console.warn('shareItem: 전송할 ID가 없습니다.');
  }

  /* ── 3️⃣ 카카오톡 공유 (기존 그대로) ─── */
  const title = isEl ? raw.querySelector('h4')?.innerText : raw.name;
  const image = isEl ? raw.querySelector('img')?.src      : raw.imgPath;
  const link  = `${location.origin}${location.pathname}?tab=${type.toLowerCase()}`;

  Kakao.Share.sendDefault({
    objectType: 'feed',
    content: {
      title: title || '무작정 랜덤여행하기!',
      description: '',
      imageUrl: image || 'https://via.placeholder.com/200',
      link: { mobileWebUrl: link, webUrl: link }
    },
    buttons: [{
      title: '웹에서 보기',
      link : { mobileWebUrl: link, webUrl: link }
    }]
  });
};
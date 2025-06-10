// Kakao JavaScript SDK를 사용하여 카카오톡 공유하기 기능을 구현하는 코드입니다.

document.addEventListener('DOMContentLoaded', () => {
  // 1) SDK 초기화
  // Kakao.init은 한 번만 호출되는 것이 좋으며,
  // HTML <head>에서 이미 Kakao JS SDK를 로드했으므로 여기서는 Kakao 객체가 정의되어 있을 겁니다.
  // 이 파일에서 다시 초기화하는 것은 중복일 수 있으니, 전체 프로젝트에서 한 곳에서만 초기화되는지 확인하세요.
  // 일반적으로는 이 코드를 <head>의 Kakao SDK 로드 스크립트 바로 다음에 두는 것이 좋습니다.
  Kakao.init('2e571aff5d4d7d129d6b79700f15b495');
  console.log('Kakao initialized:', Kakao.isInitialized());

  // 2) 탭별 첫 번째 카드 정보 읽어오는 헬퍼
  function getShareData() {
    const activeTabElement = document.querySelector('.tab.active');
    // activeTabElement가 null일 수 있으므로 null 체크를 먼저 합니다.
    if (!activeTabElement) {
      console.warn('활성화된 탭을 찾을 수 없습니다. 기본 공유 데이터를 반환합니다.');
      return {
        title: '무작정 랜덤여행하기!',
        description: '먼저 [랜덤 여행지 추천] 버튼을 눌러 코스를 받아보세요.',
        imageUrl: 'https://via.placeholder.com/200', // 기본 이미지 URL 설정
        linkUrl: window.location.href
      };
    }

    const activeTab = activeTabElement.id;

    const section = {
      'tab-course': document.querySelector('#course-list .card'),
      'tab-restaurant': document.querySelector('#restaurant-list .card'),
      'tab-accommodation': document.querySelector('#accommodation-list .card')
    }[activeTab];

    // section이 null일 수 있으므로 null 체크를 추가합니다.
    if (!section) {
      console.warn(`현재 활성화된 탭(${activeTab})에 카드를 찾을 수 없습니다. 기본 공유 데이터를 반환합니다.`);
      return {
        title: '무작정 랜덤여행하기!',
        description: '먼저 [랜덤 여행지 추천] 버튼을 눌러 코스를 받아보세요.',
        imageUrl: 'https://via.placeholder.com/200', // 기본 이미지 URL 설정
        linkUrl: window.location.href
      };
    }

    const titleElement = section.querySelector('h4');
    // 'descriptionElement'는 <a> 태그를 참조하는데, 이 <a> 태그의 텍스트가 설명이 아닌 웹사이트 링크일 수 있습니다.
    // 만약 실제 설명 텍스트가 다른 곳에 있다면 해당 요소를 참조해야 합니다.
    // 여기서는 예시로 text-xs text-gray-500 클래스를 가진 p 태그가 설명이라고 가정하고 추가합니다.
    const descriptionElement = section.querySelector('p.text-xs.text-gray-500'); // 주소나 설명을 포함하는 p 태그
    const imageElement = section.querySelector('img');

    const title = titleElement ? titleElement.innerText : '무작정 랜덤여행하기!';
    const description = descriptionElement ? descriptionElement.innerText : '추천된 여행지를 확인해 보세요.';
    const imageUrl = imageElement ? imageElement.src : 'https://via.placeholder.com/200';
    const linkUrl = window.location.href + `?tab=${activeTab.replace('tab-', '')}`;

    return { title, description, imageUrl, linkUrl };
  }

  // 3) 버튼 클릭 시 동적 공유 생성
  const kakaoSharingBtn = document.getElementById('kakaotalk-sharing-btn');
  // 버튼 요소가 존재할 때만 이벤트 리스너를 등록합니다.
  if (kakaoSharingBtn) {
    kakaoSharingBtn.addEventListener('click', () => {
      // Kakao SDK가 초기화되었는지 다시 확인
      if (!Kakao.isInitialized()) {
        console.error('카카오 SDK가 초기화되지 않았습니다. 공유를 실행할 수 없습니다.');
        alert('카카오 공유 기능을 사용할 수 없습니다. 페이지를 새로고침해주세요.');
        return;
      }

      const data = getShareData(); // 공유할 데이터 가져오기

      // 1. 공유 저장 API 호출
      const typeMap = {
        'tab-course': 'course',
        'tab-restaurant': 'restaurant',
        'tab-accommodation': 'accommodation'
      };
      const activeTabElement = document.querySelector('.tab.active');
      const activeTab = activeTabElement ? activeTabElement.id : null;
      const type = typeMap[activeTab];

      const card = document.querySelector(`#${activeTab} .card`);
      // dataset.id를 사용하려면 HTML <div class="card" data-id="some_id_value"> 형식이어야 합니다.
      const id = card ? card.dataset.id : null;

      // type과 id가 유효할 때만 공유 저장 API 호출
      if (type && id) {
        fetch('/share', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded' // form 전송
          },
          body: `type=${type}&id=${id}`
        }).then(res => {
          if (!res.ok) {
            throw new Error(`HTTP error! status: ${res.status}`);
          }
          return res.text();
        })
            .then(console.log)
            .catch(error => console.error("공유 저장 API 호출 실패:", error));
      } else {
        console.warn("공유 저장 API 호출을 위한 타입 또는 ID를 찾을 수 없습니다.");
      }

      // 2. 카카오톡 공유 실행
      Kakao.Share.sendDefault({
        objectType: 'feed',
        content: {
          title:       data.title,
          description: data.description,
          imageUrl:    data.imageUrl,
          link: {
            mobileWebUrl: data.linkUrl,
            webUrl:       data.linkUrl
          }
        },
        buttons: [
          {
            title: '웹에서 보기',
            link: {
              mobileWebUrl: data.linkUrl,
              webUrl:       data.linkUrl
            }
          }
        ]
      });
    });
  } else {
    console.error("카카오톡 공유 버튼(#kakaotalk-sharing-btn)을 찾을 수 없습니다.");
  }
}); // DOMContentLoaded 닫는 부분
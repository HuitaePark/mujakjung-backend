// Kakao JavaScript SDK를 사용하여 카카오톡 공유하기 기능을 구현하는 코드입니다.
// 1) SDK 초기화
Kakao.init('2e571aff5d4d7d129d6b79700f15b495');
console.log('Kakao initialized:', Kakao.isInitialized());
// 2) 탭별 첫 번째 카드 정보 읽어오는 헬퍼
function getShareData() {
  // 현재 활성 탭 id (tab-course, tab-restaurant, tab-accommodation)
  const activeTab = document.querySelector('.tab.active').id;

  // 각 콘텐츠 섹션에서 첫 번째 .card 요소
  const section = {
    'tab-course':      document.querySelector('#content-course .card'),
    'tab-restaurant':  document.querySelector('#content-restaurant .card'),
    'tab-accommodation': document.querySelector('#content-accommodation .card')
  }[activeTab];

  if (!section) {
    return {
      title: '무작정 랜덤여행하기!',
      description: '무작정 추천된 여행지를 확인해 보세요.',
      imageUrl: 'https://via.placeholder.com/200', 
      linkUrl: window.location.href
    };
  }

  const title       = section.querySelector('h4').innerText;
  const description = section.querySelector('p').innerText;
  const imageUrl    = section.querySelector('img').src;
  const linkUrl     = window.location.href + `?tab=${activeTab.replace('tab-', '')}`;

  return { title, description, imageUrl, linkUrl };
}

// 3) 버튼 클릭 시 동적 공유 생성
document.getElementById('kakaotalk-sharing-btn').addEventListener('click', () => {
   var data = getShareData();
    
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


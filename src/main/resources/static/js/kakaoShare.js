// Kakao JavaScript SDK를 사용하여 카카오톡 공유하기 기능을 구현하는 코드입니다.
// 1) SDK 초기화
Kakao.init('2e571aff5d4d7d129d6b79700f15b495');
console.log('Kakao initialized:', Kakao.isInitialized());
// 2) 탭별 첫 번째 카드 정보 읽어오는 헬퍼
function getShareData() {
  const activeTab = document.querySelector('.tab.active').id;

  const section = {
    'tab-course': document.querySelector('#course-list .card'),
    'tab-restaurant': document.querySelector('#restaurant-list .card'),
    'tab-accommodation': document.querySelector('#accommodation-list .card')
  }[activeTab];

  if (!section) {
    return {
      title: '무작정 랜덤여행하기!',
      description: '먼저 [랜덤 여행지 추천] 버튼을 눌러 코스를 받아보세요.',
      imageUrl: 'https://via.placeholder.com/200',
      linkUrl: window.location.href
    };
  }

  const titleElement = section.querySelector('h4');
  const descriptionElement = section.querySelector('a');
  const imageElement = section.querySelector('img');

  const title = titleElement ? titleElement.innerText : '무작정 랜덤여행하기!';
  const description = descriptionElement ? descriptionElement.innerText : '추천된 여행지를 확인해 보세요.';
  const imageUrl = imageElement ? imageElement.src : 'https://via.placeholder.com/200';
  const linkUrl = window.location.href + `?tab=${activeTab.replace('tab-', '')}`;

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


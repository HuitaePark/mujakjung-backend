/* ---------------------------------------------------
 * modalAndShare.js (모달/좋아요/공유 담당)
 * - mapMarker.js 보다 **뒤**에서 로드
 * ------------------------------------------------- */
let modalDetailList = [];
let modalCurrentIndex = 0;
let currentDetail = null;
let currentCourseId = null;

// 모든 DOM 조작 및 이벤트 리스너 등록 코드를 DOMContentLoaded 안에 넣습니다.
document.addEventListener('DOMContentLoaded', () => {

    /* ---------- 모달 열기/닫기 ---------- */
    // openModal 함수는 window.openModal로 전역적으로 노출되므로 DOMContentLoaded 바깥에서 정의되어야 합니다.
    // 하지만 이 함수가 호출될 때 내부적으로 DOM 요소를 찾으므로, 요소들이 존재함을 보장해야 합니다.
    // HTML에서 이 함수를 호출할 때는 DOM이 준비된 후에 호출되도록 주의해야 합니다.
    // (예: mapMarker.js에서 openModal을 호출할 때 mapMarker.js도 DOMContentLoaded 안에 있으면 안전)
    window.openModal = function(detail) { // window.openModal로 변경하여 다른 스크립트에서 호출 가능하게 함
        currentDetail   = detail;
        currentCourseId = detail.id;

        const modalImg = document.getElementById('modal-img');
        const modalTitle = document.getElementById('modal-title');
        const modalLike = document.getElementById('modal-like');
        const modalDesc = document.getElementById('modal-desc');
        const detailModal = document.getElementById('detail-modal');

        if (modalImg) modalImg.src = detail.imgPath;
        if (modalTitle) modalTitle.textContent = detail.name;
        if (modalLike) modalLike.textContent = detail.likeCount ?? 0;
        if (modalDesc) modalDesc.textContent = detail.description;

        if (detailModal) detailModal.style.display = 'flex';
        updateLikeButtonState(); // 이 함수는 DOMContentLoaded 안에 정의될 것입니다.
    };

    window.openModalList = function(list) { // window.openModalList로 변경
        if (!list?.length) return;
        modalDetailList   = list;
        modalCurrentIndex = 0;
        window.openModal(list[0]); // window.openModal 호출
        const prevBtn = document.getElementById('modal-prev-btn');
        const nextBtn = document.getElementById('modal-next-btn');
        const pageSpan = document.getElementById('modal-page');
        if (prevBtn) prevBtn.classList.remove('hidden');
        if (nextBtn) nextBtn.classList.remove('hidden');
        if (pageSpan) pageSpan.classList.remove('hidden');
        updateModalPage(); // 이 함수는 DOMContentLoaded 안에 정의될 것입니다.
    };

    function updateModalPage() {
        const page = document.getElementById('modal-page');
        const prevBtn = document.getElementById('modal-prev-btn');
        const nextBtn = document.getElementById('modal-next-btn');

        if (page) page.textContent = `${modalCurrentIndex + 1} / ${modalDetailList.length}`;
        if (prevBtn) prevBtn.disabled = modalCurrentIndex === 0;
        if (nextBtn) nextBtn.disabled = modalCurrentIndex === modalDetailList.length - 1;
    }

    /* ---------- 페이지 네비 ---------- */
    const modalPrevBtn = document.getElementById('modal-prev-btn');
    if (modalPrevBtn) {
        modalPrevBtn.onclick = () => {
            if (modalCurrentIndex > 0) {
                modalCurrentIndex--; window.openModal(modalDetailList[modalCurrentIndex]); updateModalPage();
            }
        };
    }

    const modalNextBtn = document.getElementById('modal-next-btn');
    if (modalNextBtn) {
        modalNextBtn.onclick = () => {
            if (modalCurrentIndex < modalDetailList.length - 1) {
                modalCurrentIndex++; window.openModal(modalDetailList[modalCurrentIndex]); updateModalPage();
            }
        };
    }

    /* ---------- 모달 닫기 ---------- */
    const closeModalBtn = document.getElementById('close-modal');
    if (closeModalBtn) {
        closeModalBtn.onclick = () => {
            const detailModal = document.getElementById('detail-modal');
            if (detailModal) detailModal.style.display = 'none';
        };
    }

    /* ---------- 좋아요 ---------- */
    function updateLikeButtonState() {
        const btn  = document.getElementById('like-btn');
        const icon = btn ? btn.querySelector('i') : null; // null 체크

        if (!btn || !icon) return; // 요소가 없으면 함수 종료

        if (currentDetail?.liked) {
            btn.disabled = true;
            icon.classList.add('text-blue-600');
            icon.classList.remove('text-gray-500'); // 회색 제거 (원래 있었다면)
        } else {
            btn.disabled = false;
            icon.classList.remove('text-blue-600');
            icon.classList.add('text-gray-500'); // 회색 추가 (원래 없었다면)
        }
    }

    const likeBtn = document.getElementById('like-btn');
    if (likeBtn) {
        likeBtn.onclick = async () => {
            if (!currentCourseId || currentDetail?.liked) return; // 이미 좋아요 했으면 종료

            // 좋아요 버튼 비활성화 (중복 클릭 방지)
            likeBtn.disabled = true;
            likeBtn.classList.add('opacity-50', 'cursor-not-allowed');

            try {
                const res = await fetch(`/course/${currentCourseId}/like`, { method: 'POST' });
                if (!res.ok) throw new Error(`HTTP ${res.status}: ${res.statusText}`);
                const data = await res.json();

                currentDetail.liked = true;
                currentDetail.likeCount = data.totalLikes;

                const modalLikeSpan = document.getElementById('modal-like');
                if (modalLikeSpan) modalLikeSpan.textContent = data.totalLikes;

                updateLikeButtonState(); // 좋아요 상태 업데이트
            } catch (e) {
                console.error('좋아요 처리 중 오류:', e);
                alert('좋아요 처리 중 오류가 발생했습니다.');
                // 오류 발생 시 버튼 상태 복구
                likeBtn.disabled = false;
                likeBtn.classList.remove('opacity-50', 'cursor-not-allowed');
            }
        };
    }

    /* ---------- 카카오톡 공유 ---------- */
    // 이 함수는 외부에서 호출될 수 있으므로, window 객체에 할당하여 전역 함수로 만듭니다.
    window.shareItem = function(item) {
        // Kakao SDK가 초기화되었는지 확인
        if (typeof Kakao === 'undefined' || !Kakao.isInitialized()) {
            console.error('카카오 SDK가 초기화되지 않았거나 로드되지 않았습니다.');
            alert('카카오 공유 기능을 사용할 수 없습니다. 페이지를 새로고침해주세요.');
            return;
        }

        const imageUrl = /^https?:\/\//.test(item.imgPath) ? item.imgPath : location.origin + item.imgPath;
        const linkUrl  = `${location.origin}/view/${item.dtoType || 'course'}/${item.id}`;

        // 백엔드에 공유 이벤트 알림 (선택 사항)
        fetch('/share', { method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `type=${item.dtoType || 'course'}&id=${item.id}` })
            .catch(e => console.error("공유 통계 전송 실패:", e)); // 오류 처리 추가

        Kakao.Share.sendDefault({
            objectType: 'feed',
            content: {
                title: item.name,
                description: item.address || item.description || '무작정 추천 여행지 정보를 확인해 보세요.',
                imageUrl, link: { mobileWebUrl: linkUrl, webUrl: linkUrl }
            },
            buttons: [{ title: '웹에서 보기', link: { mobileWebUrl: linkUrl, webUrl: linkUrl } }]
        });
    };
}); // DOMContentLoaded 닫는 부분
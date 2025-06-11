/* ---------------------------------------------------
 * modalAndShare.js (모달/좋아요/공유 담당)
 * - mapMarker.js 보다 **뒤**에서 로드
 * ------------------------------------------------- */

let modalDetailList = [];
let modalCurrentIndex = 0;
let currentDetail = null;
let currentCourseId = null;

document.addEventListener('DOMContentLoaded', () => {

    /* ---------- 모달 열기/닫기 ---------- */
    window.openModal = function(detail) {
        currentDetail   = detail;
        currentCourseId = detail.id;

        // 모달 요소들
        const modalImg    = document.getElementById('modal-img');
        const modalTitle  = document.getElementById('modal-title');
        const modalLike   = document.getElementById('modal-like');
        const modalDesc   = document.getElementById('modal-desc');
        const detailModal = document.getElementById('detail-modal');

        if (modalImg)    modalImg.src         = detail.imgPath;
        if (modalTitle)  modalTitle.textContent = detail.name;
        if (modalLike)   modalLike.textContent  = detail.likeCount ?? 0;
        if (modalDesc)   modalDesc.textContent  = detail.description;
        if (detailModal) detailModal.style.display = 'flex';

        updateLikeButtonState();
    };

    window.openModalList = function(list) {
        if (!Array.isArray(list) || list.length === 0) return;
        modalDetailList   = list;
        modalCurrentIndex = 0;
        window.openModal(list[0]);
        document.getElementById('modal-prev-btn')?.classList.remove('hidden');
        document.getElementById('modal-next-btn')?.classList.remove('hidden');
        document.getElementById('modal-page')?.classList.remove('hidden');
        updateModalPage();
    };

    function updateModalPage() {
        const page    = document.getElementById('modal-page');
        const prevBtn = document.getElementById('modal-prev-btn');
        const nextBtn = document.getElementById('modal-next-btn');

        if (page)    page.textContent = `${modalCurrentIndex + 1} / ${modalDetailList.length}`;
        if (prevBtn) prevBtn.disabled = (modalCurrentIndex === 0);
        if (nextBtn) nextBtn.disabled = (modalCurrentIndex === modalDetailList.length - 1);
    }

    /* ← 페이징 버튼 */
    document.getElementById('modal-prev-btn')?.addEventListener('click', () => {
        if (modalCurrentIndex > 0) {
            modalCurrentIndex--;
            window.openModal(modalDetailList[modalCurrentIndex]);
            updateModalPage();
        }
    });

    document.getElementById('modal-next-btn')?.addEventListener('click', () => {
        if (modalCurrentIndex < modalDetailList.length - 1) {
            modalCurrentIndex++;
            window.openModal(modalDetailList[modalCurrentIndex]);
            updateModalPage();
        }
    });

    /* ← 모달 닫기 */
    document.getElementById('close-modal')?.addEventListener('click', () => {
        document.getElementById('detail-modal').style.display = 'none';
    });

    /* ← 좋아요 버튼 상태 갱신 */
    function updateLikeButtonState() {
        const btn  = document.getElementById('like-btn');
        const icon = btn?.querySelector('i');
        if (!btn || !icon) return;

        if (currentDetail?.liked) {
            btn.disabled = true;
            icon.classList.add('text-blue-600');
            icon.classList.remove('text-gray-500');
        } else {
            btn.disabled = false;
            icon.classList.remove('text-blue-600');
            icon.classList.add('text-gray-500');
        }
    }

    /* ← 좋아요 처리 */
    document.getElementById('like-btn')?.addEventListener('click', async () => {
        if (!currentCourseId || currentDetail?.liked) return;

        const btn = document.getElementById('like-btn');
        btn.disabled = true;
        btn.classList.add('opacity-50', 'cursor-not-allowed');

        try {
            const res = await fetch(`/course/${currentCourseId}/like`, { method: 'POST' });
            if (!res.ok) throw new Error(`HTTP ${res.status}: ${res.statusText}`);
            const data = await res.json();

            currentDetail.liked     = true;
            currentDetail.likeCount = data.totalLikes;
            document.getElementById('modal-like').textContent = data.totalLikes;
            updateLikeButtonState();

        } catch (e) {
            console.error('좋아요 처리 중 오류:', e);
            alert('좋아요 처리 중 오류가 발생했습니다.');
            btn.disabled = false;
            btn.classList.remove('opacity-50', 'cursor-not-allowed');
        }
    });



    /* ← “공유” 버튼에 currentDetail 전달 */
    document.getElementById('share-btn')?.addEventListener('click', () => {
        shareItem(currentDetail);
    });

});

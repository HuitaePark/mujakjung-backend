// 세션 상태를 저장할 변수
let isLoggedIn = false;
let userInfo = null;

// 세션 확인 함수
async function checkSession() {
    try {
        const response = await fetch('/my', {
            method: 'GET',
            credentials: 'include'
        });

        if (response.ok) {
            userInfo = await response.json();
            isLoggedIn = true;
            console.log('로그인된 사용자:', userInfo.username, userInfo.nickname);
            return true;
        } else {
            isLoggedIn = false;
            userInfo = null;
            return false;
        }
    } catch (error) {
        console.error('세션 확인 중 오류 발생:', error);
        isLoggedIn = false;
        userInfo = null;
        return false;
    }
}

// 버튼 상태 업데이트 함수
function updateButtonState() {
    const myPageButton = document.querySelector('a[href="/myPage"]');

    if (myPageButton) {
        if (isLoggedIn) {
            // 로그인된 상태 - 마이페이지 버튼
            myPageButton.textContent = '마이페이지';
            myPageButton.href = '/myPage';
            myPageButton.title = `${userInfo.nickname}님의 마이페이지`;
        } else {
            // 비로그인 상태 - 로그인 버튼
            myPageButton.textContent = '로그인';
            myPageButton.href = '/login';
            myPageButton.title = '로그인이 필요합니다';
        }
    }
}

// 버튼 클릭 처리 함수
function handleButtonClick(event) {
    event.preventDefault();

    if (isLoggedIn) {
        // 로그인된 상태면 마이페이지로 이동
        window.location.href = '/myPage';
    } else {
        // 비로그인 상태면 로그인 페이지로 이동
        window.location.href = '/login-page';
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', async function() {
    // 세션 확인
    await checkSession();

    // 버튼 상태 업데이트
    updateButtonState();

    // 버튼에 이벤트 리스너 추가
    const myPageButton = document.querySelector('a[href="/myPage"], a[href="/login"]');
    if (myPageButton) {
        myPageButton.addEventListener('click', handleButtonClick);
    }
});

// 세션 상태를 수동으로 새로고침하는 함수 (필요시 사용)
async function refreshSessionStatus() {
    await checkSession();
    updateButtonState();
}
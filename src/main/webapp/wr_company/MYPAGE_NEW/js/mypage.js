$(document).ready(function() {
	// 초기 페이지 로딩 시 실행할 코드

	// 각 메뉴에 클릭 이벤트 리스너 추가
	$('.menu-link').click(function(event) {
		event.preventDefault();
		let pageId = $(this).data('page-id');

		// 기존에 선택된 메뉴에서 'selected' 클래스 제거 (선택 스타일을 추가하려면 필요)
		$('.menu-link').removeClass('selected');

		// 현재 눌린 메뉴에 'selected' 클래스 추가
		$(this).addClass('selected');
		$.ajax({
			url: 'SessionCheck', // 서버 측 코드가 처리하는 URL
			method: 'GET',
			success: function(response) {
				if (response == '0') {
					// 세션이 만료된 경우
					alert('ログインが切れました。ログインページに移動します。');
					location.href = 'LoginPageC';
				} else {


					showJSP(pageId);
				}
			}
		});
	});
});


function getPagePath(pageId) {
	switch (pageId) {
		case "register":
			return "wr_company/MYPAGE_NEW/register.jsp";
		case "delivery":
			return "wr_company/MYPAGE_NEW/delivery.jsp";
		case "stock":
			return "wr_company/MYPAGE_NEW/stock.jsp";
		case "mypage":
			return "lgh_account/farmpage.jsp";
		default:
			console.error('JSP 페이지 로딩 중 에러: 페이지 ID가 잘못되었습니다.');
			return "";
	}
}

function showJSP(pageId) {
	var jspContainer = $('.jsp_view');

	if (jspContainer.length > 0) {
		// 이전에 로드된 JSP 파일을 삭제
		jspContainer.empty();

		let pagePath = getPagePath(pageId);

		$.ajax({
			url: pagePath,
			method: 'GET',
			success: function(data) {
				// 새로운 JSP 파일을 로드
				jspContainer.html(data);
			},
			error: function(xhr, status, error) {
				console.error('JSP 페이지 로딩 중 에러:', error);
			}
		});
	}
}
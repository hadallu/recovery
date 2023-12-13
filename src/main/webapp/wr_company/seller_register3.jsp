<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>상품 및 배송 관리</title>
<link rel="stylesheet" href="css/seller_register3.css">
<link rel="stylesheet" href="css/header.css">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="tabs">
        <div class="tab" onclick="showContent('product-registration')">상품 등록</div>
        <div class="tab" onclick="showContent('product-management')">상품 관리</div>
        <div class="tab" onclick="showContent('shipping-management')">배송 관리</div>
    </div>

    <div class="content" id="product-registration">
        <h2>상품 등록</h2>
            <form action="../sb_mypage/jsp/AddItemC" method="post" enctype="multipart/form-data">
	<div>
		<div>
			<div>상품명</div>
			<div><input name="name"></div>
		</div>
		<div>
			<div>상품 설명</div>
			<div><input name="story"></div>
		</div>
		<div>
			<div>상품 종류</div>
			<div>
				<label>못난이<input type="radio" value="1" name="type"></label>
				<label>일반<input type="radio" value="2" name="type"></label>
			</div>
		</div>
		<div>
			<div>유통기한</div>
			<div><input name="enddate" type="date"></div>
		</div>
		<div>
			<div>상품 재고</div>
			<div><input name="stock"></div>
		</div>
		<div>
			<div>상품 가격</div>
			<div><input name="price"></div>
		</div>
		<div>
			<div>사진 등록</div>
			<div><input type="file" name="img"></div>
			<div><input type="file" name="img2"></div>
			<div><input type="file" name="img3"></div>
			<div><input type="file" name="img4"></div>
		</div>
	</div>

            <!-- 추가적인 필드들을 필요에 따라 추가하세요 -->
            <button type="submit">등록</button>
        </form>
    </div>

    <div class="content" id="product-management" style="display: none;">
        <h2>상품 관리</h2>
        <!-- 상품 관리 내용을 여기에 추가하세요 -->
    </div>

    <div class="content" id="shipping-management" style="display: none;">
        <h2>배송 관리</h2>
        <!-- 배송 관리 내용을 여기에 추가하세요 -->
    </div>

    <script>
        function showContent(contentId) {
            // Hide all content divs
            document.querySelectorAll('.content').forEach(function(content) {
                content.style.display = 'none';
            });

            // Deactivate all tabs
            document.querySelectorAll('.tab').forEach(function(tab) {
                tab.classList.remove('active');
            });

            // Show the selected content
            document.getElementById(contentId).style.display = 'block';

            // Activate the corresponding tab
            document.querySelector(`.tab[onclick="showContent('${contentId}')"]`).classList.add('active');
        }

        function submitProductForm() {
            // 상품 등록 폼 제출 시 동작하는 함수
            // 필요에 따라 추가적인 로직을 여기에 작성하세요
            alert('상품이 등록되었습니다!');
        }
    </script>


</body>
</html>
package com.recovery.account;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.recovery.account.User;
import com.recovery.main.DBManager;

public class SellerAccountDAO {

	public static boolean login(HttpServletRequest request) {
		String newid = (String) request.getAttribute("newid");
		String newpw = (String) request.getAttribute("newpw");
		
		String sellerID = request.getParameter("sellerID");
		String sellerPW = request.getParameter("sellerPW");
		
		if (newid != null) {
			sellerID = newid;
			sellerPW = newpw;
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from seller where s_id = ?";
		String dbSellerPW = "";
		boolean Check = false;
		try {
			// 연결 시작
			con = DBManager.connect();
			System.out.println("연결 성공");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sellerID);
			
			rs = pstmt.executeQuery();
			
			// id, pw 맞는지 확인
			if (rs.next()) {
				dbSellerPW = rs.getString("s_pw");
				if (sellerPW.equals(dbSellerPW)) {
					System.out.println("판매자 로그인 성공");
					// Seller bean 에 판매자 정보 입력
					Seller seller = new Seller();
					seller.setS_id(sellerID);
					seller.setS_pw(sellerPW);
					seller.setS_kanji_ln(rs.getString("s_kanji_ln"));
					seller.setS_kanji_fn(rs.getString("s_kanji_fn"));
					seller.setS_kata_ln(rs.getString("s_kata_ln"));
					seller.setS_kata_fn(rs.getString("s_kata_fn"));
					seller.setS_tel(rs.getString("s_tel"));
					seller.setS_Fphoto(rs.getString("s_f_photo"));
					seller.setS_Faddr(rs.getString("s_f_addr"));
					seller.setS_Fname(rs.getString("s_f_name"));
					seller.setS_Fstory(rs.getString("s_f_story"));
					
					// 세션 생성
					HttpSession sellerHS = request.getSession();
					sellerHS.setAttribute("sellerAccount", seller);
					Check = true;
				} else {
					System.out.println("비밀번호 오류");
				}
			} else {
				System.out.println("존재하지 않는 판매자");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("sellerLogin 서버 오류");
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		return Check;
	}

	public static void regSeller(HttpServletRequest request) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "insert into seller values(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			request.setCharacterEncoding("utf-8");
			String path = request.getServletContext().getRealPath("lgh_account/farmImg");
			MultipartRequest mr = new MultipartRequest(request, path, 30*1024*1024, "utf-8", new DefaultFileRenamePolicy()
					);
			String id = mr.getParameter("sellerID");
			String pw = mr.getParameter("sellerPW");
			String kanjiLast = mr.getParameter("sellerKanji_ln");
			String kanjiName = mr.getParameter("sellerKanji_fn");
			String kataLast = mr.getParameter("sellerKata_ln");
			String kataName = mr.getParameter("sellerKata_fn");
			String tel1 = mr.getParameter("sellerTel1");
			String tel2 = mr.getParameter("sellerTel2");
			String tel3 = mr.getParameter("sellerTel3");
			String tel = tel1+"-"+tel2+"-"+tel3;
			String farmImg = mr.getFilesystemName("farmImg");
			String farmName = mr.getParameter("farmName");
			String farmStory = mr.getParameter("farmStory");
			String addrN = mr.getParameter("sellerAddrN");
			String addrP = mr.getParameter("sellerAddrP");
			String addrC = mr.getParameter("sellerAddrC");
			String addrD = mr.getParameter("sellerAddrD");
			String addr = addrN+"!"+addrP+"!"+addrC+"!"+addrD;
			System.out.println(tel);
			System.out.println(addr);
			farmStory = farmStory.isEmpty() ? "説明がありません" : farmStory.replaceAll("\r\n", "<br>");
			System.out.println(farmStory);
			if (addrD.isEmpty()) {
				addrD = "...";
			}
			
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, kanjiLast);
			pstmt.setString(4, kanjiName);
			pstmt.setString(5, kataLast);
			pstmt.setString(6, kataName);
			pstmt.setString(7, tel);
			pstmt.setString(8, farmImg);
			pstmt.setString(9, addr);
			pstmt.setString(10, farmName);
			pstmt.setString(11, farmStory);
			
			if (pstmt.executeUpdate() == 1) {
				System.out.println("판매자 등록 성공");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
				System.out.println("판매자 등록 실패");
		} finally {
			DBManager.close(con, pstmt, null);
		}
		
	}

	public static void updateSeller(HttpServletRequest request) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Seller seller = (Seller) request.getSession().getAttribute("sellerAccount");
		
		String sql = "UPDATE seller "
				+ "SET s_kanji_ln = ?, s_kanji_fn = ?, s_kata_ln = ?, s_kata_fn = ?, "
				+ "s_tel = ?, s_f_photo = ?, s_f_addr = ?, s_f_name = ?, s_f_story = ? "
				+ "WHERE s_id = ?";
		try {
			request.setCharacterEncoding("utf-8");
			String path = request.getServletContext().getRealPath("lgh_account/farmImg");
			MultipartRequest mr = new MultipartRequest(request, path, 30*1024*1024, "utf-8", new DefaultFileRenamePolicy()
					);
			String kanjiLast = mr.getParameter("sellerKanji_ln");
			String kanjiName = mr.getParameter("sellerKanji_fn");
			String kataLast = mr.getParameter("sellerKata_ln");
			String kataName = mr.getParameter("sellerKata_fn");
			
			String tel1 = mr.getParameter("sellerTel1");
			String tel2 = mr.getParameter("sellerTel2");
			String tel3 = mr.getParameter("sellerTel3");
			String tel = tel1+"-"+tel2+"-"+tel3;
			
			String farmName = mr.getParameter("farmName");

			String farmStory = mr.getParameter("farmStory");
			farmStory = farmStory.isEmpty() ? "説明がありません" : farmStory.replaceAll("\r\n", "<br>");

			String addrN = mr.getParameter("sellerAddrN");
			String addrP = mr.getParameter("sellerAddrP");
			String addrC = mr.getParameter("sellerAddrC");
			String addrD = mr.getParameter("sellerAddrD");
			String addr = addrN+"!"+addrP+"!"+addrC+"!"+addrD;
			System.out.println(tel);
			System.out.println(addr);
			System.out.println(farmStory);
			if (addrD.isEmpty()) {
				addrD = "...";
			}
			
			// 이미지 새로 들어왔는지 안들어왔는지 체크 
			String newFarmImg = mr.getFilesystemName("newFarmImg");
			String img = newFarmImg != null ? newFarmImg : seller.getS_Fphoto();
			
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, kanjiLast);
			pstmt.setString(2, kanjiName);
			pstmt.setString(3, kataLast);
			pstmt.setString(4, kataName);
			pstmt.setString(5, tel);
			pstmt.setString(6, img);
			pstmt.setString(7, addr);
			pstmt.setString(8, farmName);
			pstmt.setString(9, farmStory);
			pstmt.setString(10, seller.getS_id());
			
			if (pstmt.executeUpdate() == 1) {
				System.out.println("수정 성공");
				
				// 기존 이미지 삭제 
				if (newFarmImg != null) {
					File f = new File(path+"/"+seller.getS_Fphoto());
					f.delete();
				}
				
				// 세션 업데이트
				request.setAttribute("newid", seller.getS_id());
				request.setAttribute("newpw", seller.getS_pw());
				login(request);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("수정 실패");
		} finally {
			DBManager.close(con, pstmt, null);
		}
	};
}

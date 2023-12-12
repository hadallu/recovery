package com.recovery.account;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.recovery.main.DBManager;

public class AccountDAO {

	// �α���
	public static void login(HttpServletRequest request) {
		String userID = request.getParameter("userID");
		String userPW = request.getParameter("userPW");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from users where u_id = ?";
		String dbUserPW = "";
		try {
			// ���� ����
			con = DBManager.connect();
			System.out.println("���� ����");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userID);
			
			rs = pstmt.executeQuery();
			
			// id, pw �´��� Ȯ��
			if (rs.next()) {
				dbUserPW = rs.getString("u_pw");
				if (userPW.equals(dbUserPW)) {
					System.out.println("�α��� ����");
					// User bean �� ���� ���� �Է�
					User user = new User();
					user.setU_id(userID);
					user.setU_pw(userPW);
					user.setU_nicname(rs.getString("u_nicname"));
					user.setU_tel(rs.getString("u_tel"));
					user.setU_email(rs.getString("u_email"));
					user.setU_kanji_ln(rs.getString("u_kanji_ln"));
					user.setU_kanji_fn(rs.getString("u_kanji_fn"));
					user.setU_kata_ln(rs.getString("u_kata_ln"));
					user.setU_kata_fn(rs.getString("u_kata_fn"));
					user.setU_img(rs.getString("u_img"));
					user.setU_signout(rs.getString("u_signout"));
					
					// ���� ����
					HttpSession userHS = request.getSession();
					userHS.setAttribute("userAccount", user);
					userHS.setMaxInactiveInterval(10);
				} else {
					System.out.println("��й�ȣ ����");
				}
			} else {
				System.out.println("�������� �ʴ� ȸ��");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userLogin ���� ����");
		} finally {
			DBManager.close(con, pstmt, rs);
		}
	}
	
	// login �ߴ��� ���ߴ��� Ȯ��(seller ����)
	public static int loginCheck(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("userAccount");
		Seller seller = (Seller) request.getSession().getAttribute("sellerAccount");
		if (user != null) {
			request.setAttribute("loginChange", "lgh_account/loginButton/loginOK.jsp");
			return 1;
		} 
		else if (seller != null) {
			request.setAttribute("loginChange", "lgh_account/loginButton/sellerLoginOK.jsp");
			return 2;
		}
		else {
			request.setAttribute("loginChange", "lgh_account/loginButton/loginButton.jsp");
			return 0;
		}
	}
	
	public static void logout(HttpServletRequest request) {
		
		User user = (User) request.getSession().getAttribute("userAccount");
		HttpSession hs = request.getSession();
		if (user != null) {
			hs.removeAttribute("userAccount");
		}else {			
			hs.removeAttribute("sellerAccount");
		}
	};
	
	
	//id�� �����ϴ��� Ȯ��(user,seller �Ѵ� ����)
	public static int idCheck(HttpServletRequest request) {
		String table = request.getParameter("table");
		String inputID = request.getParameter("ID");
		String dbID = "s_id";
		System.out.println(table);
		if (table.equals("users")) {
			dbID = "u_id";
		}
		System.out.println("inputID: " + inputID);
		System.out.println(dbID);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM " + table + " WHERE " + dbID + " = ?";
		int idCheck = 0;
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, inputID);
			rs = pstmt.executeQuery();
			if (rs.next() || inputID.equals("")) {
				// ���� ���� �ϰų� ��ĭ�̸� �����ϴ� ȸ��
				idCheck = 0;
			} else {
				// ���� ���ų� ��ĭ�� �ƴϸ� ��� ����
				idCheck = 1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		return idCheck;
	}
	
	//db�� users / address�� ���� ���
	public static void regUser(HttpServletRequest request) {
		Connection con = null;
		PreparedStatement pstmt = null;
		// ����
		String sql = "insert into users values(?,?,?,?,?,?,?,?,?,?, 'N')";
		// �����
		String sql2 = "insert into address values(address_seq.nextval,?,?,?,?,?,?,?)";
		try {
			request.setCharacterEncoding("utf-8");
			String path = request.getServletContext().getRealPath("lgh_account/userImg");
			MultipartRequest mr = new MultipartRequest(request, path, 30*1024*1024, "utf-8", new DefaultFileRenamePolicy()
					);
			String id = mr.getParameter("userID");
			String pw = mr.getParameter("userPW");
			String kanjiLast = mr.getParameter("userKanji_ln");
			String kanjiName = mr.getParameter("userKanji_fn");
			String kataLast = mr.getParameter("userKata_ln");
			String kataName = mr.getParameter("userKata_fn");
			String nicName = mr.getParameter("userNicname");
			String tel1 = mr.getParameter("userTel1");
			String tel2 = mr.getParameter("userTel2");
			String tel3 = mr.getParameter("userTel3");
			String telAll = tel1+"-"+tel2+"-"+tel3;
			String email = mr.getParameter("userEmail");
			String img = mr.getFilesystemName("userImg");
			
//			System.out.println(id);
//			System.out.println(pw);
//			System.out.println(kanjiLast);
//			System.out.println(kanjiName);
//			System.out.println(kataLast);
//			System.out.println(kataName);
//			System.out.println(nicName);
//			System.out.println(telAll);
//			System.out.println(email);
//			System.out.println(img);
			
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, nicName);
			pstmt.setString(4, telAll);
			pstmt.setString(5, email);
			pstmt.setString(6, kanjiLast);
			pstmt.setString(7, kanjiName);
			pstmt.setString(8, kataLast);
			pstmt.setString(9, kataName);
			pstmt.setString(10, img);
			
			
			
			if (pstmt.executeUpdate() == 1) {
				System.out.println("���� ��ϼ���");
			}
			pstmt.close();
			String addrNum = mr.getParameter("userAddrN");
			String addrP = mr.getParameter("userAddrP");
			String addrCity = mr.getParameter("userAddrC");
			String addrDetail = mr.getParameter("userAddrD");
			
			System.out.println(addrNum);
			System.out.println(addrP);
			System.out.println(addrCity);
			System.out.println(addrDetail);
			
			
			pstmt = con.prepareStatement(sql2);
			
			pstmt.setString(1, addrNum);
			pstmt.setString(2, addrP+"!"+addrCity);
			pstmt.setString(3, addrDetail);
			pstmt.setString(4, "name");
			pstmt.setString(5, "tel");
			pstmt.setString(6, "null");
			pstmt.setString(7, id);
			
			if (pstmt.executeUpdate() == 1) {
				System.out.println("����� ��� ����");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
				System.out.println("��� ����");
		} finally {
			DBManager.close(con, pstmt, null);
		}
	}
	


}

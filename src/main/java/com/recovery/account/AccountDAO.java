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
		// ���� �α��� �ϴ� ����Դϴ�.
		// ��/��
		String userID = request.getParameter("userID");
		String userPW = request.getParameter("userPW");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from users where u_id = ?";
		String dbUserPW = "";
		try {
			// ���� �õ�
			con = DBManager.connect();
			System.out.println("���Ἲ��");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userID);
			
			rs = pstmt.executeQuery();
			
			// id, pw Ȯ������
			if (rs.next()) {
				dbUserPW = rs.getString("u_pw");
				if (userPW.equals(dbUserPW)) {
					System.out.println("�α��� ����");
					// ���� �α��θ� ����ϱ� ���ؼ� �����ڿ� ���
					User user = new User();
					user.setU_id(userID);
					user.setU_pw(userPW);
					user.setU_nicname(rs.getString("u_nicname"));
					user.setU_tel(rs.getString("u_tel"));
					user.setU_email(rs.getString("u_email"));
					user.setU_addrno(rs.getString("u_addrno"));
					user.setU_kanji_ln(rs.getString("u_kanji_ln"));
					user.setU_kanji_fn(rs.getString("u_kanji_fn"));
					user.setU_kata_ln(rs.getString("u_kata_ln"));
					user.setU_kata_fn(rs.getString("u_kata_fn"));
					user.setU_img(rs.getString("u_img"));
					
					// ���� ����
					HttpSession userHS = request.getSession();
					userHS.setAttribute("userAccount", user);
					userHS.setMaxInactiveInterval(10);
				} else {
					System.out.println("�������");
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
	
	// login �ߴ��� ���ߴ��� Ȯ��
	public static boolean loginCheck(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("userAccount");
		if (user == null) {
			request.setAttribute("loginChange", "lgh_account/loginButton/loginButton.jsp");
			return false;
		} else {
			request.setAttribute("loginChange", "lgh_account/loginButton/loginOK.jsp");
			return true;
		}
	}

	//id �ߺ� Ȯ�� ���
	public static int idCheck(HttpServletRequest request) {
		System.out.println("aa");
		String userID = request.getParameter("userID");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from users where u_id = ?";
		int idCheck = 0;
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next() || userID.equals("")) {
				// ���� �ְų�, �ƿ� �Է� ���� �ʾ��� ��� ���� �Ұ����ϰ�
				idCheck = 0;
			} else {
				// ���� �������� ���� == id �ߺ��� �ƴ��̹Ƿ� ���� ����
				idCheck = 1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		return idCheck;
	}
	
	//db�� �������� ���(ȸ������)
	public static void regUser(HttpServletRequest request) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "insert into users values(?,?,?,?,?,?,?,?,?,?,?);";
		try {
			request.setCharacterEncoding("utf-8");
			String path = request.getServletContext().getRealPath("lgh_account/userImg");
			MultipartRequest mr = new MultipartRequest(request, path, 30*1024*1024, "utf-8", new DefaultFileRenamePolicy()
					);
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
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
			
			System.out.println(id);
			System.out.println(pw);
			System.out.println(kanjiLast);
			System.out.println(kanjiName);
			System.out.println(kataLast);
			System.out.println(kataName);
			System.out.println(nicName);
			System.out.println(telAll);
			System.out.println(email);
			System.out.println(img);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, null);
		}
	}
	
//	public static void regUserAddr(HttpServletRequest request) {
//		Connection con = null;
//		pre
//	}

}

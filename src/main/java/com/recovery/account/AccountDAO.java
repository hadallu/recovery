package com.recovery.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.recovery.main.DBManager;

public class AccountDAO {

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
					user.setU_name(rs.getString("u_name"));
					user.setU_tel(rs.getString("u_tel"));
					user.setU_email(rs.getString("u_email"));
					user.setU_addrno(rs.getString("u_addrno"));
					user.setU_signout(rs.getString("u_signout"));
					
					// ���� ����
					HttpSession userHS = request.getSession();
					userHS.setAttribute("userAccount", user);
					userHS.setMaxInactiveInterval(30);
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

	public static boolean loginCheck(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("userAccount");
		if (user == null) {
			request.setAttribute("loginChange", "../lgh_account/loginButton/loginButton.jsp");
			return false;
		} else {
			request.setAttribute("loginChange", "../lgh_account/loginButton/loginOK.jsp");
			return true;
		}
	}

}

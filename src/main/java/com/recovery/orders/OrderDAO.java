package com.recovery.orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.recovery.account.User;
import com.recovery.account.UserAddr;
import com.recovery.main.DBManager;

public class OrderDAO {

	// 배송 조회
	public static UserAddr userAddr(HttpServletRequest request) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = (User) request.getSession().getAttribute("userAccount");
		System.out.println(user.getU_id());
		String sql = "SELECT a_no, a_postcode, a_addr, a_addrdetail, a_name, a_tel, a_req" + " FROM address, users"
				+ " where users.u_id = address.u_id" + " and users.u_id = ?";

		UserAddr addrs = null;
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getU_id());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				System.out.println("배송조회 성공");
				addrs = new UserAddr();
				addrs.setA_no(rs.getInt("a_no"));
				addrs.setA_postcode(rs.getString("a_postcode"));

				String addr = rs.getString("a_addr");
				addr = addr.replaceAll("!", " ");

				addrs.setA_addr(addr);
				addrs.setA_addrDetail(rs.getString("a_addrdetail"));
				addrs.setA_name(rs.getString("a_name"));
				addrs.setA_tel(rs.getString("a_tel"));
				addrs.setA_req(rs.getString("a_req"));
				request.setAttribute("addr", addrs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("배송 조회 실패");
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		return addrs;

	}

	// order에 db 등록
	public static void regOrders(HttpServletRequest request) {
		Connection con = null;
		PreparedStatement pstmt = null;
		User user = (User) request.getSession().getAttribute("userAccount");
		UserAddr addrs = userAddr(request);

		if (addrs != null) {
			System.out.println(addrs.getA_name());
			System.out.println(addrs.getA_addr());
			System.out.println(addrs.getA_addrDetail());
			System.out.println(addrs.getA_postcode());
			System.out.println(addrs.getA_tel());
			System.out.println(addrs.getA_req());

		}

		String i_noArray = request.getParameter("i_no");
		String quantityArray = request.getParameter("quantity");
		String subtotalArray = request.getParameter("subtotal");
		System.out.println(i_noArray);
		System.out.println(quantityArray);
		System.out.println(subtotalArray);
		
		String[] i_no = i_noArray.split(",");
		String[] quantity = quantityArray.split(",");
		String[] subtotal = subtotalArray.split(",");
		
//		for (String string : i_no) {
//			System.out.println(string);
//		}

		UUID uuid = UUID.randomUUID();
		LocalDate currentDate = LocalDate.now();

		// 년, 월, 일 추출
		int year = currentDate.getYear();
		int month = currentDate.getMonthValue();
		int dayOfMonth = currentDate.getDayOfMonth();
		String dayInfo = "" + year + month + dayOfMonth;
		System.out.println(dayInfo);
		System.out.println(uuid.toString().split("-")[0]+dayInfo);
		String orderNum = uuid.toString().split("-")[0]+dayInfo;

		String sql = "insert into orders values(orders_seq.nextval,?,?,?,?,?,?,?,?,?,?, DEFAULT, sysdate, DEFAULT)";
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getU_id());
			pstmt.setString(2, orderNum);
			
			if (addrs != null) {
			pstmt.setString(4, addrs.getA_name());
			pstmt.setString(5, addrs.getA_postcode());
			pstmt.setString(6, addrs.getA_addr() + " " +addrs.getA_addrDetail());
			pstmt.setString(7, addrs.getA_tel());
			pstmt.setString(8, addrs.getA_req());
			}

			for (int i = 0; i < i_no.length; i++) {
				pstmt.setString(3, i_no[i]);
				pstmt.setString(9, quantity[i]);
				pstmt.setString(10, subtotal[i]);

				if (pstmt.executeUpdate() == 1) {
					System.out.println("주문 등록 성공" + i);
				}
			}
			deleteOrdersCart(request);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("주문 등록 실패");
		} finally {
			DBManager.close(con, pstmt, null);
		}
	}

	// order 등록이 되면 cart 삭제
	public static void deleteOrdersCart(HttpServletRequest request) {
		Connection con = null;
		PreparedStatement pstmt = null;
		User user = (User) request.getSession().getAttribute("userAccount");
		String[] i_no = request.getParameter("i_no").split(",");
		
		

		for (String i : i_no) {
			System.out.println("i_no: " + i);
		}

		String sql = "delete cart where u_id = ? and i_no = ?";
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getU_id());
			for (int i = 0; i < i_no.length; i++) {

				pstmt.setString(2, i_no[i]);

				if (pstmt.executeUpdate() == 1) {
					System.out.println("장바구니 삭제 성공 " + i);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("장바구니 삭제 실패");
		} finally {
			DBManager.close(con, pstmt, null);
		}
	}

}

package com.recovery.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;

import com.google.gson.Gson;
import com.recovery.account.User;
import com.recovery.main.DBManager;

public class ReviewDAO {

	// 상품 상페 페이지 후기 전체 보기
	public static void getItemReview(HttpServletRequest request, HttpServletResponse res) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String paramNo = request.getParameter("no");
		System.out.println(paramNo);
		String sql = "select * from review where i_no = ? order by r_grade desc";
		
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, paramNo);
			rs = pstmt.executeQuery();
			
			ReviewDTO review = null;
			ArrayList<ReviewDTO> reviews = new ArrayList<ReviewDTO>();
			while (rs.next()) {
				review = new ReviewDTO();
				review.setR_no(rs.getInt("r_no"));
				review.setR_estimation(rs.getString("r_estimation"));
				review.setR_date(rs.getDate("r_date"));
				review.setR_grade(rs.getDouble("r_grade"));
				review.setR_nickname(rs.getString("r_nickname"));
				review.setR_productname(rs.getString("r_productname"));
				review.setU_id(rs.getString("u_id"));
				review.setI_no(rs.getInt("i_no"));
				reviews.add(review);
			}
			request.setAttribute("reviews", reviews);

			Gson g = new Gson();
			String jsonData = g.toJson(reviews);
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			res.getWriter().write(jsonData);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		
	}
	
	// 상품 상세 페이지 후기 평균 점수 조회
	public static void getAvgReview(HttpServletRequest request) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String paramNo = request.getParameter("no");
		String sql = "SELECT ROUND(AVG(r_grade), 1) AS avg_grade FROM review where i_no = ?";
		
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, paramNo);
			rs = pstmt.executeQuery();
			
			ReviewDTO r = new ReviewDTO();
			if (rs.next()) {
				r.setR_grade(rs.getDouble("avg_grade"));
				request.setAttribute("gradeAvg", r);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
	}
	
	// 개인 마이페이지 후기 리스트 조회	
	public static void getReview(HttpServletRequest request, HttpServletResponse res) {
		
		HttpSession hs = request.getSession();
		User user = (User) request.getSession().getAttribute("userAccount");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from review where u_id = ? order by r_no desc";
		
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getU_id());
			rs = pstmt.executeQuery();
			
			ReviewDTO review = null;
			ArrayList<ReviewDTO> reviews = new ArrayList<ReviewDTO>();
			while (rs.next()) {
				review = new ReviewDTO();
				review.setR_no(rs.getInt("r_no"));
				review.setR_estimation(rs.getString("r_estimation"));
				review.setR_date(rs.getDate("r_date"));
				review.setR_grade(rs.getDouble("r_grade"));
				review.setR_nickname(rs.getString("r_nickname"));
				review.setR_productname(rs.getString("r_productname"));
				review.setI_no(rs.getInt("i_no"));
				reviews.add(review);
			}
			request.setAttribute("reviews", reviews);
			
			Gson g = new Gson();
			String jsonData = g.toJson(reviews);
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			res.getWriter().write(jsonData);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		
	}
	

	// 개인 마이페이지 후기 삭제 기능
	public static void reviewDelete(HttpServletRequest request) {
		
		HttpSession hs = request.getSession();
		User user = (User) request.getSession().getAttribute("userAccount");
		String no = request.getParameter("no");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "delete from review where u_id = ? and i_no = ?";
		
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getU_id());
			pstmt.setString(2, no);
//			System.out.println(no);
//			System.out.println(user.getU_id());
			
			if (pstmt.executeUpdate() == 1) {
				System.out.println("리뷰 삭제 성공");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("리뷰 삭제 실패");
		} finally {
			DBManager.close(con, pstmt, null);
		}
	}
	
	
	// 개인 마이페이지 후기 추가 기능
	public static boolean addReview(HttpServletRequest request) {
		
		User user = (User) request.getSession().getAttribute("userAccount");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "insert into review VALUES (review_seq.nextval, ?, SYSDATE, ?, ?, ?, ?, ?)";
		System.out.println("왔?");
		
		boolean check = false;
		
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			request.setCharacterEncoding("UTF-8");
//			String product = request.getParameter("name");
//			System.out.println(product);
//			System.out.println(request.getParameter("story"));
//			System.out.println(request.getParameter("grade"));
//			System.out.println(request.getParameter("no"));
			
			
			
			pstmt.setString(1, request.getParameter("story"));
			pstmt.setString(2, user.getU_nicname());
			pstmt.setString(3, request.getParameter("name"));
			pstmt.setString(4, user.getU_id());
			pstmt.setString(5, request.getParameter("no"));
			pstmt.setString(6, request.getParameter("grade"));
			
			
			
			if (pstmt.executeUpdate() == 1) {
				System.out.println("리뷰 등록 성공");
				check = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("리뷰 등록 실패");
		} finally {
			DBManager.close(con, pstmt, null);
		} return check;
	}
}

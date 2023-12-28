package com.recovery.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.recovery.main.DBManager;

public class ReviewDAO {

	// 상품 상페 페이지 후기 전체 보기
	public static void getAllReview(HttpServletRequest request, HttpServletResponse res) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from review where i_no = ? ";
		
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			
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
	
	// 개인 마이페이지 후기 리스트 조회	
	public static void getReview(HttpServletRequest request, HttpServletResponse res) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from review where u_id = ? ";
		
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			
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
	
	
	public static void reviewDelete(HttpServletRequest request) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "delete from review where u_id = ? and i_no = ?";
		
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, request.getParameter(""));
			pstmt.setString(2, request.getParameter(""));
			
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
	
}
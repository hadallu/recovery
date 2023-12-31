package com.recovery.mypage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.recovery.account.AccountDAO;
import com.recovery.shop.ItemDAO;


@WebServlet("/SellerMypageC")
public class SellerMypageC extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccountDAO.loginCheck(request) == 0) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();

			out.println("<script language='javascript'>");
			out.println("alert('ログインが切れました。ログインページに行きます')");
			out.println("location.href='LoginPageC'");
			out.println("</script>");

			out.flush();
		} else {
		request.setAttribute("contentPage", "wr_company/MYPAGE_NEW/myPage.jsp");
		request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		
	}

}

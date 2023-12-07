package com.recovery.shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.recovery.main.DBManager;

public class ItemDAO {

	
	// ���θ� ���������� ��ǰ �����ֱ�
	public static void getAllItems(HttpServletRequest request) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from item";
		
		try {
			con = DBManager.connect();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ItemDTO item = null;
			
			ArrayList<ItemDTO> items = new ArrayList<ItemDTO>();
			while (rs.next()) {
				item = new ItemDTO();
				item.setI_no(rs.getInt("i_no"));
				item.setI_name(rs.getString("i_name"));
				item.setI_img(rs.getString("i_img"));
				item.setI_des(rs.getString("i_des"));
				item.setI_category(rs.getInt("i_category"));
				item.setI_price(rs.getInt("i_price"));
				item.setI_stock(rs.getInt("i_stock"));
				item.setI_star_avg(rs.getDouble("i_star_avg"));
				items.add(item);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		
		
		
	}

}

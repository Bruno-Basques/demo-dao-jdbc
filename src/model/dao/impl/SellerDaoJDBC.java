package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.GenericDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements GenericDao<Seller>{

	private Connection conn;	
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByIt(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller getById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "SELECT seller.*,department.Name as DepName "
				+ "FROM seller INNER JOIN department "
				+ "On seller.DepartmentId = department.Id "
				+ "Where seller.Id = ?";
		
		try 
		{			
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next())
			{
				Department dep = new Department(rs.getInt("DepartmentId"), 
						rs.getString("DepName"));
				Seller seller = new Seller(rs.getInt("Id"),
						rs.getString("Name"),
						rs.getString("Email"),
						rs.getDate("BirthDate"),
						rs.getDouble("BaseSalary"),						
						dep);
				return seller;
			}
			
			return null;
		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}

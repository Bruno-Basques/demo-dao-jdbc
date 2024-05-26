package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	private Connection conn;	
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "INSERT INTO seller "
				+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
				+ "VALUES"
				+ "(?, ?, ?, ?, ?)";
		
		try 
		{			
			st = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0)
			{
				rs = st.getGeneratedKeys();
				if(rs.next())
				{
					int id = rs.getInt(1);
					obj.setId(id);
				}
			}
			else
			{
				throw new DbException("Unexpected error! No rows affected");
			}
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
	public void update(Seller obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "UPDATE seller "
				+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
				+ "WHERE Id = ?";
		
		try 
		{			
			st = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();			
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
	public void deleteByIt(Integer id) {
		PreparedStatement st =  null;
		String sql = "DELETE FROM seller WHERE Id = ?";
		try
		{			
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());			
		}
		finally
		{			
			DB.closeStatement(st);
		}		
	}

	@Override
	public Seller getById(Integer sellerId) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "SELECT seller.*,department.Name as DepName "
				+ "FROM seller INNER JOIN department "
				+ "On seller.DepartmentId = department.Id "
				+ "Where seller.Id = ?";
		
		try 
		{			
			st = conn.prepareStatement(sql);
			st.setInt(1, sellerId);
			rs = st.executeQuery();
			if(rs.next())
			{
				Department dep = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, dep);
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

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		return new Seller(rs.getInt("Id"),
				rs.getString("Name"),
				rs.getString("Email"),
				rs.getDate("BirthDate"),
				rs.getDouble("BaseSalary"),						
				dep);
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		return new Department(rs.getInt("DepartmentId"), 
				rs.getString("DepName"));
	}

	@Override
	public List<Seller> getAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "SELECT seller.*,department.Name as DepName "
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id "
				+ "ORDER BY seller.Name";
		try 
		{			
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			List<Seller> sellers = new ArrayList<Seller>();
			Map<Integer, Department> department = new HashMap<>();
			while(rs.next())
			{
				Department dep = department.get(rs.getInt("DepartmentId"));
				if(dep == null)
				{
					dep = instantiateDepartment(rs);
					department.put(dep.getId(), dep);
				}				
				
				Seller seller = instantiateSeller(rs, dep);
				sellers.add(seller);
			}
			
			return sellers;
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
	public List<Seller> getByDepartmentId(Integer departmentId) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "SELECT seller.*,department.Name as DepName "
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id "
				+ "WHERE seller.DepartmentId = ? "
				+ "ORDER BY seller.Name";
		try 
		{			
			st = conn.prepareStatement(sql);
			st.setInt(1, departmentId);
			rs = st.executeQuery();
			List<Seller> sellers = new ArrayList<Seller>();
			Map<Integer, Department> department = new HashMap<>();
			while(rs.next())
			{
				Department dep = department.get(departmentId);
				if(dep == null)
				{
					dep = instantiateDepartment(rs);
					department.put(dep.getId(), dep);
				}				
				
				Seller seller = instantiateSeller(rs, dep);
				sellers.add(seller);
			}
			
			return sellers;
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

}

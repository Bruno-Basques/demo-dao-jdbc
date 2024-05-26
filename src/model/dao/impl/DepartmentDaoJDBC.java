package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection conn;	
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "INSERT INTO department "
				+ "(Name) "
				+ "VALUES"
				+ "(?)";
		
		try 
		{			
			st = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			
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
	public void update(Department obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "UPDATE department "
				+ "SET Name = ? "
				+ "WHERE Id = ?";
		
		try 
		{			
			st = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
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
		String sql = "DELETE FROM department WHERE Id = ?";
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
	public Department getById(Integer departmentId) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "SELECT * "
				+ "FROM department "
				+ "WHERE Id = ?";
		
		try 
		{			
			st = conn.prepareStatement(sql);
			st.setInt(1, departmentId);
			rs = st.executeQuery();
			if(rs.next())
			{
				Department dep = instantiateDepartment(rs);
				return dep;
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

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		return new Department(rs.getInt("Id"), 
				rs.getString("Name"));
	}

	@Override
	public List<Department> getAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "SELECT * "
				+ "FROM department "
				+ "ORDER BY Name";
		try 
		{			
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			List<Department> departments = new ArrayList<Department>();
			while(rs.next())
			{
				Department department = instantiateDepartment(rs);
				departments.add(department);
			}
			
			return departments;
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

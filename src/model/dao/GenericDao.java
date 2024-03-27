package model.dao;

import java.util.List;

public interface GenericDao <T>
{	
	void insert(T obj);
	void update (T obj);
	void deleteByIt(Integer id);
	T getById(Integer id);
	List<T> getAll();
}

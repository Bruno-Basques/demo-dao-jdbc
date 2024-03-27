package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TEST 1: seller getById ===");
		Seller seller = sellerDao.getById(3);
		System.out.println(seller.toString());
		
		System.out.println("=== TEST 2: seller getByDepartmentId ===");
		List<Seller> sellers = sellerDao.getByDepartmentId(2);
		
		for(Seller obj : sellers)
		{
			System.out.println(obj.toString());
		}	
		
		System.out.println("=== TEST 3: seller getAll ===");
		sellers = sellerDao.getAll();
		
		for(Seller obj : sellers)
		{
			System.out.println(obj.toString());
		}	
	}
}

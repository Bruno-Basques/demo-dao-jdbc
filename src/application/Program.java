package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
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
		
		System.out.println("=== TEST 4: seller insert ===");
		Department department = new Department(2, "Electronics");
		Seller newSeller = new Seller(null, 
				"New Seller", 
				"newseller@email.com",
				new Date(),
				4000.00,
				department);
		sellerDao.insert(newSeller);	
		System.out.println("Inserted! New id = " + newSeller.getId());
		
		System.out.println("=== TEST 5: seller update ===");
		seller = sellerDao.getById(1);
		seller.setName("Test Update");
		sellerDao.update(seller);
		System.out.println("Update completed!");
	}
}

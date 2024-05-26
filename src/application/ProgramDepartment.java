package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class ProgramDepartment {

public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("\n=== TEST 1: department getById ===");
		Department department = departmentDao.getById(3);
		System.out.println(department.toString());
	
		System.out.println("\n=== TEST 2: department getAll ===");
		List<Department> departments = departmentDao.getAll();		
		for(Department obj : departments)
		{
			System.out.println(obj.toString());
		}	
		
		System.out.println("\n=== TEST 3: department insert ===");
		Department newDepartment = new Department(null, "New Department");
		departmentDao.insert(newDepartment);	
		System.out.println("Inserted! New id = " + newDepartment.getId());
		
		System.out.println("\n=== TEST 4: department update ===");
		department = departmentDao.getById(1);
		department.setName("Test Update");
		departmentDao.update(department);
		System.out.println("Update completed!");
		
		System.out.println("\n=== TEST 5: department delete ===");
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		departmentDao.deleteByIt(id);
		System.out.println("Delete completed");
		
		sc.close();
	}
}

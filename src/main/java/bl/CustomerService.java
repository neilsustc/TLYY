package main.java.bl;

import java.util.List;

import main.java.da.dao.CustomerDao;
import main.java.pojo.Customer;

public class CustomerService
{
    private CustomerDao customerDao = new CustomerDao();

    public boolean isCustomerExisting(String idCard)
    {
        return customerDao.findByIdCard(idCard) != null;
    }

    public boolean addCustomer(Customer customer)
    {
        return customerDao.saveCustomer(customer);
    }

    public List<String[]> findAllCustemers()
    {
        // TODO
        return null;
    }
}

package main.java.bl;

import java.util.List;
import java.util.stream.Collectors;

import main.java.da.dao.CustomerDao;
import main.java.pojo.Customer;

public class CustomerService
{
    private CustomerDao customerDao = new CustomerDao();

    public boolean isCustomerExisting(String idCard)
    {
        return customerDao.findByIdCard(idCard) != null;
    }

    public int addCustomer(Customer customer)
    {
        return customerDao.saveCustomer(customer);
    }

    public List<String[]> findAllCustemersInArray()
    {
        return customerDao
                .findAllCustemers().stream().map(c -> new String[] {
                        c.getIdCard(), c.getName(), c.getPhoneNum() })
                .collect(Collectors.toList());
    }

    public int deleteCustomer(String idCard)
    {
        return customerDao.deleteCustomer(idCard);
    }
}

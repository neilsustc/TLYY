package main.java.da.dao;

import main.java.pojo.Customer;

public class CustomerDao extends BaseDao<Customer, String>
{
    public Customer findByIdCard(String idCard)
    {
        return findByPk(idCard);
    }

    public boolean saveCustomer(Customer customer)
    {
        return insert(customer);
    }
}

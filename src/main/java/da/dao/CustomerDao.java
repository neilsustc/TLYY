package main.java.da.dao;

import java.util.List;

import main.java.pojo.Customer;

public class CustomerDao extends BaseDao<Customer, String>
{
    public Customer findByIdCard(String idCard)
    {
        return findByPk(idCard);
    }

    public int saveCustomer(Customer customer)
    {
        return insert(customer);
    }

    public List<Customer> findAllCustemers()
    {
        return findAll();
    }

    public int deleteCustomer(String idCard)
    {
        return deleteByPk(idCard);
    }
}

package main.java.da.dao;

import main.java.pojo.Admin;

public class AdminDao extends BaseDao<Admin, String>
{
    public Admin findByStaffNum(String staffNum)
    {
        return findByPk(staffNum);
    }

    public String findPassByAccount(String staffNum)
    {
        return executeQuery("SELECT password FROM admin WHERE staff_num = '"
                + staffNum + "'").get(0).get("password").toString();
    }

    public boolean setNewPass(String staffNum, String newPass)
    {
        return executeUpdate("UPDATE admin SET password = '" + newPass
                + "' WHERE staff_num = '" + staffNum + "'") == 1;
    }
}

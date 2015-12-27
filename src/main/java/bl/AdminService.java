package main.java.bl;

import main.java.da.dao.AdminDao;

public class AdminService
{
    private AdminDao adminDao = new AdminDao();

    public boolean checkPassword(String staffNum, String pass)
    {
        if (staffNum == null || pass == null || "".equals(staffNum)
                || "".equals(pass))
            return false;
        else
            return pass.equals(adminDao.findPassByAccount(staffNum));
    }

    public boolean changePass(String staffNum, String newPass)
    {
        return adminDao.setNewPass(staffNum, newPass);
    }
}

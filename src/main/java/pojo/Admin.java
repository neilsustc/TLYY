package main.java.pojo;

public class Admin
{
    private String staffNum;
    private String staffName;
    private String password;

    public Admin()
    {
    }

    public Admin(String staffNum, String staffName, String password)
    {
        this.staffNum = staffNum;
        this.staffName = staffName;
        this.password = password;
    }

    public String getStaffNum()
    {
        return staffNum;
    }

    public void setStaffNum(String staffNum)
    {
        this.staffNum = staffNum;
    }

    public String getStaffName()
    {
        return staffName;
    }

    public void setStaffName(String staffName)
    {
        this.staffName = staffName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}

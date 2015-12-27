package main.java.pojo;

public class Customer
{
    private String idCard;
    private String name;
    private String phoneNum;

    public Customer()
    {
    }

    public Customer(String idCard, String name, String phoneNum)
    {
        this.idCard = idCard;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

}

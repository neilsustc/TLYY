package main.java.pojo;

public class RoomPrice
{
    private String roomType;
    private String roomLevel;
    private Double price;

    public String getRoomType()
    {
        return roomType;
    }

    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }

    public String getRoomLevel()
    {
        return roomLevel;
    }

    public void setRoomLevel(String roomLevel)
    {
        this.roomLevel = roomLevel;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }
}

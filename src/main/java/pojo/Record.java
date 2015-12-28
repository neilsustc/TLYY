package main.java.pojo;

import java.sql.Timestamp;

public class Record
{
    private String id;
    private String customerId;
    private String type;
    private Timestamp timestamp;
    private Double actualCost;

    public Record()
    {
    }

    public Record(String id, String customerId, String type,
            Timestamp timestamp, Double actualCost)
    {
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.timestamp = timestamp;
        this.actualCost = actualCost;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    public Double getActualCost()
    {
        return actualCost;
    }

    public void setActualCost(Double actualCost)
    {
        this.actualCost = actualCost;
    }

    @Override
    public String toString()
    {
        return String.format("%s; %s; %s; %s; %f", id, customerId, type,
                timestamp, actualCost);
    }
}

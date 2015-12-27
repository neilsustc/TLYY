package main.java.da.dao;

import main.java.pojo.Record;

public class RecordDao extends BaseDao<Record, String>
{
    public Record findById(String id)
    {
        return findByPk(id);
    }

    public void save(Record record)
    {
        insert(record);
    }
}

package test.java;

import java.sql.Timestamp;
import java.util.Date;

import main.java.da.dao.RecordDao;
import main.java.pojo.Record;

/**
 * RecordDao:<br>
 * save(Record)<br>
 * findById(String)<br>
 * <br>
 * BaseDao:<br>
 * insert(T)<br>
 * findByPk(Pk)
 */
public class TestRecordDao
{
    public static void main(String[] args)
    {
        Record record = new Record("longlongId", "customerId", "testType",
                new Timestamp(new Date().getTime()), 123.45);
        RecordDao recordDao = new RecordDao();
        recordDao.save(record);
        Record record2 = recordDao.findById(record.getId());
        System.out.println(record2.getId());
        System.out.println(record2.getCustomerId());
        System.out.println(record2.getType());
        System.out.println(record2.getTimestamp());
        System.out.println(record2.getActualCost());
    }
}

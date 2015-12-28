package test.java;

import java.sql.Timestamp;
import java.util.Date;

import main.java.da.dao.AdminDao;
import main.java.da.dao.RecordDao;
import main.java.pojo.Record;

/**
 * RecordDao:<br>
 * save(Record)<br>
 * findById(String)<br>
 * <br>
 * AdminDao:<br>
 * findPassByStaffNum(String)<br>
 * setNewPass(String, String)<br>
 * <br>
 * BaseDao:<br>
 * insert(T)<br>
 * findByPk(Pk)<br>
 * executeQuery(String)<br>
 * executeUpdate(String)<br>
 */
public class TestDao
{
    public static void main(String[] args)
    {
        Record record = new Record("longlongId", "customerId", "testType",
                new Timestamp(new Date().getTime()), 123.45);
        RecordDao recordDao = new RecordDao();
        recordDao.save(record);
        System.out.println(recordDao.findById(record.getId()));

        AdminDao adminDao = new AdminDao();
        System.out.println(adminDao.findPassByStaffNum("2333"));
        adminDao.setNewPass("2333", "666");

        System.out.println("over");
    }
}

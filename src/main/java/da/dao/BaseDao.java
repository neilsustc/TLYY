package main.java.da.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.common.StringUtil;
import main.java.da.JDBC;

/**
 * 
 * @param <T>
 * @param <PK>
 *            PrimaryKey
 */
public class BaseDao<T, PK extends Serializable>
{
    private final Logger logger = LogManager.getLogger("test");
    private StringUtil stringUtil = StringUtil.INSTANCE;

    private Class<T> entityClass;
    private Field[] fields;
    private String fullClassName;
    private String simpleClassName;
    private String firstField;

    @SuppressWarnings("unchecked")
    public BaseDao()
    {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class<T>) params[0];
        fields = entityClass.getDeclaredFields();
        simpleClassName = entityClass.getSimpleName();
        fullClassName = entityClass.getName();
        firstField = fields[0].getName();
    }

    @SuppressWarnings("unchecked")
    protected T findByPk(PK pk)
    {
        String sql = "SELECT * FROM "
                + stringUtil.upperCamel2Underscore(simpleClassName) + " WHERE "
                + stringUtil.lowerCamel2Underscore(firstField) + " = ?";

        List<Map<String, Object>> resultList = executePreparedStatmentQuery(sql,
                new Object[] { pk });

        if (resultList.size() > 0)
        {
            T obj = null;
            try
            {
                obj = (T) Class.forName(fullClassName).newInstance();
                Map<String, Object> row = resultList.get(0);
                dataMapToEntity(obj, row);
            } catch (Exception e)
            {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            return obj;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected List<T> findAll()
    {
        String sql = "SELECT * FROM "
                + stringUtil.upperCamel2Underscore(simpleClassName);

        List<Map<String, Object>> resultList = executePreparedStatmentQuery(sql,
                new Object[0]);
        List<T> list = new ArrayList<>();
        for (Map<String, Object> row : resultList)
        {
            try
            {
                T obj = (T) Class.forName(fullClassName).newInstance();
                dataMapToEntity(obj, row);
                list.add(obj);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return list;
    }

    protected int insert(T obj)
    {
        try
        {
            StringBuffer sqlBuffer = new StringBuffer("INSERT INTO "
                    + stringUtil.upperCamel2Underscore(simpleClassName)
                    + " VALUES (");
            Arrays.stream(fields).forEach(f -> sqlBuffer.append("?, "));
            sqlBuffer.delete(sqlBuffer.length() - 2, sqlBuffer.length());
            sqlBuffer.append(")");

            Object[] args = Arrays.stream(fields).map(f ->
            {
                f.setAccessible(true);
                try
                {
                    return f.get(obj);
                } catch (Exception e)
                {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }).toArray(Object[]::new);

            return executePreparedStatmentUpdate(sqlBuffer.toString(), args);
        } catch (Exception e)
        {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    protected int deleteByPk(PK pk)
    {
        try
        {
            StringBuffer sqlBuffer = new StringBuffer("DELETE FROM ");
            sqlBuffer.append(stringUtil.upperCamel2Underscore(simpleClassName));
            sqlBuffer.append(" WHERE ");
            sqlBuffer.append(stringUtil.lowerCamel2Underscore(firstField));
            sqlBuffer.append(" = ?");

            return executePreparedStatmentUpdate(sqlBuffer.toString(),
                    new Object[] { pk });
        } catch (Exception e)
        {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    protected List<Map<String, Object>> executeQuery(String sql)
    {
        return executePreparedStatmentQuery(sql, new Object[0]);
    }

    protected int executeUpdate(String sql)
    {
        return executePreparedStatmentUpdate(sql, new Object[0]);
    }

    private List<Map<String, Object>> executePreparedStatmentQuery(String sql,
            Object[] args)
    {
        Connection connection = JDBC.getConnection();
        PreparedStatement ps = null;
        try
        {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++)
            {
                Object arg = args[i];
                setAttribute(ps, i, arg);
            }
            logger.info(toString(ps));
            ResultSet resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<Map<String, Object>> resultList = new ArrayList<>();
            while (resultSet.next())
            {
                HashMap<String, Object> rowData = new HashMap<>();
                for (int i = 0; i < columnCount; i++)
                {
                    rowData.put(metaData.getColumnLabel(i + 1),
                            resultSet.getObject(i + 1));
                }
                resultList.add(rowData);
            }
            return resultList;
        } catch (Exception e)
        {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            } catch (SQLException e)
            {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    private int executePreparedStatmentUpdate(String sql, Object[] args)
    {
        Connection connection = JDBC.getConnection();
        PreparedStatement ps = null;
        try
        {
            connection.setAutoCommit(false); // Transaction
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++)
            {
                Object arg = args[i];
                setAttribute(ps, i, arg);
            }
            logger.info(toString(ps));
            int affectRowCount = ps.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            return affectRowCount;
        } catch (Exception e)
        {
            logger.error(e.getMessage());
            e.printStackTrace();
            try
            {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e1)
            {
                logger.error(e1.getMessage());
                e1.printStackTrace();
            }
        } finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            } catch (SQLException e)
            {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void dataMapToEntity(Object obj, Map<String, Object> dataMap)
            throws Exception
    {
        for (Entry<String, Object> entry : dataMap.entrySet())
        {
            Method set = entityClass.getDeclaredMethod(
                    "set" + stringUtil.underscore2UpperCamel(entry.getKey()),
                    entry.getValue().getClass());
            set.invoke(obj, entry.getValue());
        }
    }

    private String toString(PreparedStatement ps)
    {
        return ps.toString().split(": ")[1];
    }

    private void setAttribute(PreparedStatement ps, int index, Object arg)
            throws SQLException
    {
        if (arg instanceof Integer)
            ps.setInt(index + 1, (int) arg);
        else if (arg instanceof Double)
            ps.setDouble(index + 1, (double) arg);
        else if (arg instanceof String)
            ps.setString(index + 1, (String) arg);
        else if (arg instanceof Timestamp)
            ps.setTimestamp(index + 1, (Timestamp) arg);
    }
}

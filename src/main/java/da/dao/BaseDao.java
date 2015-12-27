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
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
        List<LinkedHashMap<String, Object>> resultList = executePreparedStatment(
                sql, new Object[] { pk });
        if (resultList.size() > 0)
        {
            Object obj = null;
            try
            {
                obj = Class.forName(fullClassName).newInstance();
                LinkedHashMap<String, Object> row = resultList.get(0);
                for (Entry<String, Object> entry : row.entrySet())
                {
                    Method set = entityClass.getDeclaredMethod(
                            "set" + stringUtil
                                    .underscore2UpperCamel(entry.getKey()),
                            entry.getValue().getClass());
                    set.invoke(obj, entry.getValue());
                }
            } catch (Exception e)
            {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            return (T) obj;
        }
        return null;
    }

    protected boolean insert(T obj)
    {
        Connection connection = JDBC.getConnection();
        PreparedStatement ps = null;
        try
        {
            StringBuffer sqlBuffer = new StringBuffer("INSERT INTO "
                    + stringUtil.upperCamel2Underscore(simpleClassName)
                    + " VALUES (");
            for (int i = 0; i < fields.length; i++)
            {
                sqlBuffer.append("?, ");
            }
            sqlBuffer.delete(sqlBuffer.length() - 2, sqlBuffer.length());
            sqlBuffer.append(")");

            connection.setAutoCommit(false); // Transaction
            ps = connection.prepareStatement(sqlBuffer.toString());
            for (int i = 0; i < fields.length; i++)
            {
                Field field = fields[i];
                field.setAccessible(true);
                setAttribute(ps, i, field.get(obj));
            }
            logger.info(toString(ps));
            ps.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            return true;
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
                ps.close();
            } catch (SQLException e)
            {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    protected List<List<Object>> findAll()
    {
        // TODO
        return null;
    }

    protected List<LinkedHashMap<String, Object>> executePreparedStatment(
            String sql, Object[] args)
    {
        Connection connection = JDBC.getConnection();
        try
        {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++)
            {
                Object arg = args[i];
                setAttribute(ps, i, arg);
            }
            logger.info(toString(ps));
            ResultSet resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
            while (resultSet.next())
            {
                LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
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
        }
        return null;
    }

    protected List<LinkedHashMap<String, Object>> executeQuery(String sql)
    {
        logger.info(sql);
        Connection connection = JDBC.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
            while (resultSet.next())
            {
                LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
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
                resultSet.close();
                statement.close();
            } catch (SQLException e)
            {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    protected int executeUpdate(String sql)
    {
        logger.info(sql);
        Connection connection = JDBC.getConnection();
        Statement statement = null;
        try
        {
            connection.setAutoCommit(false); // Transaction
            statement = connection.createStatement();
            int affect = statement.executeUpdate(sql);
            connection.commit();
            connection.setAutoCommit(true);
            return affect;
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
                statement.close();
            } catch (SQLException e)
            {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return 0;
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

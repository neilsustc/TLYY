package main.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map.Entry;

public enum Config
{
    JDBC("jdbc.properties"), OTHER("other.properties");

    private final Logger logger = LogManager.getLogger();
    private volatile Properties props = new Properties();

    private Config(String path)
    {
        try
        {
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null)
            {
                logger.warn("Cannot find config file '" + path + "'.");
                return;
            }
            props.load(inputStream);
            inputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Object setValue(String key, String value)
    {
        return props.setProperty(key, value);
    }

    public String getValue(String key)
    {
        return props.getProperty(key);
    }

    public void printAll()
    {
        System.out.println("Properties:");
        for (Entry<Object, Object> entry : props.entrySet())
        {
            System.out.printf("%-25s%s\n", entry.getKey() + ": ",
                    entry.getValue());
        }
        System.out.println();
    }
}

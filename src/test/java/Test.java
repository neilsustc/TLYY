package test.java;

import java.util.Arrays;
import java.util.Date;

import main.java.common.StringUtil;
import main.java.pojo.Admin;

public class Test
{
    public static void main(String[] args)
    {
        // streamAPI();
        stringBuffer();
    }

    private static void stringBuffer()
    {
        String hot = "balabala" + StringUtil.INSTANCE
                .underscore2UpperCamel("underscore_format");

        long start1 = new Date().getTime();
        String sql = "DELETE FROM "
                + StringUtil.INSTANCE.upperCamel2Underscore("SimpleClassName")
                + " WHERE "
                + StringUtil.INSTANCE.lowerCamel2Underscore("firstField")
                + " = ?";
        System.out.println(sql);
        long end1 = new Date().getTime();

        long start2 = new Date().getTime();
        StringBuffer sqlBuffer = new StringBuffer("DELETE FROM ");
        sqlBuffer.append(
                StringUtil.INSTANCE.upperCamel2Underscore("SimpleClassName"));
        sqlBuffer.append(" WHERE ");
        sqlBuffer.append(
                StringUtil.INSTANCE.lowerCamel2Underscore("firstField"));
        sqlBuffer.append(" = ?");
        System.out.println(sqlBuffer.toString());
        long end2 = new Date().getTime();

        System.out.printf("%d\n%d\n", (end1 - start1), (end2 - start2));
    }

    private static void streamAPI()
    {
        String[] strings = Arrays
                .asList(new Admin("1", "2", "3"), new Admin("4", "5", "6"),
                        new Admin("7", "8", "9"))
                .stream().map(Admin::getPassword).toArray(String[]::new);
        Arrays.stream(strings).forEach(s -> System.out.println(s));
    }
}

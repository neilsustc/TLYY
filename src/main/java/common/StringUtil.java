package main.java.common;

import com.google.common.base.CaseFormat;

public enum StringUtil
{
    INSTANCE;

    public String capitalize(String string)
    {
        return Character.toUpperCase(string.charAt(0))
                + (string.length() > 1 ? string.substring(1) : "");
    }

    public String lowerCamel2Underscore(String str)
    {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }

    public String upperCamel2Underscore(String str)
    {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }

    public String underscore2UpperCamel(String str)
    {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);
    }
}

package com.damo.tools.maven.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * 数据模型操作类
 *
 */
public class DataHelper {
    static String prefix = "u_";

    public static void main(String[] args) {
        System.out.println(getCamelCaseName("index"));
        System.out.println(getPascalCaseName("index"));
        DataType dt = new DataType("decimal");
        System.out.println(getJavaVariableType(dt));
    }

    public static LinkedHashMap<String, Table> paresTables(BufferedReader bf)
        throws IOException, MojoExecutionException {
        String line = null;
        LinkedHashMap<String, Table> result = new LinkedHashMap<String, Table>();
        Table table = null;
        boolean t = true;
        while ((line = bf.readLine()) != null) {
            System.out.println(line);
            if (line.startsWith("CREATE TABLE") || line.startsWith("create table")) {
                table = new Table();
                table.setName(findName(line));
                t = false;

            } else if (line.startsWith(") ENGINE=") || line.startsWith(")")) {
                table.setCommnet(findCommnet(line));
                result.put(table.getName(), table);
                t = true;
            } else if (line.indexOf("KEY") > -1) {

            } else if (line.length() == 0) {

            } else if (line.indexOf("`") == -1) {

            } else if (line.startsWith("DROP TABLE") || line.startsWith("drop table")) {

            } else {
                table.addColumn(findColumn(line));
            }
        }
        if (t == false) {
            throw new MojoExecutionException("表结构读取失败，结构不完整.");
        }
        return result;
    }

    private static Column findColumn(String line) {
        Column col = new Column();
        col.setName(findName(line));
        col.setCommnet(findCommnet(line));
        col.setType(findType(line));
        return col;
    }

    private static DataType findType(String line) {

        String[] cc = readright(line, "`");
        cc = readright(cc[1], "`");
        cc = readright(cc[1], "COMMENT");
        cc = readright(cc[0], "(");
        DataType dt = new DataType(trimName(cc[0]));// 类型名
        cc = readright(cc[1], ")");
        cc = cc[0].split(",");
        if (cc.length == 2) {// 字段长度，精度
            dt.setLength(Integer.parseInt(cc[0]));
            dt.setPoint(Integer.parseInt(cc[0]));
        } else if (cc.length == 1) {
            if (!cc[0].equals("")) {
                dt.setLength(Integer.parseInt(cc[0]));
            }
        }
        return dt;
    }

    private static String trimName(String str) {
        String s = str.trim();
        String[] cc = readright(s, " ");
        s = cc[0];
        if (s.endsWith(",")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    private static String findCommnet(String line) {
        String[] cc = readright(line, "COMMENT");
        if (cc[1].length() == 0)
            return "";
        cc = readright(cc[1], "'");
        cc = readright(cc[1], "'");
        return cc[0];
    }

    private static String findName(String line) {
        String[] cc = readright(line, "`");
        cc = readright(cc[1], "`");
        return cc[0];
    }

    public static String[] readright(String in, String delim) {
        int a = in.indexOf(delim);
        if (a >= 0) {
            return new String[] {in.substring(0, a), in.substring(a + delim.length())};
        }
        return new String[] {in, ""};
    }

    public static String getCamelCaseName(String in) {
        String result = in.substring(0, 1).toLowerCase() + in.substring(1);
        return result;
    }

    public static String getPascalCaseName(String in) {
        String result = in.substring(0, 1).toUpperCase() + in.substring(1);
        return result;
    }

    public static String getJavaVariableType(DataType dt) {
        String result = dt.name;
        switch (dt.name) {
            case "decimal":
                result = "BigDecimal";
                break;
            case "varchar":
            case "mediumtext":
            case "text":
                result = "String";
                break;
            case "bit":
            case "tinyint":
            case "smallint":
            case "mediumint":
                result = "Integer";
                break;
            case "date":
            case "datetime":
                result = "Date";
                break;
            case "bigint":
            case "timestamp":
                result = "Long";
                break;
            case "int":
                if (dt.getLength() > 4) {
                    result = "Long";
                }
                break;
            default:
                break;
        }

        // System.out.println(dt+"==>"+result);
        return result;
    }

    
    public static String getJavaVariableValue(DataType dt) {
        String result = "0";
        switch (dt.name) {
            case "decimal":
                result = "new BigDecimal(\"0.0\")";
                break;
            case "varchar":
            case "mediumtext":
            case "text":
                result = "\"String\"";
                break;
            case "bit":
            case "tinyint":
            case "smallint":
            case "mediumint":
                result = "0";
                break;
            case "float":
                result = "0.0f";
                break;
            case "date":
            case "datetime":
                result = "new Date()";
                break;
            case "bigint":
            case "timestamp":
                result = "0L";
                break;
            case "int":
                if (dt.getLength() > 4) {
                    result = "0L";
                }
                break;
            default:
                break;
        }

        // System.out.println(dt+"==>"+result);
        return result;
    }
    public static String getClassName(Table table) {
        String result = table.name;
        if (result.startsWith(prefix)) {
            result = result.substring(prefix.length());
        }
        return getJaveName(result);
    }

    public static String getDaoName(Table table) {
        return getClassName(table) + "Dao";
    }

    public static String getServiceName(Table table) {
        return getClassName(table) + "Service";
    }

    public static String getServiceImplName(Table table) {
        return getClassName(table) + "ServiceImpl";
    }

    /**
     * bank_status >> BankStatus
     * 
     * @param in
     * @return
     */
    public static String getJaveName(String in) {
        String[] cc = in.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cc.length; i++) {
            sb.append(getPascalCaseName(cc[i]));
        }
        return sb.toString();
    }

    private String getLocalIp() throws UnknownHostException {
        return System.getProperty("localIp", InetAddress.getLocalHost().getHostAddress());
    }

    public static void setPrefix(String value) {
        prefix = value;
    }

}

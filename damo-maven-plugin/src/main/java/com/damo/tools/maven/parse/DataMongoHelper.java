package com.damo.tools.maven.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * 数据模型操作类
 *
 */
public class DataMongoHelper extends DataHelper {

    public static void main(String[] args) {
        System.out.println(getCamelCaseName("index"));
        System.out.println(getPascalCaseName("index"));
        DataType dt = new DataType("decimal");
        System.out.println(getJavaVariableType(dt));
    }
    
    public static String getEntName(Table table) {
        return getClassName(table) + "Ent";
    }
    
    public static String getRepositoryName(Table table) {
        return getClassName(table) + "Repository";
    }

}

package com.damo.tools.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;

import com.damo.tools.maven.parse.Column;
import com.damo.tools.maven.parse.DataHelper;
import com.damo.tools.maven.parse.DataMongoHelper;
import com.damo.tools.maven.parse.Table;

@Mojo(name = "mongo", threadSafe = true)
public class MongoMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject   project;

    private static boolean isTest = false;

    public static void main(String[] args) throws MojoExecutionException, MojoFailureException {

        isTest = true;
        MongoMojo pmj = new MongoMojo();
        pmj.execute();

    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        String file = getFile();
        String prefix = getPrefix();
        DataHelper.setPrefix(prefix);

        getLog().info("Start run mongo." + file);
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            LinkedHashMap<String, Table> tables = DataHelper.paresTables(bf);

            String packageName = getPackage();
            String dirPath = getProjectPath() + "\\src\\main\\java\\" + packageName.replace('.', '\\') + "\\";
            new File(dirPath).mkdirs();
            String testPath = getProjectPath() + "\\src\\test\\java\\" + packageName.replace('.', '\\') + "\\";
            new File(testPath).mkdirs();
            for (Entry<String, Table> entry : tables.entrySet()) {
                genJavaEnt(dirPath + "mongo\\", packageName, entry.getValue());
                genJavaRepository(dirPath + "mongo\\", packageName, entry.getValue());
                genJavaTest(testPath + "mongo\\", packageName, entry.getValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(bf);
        }
        getLog().info("End run mysql.");
    }

    private void genJavaTest(String dirPath, String packageName, Table table)
        throws FileNotFoundException, UnsupportedEncodingException {
        getLog().info("Start build java mongo test:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table) + "RepositoryTest";

        PrintWriter out = new PrintWriter(dirPath + className + ".java", "UTF-8");
        out.println("package " + packageName + ".mongo;");
        out.println();

        out.println("import java.util.Date;");
        out.println("import java.util.List;");
        out.println("import org.junit.Test;");
        out.println("import org.junit.runner.RunWith;");
        out.println("import org.slf4j.Logger;");
        out.println("import org.slf4j.LoggerFactory;");
        out.println("import org.springframework.beans.factory.annotation.Autowired;");
        out.println("import org.springframework.boot.test.context.SpringBootTest;");
        out.println("import org.springframework.test.context.junit4.SpringRunner;");
        out.println("import " + packageName + ".Application;");
//        out.println("import " + packageName + ".domain." + DataHelper.getClassName(table) + ";");
        // out.println("import " + packageName + ".service." + DataHelper.getClassName(table) + "Service;");
        out.println();

        out.println("@RunWith(SpringRunner.class)");
        out.println("@SpringBootTest(classes = Application.class)");
        out.println("public class " + className + " {");
        out.println();

        out.println("private static Logger logger = LoggerFactory.getLogger(" + className
            + ".class);");

        out.println("@Autowired");
        out.println("private " + DataMongoHelper.getRepositoryName(table) + " "
            + DataHelper.getCamelCaseName(DataMongoHelper.getRepositoryName(table)) + ";");

        out.println("    @Test");
        out.println("    public void m_Test(){");

        out.println("   "+DataMongoHelper.getEntName(table) + " ent = new " + DataMongoHelper.getEntName(table) + "();");
        for (Entry<String, Column> ent : table.getColumns().entrySet()) {
            out.println(genFieldSetValue(ent.getValue()));
        }
        out.println("");
        out.println("        " + DataMongoHelper.getEntName(table) + " c = "
            + DataHelper.getCamelCaseName(DataMongoHelper.getRepositoryName(table)) + ".save(ent);");
        out.println("");
        out.println("        logger.info(\"step1:{}\",ent);");
        out.println("        logger.info(\"step1.1:{}\",c);");
        out.println("");
        Column keyCol = getKeyColumn(table);
        out.println("        "+DataHelper.getJavaVariableType(keyCol.getType())+" id = c.get"+DataHelper.getJaveName(keyCol.getName())+"();");
        
        out.println("        List<" + DataMongoHelper.getEntName(table) + "> list = "
            + DataHelper.getCamelCaseName(DataMongoHelper.getRepositoryName(table)) + ".findAll();");
        out.println("        logger.info(\"step2:{}\",list);");
        
        out.println("        "+ DataHelper.getCamelCaseName(DataMongoHelper.getRepositoryName(table)) + ".delete(id);");
        
        out.println("        logger.info(\"step3:count={}\","+ DataHelper.getCamelCaseName(DataMongoHelper.getRepositoryName(table)) + ".count());");
        
        out.println("    }");
        out.println(" }");
        out.close();
    }

    private void genJavaServiceImpl(String dirPath, String packageName, Table table)
        throws FileNotFoundException, UnsupportedEncodingException {
        getLog().info("Start build java service impl:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table) + "ServiceImpl";

        PrintWriter out = new PrintWriter(dirPath + className + ".java", "UTF-8");

        out.println("package " + packageName + ".service;");
        out.println();
        out.println("import org.slf4j.Logger;");
        out.println("import org.slf4j.LoggerFactory;");
        out.println("import java.util.Date;");
        out.println("import org.springframework.beans.factory.annotation.Autowired;");
        out.println("import org.springframework.stereotype.Service;");
        out.println("import com.damo.core.service.BaseServiceImpl;");

        out.println("import " + packageName + ".domain." + DataHelper.getClassName(table) + ";");
        out.println("import " + packageName + ".dao." + DataHelper.getClassName(table) + "Dao;");
        out.println();

        out.println("@Service");
        out.println("public class " + className + " extends BaseServiceImpl<" + DataHelper.getClassName(table)
            + ", Long , " + DataHelper.getDaoName(table) + "> implements " + DataHelper.getServiceName(table) + " {");
        out.println();
        out.println("private static Logger logger = LoggerFactory.getLogger(" + DataHelper.getServiceImplName(table)
            + ".class);");
        out.println();

        out.println("@Autowired");
        out.println("private " + DataHelper.getDaoName(table) + " "
            + DataHelper.getCamelCaseName(DataHelper.getDaoName(table)) + ";");
        out.println();

        out.println(" private " + DataHelper.getClassName(table) + " getEnt(){");
        out.println(DataHelper.getClassName(table) + " ent = new " + DataHelper.getClassName(table) + "();");
        for (Entry<String, Column> ent : table.getColumns().entrySet()) {
            out.println(genFieldSetValue(ent.getValue()));
        }
        out.println("return ent;");

        out.println("}");
        out.println();
        out.println("}");
        out.close();

    }

    private void genJavaService(String dirPath, String packageName, Table table)
        throws FileNotFoundException, UnsupportedEncodingException {
        getLog().info("Start build java service:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table) + "Service";

        PrintWriter out = new PrintWriter(dirPath + className + ".java", "UTF-8");
        out.println("package " + packageName + ".service;");
        out.println();
        out.println("import com.damo.core.persistence.BaseDao;");
        out.println("import com.damo.core.service.BaseService;");
        out.println("import " + packageName + ".domain." + DataHelper.getClassName(table) + ";");
        out.println("import " + packageName + ".dao." + DataHelper.getClassName(table) + "Dao;");
        out.println();

        out.println(
            "public interface " + className + " extends BaseService<" + DataHelper.getClassName(table) + ", Long> {");

        out.println("}");
        out.close();

    }

    private void genJavaRepository(String dirPath, String packageName, Table table)
        throws FileNotFoundException, UnsupportedEncodingException {
        getLog().info("Start build java  mongo repository:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table) + "Repository";
        
        PrintWriter out = new PrintWriter(dirPath + className + ".java", "UTF-8");
        out.println("package " + packageName + ".mongo;");
        out.println();
        out.println("import org.springframework.data.mongodb.repository.MongoRepository;");

        out.println();
        out.println(
            "public interface " + className + " extends MongoRepository<" + DataHelper.getClassName(table) + "Ent, String> {");

        out.println("}");
        out.close();

    }

    private void genJavaEnt(String dirPath, String packageName, Table table)
        throws FileNotFoundException, UnsupportedEncodingException {
        getLog().info("Start build java mongo ent:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table)+"Ent";

        PrintWriter out = new PrintWriter(dirPath + className + ".java", "UTF-8");
        out.println("package " + packageName + ".mongo;");
        out.println();
        
        out.println("import java.util.Date;");
        out.println("import java.math.BigDecimal;");
        out.println("import org.springframework.data.annotation.Id;");
        out.println("import org.springframework.data.mongodb.core.mapping.Document;");
        out.println("import org.springframework.data.mongodb.core.mapping.Field;");
        out.println();

        out.println("@Document(collection = \"" + table.getName() + "\")");
        out.println("public class " + className + " {");
        int point = 0;
        for (Entry<String, Column> ent : table.getColumns().entrySet()) {
            if(point ==0){
                out.println("\t@Id");
            }else{
                out.println("\t@Field");
            }
            out.println(genField(ent.getValue()));
            point++;
        }
        out.println();
//        //构造函数
//        out.println("\tpublic "+className+"(){");
//        out.println();
//        out.println("\t}");
//        out.println();
//       //构造函数 带参
//        out.print("\tpublic "+className+"(");
//        int i = 0;
//        int h = table.getColumns().size();
//        for (Entry<String, Column> ent : table.getColumns().entrySet()) {
//            out.print(DataHelper.getJavaVariableType(ent.getValue().getType()));
//            out.print(" ");
//            out.print(DataHelper.getCamelCaseName(DataHelper.getJaveName(ent.getValue().getName())));
//            if(++i < h){
//                out.print(",");
//            }
//        }
//        out.println("){");
//        for (Entry<String, Column> ent : table.getColumns().entrySet()) {
//            String p = DataHelper.getCamelCaseName(DataHelper.getJaveName(ent.getValue().getName()));
//            out.println("\t\tthis."+p+" = "+p+";");
//        }
//        out.println("\t}");
//        out.println(); 
        //字段 属性
//        out.println("\t@Id");
//        out.println("\t@GeneratedValue(strategy = GenerationType.IDENTITY)");
        for (Entry<String, Column> ent : table.getColumns().entrySet()) {
            out.println(genFieldGetSet(ent.getValue()));
        }
        out.println(genToString(table));

        out.println("}");
        out.close();
    }

    private void createFile(String dirPath) {
        File f = new File(dirPath);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    private String genToString(Table table) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t@Override\r\n");
        sb.append("\tpublic String toString() {\r\n");
        sb.append("\t\treturn \"").append(DataHelper.getClassName(table)).append("[");
        String k = ",";
        int i = 0;
        for (Entry<String, Column> ent : table.getColumns().entrySet()) {
            i++;
            if (i == table.getColumns().size()) {
                k = "";
            }
            Column c = ent.getValue();
            String cName = DataHelper.getCamelCaseName(DataHelper.getJaveName(c.getName()));
            sb.append(String.format("%s=\" + %s + \"%s\"\r\n\t\t+ \"", cName, cName, k));
        }
        sb.substring(0, sb.length() - 1);

        sb.append("]\";\r\n");
        sb.append("\t}");
        return sb.toString();
    }


    private Column getKeyColumn(Table table) {
        for (Entry<String, Column> ent : table.getColumns().entrySet()) {
            return ent.getValue();
        }
        return null;
    }
    private String genFieldGetSet(Column value) {
        String cName = DataHelper.getCamelCaseName(DataHelper.getJaveName(value.getName()));

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\tpublic %s get%s() {\r\n", DataHelper.getJavaVariableType(value.getType()),
            DataHelper.getJaveName(value.getName())));

        sb.append("\t\treturn ");
        sb.append(cName);

        sb.append(";\r\n\t}\r\n");

        sb.append("\tpublic void set");
        sb.append(DataHelper.getJaveName(value.getName()));
        sb.append("(");
        sb.append(DataHelper.getJavaVariableType(value.getType()));
        sb.append(" ");
        sb.append(cName);
        sb.append(") {\r\n");

        sb.append(String.format("\t\tthis.%s = %s;\r\n", cName, cName));

        sb.append("\t}");

        return sb.toString();
    }

    private String genFieldSetValue(Column value) {
        StringBuilder sb = new StringBuilder();
        sb.append("   ent.set");
        sb.append(DataHelper.getJaveName(value.getName()));
        sb.append("(");
        sb.append(DataHelper.getJavaVariableValue(value.getType()));
        sb.append(");");
        return sb.toString();
    }

    private String genField(Column value) {
        StringBuilder sb = new StringBuilder();
        sb.append("\tprivate ");
        sb.append(DataHelper.getJavaVariableType(value.getType()));
        sb.append(" ");
        sb.append(DataHelper.getCamelCaseName(DataHelper.getJaveName(value.getName())));
        sb.append(";  //");
        sb.append(value.getCommnet());
        return sb.toString();
    }

    private String getFile() {
        return System.getProperty("file", "C:\\Users\\Administrator\\Desktop\\crebas.sql");
    }

    private String getPrefix() {
        return System.getProperty("prefix", "u_");
    }
    
    private String getPackage() {
        return System.getProperty("package", "com.damo.base.vip");
    }

    private String getDBname() {
        return System.getProperty("dbname", "damo_user");
    }
    
    private String getProjectPath() {
        if (isTest) {
            return "D:\\Workspaces\\damo\\damo-service-user\\damo-service-user-provider";
        } else {
            return project.getBasedir().getPath();
        }
    }
}

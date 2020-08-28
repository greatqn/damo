package com.damo.tools.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;

import com.damo.tools.maven.parse.DataHelper;
import com.damo.tools.maven.script.Platform;

@Mojo(name = "boot_service", threadSafe = true)
public class BootServiceMojo extends ProjectBootMojo{
    
   
    public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
        
        isTest = true;
        BootServiceMojo pmj = new BootServiceMojo();
        pmj.execute();

    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Start run Build Service "+getPackage()+"."+getName());
        
        File baseDir = new File(getProjectPath());

        getLog().info("Start build api.");
        buildApi(baseDir);

        buildConsumerTest(baseDir);

        buildProvider(baseDir);
        buildProviderTest(baseDir);

        getLog().info("Build Sucess. 添加下列代码到 xml里");
        getLog().info(String.format("<dubbo:reference id=\"%s\"      interface=\"%s\" />",getName(),getPackage()+"."+getName()));
//        getLog().info(String.format("<module>%s</module>",getProjectConsumerName()));
//        getLog().info(String.format("<module>%s</module>",getProjectProviderName()));
    }

    private void buildApi(File path) throws MojoExecutionException {
        File dirPath = new File(path, getProjectApiName()+ "\\src\\main\\java\\" + getPackage().replace('.', '\\'));
        dirPath.mkdirs();
        
        String className = getName();
        try {
            PrintWriter out = new PrintWriter(dirPath.getAbsolutePath()+"\\" + className + ".java", "UTF-8");
            getLog().info("write:"+dirPath + className + ".java");
            out.println("package " + getPackage() + ";");
            out.println();
            out.println("public interface "+getName()+" {");
            out.println();
            out.println("}");
            out.close();
        } catch (Exception e) {
            getLog().error(e);
        }
        
    }
    
    private void buildProvider(File path) throws MojoExecutionException {
        File dirPath = new File(path, getProjectProviderName()+ "\\src\\main\\java\\" + getPackage().replace('.', '\\'));
        dirPath.mkdirs();
        
        String className = getName();
        try {
            PrintWriter out = new PrintWriter(dirPath.getAbsolutePath()+"\\" + className + "Impl.java", "UTF-8");
            getLog().info("write:"+dirPath + className + "Impl.java");
            out.println("package " + getPackage() + ";");
            out.println();
            out.println("import org.springframework.stereotype.Component;");
            out.println("");
            out.println("import com.alibaba.dubbo.config.annotation.Service;");
            out.println("import "+getPackage()+"."+getName()+";");
            out.println("");
            out.println("@Service(interfaceClass = "+getName()+".class)");
            out.println("@Component");
            out.println("public class "+getName()+"Impl implements "+getName()+" {");
            out.println();
            out.println("}");
            out.close();
        } catch (Exception e) {
            getLog().error(e);
        }
    }

    private void buildProviderTest(File path) throws MojoExecutionException {
        File dirPath = new File(path, getProjectProviderName()+ "\\src\\test\\java\\" + getPackage().replace('.', '\\'));
        dirPath.mkdirs();
        
        String className = getName();
        try {
            PrintWriter out = new PrintWriter(dirPath.getAbsolutePath()+"\\" + className + "Test.java", "UTF-8");
            getLog().info("write:"+dirPath + className + "Test.java");
            out.println("package " + getPackage() + ";");
            out.println();
            out.println("import org.junit.Test;");
            out.println("import org.junit.runner.RunWith;");
            out.println("import org.springframework.boot.test.context.SpringBootTest;");
            out.println("import org.springframework.test.context.junit4.SpringRunner;");
            out.println("");
            out.println("import org.springframework.beans.factory.annotation.Autowired;");
            out.println("import com.damo.Application;;");
            out.println("");
            out.println("@RunWith(SpringRunner.class)");
            out.println("@SpringBootTest(classes = Application.class)");
            out.println("public class "+getName()+"Test {");
            out.println();
            out.println("    @Autowired");
            out.println("    private "+getName()+" "+DataHelper.getCamelCaseName(getName())+";");
            out.println();
            out.println("}");
            out.close();
        } catch (Exception e) {
            getLog().error(e);
        }
    }
    
    private void buildConsumerTest(File path) throws MojoExecutionException {
        File dirPath = new File(path, getProjectConsumerName()+ "\\src\\test\\java\\" + getPackage().replace('.', '\\'));
        dirPath.mkdirs();
        
        String className = getName();
        try {
            PrintWriter out = new PrintWriter(dirPath.getAbsolutePath()+"\\" + className + "Test.java", "UTF-8");
            getLog().info("write:"+dirPath + className + "Test.java");
            out.println("package " + getPackage() + ";");
            out.println();
            out.println("import org.junit.Test;");
            out.println("import org.junit.runner.RunWith;");
            out.println("import org.springframework.boot.test.context.SpringBootTest;");
            out.println("import org.springframework.test.context.junit4.SpringRunner;");
            out.println("");
            out.println("import com.alibaba.dubbo.config.annotation.Reference;");
            out.println("import com.damo.Application;;");
            out.println("");
            out.println("@RunWith(SpringRunner.class)");
            out.println("@SpringBootTest(classes = Application.class)");
            out.println("public class "+getName()+"Test {");
            out.println();
            out.println("    @Reference");
            out.println("    private "+getName()+" "+DataHelper.getCamelCaseName(getName())+";");
            out.println();
            out.println("}");
            out.close();
        } catch (Exception e) {
            getLog().error(e);
        }
    }
    
    private void buildMavenDir(File path) {
        File srcDir = new File(path, "src");
        srcDir.mkdir();
        File mainDir = new File(srcDir, "main");
        mainDir.mkdir();
        File a = new File(mainDir, "java");
        a.mkdir();
        File r = new File(mainDir, "resources");
        r.mkdir();
        buildFile(r,"application.properties","application.properties");
        
//        File meta = new File(r, "META-INF");
//        meta.mkdir();
//        File sp = new File(meta, "spring");
//        sp.mkdir();
        
        File testDir = new File(srcDir, "test");
        testDir.mkdir();
        File tj = new File(testDir, "java");
        tj.mkdir();
        File tr = new File(testDir, "resources");
        tr.mkdir();
        buildFile(tr,"logback.xml","logback.xml");
    }


    
    private void buildFile(File path,String from,String to){
        path.mkdirs();
        InputStream in = null;
        FileWriter out = null;
        try {
            in = Platform.class.getResourceAsStream(from);
            InputStreamReader reader = new InputStreamReader(in);
            File configFile = new File(path,to);
            out = new FileWriter(configFile);
            IOUtil.copy(reader, out);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            IOUtil.close(out);
            IOUtil.close(in);
        }
    }

    private void buildProviderPom(File path) throws MojoExecutionException {
        InputStream in = null;
        FileWriter out = null;
        try {
            in = Platform.class.getResourceAsStream("boot.provider.pom.xml");
            InputStreamReader reader = new InputStreamReader(in);
            Map<Object, Object> context = new HashMap<Object, Object>();


            context.put("PR_ARTIFACTID", getProjectName());

            InterpolationFilterReader interpolationFilterReader = new InterpolationFilterReader(reader, context, "$", "$");

//          System.out.println(IOUtil.toString(interpolationFilterReader));
            File configFile = new File(path,"pom.xml");
            out = new FileWriter(configFile);
            IOUtil.copy(interpolationFilterReader, out);
            
        } catch (FileNotFoundException e) {
            throw new MojoExecutionException("Failed to get template for config file.", e);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write config file.", e);
        } finally {
            IOUtil.close(out);
            IOUtil.close(in);
        }
    }
   
    private String getProjectPath() {
        if(isTest){
            return "D:\\Workspaces\\damo\\damo-service-test2";
        }else{
            return project.getBasedir().getPath(); 
        }
    }
    private String getName() {
        return System.getProperty("name", "HelloService");        
    }
    
    private String getPackage() {
        return System.getProperty("package", "com.damo.service");        
    }
    
    private String getProjectName(){
        String afid = "damo-service-user";
        if(!isTest){
            afid = project.getArtifactId();
        }
        return afid;
    }
}

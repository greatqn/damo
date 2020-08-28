package com.damo.tools.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;

import com.damo.tools.maven.parse.DataHelper;

@Mojo(name = "newapi", threadSafe = true)
public class NewApiMojo extends AbstractMojo{

	@Parameter(defaultValue = "${project}")
    private MavenProject project;
	
	public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
		
		NewApiMojo pmj = new NewApiMojo();
		pmj.execute();

	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		String action = getAction();
		getLog().info("Start run create api." + action);
		BufferedReader bf = null;
		try {
			String dirPath = getProjectPath() + "/src/main/java/" ;
	        new File(dirPath).mkdirs();
	        
	        genJava(dirPath,action);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.close(bf);
		}
		getLog().info("End run create api.");
	}

	private void genJava(String dirPath, String action) throws FileNotFoundException, UnsupportedEncodingException {
		getLog().info("Start build java "+ action);
		String ac[] = action.split("_");
		PrintWriter out = new PrintWriter(dirPath + action + ".java", "UTF-8");
		out.println("//step 4 service配置 qtyd-rest config-provider your-config.xml add");
		out.println(String.format("<dubbo:reference id=\"%sService\" interface=\"%s.%s.%sService\" />",
				DataHelper.getCamelCaseName(ac[0]),getPackage(),getLevel(),DataHelper.getPascalCaseName(ac[0])));
		out.println("//step 5 api项目里定义接口类");
		out.println("//step 6 provider项目里实现接口类");
		out.println("//step 7 provider项目里 接口配置xml add");
		out.println(String.format("<bean id=\"%sService\" class=\"%s.%s.%sServiceImpl\" />",
				DataHelper.getCamelCaseName(ac[0]),getPackage(),getLevel(),DataHelper.getPascalCaseName(ac[0])));
		out.println(String.format("<dubbo:service interface=\"%s.%s.%sService\" ref=\"%sService\" timeout=\"30000\" retries=\"0\"/>",
				getPackage(),getLevel(),DataHelper.getPascalCaseName(ac[0]),DataHelper.getCamelCaseName(ac[0])));
        out.close();
	}

    private String getAction() {
        return System.getProperty("action", "user_execute");
    }
    private String getPackage() {
        return System.getProperty("package", "com.damo.user");
    }
    private String getLevel() {
        return System.getProperty("level", "service");
    }
    
    private String getProjectPath() {
//        return "E:\\workspace\\qtyd-dubbo-v4\\qtyd-service-test";
    	return project.getBasedir().getPath();
    }
    
}

package com.damo.tools.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import com.damo.tools.maven.script.Platform;

@Mojo(name = "project", threadSafe = true)
public class ProjectInitMojo extends AbstractMojo{

	@Parameter(defaultValue = "${project}")
    private MavenProject project;
	
	private static boolean isTest = false;
	
	public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
		
	    isTest = true;
		ProjectInitMojo pmj = new ProjectInitMojo();
		pmj.execute();

	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Start run Project init..."+getProjectPath());
		
        File baseDir = new File(getProjectPath());

        getLog().info("Start build api.");
        File apiDir = new File(baseDir,getProjectApiName());
        if(!apiDir.exists()){
        	apiDir.mkdir();
        }
        buildApi(apiDir);
        
        getLog().info("Start build consumer.");
        File consumerDir = new File(baseDir,getProjectConsumerName());
        if(!consumerDir.exists()){
        	consumerDir.mkdir();
        }
        buildConsumer(consumerDir);
        
        getLog().info("Start build provider.");
        File providerDir = new File(baseDir,getProjectProviderName());
        if(!providerDir.exists()){
        	providerDir.mkdir();
        }
        buildProvider(providerDir);

        getLog().info("Build Sucess. 添加下列代码到 modules里");
        getLog().info(String.format("<module>%s</module>",getProjectApiName()));
        getLog().info(String.format("<module>%s</module>",getProjectConsumerName()));
        getLog().info(String.format("<module>%s</module>",getProjectProviderName()));
	}

	private void buildApi(File path) throws MojoExecutionException {
		buildApiPom(path);
		buildMavenDir(path);
		buildFile(path,"gitignore",".gitignore");
	}
	
    private void buildProvider(File path) throws MojoExecutionException {
    	buildProviderPom(path);
		buildMavenDir(path);
		buildFile(path,"gitignore",".gitignore");
		buildAssembly(path);
	    File a = new File(path, "src\\main\\resources\\META-INF\\spring");
	    buildFile(a,"provider.pom.xml","provider.pom.xml");
	    File b = new File(path, "src\\test\\resources");
        buildFile(b,"dubbo.properties","dubbo.properties");
	}

	private void buildConsumer(File path) throws MojoExecutionException {
		buildConsumerPom(path);
		buildMavenDir(path);
		buildFile(path,"gitignore",".gitignore");
		File a = new File(path, "src\\main\\resources\\META-INF\\spring");
		buildFile(a,"consumer.dubbo.xml","consumer.dubbo.xml");
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
		buildFile(r,"logback.xml","logback.xml");
        File meta = new File(r, "META-INF");
        meta.mkdir();
        File sp = new File(meta, "spring");
        sp.mkdir();
        
		File testDir = new File(srcDir, "test");
		testDir.mkdir();
		File tj = new File(testDir, "java");
		tj.mkdir();
		File tr = new File(testDir, "resources");
		tr.mkdir();
	}

	private void buildAssembly(File path) throws MojoExecutionException {
		InputStream in = null;
		FileWriter out = null;
		try {
			in = Platform.class.getResourceAsStream("assembly.xml");
			InputStreamReader reader = new InputStreamReader(in);
			Map<Object, Object> context = new HashMap<Object, Object>();
			// context.put("ARTIFACTID", getProjectProviderName());
			// context.put("ARTIFACTID_API", getProjectApiName());
			// context.put("ARTIFACTID_PROVIDER", getProjectProviderName());

			InterpolationFilterReader interpolationFilterReader = new InterpolationFilterReader(reader, context, "$", "$");
//src\main\assembly\assembly.xml
			File mainDir = new File(path, "src/main");
			File a = new File(mainDir, "assembly");
			a.mkdir();

//			System.out.println(IOUtil.toString(interpolationFilterReader));
			File configFile = new File(a,"assembly.xml");
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

	
	private void buildFile(File path,String from,String to){
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
			in = Platform.class.getResourceAsStream("provider.pom.xml");
			InputStreamReader reader = new InputStreamReader(in);
			Map<Object, Object> context = new HashMap<Object, Object>();

			context.put("ARTIFACTID", getProjectProviderName());
			context.put("ARTIFACTID_API", getProjectApiName());
			context.put("ARTIFACTID_PROVIDER", getProjectProviderName());
            context.put("PR_ARTIFACTID", getProjectName());

			InterpolationFilterReader interpolationFilterReader = new InterpolationFilterReader(reader, context, "$", "$");

//			System.out.println(IOUtil.toString(interpolationFilterReader));
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

	private void buildConsumerPom(File path) throws MojoExecutionException {
		InputStream in = null;
		FileWriter out = null;
		try {
			in = Platform.class.getResourceAsStream("consumer.pom.xml");
			InputStreamReader reader = new InputStreamReader(in);
			Map<Object, Object> context = new HashMap<Object, Object>();

			context.put("ARTIFACTID", getProjectConsumerName());
			context.put("ARTIFACTID_API", getProjectApiName());
            context.put("PR_ARTIFACTID", getProjectName());

			InterpolationFilterReader interpolationFilterReader = new InterpolationFilterReader(reader, context, "$", "$");

//			System.out.println(IOUtil.toString(interpolationFilterReader));
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

	private void buildApiPom(File path) throws MojoExecutionException {
		InputStream in = null;
		FileWriter out = null;
		try {
			in = Platform.class.getResourceAsStream("api.pom.xml");
			InputStreamReader reader = new InputStreamReader(in);
			Map<Object, Object> context = new HashMap<Object, Object>();

			context.put("ARTIFACTID", getProjectApiName());
			context.put("PR_ARTIFACTID", getProjectName());
			
			InterpolationFilterReader interpolationFilterReader = new InterpolationFilterReader(reader, context, "$", "$");

//			System.out.println(IOUtil.toString(interpolationFilterReader));
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
            return "D:\\Workspaces\\damo\\damo-service-test";
        }else{
            return project.getBasedir().getPath(); 
        }
    }
    
    private String getProjectApiName(){
    	return getProjectName() + "-api";
    }
    private String getProjectConsumerName(){
    	return getProjectName() + "-consumer";
    }
    private String getProjectProviderName(){
    	return getProjectName() + "-provider";
    }
    private String getProjectName(){
    	String afid = "damo-service-user";
    	if(!isTest){
    	    afid = project.getArtifactId();
        }
    	return afid;
    }
}

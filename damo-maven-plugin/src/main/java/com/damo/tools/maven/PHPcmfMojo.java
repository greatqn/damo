package com.damo.tools.maven;

import com.damo.tools.maven.parse.Column;
import com.damo.tools.maven.parse.DataHelper;
import com.damo.tools.maven.parse.Table;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * 生成ThinkCMF的代码，api,app,veiw;
 */
@Mojo(name = "phpcmf", threadSafe = true)
public class PHPcmfMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject   project;

    private static boolean isTest = false;

    public static void main(String[] args) throws MojoExecutionException, MojoFailureException {

        isTest = true;
        PHPcmfMojo pmj = new PHPcmfMojo();
        pmj.execute();

    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        String file = getFile();
        String prefix = getPrefix();
        DataHelper.setPrefix(prefix);

        getLog().info("Start run phpcmf." + file);
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            LinkedHashMap<String, Table> tables = DataHelper.paresTables(bf);

            String packageName = getPackage();
            String dirPath = getProjectPath() + "\\app\\" + packageName.replace('.', '\\') + "\\";
            new File(dirPath).mkdirs();
            String dirApiPath = getProjectPath() + "\\api\\" + packageName.replace('.', '\\') + "\\";
            new File(dirApiPath).mkdirs();
            for (Entry<String, Table> entry : tables.entrySet()) {
                genPhpModel(dirPath + "model\\", packageName, entry.getValue());
                genPhpService(dirPath + "service\\", packageName, entry.getValue());
                genPhpController(dirPath + "controller\\", packageName, entry.getValue());
                genPhpApiController(dirApiPath + "controller\\", packageName, entry.getValue());
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
        getLog().info("Start build java service test:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table) + "ServiceTest";

        PrintWriter out = new PrintWriter(dirPath + className + ".java", "UTF-8");
        out.println("package " + packageName + ".service;");
        out.println();

        out.println("import java.util.Date;");
        out.println("import java.util.List;");
        out.println("import java.math.BigDecimal;");
        out.println("import org.junit.Test;");
        out.println("import org.junit.runner.RunWith;");
        out.println("import org.slf4j.Logger;");
        out.println("import org.slf4j.LoggerFactory;");
        out.println("import org.springframework.beans.factory.annotation.Autowired;");
        out.println("import org.springframework.boot.test.context.SpringBootTest;");
        out.println("import org.springframework.test.context.junit4.SpringRunner;");
        out.println("import " + packageName + ".Application;");
        out.println("import " + packageName + ".domain." + DataHelper.getClassName(table) + ";");
        // out.println("import " + packageName + ".service." + DataHelper.getClassName(table) + "Service;");
        out.println();

        out.println("@RunWith(SpringRunner.class)");
        out.println("@SpringBootTest(classes = Application.class)");
        out.println("public class " + DataHelper.getClassName(table) + "ServiceTest {");
        out.println();

        out.println("private static Logger logger = LoggerFactory.getLogger(" + DataHelper.getClassName(table)
            + "ServiceTest.class);");

        out.println("@Autowired");
        out.println("private " + DataHelper.getServiceName(table) + " "
            + DataHelper.getCamelCaseName(DataHelper.getServiceName(table)) + ";");

        out.println("    @Test");
        out.println("    public void m_Test(){");

        out.println("   "+DataHelper.getClassName(table) + " ent = new " + DataHelper.getClassName(table) + "();");
        for (Entry<String, Column> ent : table.getColumns().entrySet()) {
            out.println(genFieldSetValue(ent.getValue()));
        }
        out.println("");
        out.println("        " + DataHelper.getClassName(table) + " c = "
            + DataHelper.getCamelCaseName(DataHelper.getServiceName(table)) + ".save(ent);");
        out.println("");
        out.println("        logger.info(\"step1:{}\",ent);");
        out.println("        logger.info(\"step1.1:{}\",c);");
        out.println("");
        Column keyCol = getKeyColumn(table);
        out.println("        "+DataHelper.getJavaVariableType(keyCol.getType())+" id = c.get"+DataHelper.getJaveName(keyCol.getName())+"();");
        
        out.println("        List<" + DataHelper.getClassName(table) + "> list = "
            + DataHelper.getCamelCaseName(DataHelper.getServiceName(table)) + ".findAll();");
        out.println("        logger.info(\"step2:{}\",list);");
        
        out.println("        "+ DataHelper.getCamelCaseName(DataHelper.getServiceName(table)) + ".delete(id);");
        
        out.println("        logger.info(\"step3:count={}\","+ DataHelper.getCamelCaseName(DataHelper.getServiceName(table)) + ".count());");
        
        out.println("    }");
        out.println(" }");
        out.close();
    }

    private void genPhpController(String dirPath, String packageName, Table table)
        throws FileNotFoundException, UnsupportedEncodingException {
        getLog().info("Start build php controller:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table);
        String entName = DataHelper.getCamelCaseName(className);

        PrintWriter out = new PrintWriter(dirPath + className + "Controller.php", "UTF-8");
        out.println("<?php\n" +
                "// +----------------------------------------------------------------------\n" +
                "// | Damo build \n" +
                "// +----------------------------------------------------------------------");
        out.println("namespace app\\"+ packageName +"\\controller;");
        out.println();
        out.println("use cmf\\controller\\AdminBaseController;");
        out.println("use app\\"+ packageName +"\\model\\"+className+"Model;");
        out.println("use app\\"+ packageName +"\\service\\"+className+"Service;");
        out.println("use think\\Db;");
        out.println("use app\\admin\\model\\ThemeModel;");
        out.println();

        out.println("class "+ className +"Controller extends AdminBaseController");
        out.println("{");
        out.println("public function index()");
        out.println("{");
        out.println("        $content = hook_one('portal_admin_article_index_view');");
        out.println();
        out.println("        if (!empty($content)) {");
        out.println("            return $content;");
        out.println("        }");
        out.println("        $param = $this->request->param();");
        out.println("        $"+entName+"Service = new "+className+"Service();");
        out.println("        $data        = $"+entName+"Service->list($param);");
        out.println("        $data->appends($param);");
        out.println("        $this->assign('page', $data->render());");
        out.println("        return $this->fetch();");
        out.println("}");
        out.println("public function add()");
        out.println("{");
        out.println("}");
        out.println("public function add"+className+"()");
        out.println("{");
        out.println("}");
        out.println("public function edit()");
        out.println("{");
        out.println("}");
        out.println("public function edit"+className+"()");
        out.println("{");
        out.println("}");
        out.println("public function delete()");
        out.println("{");
        out.println("}");

        out.println("}");
        out.close();

    }

    private void genPhpApiController(String dirPath, String packageName, Table table)
            throws FileNotFoundException, UnsupportedEncodingException {
        getLog().info("Start build php controller:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table);
        String entName = DataHelper.getCamelCaseName(className);

        PrintWriter out = new PrintWriter(dirPath + className + "Controller.php", "UTF-8");
        out.println("<?php\n" +
                "// +----------------------------------------------------------------------\n" +
                "// | Damo build \n" +
                "// +----------------------------------------------------------------------");
        out.println("namespace api\\"+ packageName +"\\controller;");
        out.println();
        out.println("use api\\"+ packageName +"\\model\\"+className+"Model;");
        out.println("use api\\"+ packageName +"\\service\\"+className+"Service;");
        out.println("use think\\Db;");
        out.println("use cmf\\controller\\RestBaseController;");
        out.println();

        out.println("class "+ className +"Controller extends RestBaseController");
        out.println("{");
        out.println("public function index()");
        out.println("{");
        out.println("        $param = $this->request->param();");
        out.println("        $"+entName+"Service = new "+className+"Service();");
        out.println("        $data        = $"+entName+"Service->list($param);");
        out.println("        $data->appends($param);");
        out.println("        $this->success('请求成功!', $data);");
        out.println("}");
        out.println("public function save()");
        out.println("{");
        out.println("}");
        out.println("public function read($id)");
        out.println("{");
        out.println("}");
        out.println("public function update($id)");
        out.println("{");
        out.println("}");
        out.println("public function delete($id)");
        out.println("{");
        out.println("}");
        out.println("public function search()");
        out.println("{");
        out.println("}");

        out.println("}");
        out.close();

    }

    private void genPhpService(String dirPath, String packageName, Table table)
        throws FileNotFoundException, UnsupportedEncodingException {
        getLog().info("Start build php service:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table);

        PrintWriter out = new PrintWriter(dirPath + className + "Service.php", "UTF-8");
        out.println("<?php\n" +
                "// +----------------------------------------------------------------------\n" +
                "// | Damo build \n" +
                "// +----------------------------------------------------------------------");

        out.println("namespace app\\" + packageName + "\\service;");
        out.println();
        out.println("use app\\"+ packageName +"\\model\\"+ className +"Model;");
        out.println("use think\\db\\Query;");

        out.println("class "+ className +"Service");
        out.println("{");
        out.println("public function list($filter, $isPage = false)");
        out.println("{");
        out.println("        $join = [");
        out.println("            ['__USER__ u', 'a.user_id = u.id']");
        out.println("         ];");
        out.println("        $field = 'a.*,u.user_login,u.user_nickname,u.user_email';");
        out.println("        $portalPostModel = new PortalPostModel();");
        out.println("        $articles        = $portalPostModel->alias('a')->field($field)");
        out.println("        ->join($join)");
        out.println("        ->where('a.create_time', '>=', 0)");
        out.println("        ->where('a.delete_time', 0)");
        out.println("        ->where(function (Query $query) use ($filter, $isPage) {");
        out.println("                $keyword = empty($filter['keyword']) ? '' : $filter['keyword'];");
        out.println("                if (!empty($keyword)) {");
        out.println("                    $query->where('a.post_title', 'like', \"%$keyword%\");");
        out.println("                }");
        out.println("                })");
        out.println("        ->order('update_time', 'DESC')");
        out.println("        ->paginate(10);");
        out.println("        return $articles;");
        out.println("}");
        out.println("}");
        out.close();

    }

    private void genPhpModel(String dirPath, String packageName, Table table)
        throws FileNotFoundException, UnsupportedEncodingException {
        getLog().info("Start build php model:" + table.getName() + " path:" + dirPath);
        createFile(dirPath);
        String className = DataHelper.getClassName(table);

        PrintWriter out = new PrintWriter(dirPath + className + "Model.php", "UTF-8");
        out.println("<?php\n" +
                "// +----------------------------------------------------------------------\n" +
                "// | Damo build \n" +
                "// +----------------------------------------------------------------------");

        out.println("namespace app\\" + packageName + "\\model;");
        out.println();
        out.println("use app\\admin\\model\\RouteModel;");
        out.println("use think\\Model;");
        out.println("use think\\Db;");
        out.println();
        out.println("/**\n" +
                " * @property mixed id\n" +
                " */");
        out.println("class "+ className +"Model extends Model");
        out.println("{");
        out.println();
        out.println("    protected $type = [\n" +
                "        'more' => 'array',\n" +
                "    ];\n" +
                "\n" +
                "    // 开启自动写入时间戳字段\n" +
                "    protected $autoWriteTimestamp = true;");
        out.println();
        out.println("    public function add"+ className +"($data, $categories)");
        out.println("    {");
        out.println("        $data['user_id'] = cmf_get_current_admin_id();");
        out.println("        $this->allowField(true)->data($data, true)->isUpdate(false)->save();");
        out.println("        return $this;");
        out.println("    }");
        out.println();
        out.println("    public function edit"+ className +"($data, $categories)");
        out.println("    {");
        out.println("        unset($data['user_id']);");
        out.println("        $this->allowField(true)->isUpdate(true)->data($data, true)->save();");
        out.println("        return $this;");
        out.println("    }");
        out.println();
        out.println("    public function del"+ className +"($data, $categories)");
        out.println("    {");
        out.println("        if (isset($data['id'])) {");
        out.println("            $id = $data['id']; //获取删除id");
        out.println("            $res = $this->where('id', $id)->find();");
        out.println("            if ($res) {");
        out.println("                $res = json_decode(json_encode($res), true); //转换为数组");
        out.println("                $recycleData = [\n" +
                "                    'object_id'   => $res['id'],\n" +
                "                    'create_time' => time(),\n" +
                "                    'table_name'  => 'portal_post#page',\n" +
                "                    'name'        => $res['post_title'],\n" +
                "\n" +
                "                ];");
        out.println("                Db::startTrans(); //开启事务");
        out.println("                $transStatus = false;");
        out.println("                try {");
        out.println("                    Db::name('portal_post')->where('id', $id)->update([");
        out.println("                    'delete_time' => time()");
        out.println("                 ]);");
        out.println("                 Db::name('recycle_bin')->insert($recycleData);");
        out.println("                 $transStatus = true;");
        out.println("                 Db::commit();");
        out.println("             } catch (\\Exception $e) {");
        out.println("                 Db::rollback();");
        out.println("             }");
        out.println("             return $transStatus;");
        out.println("        }");
        out.println("        return false;");
        out.println("    }");
        out.println();
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
        return System.getProperty("prefix", "cmf_portal_");
    }
    
    private String getPackage() {
        return System.getProperty("package", "portal");
    }

    private String getDBname() {
        return System.getProperty("dbname", "portal");
    }
    
    private String getProjectPath() {
        if (isTest) {
            return "D:\\Workspaces\\p1";
        } else {
            return project.getBasedir().getPath();
        }
    }
}

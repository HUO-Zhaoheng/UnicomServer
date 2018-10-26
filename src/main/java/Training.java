import jdk.internal.util.xml.impl.ReaderUTF8;
//乱码，先中止处理
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
public class Training extends HttpServlet {
    final static String projectRoot="E:\\apache-tomcat-9.0.11\\webapps\\Unicom\\";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        InputStream inputStream=request.getInputStream();

        File out=new File(projectRoot+"resources\\casualty_for_compression.csv");

        if(!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        out.createNewFile();
        FileWriter fileWriter=new FileWriter(out);
        int charInt;
        while ((charInt=inputStream.read())!=-1) {
            fileWriter.write(charInt);
        }
        fileWriter.close();
        //将上传的工单文件按工单号合并
        //Compress.compress();
    }
}

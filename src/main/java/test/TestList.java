package test;

import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.util.LinkedList;

@WebServlet("/test.TestList")
public class TestList extends HttpServlet {

    private static LinkedList<WorkOrder> workOrders=new LinkedList<>();

    public TestList() {
        super();
        workOrders.add(new WorkOrder("2018.9.9", "ToiletBulb", "R123", "S123", "Broken"));

    }


    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServerException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(response.SC_OK);
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter printWriter = response.getWriter();

        ObjectMapper mapperObj = new ObjectMapper();
        String jsonStr=mapperObj.writeValueAsString(workOrders);
        System.out.println(jsonStr);


        printWriter.println(jsonStr);



    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

class WorkOrder {
    public String time;
    public String item;
    public String repairerID;
    public String studentID;
    public String description;

    public WorkOrder(String time, String item, String repairerID, String studentID, String description) {
        this.time = time;
        this.item = item;
        this.repairerID = repairerID;
        this.studentID = studentID;
        this.description = description;
    }

}
package org.Ifn653.ArchiveLoanApplication;
import java.sql.*;
import org.jdom2.Element;
import org.yawlfoundation.yawl.elements.data.YParameter;
import org.yawlfoundation.yawl.engine.interfce.WorkItemRecord;
import org.yawlfoundation.yawl.engine.interfce.interfaceB.InterfaceBWebsideController;
import org.yawlfoundation.yawl.exceptions.YAWLException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ArchiveLoanApplication extends InterfaceBWebsideController {
    private final String USER = "akash";            //Service username
    private final String PASSWORD = "akash";        //Service password
    static final String USERDB = "";              //DB username
    static final String PASS = "";                  //DB password
    static final String JDBC_DRIVER = "org.h2.Driver";  //JDBC Driver
    //static final String DB_URL = " jdbc:h2:\\\\C:\\database\\yawldb;AUTO_SERVER=TRUE";      //DB URL
    static final String DB_URL = "jdbc:h2:C:/Users/Lenovo Ideapad/Documents/YAWL-4.5/engine/apache-tomcat-7.0.65/database/yawldb";      //DB URL
    private String handle;
    @Override
    public void handleEnabledWorkItemEvent(WorkItemRecord workItemRecord) {
        try {
            if (!connected()) handle = connect(USER, PASSWORD);
            WorkItemRecord wir = checkOut(workItemRecord.getID(), handle);
            String Status = getStatus(wir.getDataList(), "Status");
            String Comments = getComments(wir.getDataList(),"Comments");
            String LoanApplicationID = getLoanID(wir.getDataList(), "LoanApplicationID");
            String SubmissionDate = getSubmissionDate(wir.getDataList(), "SubmissionDate");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            String CurrentDate = formatter.format(LocalDate.now());
            Connection conn = null;
            Statement stmt;
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USERDB,PASS);
            stmt = conn.createStatement();
            String sql =  "INSERT INTO LOANAPPID " + "VALUES ('"+LoanApplicationID+"','"+CurrentDate+"','"+SubmissionDate+"','"+Status+"','"+Comments+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (IOException | YAWLException /*| JDOMException*/ | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void handleCancelledWorkItemEvent(WorkItemRecord wir) {
    }
    // these parameters are automatically inserted (in the Editor) into a task
    // decomposition when this service is selected from the list
    public YParameter[] describeRequiredParams() {
        YParameter[] parameters = new YParameter[3];
        YParameter parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "Status", XSD_NAMESPACE);
        parameters[1] = parameter;
        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "Comments", XSD_NAMESPACE);
        parameters[2] = parameter;
        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "LoanApplicationID", XSD_NAMESPACE);
        parameters[3] = parameter;
        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "SubmissionDate", XSD_NAMESPACE);
        parameters[4] = parameter;
        return parameters;
    }

    private String getStatus(Element datalist,String Status){    // status declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(Status);
    }
    private String getComments(Element datalist,String Comments){       // comments declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(Comments);
    }
    private String getLoanID(Element datalist,String LoanApplicationID){    // Loan id declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(LoanApplicationID);
    }
    private String getSubmissionDate(Element datalist, String SubmissionDate){    // Submission date declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(SubmissionDate);
    }
    private boolean connected() throws IOException{
        return handle != null && checkConnection(handle);
    }
}
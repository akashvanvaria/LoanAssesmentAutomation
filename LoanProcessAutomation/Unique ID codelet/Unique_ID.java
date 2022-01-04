// Author Name- Urusa Zack
// Roll number N10667814
package org.ifn653.Unique_ID;
import org.jdom2.Element;
import org.yawlfoundation.yawl.elements.YDecomposition;
import org.yawlfoundation.yawl.elements.data.YParameter;
import org.yawlfoundation.yawl.resourcing.codelets.AbstractCodelet;
import org.yawlfoundation.yawl.resourcing.codelets.CodeletExecutionException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;



public class Unique_ID extends AbstractCodelet {
    public Unique_ID() {
        this.setDescription("Calculate the repayment amount");
    }

    public Element execute(Element inData, List<YParameter> inParams, List<YParameter> outParams) throws CodeletExecutionException {
        this.setInputs(inData, inParams, outParams);


        Date SubmissionDate = (Date) this.getParameterValue("SubmissionDate");
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM");
        String strMonth= formatter.format(SubmissionDate);
        SimpleDateFormat formatYear = new SimpleDateFormat("YY");
        String strYear= formatYear.format(SubmissionDate);

        String caseid = getWorkItem().getRootCaseID();
        String rand = String.format("%05d", Integer.parseInt(caseid));
        // switch case or else if Alphabets to Month

        String modifiedstrMonth = null;
        if (strMonth.equalsIgnoreCase("January"))
            modifiedstrMonth = "A";
        else if (strMonth.equalsIgnoreCase("February"))
            modifiedstrMonth = "B";
        else if (strMonth.equalsIgnoreCase("March"))
            modifiedstrMonth = "C";
        else if (strMonth.equalsIgnoreCase("April"))
            modifiedstrMonth = "D";
        else if (strMonth.equalsIgnoreCase("May"))
            modifiedstrMonth = "E";
        else if (strMonth.equalsIgnoreCase("June"))
            modifiedstrMonth = "F";
        else if (strMonth.equalsIgnoreCase("July"))
            modifiedstrMonth = "G";
        else if (strMonth.equalsIgnoreCase("August"))
            modifiedstrMonth = "H";
        else if (strMonth.equalsIgnoreCase("September"))
            modifiedstrMonth = "I";
        else if (strMonth.equalsIgnoreCase("October"))
            modifiedstrMonth = "J";
        else if (strMonth.equalsIgnoreCase("November"))
            modifiedstrMonth = "K";
        else if (strMonth.equalsIgnoreCase("December"))
            modifiedstrMonth = "L";
        else
            modifiedstrMonth = "Invalid Input is given";

        Date DOB = (Date) this.getParameterValue("DOB");
        long age = Math.abs(SubmissionDate.getYear() - DOB.getYear());
        String LoanApplicationID = rand + "/" + modifiedstrMonth +  "/" + strYear +"/" + age;
        this.setParameterValue("LoanApplicationID", LoanApplicationID);
        return this.getOutputData();
    }


    public List<YParameter> getRequiredParams() {
        List<YParameter>parameters= new ArrayList<>();
        YParameter parameter = new YParameter((YDecomposition)null, 0);
        parameter.setDataTypeAndName("date","SubmissionDate","http://www.w3.org/2001/XMLSchema");
        parameter.setDocumentation("Submission date:");
        parameters.add(parameter);
        parameter = new YParameter((YDecomposition)null, 0);
        parameter.setDataTypeAndName("date","DOB","http://www.w3.org/2001/XMLSchema");
        parameter.setDocumentation("Enter Birth date:");
        parameters.add(parameter);
        parameter = new YParameter((YDecomposition)null, 1);
        parameter.setDataTypeAndName("string","LoanApplicationID","http://www.w3.org/2001/XMLSchema");
        parameter.setDocumentation("ID:");
        parameters.add(parameter);
        return parameters;

    }
}
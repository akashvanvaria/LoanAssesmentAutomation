// AUthor Name- Aakash Jhadhav 
// Student ID - N10726888
package org.Ifn653.RiskAssesment;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.yawlfoundation.yawl.elements.data.YParameter;
import org.yawlfoundation.yawl.engine.interfce.WorkItemRecord;
import org.yawlfoundation.yawl.engine.interfce.interfaceB.InterfaceBWebsideController;
import org.yawlfoundation.yawl.exceptions.YAWLException;

import java.io.IOException;


public class RiskAssesment extends InterfaceBWebsideController {

    private final String USER = "akash";            //Service username
    private final String PASSWORD = "akash";        //Service password
    private String handle;

    @Override
    public void handleEnabledWorkItemEvent(WorkItemRecord workItemRecord) {
        try {
            if (!connected()) handle = connect(USER, PASSWORD);
            WorkItemRecord wir = checkOut(workItemRecord.getID(), handle);
            Integer RiskWeight = getInputs(wir);
            checkInWorkItem(wir.getID(), wir.getDataList(),
                    getOutputData(wir.getTaskID(), RiskWeight), null, handle);

        } catch (IOException | YAWLException | JDOMException e) {
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
        parameter.setDataTypeAndName("string", "PurchasePrice", XSD_NAMESPACE);//
        parameters[1] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "LoanAmount", XSD_NAMESPACE);//
        parameters[2] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "Current", XSD_NAMESPACE);//
        parameters[3] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "MonthlyRepayments", XSD_NAMESPACE);//
        parameters[4] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "CourtJudgementInformation", XSD_NAMESPACE);//
        parameters[5] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "PreviousAddressSum", XSD_NAMESPACE);//
        parameters[6] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "EstimatedPropertyMarketValue", XSD_NAMESPACE);//
        parameters[7] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "CreditAssessmentResult", XSD_NAMESPACE);//
        parameters[8] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "CurrentCustomer", XSD_NAMESPACE);//
        parameters[9] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "NumberOfPreviousAddress", XSD_NAMESPACE);
        parameters[10] = parameter;

        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "MonthlyNetSalary", XSD_NAMESPACE);
        parameters[11] = parameter;
        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "MonthlyNetIncome", XSD_NAMESPACE);
        parameters[12] = parameter;
        parameter = new YParameter(null, YParameter._INPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "ExcludingExistingLoans", XSD_NAMESPACE);
        parameters[13] = parameter;

        parameter = new YParameter(null, YParameter._OUTPUT_PARAM_TYPE);
        parameter.setDataTypeAndName("string", "RiskWeight", XSD_NAMESPACE);
        parameters[14] = parameter;
        return parameters;
    }



    private Element getOutputData(String taskID, int RiskWeight) {
        Element root = new Element(taskID);
        Element inner = new Element("RiskWeight");
        inner.setText(String.valueOf(RiskWeight));
        root.setContent(inner);
        return root;
    }

    private int getInputs(WorkItemRecord wir) throws IOException {
        Double PurchasePrice = Double.parseDouble(getPurchasePrice(wir.getDataList(), "PurchasePrice"));
        Double LoanAmount = Double.parseDouble(getLoanAmount(wir.getDataList(),"LoanAmount"));
        Double MonthlyRepaymentAmount = Double.parseDouble(getMonthlyLoanRepayement(wir.getDataList(),"MonthlyRepaymentAmount"));
        Integer CourtJudgementInformation = Integer.parseInt(getCourtJudgementInfo(wir.getDataList(),"CourtJudgementInformation"));
        Double PreviousAddressSum = Double.parseDouble(getSurroundingProperty(wir.getDataList(),"PreviousAddressSum"));
        String CreditAssessmentResult = getCreditAssessment(wir.getDataList(),"CreditAssessmentResult");
        String CurrentCustomer = getBankCurrentCustomer(wir.getDataList(),"CurrentCustomer");
        Double EstimatedPropertyMarketValue = Double.parseDouble(getEstimatedPropertyMarketValue(wir.getDataList(),"EstimatedPropertyMarketValue"));
        String Current = (getBankruptcyDetails(wir.getDataList(),"Current"));
        Integer NumberOfPreviousAddress = Integer.parseInt(getPreviousAddressNumber(wir.getDataList(),"NumberOfPreviousAddress"));
        Double MonthlyNetSalary = Double.parseDouble(getMonthlyNetSalary(wir.getDataList(), "MonthlyNetSalary"));
        Double ExcludingExistingLoans = Double.parseDouble(getExcludingExistingLoans(wir.getDataList(), "ExcludingExistingLoans"));
        Double MonthlyNetIncome = Double.parseDouble(getMonthlyNetIncome(wir.getDataList(), "MonthlyNetIncome"));
        double DepositPaid = PurchasePrice - LoanAmount;
        Double MonthlyAvailableIncome = (MonthlyNetSalary + MonthlyNetIncome) - ExcludingExistingLoans;
        int RiskWeight = 100;
        if(DepositPaid<=PurchasePrice*10/100)
        {
            RiskWeight = RiskWeight+ 100;
        }
        else if (PurchasePrice*30/100>=DepositPaid && DepositPaid>=PurchasePrice*10/100)
        {
            RiskWeight = RiskWeight +5;
        }
        if (Current == "true")
        {
            RiskWeight =RiskWeight+100;
        }
        if (MonthlyRepaymentAmount>50/100*MonthlyAvailableIncome)
        {
            RiskWeight = RiskWeight+100;
        }
        else if (MonthlyRepaymentAmount<=50/100*MonthlyAvailableIncome && MonthlyAvailableIncome>=30/100*MonthlyAvailableIncome)
        {
            RiskWeight = RiskWeight +3;
        }
        if (NumberOfPreviousAddress>=2)
        {
            RiskWeight=RiskWeight*NumberOfPreviousAddress;
        }

        if (CourtJudgementInformation==1)
        {
            RiskWeight = RiskWeight + 10;
        }
        else if (CourtJudgementInformation>=2)
        {
            RiskWeight = RiskWeight+35;
        }
        else if (CourtJudgementInformation>=3)
        {
            RiskWeight = RiskWeight+50;
        }

        if (EstimatedPropertyMarketValue<PreviousAddressSum)
        {
            RiskWeight = RiskWeight+35;
        }
        else if (EstimatedPropertyMarketValue>=PreviousAddressSum)
        {
            RiskWeight = RiskWeight-10;
        }
        if (CreditAssessmentResult=="AAA")
        {
            RiskWeight= RiskWeight-10;
        }
        else if (CreditAssessmentResult=="A")
        {
            RiskWeight= RiskWeight+5;
        }
        else if (CreditAssessmentResult=="BBB")
        {
            RiskWeight= RiskWeight+10;
        }
        else if (CreditAssessmentResult=="BB")
        {
            RiskWeight= RiskWeight+20;
        }
        else if (CreditAssessmentResult=="B")
        {
            RiskWeight= RiskWeight+30;
        }
        else if (CurrentCustomer=="true")
        {
            RiskWeight= RiskWeight - 15;
        }
        if(RiskWeight>100)
        {
            RiskWeight = 100;
        }

        return RiskWeight;
    }


    //    private Integer getPreviousAddress(Element datalist, int ){    // status declaration
//        datalist.getChildText("PreviousAddress");
//        int size = datalist.getContentSize();
//        return size;
//    }
    private String getPurchasePrice(Element datalist,String PurchasePrice){    // status declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(PurchasePrice);

    }

    private String getLoanAmount(Element datalist,String LoanAmount){    // status declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(LoanAmount);

    }

    private String getBankruptcyDetails(Element datalist,String Current){    // status declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(Current);

    }

    private String getMonthlyLoanRepayement(Element datalist,String MonthlyRepaymentAmount){    // status declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(MonthlyRepaymentAmount);

    }

    private String getMonthlyNetSalary(Element datalist,String MonthlyNetSalary){    // status declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(MonthlyNetSalary);

    }
    private String getExcludingExistingLoans(Element datalist,String ExcludingExistingLoans){    // status declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(ExcludingExistingLoans);

    }
    private String getMonthlyNetIncome(Element datalist,String MonthlyNetIncome){    // status declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(MonthlyNetIncome);

    }

    private String getCourtJudgementInfo(Element datalist,String CourtJudgementInformation){    // status declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(CourtJudgementInformation);

    }
    private String getSurroundingProperty(Element datalist,String PreviousAddressSum){       // comments declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(PreviousAddressSum);

    }
    private String getEstimatedPropertyMarketValue(Element datalist,String EstimatedPropertyMarketValue){    // Loan id declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(EstimatedPropertyMarketValue);

    }
    private String getCreditAssessment(Element datalist, String CreditAssessmentResult){    // Current date declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(CreditAssessmentResult);

    }
    private String getBankCurrentCustomer(Element datalist, String CurrentCustomer){    // Submission date declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(CurrentCustomer);

    }
    private String getPreviousAddressNumber(Element datalist, String NumberOfPreviousAddress){    // Submission date declaration
        if(datalist==null){
            return "error: no currency defined";
        }
        return datalist.getChildText(NumberOfPreviousAddress);

    }


    private boolean connected() throws IOException{
        return handle != null && checkConnection(handle);
    }


}
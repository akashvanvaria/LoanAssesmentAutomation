//Author= Harshit Tyagi
// Student ID- N10644571

package org.ifn653.Unique_ID;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;
import org.yawlfoundation.yawl.elements.YDecomposition;
import org.yawlfoundation.yawl.elements.data.YParameter;
import org.yawlfoundation.yawl.resourcing.codelets.AbstractCodelet;
import org.yawlfoundation.yawl.resourcing.codelets.CodeletExecutionException;

public class RepaymentAmountCalculator extends AbstractCodelet {
    public RepaymentAmountCalculator() {
        this.setDescription("Calculate the repayment amount");
    }

    public Element execute(Element inData, List<YParameter> inParams, List<YParameter> outParams) throws CodeletExecutionException {
        this.setInputs(inData, inParams, outParams);
        int NumberOfMonths;
        try {
            NumberOfMonths = Integer.parseInt((String) getParameterValue("NumberOfMonths"));
        } catch (ClassCastException cce) {
            throw new CodeletExecutionException("Exception casting input values to " +
                    "int types.");
        }

        Double LoanAmount = (Double) this.getParameterValue("LoanAmount");
        setParameterValue("LoanAmount", String.valueOf(LoanAmount));

        Double InterestRate = (Double) this.getParameterValue("InterestRate");
        setParameterValue("InterestRate", String.valueOf(InterestRate));

        double MonthlyRepaymentAmount;
        MonthlyRepaymentAmount= LoanAmount * InterestRate/1200*((Math.pow((1 + InterestRate/1200), NumberOfMonths)) / (Math.pow((1 + InterestRate/1200), NumberOfMonths) - 1));
        this.setParameterValue("MonthlyRepaymentAmount", String.valueOf(MonthlyRepaymentAmount));
        return this.getOutputData();

    }


    public List<YParameter> getRequiredParams() {
        List<YParameter>parameters= new ArrayList<>();
        YParameter parameter = new YParameter((YDecomposition)null, 0);
        parameter.setDataTypeAndName("double","LoanAmount","http://www.w3.org/2001/XMLSchema");
        parameter.setDocumentation("Enter the amount of loan requested:");
        parameters.add(parameter);
        parameter = new YParameter((YDecomposition)null, 0);
        parameter.setDataTypeAndName("double","InterestRate","http://www.w3.org/2001/XMLSchema");
        parameter.setDocumentation("Enter the monthly interest rate in percentage:");
        parameters.add(parameter);
        parameter = new YParameter((YDecomposition)null, 0);
        parameter.setDataTypeAndName("int","NumberOfMonths","http://www.w3.org/2001/XMLSchema");
        parameter.setDocumentation("Enter the total number of months in the repayment period");
        parameters.add(parameter);
        parameter = new YParameter((YDecomposition)null, 1);
        parameter.setDataTypeAndName("double","MonthlyRepaymentAmount","http://www.w3.org/2001/XMLSchema");
        parameter.setDocumentation("Repayment Amount");
        parameters.add(parameter);
        return parameters;

    }
}






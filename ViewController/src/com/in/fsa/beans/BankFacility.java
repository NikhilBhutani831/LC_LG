package com.in.fsa.beans;

import com.in.fsa.Pojo.DATA_DS;
import com.in.fsa.Pojo.G_1;
import com.in.fsa.Pojo.ProjectReportResponse;
import com.in.fsa.Pojo.ProjectReportResponseType;
import com.in.fsa.Pojo.ReportsService;
import com.in.fsa.utils.JSFUtils;

import com.oracle.xmlns.oxp.service.publicreportservice.AccessDeniedException_Exception;
import com.oracle.xmlns.oxp.service.publicreportservice.InvalidParametersException_Exception;
import com.oracle.xmlns.oxp.service.publicreportservice.OperationFailedException_Exception;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import java.util.HashSet;
import java.util.List;

import java.util.Map;

import java.util.Set;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.domain.Number;


import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.model.SelectItem;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.event.PopupCanceledEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;


import javax.faces.application.FacesMessage;


import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;

import javax.xml.bind.JAXBException;

import oracle.adf.view.rich.context.AdfFacesContext;


import oracle.adfinternal.view.faces.taglib.listener.ResetActionListener;

import oracle.jbo.domain.Date;

import org.apache.myfaces.trinidad.component.UIXEditableValue;


public class BankFacility {
  List<SelectItem> facilityList;
  private RichPanelFormLayout facilityBasicForm;
  private RichInputText docNum;
  private RichInputText searchInputText;
  private RichInputText noExtends;
  private RichInputDate expryDateBind;
  private RichInputDate facDateBind;
  Map<String, Number> facilityAmountMap;
  private RichTable syndctnTable;
  private RichInputText facInputBind;
  private RichOutputText hiddenOutputBind;
  List<SelectItem> bankList;
  List<SelectItem> projectNamesList;
  List<G_1> banksDataList;
  List<ProjectReportResponseType> projectsDataList;
  private RichSelectOneChoice bankNameLov;
  List<SelectItem> branchNameList;
  List<SelectItem> projectNumberList;
  private RichSelectOneChoice branchNameLov;
  List<SelectItem> accountNameList;
  private RichSelectOneChoice accntNameLov;
  private RichSelectOneChoice crrncyCodeLov;
  List<SelectItem> currencyCodeList;
  List<SelectItem> accountNumberList;
  private RichSelectOneChoice accountNumberLov;
  private RichOutputText hiddenProjectOutputBind;
  private RichSelectOneChoice projectNameLov;
  private RichSelectOneChoice projectNumberLov;
  private RichSelectOneChoice buBindVar;
  private RichInputText prcntgeBind;
  private RichInputText isCompleteBind;
  private RichInputDate amendFacDateBind;
  private RichInputDate amendExpDateBind;
  HashMap<String, Integer> inverseFacTypeMap;
  private RichTable facilityFinancialTableBind;
  private RichSelectOneChoice facFinFacTypeBind;

  public BankFacility() {
  }


  public void saveFacility(ActionEvent actionEvent) {
    DCBindingContainer bindings = getBindings();
    DCIteratorBinding financialVoBinding =
      bindings.findIteratorBinding("FacilityFinancialVO2Iterator");
    ViewObject financialVo = financialVoBinding.getViewObject();
    RowSetIterator rsIteratorFinVo = financialVo.createRowSetIterator(null);
    Double totalAmount = 0.0;
    while (rsIteratorFinVo.hasNext()) {
      Row finRow = rsIteratorFinVo.next();
      if (finRow.getAttribute("AmountExtd") != null) {
        Number amtExtnded = (Number)finRow.getAttribute("AmountExtd");
        totalAmount = totalAmount + amtExtnded.getValue();
      } else if (finRow.getAttribute("AmountOrg") != null) {
        Number amtOrg = (Number)finRow.getAttribute("AmountOrg");
        totalAmount = totalAmount + amtOrg.getValue();
      }
    }
    Number facilityTotalAmount =
      (Number)JSFUtils.resolveExpression("#{bindings.TotalAmount.inputValue}");
    String facNum =
      (String)JSFUtils.resolveExpression("#{bindings.FacilityNumber.inputValue}");
    if (facNum == null || "".equals(facNum)) {
      showMessage("Please enter a facility number");
      throw new AbortProcessingException("Invalid");
    }
    if ((facilityTotalAmount != null && totalAmount != null) &&
        (totalAmount > facilityTotalAmount.getValue())) {
      showMessage("Sum of components can't be greater than Total Facility Amount");
      throw new AbortProcessingException("Invalid");
    }
    OperationBinding operation = bindings.getOperationBinding("saveorUpdate");
    operation.execute();
    ResetActionListener ral = new ResetActionListener();
    ral.processAction(actionEvent);
    AdfFacesContext.getCurrentInstance().addPartialTarget(getFacilityBasicForm());
    // refreshPage();
  }


  //  public void saveFacility(ActionEvent actionEvent) {
  //    DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
  //    DCIteratorBinding obj = bindings.findIteratorBinding("FacilityTxnVO1Iterator");
  //    ViewObject facilityTxnVO= obj.getViewObject();
  //    RowSetIterator rsIterator=facilityTxnVO.createRowSetIterator(null);
  //    Row[] allRows= rsIterator.getAllRowsInRange();
  //    System.out.println("COUNT"+allRows.length);
  //    Row r=allRows[0];
  //    r.setAttribute("TxnId", "200");
  //    facilityTxnVO.insertRow(r);
  //
  //
  //
  //
  //   DCIteratorBinding obj1 = bindings.findIteratorBinding("FacilityFinancialVO2Iterator");
  //    ViewObject financialVO= obj1.getViewObject();
  //    RowSetIterator finIterator=financialVO.createRowSetIterator(null);
  //    Row[] allRows1= finIterator.getAllRowsInRange();
  //    for(Row p:allRows1) {
  //      p.setAttribute("TxnId", "200");
  //    }
  //    OperationBinding operation = bindings.getOperationBinding("Commit");
  //
  //            operation.execute();
  //
  //  }

  public void setFacilityList(List<SelectItem> facilityList) {
    this.facilityList = facilityList;
  }

  public List<SelectItem> getFacilityList() {
    return facilityList;
  }

  public void facilityPopupFetchLsnr(PopupFetchEvent popupFetchEvent) throws SQLException {
    DCBindingContainer bindings = getBindings();
    DCIteratorBinding facilityLovBinding =
      bindings.findIteratorBinding("FacilityLovVo1Iterator");
    ViewObject facilityLovVo = facilityLovBinding.getViewObject();
    facilityLovVo.executeQuery();
    RowSetIterator rsIterator = facilityLovVo.createRowSetIterator(null);
    HashMap<Integer, String> lovMap = new HashMap<Integer, String>();
    facilityAmountMap = new HashMap<String, Number>();

    while (rsIterator.hasNext()) {
      Row r = rsIterator.next();
      oracle.jbo.domain.Number var =
        (oracle.jbo.domain.Number)(r.getAttribute("Id"));

      lovMap.put(var.intValue(), (String)r.getAttribute("LookupValue"));
    }
    rsIterator.closeRowSetIterator();
    System.out.println("MAP---->>>>>" + lovMap);

    List<SelectItem> facilityList = new ArrayList<SelectItem>();

    DCIteratorBinding financialVOBinding =
      bindings.findIteratorBinding("FacilityFinancialVO2Iterator");
    ViewObject financialVo = financialVOBinding.getViewObject();
    RowSetIterator rsIteratorFinVo = financialVo.createRowSetIterator(null);
    while (rsIteratorFinVo.hasNext()) {
      Row r = rsIteratorFinVo.next();
      Integer facValue =
        Integer.parseInt(r.getAttribute("FacilityType").toString());
      facilityList.add(new SelectItem(lovMap.get(facValue),
                                      lovMap.get(facValue)));

      if (r.getAttribute("AmountExtd") != null) {
        facilityAmountMap.put(lovMap.get(facValue),
                              (Number)r.getAttribute("AmountExtd"));
      } else {
        facilityAmountMap.put(lovMap.get(facValue),
                              (Number)r.getAttribute("AmountOrg"));
      }
    }
    rsIteratorFinVo.closeRowSetIterator();


    setFacilityList(facilityList);
  }

  public DCBindingContainer getBindings() {
    DCBindingContainer bindings =
      (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
    return bindings;
  }

  public void facilityAmtSummPopupFetch(PopupFetchEvent popupFetchEvent) {
    DCBindingContainer bindings = getBindings();
    DCIteratorBinding facilityAmtSummBinding =
      bindings.findIteratorBinding("FacilityAmtSumVO1Iterator");
    ViewObject facilityAmtSummVo = facilityAmtSummBinding.getViewObject();
    System.out.println("TRANSACTION ID FOR AMOUNT SUMMARY----->>>" +
                       JSFUtils.resolveExpression("#{bindings.TxnId.inputValue}"));
    facilityAmtSummVo.setNamedWhereClauseParam("txnIdBindVar",
                                               JSFUtils.resolveExpression("#{bindings.TxnId.inputValue}"));
    facilityAmtSummVo.executeQuery();

    DCIteratorBinding facilityLovBinding =
      bindings.findIteratorBinding("FacilityLovVo1Iterator");
    ViewObject facilityLovVo = facilityLovBinding.getViewObject();
    facilityLovVo.executeQuery();
    RowSetIterator rsIterator = facilityLovVo.createRowSetIterator(null);
    HashMap<Integer, String> lovMap = new HashMap<Integer, String>();
    facilityAmountMap = new HashMap<String, Number>();

    while (rsIterator.hasNext()) {
      Row r = rsIterator.next();
      oracle.jbo.domain.Number var =
        (oracle.jbo.domain.Number)(r.getAttribute("Id"));

      lovMap.put(var.intValue(), (String)r.getAttribute("LookupValue"));
    }
    rsIterator.closeRowSetIterator();
    System.out.println("MAP---->>>>>" + lovMap);

    List<SelectItem> facilityList = new ArrayList<SelectItem>();

    DCIteratorBinding financialVOBinding =
      bindings.findIteratorBinding("FacilityFinancialVO2Iterator");
    ViewObject financialVo = financialVOBinding.getViewObject();
    RowSetIterator rsIteratorFinVo = financialVo.createRowSetIterator(null);
    while (rsIteratorFinVo.hasNext()) {
      Row r = rsIteratorFinVo.next();
      Integer facValue =
        Integer.parseInt(r.getAttribute("FacilityType").toString());
      facilityList.add(new SelectItem(lovMap.get(facValue),
                                      lovMap.get(facValue)));

      if (r.getAttribute("AmountExtd") != null) {
        facilityAmountMap.put(lovMap.get(facValue),
                              (Number)r.getAttribute("AmountExtd"));
      } else {
        facilityAmountMap.put(lovMap.get(facValue),
                              (Number)r.getAttribute("AmountOrg"));
      }
    }
    rsIteratorFinVo.closeRowSetIterator();


    setFacilityList(facilityList);

  }

  public void setFacilityBasicForm(RichPanelFormLayout facilityBasicForm) {
    this.facilityBasicForm = facilityBasicForm;
  }

  public RichPanelFormLayout getFacilityBasicForm() {
    return facilityBasicForm;
  }

  public void setDocNum(RichInputText docNum) {
    this.docNum = docNum;
  }

  public RichInputText getDocNum() {
    return docNum;
  }


  public void searchFacility(ActionEvent actionEvent) {
    DCBindingContainer bindings = getBindings();

    System.out.println("Search text" + searchInputText.getValue());
    String facNum = (String)searchInputText.getValue();
    if (facNum == null || "".equals(facNum)) {
      showMessage("Please enter a value");
      return;
    }

    ResetActionListener ral = new ResetActionListener();
    ral.processAction(actionEvent);
    DCIteratorBinding obj =
      bindings.findIteratorBinding("FacilityHdrVO1Iterator");
    ViewObject facilityHdrVO = obj.getViewObject();
    facilityHdrVO.setNamedWhereClauseParam("facilityNumber", facNum);
    facilityHdrVO.executeQuery();
    RowSetIterator rsIterator = facilityHdrVO.createRowSetIterator(null);
    Row[] hdrRows = rsIterator.getAllRowsInRange();
    System.out.println("HDR Row COunt" + hdrRows.length);
    if (hdrRows.length < 1) {
      showMessage("No Facility Found");
      return;
    }
    DCIteratorBinding obj1 =
      bindings.findIteratorBinding("FacilityTxnVO1Iterator");
    ViewObject facilityTxnVO = obj1.getViewObject();
    facilityTxnVO.executeEmptyRowSet();
    facilityTxnVO.setNamedWhereClauseParam("docNum",
                                           hdrRows[0].getAttribute("DocumentNumber"));
    facilityTxnVO.executeQuery();
    RowSetIterator rsIterator1 = facilityTxnVO.createRowSetIterator(null);
    Row[] txnRows = rsIterator1.getAllRowsInRange();
    Row txnRow = txnRows[0];
    if (txnRow.getAttribute("BankName") != null) {
      loadLovsByBankName(txnRow.getAttribute("BankName").toString());
    } else {
      if (branchNameList != null) {
        branchNameList.clear();
      }
      if (accountNameList != null) {
        accountNameList.clear();
      }
      if (accountNumberList != null) {
        accountNumberList.clear();
      }
      if (currencyCodeList != null) {
        currencyCodeList.clear();
      }
    }
    if (txnRow.getAttribute("BranchName") != null) {
      loadLovsByBranchName(txnRow.getAttribute("BankName").toString(),
                           txnRow.getAttribute("BranchName").toString());
    } else {
      if (accountNameList != null) {
        accountNameList.clear();
      }
      if (accountNumberList != null) {
        accountNumberList.clear();
      }
      if (currencyCodeList != null) {
        currencyCodeList.clear();
      }
    }
    if (txnRow.getAttribute("AccountName") != null) {
      loadLovsByAccountName(txnRow.getAttribute("BankName").toString(),
                            txnRow.getAttribute("BranchName").toString(),
                            txnRow.getAttribute("AccountName").toString());
    }
    if (txnRow.getAttribute("ProjectName") != null) {
      loadLovsByProjectName(txnRow.getAttribute("ProjectName").toString());
    }
    AdfFacesContext.getCurrentInstance().addPartialTarget(branchNameLov);
    AdfFacesContext.getCurrentInstance().addPartialTarget(accntNameLov);
    AdfFacesContext.getCurrentInstance().addPartialTarget(accountNumberLov);
    AdfFacesContext.getCurrentInstance().addPartialTarget(crrncyCodeLov);
    AdfFacesContext.getCurrentInstance().addPartialTarget(projectNumberLov);
    OperationBinding operation1 = bindings.getOperationBinding("Rollback");
    operation1.execute();

  }

  public void setSearchInputText(RichInputText searchInputText) {
    this.searchInputText = searchInputText;
  }

  public RichInputText getSearchInputText() {
    return searchInputText;
  }

  public void showMessage(String msg) {
    FacesMessage fm = new FacesMessage(msg);
    fm.setSeverity(FacesMessage.SEVERITY_INFO);
    FacesContext context = FacesContext.getCurrentInstance();
    context.addMessage(null, fm);
  }

  public void facilityAmendPopupCancelLstnr(PopupCanceledEvent popupCanceledEvent) {
    DCBindingContainer bindings = getBindings();
    DCIteratorBinding facilityAmendBinding =
      bindings.findIteratorBinding("FacilityExtnVO2Iterator");
    ViewObject facilityAmendVo = facilityAmendBinding.getViewObject();
    RowSetIterator rsFacExtVoIteraor =
      facilityAmendVo.createRowSetIterator(null);
    DCIteratorBinding facilityFinBinding =
      bindings.findIteratorBinding("FacilityFinancialVO2Iterator");
    ViewObject facilityFinancialVo = facilityFinBinding.getViewObject();

    HashSet<String> facTypeIdSet = new HashSet<String>();

    while (rsFacExtVoIteraor.hasNext()) {
      Row facExtRow = rsFacExtVoIteraor.next();
      if (facExtRow.getAttribute("FacilityType") != null) {
        String facTypeId = facExtRow.getAttribute("FacilityType").toString();
        facTypeIdSet.add(facTypeId);
      }
    }
    rsFacExtVoIteraor.closeRowSetIterator();

    for (String facId : facTypeIdSet) {
      Double totalAmt = 0.0;
      Row[] facExtRows =
        facilityAmendVo.getFilteredRows("FacilityType", facId);
      for (Row facExtRow : facExtRows) {
        if (facExtRow != null &&
            facExtRow.getAttribute("FacilityType") != null &&
            facExtRow.getAttribute("Amount") != null &&
            facId.equals(facExtRow.getAttribute("FacilityType").toString())) {
          java.math.BigDecimal facAmount =
            (java.math.BigDecimal)facExtRow.getAttribute("Amount");
          totalAmt = totalAmt + facAmount.intValue();
        }
      }

      Row[] facFinRows =
        facilityFinancialVo.getFilteredRows("FacilityType", inverseFacTypeMap.get(facId).toString());
      for (Row facFinRow : facFinRows) {
        Integer id = inverseFacTypeMap.get(facId);
        if (facFinRow != null &&
            facFinRow.getAttribute("FacilityType") != null &&
            id.equals(Integer.parseInt(facFinRow.getAttribute("FacilityType").toString())) &&
            totalAmt > 0) {
          Number orgAmnt = new Number(0);
          if (facFinRow.getAttribute("AmountOrg") != null) {
            orgAmnt = (Number)facFinRow.getAttribute("AmountOrg");
          }
          facFinRow.setAttribute("AmountExtd",
                                 (totalAmt.intValue() + orgAmnt.intValue()));
        }
      }
    }
    AdfFacesContext.getCurrentInstance().addPartialTarget(facilityFinancialTableBind);
    Number noOfExtends =
      new Number(facilityAmendVo.getAllRowsInRange().length);


    JSFUtils.setExpressionValue("#{bindings.NumberOfExtends.inputValue}",
                                noOfExtends);
    System.out.println("VALUE AFTER SETTING BINDING" +
                       JSFUtils.resolveExpression("#{bindings.NumberOfExtends.inputValue}"));
    AdfFacesContext.getCurrentInstance().addPartialTarget(getNoExtends());
  }

  public void setNoExtends(RichInputText noExtends) {
    this.noExtends = noExtends;
  }

  public RichInputText getNoExtends() {
    return noExtends;
  }

  public void expiryDateVcl(ValueChangeEvent valueChangeEvent) {
    Date expDate = (Date)valueChangeEvent.getNewValue();
    Date facDate =
      (Date)JSFUtils.resolveExpression("#{bindings.FacilityDate.inputValue}");
    if ((expDate != null && !"".equals(expDate)) &&
        (facDate != null && !"".equals(facDate))) {
      java.sql.Date sqlExpDate = expDate.dateValue();
      java.sql.Date sqlFacDate = facDate.dateValue();
      if (sqlExpDate.before(sqlFacDate)) {
        JSFUtils.setExpressionValue("#{bindings.ExpiryDate.inputValue}", "");
        AdfFacesContext.getCurrentInstance().addPartialTarget(getExpryDateBind());
        getExpryDateBind().resetValue();
        showMessage("Expiry date should be greater than Facility Date");
      }
    }
  }

  public void facDateVcl(ValueChangeEvent valueChangeEvent) {
    Date facDate = (Date)valueChangeEvent.getNewValue();
    Date expDate =
      (Date)JSFUtils.resolveExpression("#{bindings.ExpiryDate.inputValue}");
    if ((expDate != null && !"".equals(expDate)) &&
        (facDate != null && !"".equals(facDate))) {
      java.sql.Date sqlExpDate = expDate.dateValue();
      java.sql.Date sqlFacDate = facDate.dateValue();
      if (sqlExpDate.before(sqlFacDate)) {
        JSFUtils.setExpressionValue("#{bindings.ExpiryDate.inputValue}", "");
        AdfFacesContext.getCurrentInstance().addPartialTarget(getExpryDateBind());
        getExpryDateBind().resetValue();
        showMessage("Expiry date should be greater than Facility Date");
      }
    }
  }

  public void setExpryDateBind(RichInputDate expryDateBind) {
    this.expryDateBind = expryDateBind;
  }

  public RichInputDate getExpryDateBind() {
    return expryDateBind;
  }


  public void sydnctnPercntgeVcl(ValueChangeEvent valueChangeEvent) throws SQLException {
    Number percntge = (Number)valueChangeEvent.getNewValue();
    Number oldPercntge = (Number)valueChangeEvent.getOldValue();

    DCBindingContainer bindings = getBindings();
    DCIteratorBinding dcItteratorBindings =
      bindings.findIteratorBinding("FacilitySynBankVO2Iterator");
    ViewObject facSyndctnVo = dcItteratorBindings.getViewObject();
    Row row = facSyndctnVo.getCurrentRow();
    String facilityType = "";
    Number amount = null;
    if ((row.getAttribute("FacilityType") == null)) {
      ((UIXEditableValue)valueChangeEvent.getComponent()).resetValue();
      showMessage("Please choose facility type before percentage");
      AdfFacesContext.getCurrentInstance().addPartialTarget(prcntgeBind);
      return;
    }
    if (percntge.getValue() < 0 || percntge.getValue() > 100) {

      ((UIXEditableValue)valueChangeEvent.getComponent()).resetValue();
      showMessage("Percentage should be between 0-100");
      AdfFacesContext.getCurrentInstance().addPartialTarget(prcntgeBind);
      return;
    }
    facilityType = row.getAttribute("FacilityType").toString();
    RowSetIterator rsIterator = facSyndctnVo.createRowSetIterator(null);
    Double totalPrcnt = 0.0;
    while (rsIterator.hasNext()) {
      Row syndcnRow = rsIterator.next();
      if (syndcnRow.getAttribute("FacilityType") != null &&
          facilityType.equals(syndcnRow.getAttribute("FacilityType").toString())) {
        if (syndcnRow.getAttribute("Percentage") != null) {
          Number prcnt = (Number)syndcnRow.getAttribute("Percentage");
          totalPrcnt = totalPrcnt + prcnt.getValue();
        }
      }
    }
    Double oldprctge = 0.0;
    if (oldPercntge != null) {
      oldprctge = oldPercntge.getValue();
    }
    totalPrcnt = (totalPrcnt + percntge.getValue()) - oldprctge;
    if (totalPrcnt > 100) {

      ((UIXEditableValue)valueChangeEvent.getComponent()).resetValue();
      showMessage("Total amount for a facility type " + facilityType +
                  " can't exceed the maximum available limt");
      AdfFacesContext.getCurrentInstance().addPartialTarget(prcntgeBind);
      return;
    }
    if (row.getAttribute("FacilityType") != null &&
        valueChangeEvent.getNewValue() != null) {
      facilityType = (String)row.getAttribute("FacilityType");
      Integer amt = null;
      amt =
          (percntge.intValue() * (facilityAmountMap.get(facilityType).intValue())) /
          100;
      amount = new Number(amt);
    }
    row.setAttribute("Amount", amount);
    AdfFacesContext.getCurrentInstance().addPartialTarget(syndctnTable);
  }

  public void setSyndctnTable(RichTable syndctnTable) {
    this.syndctnTable = syndctnTable;
  }

  public RichTable getSyndctnTable() {
    return syndctnTable;
  }

  public void fcltyNmbrVcl(ValueChangeEvent valueChangeEvent) {
    DCBindingContainer bindings = getBindings();
    DCIteratorBinding obj =
      bindings.findIteratorBinding("FacilityHdrVO1Iterator");
    ViewObject facilityHdrVO = obj.getViewObject();
    facilityHdrVO.setNamedWhereClauseParam("facilityNumber",
                                           valueChangeEvent.getNewValue());
    facilityHdrVO.executeQuery();
    RowSetIterator rsIterator = facilityHdrVO.createRowSetIterator(null);
    if (rsIterator.getAllRowsInRange().length > 0) {
      JSFUtils.setExpressionValue("#{bindings.FacilityNumber.inputValue}", "");
      AdfFacesContext.getCurrentInstance().addPartialTarget(getFacInputBind());
      getFacInputBind().resetValue();
      showMessage("Facility Number already exists");
    }
  }

  public void setFacInputBind(RichInputText facInputBind) {
    this.facInputBind = facInputBind;
  }

  public RichInputText getFacInputBind() {
    return facInputBind;
  }

  public void setHiddenOutputBind(RichOutputText hiddenOutputBind) {
    this.hiddenOutputBind = hiddenOutputBind;
  }

  public RichOutputText getHiddenOutputBind() {
    try {
      getBanksList();
      AdfFacesContext.getCurrentInstance().addPartialTarget(bankNameLov);
    } catch (InvalidParametersException_Exception e) {
    } catch (AccessDeniedException_Exception e) {
    } catch (OperationFailedException_Exception e) {
    } catch (JAXBException e) {
    }
    return hiddenOutputBind;
  }

  public void getBanksList() throws InvalidParametersException_Exception,
                                    AccessDeniedException_Exception,
                                    OperationFailedException_Exception,
                                    JAXBException {
    ReportsService reportsSrvc = new ReportsService();
    DATA_DS banksData = reportsSrvc.RunReport_Reponse();
    banksDataList = banksData.getG_1();
    SortedSet<String> banksDataSet = new TreeSet<String>();
    List<SelectItem> bankNamesList = new ArrayList<SelectItem>();
    for (G_1 bankData : banksDataList) {
      banksDataSet.add(bankData.getBANK_NAME());
    }
    for (String bankName : banksDataSet) {
      bankNamesList.add(new SelectItem(bankName, bankName));
    }
    setBankList(bankNamesList);
  }

  public void getProjectsList() throws InvalidParametersException_Exception,
                                       AccessDeniedException_Exception,
                                       OperationFailedException_Exception,
                                       JAXBException {
    ReportsService reportsSrvc = new ReportsService();
    ProjectReportResponse projectsData =
      reportsSrvc.RunProjectReport_Reponse();
    projectsDataList = projectsData.getG_1();
    SortedSet<String> projectsDataSet = new TreeSet<String>();
    List<SelectItem> projectsNamesList = new ArrayList<SelectItem>();
    for (ProjectReportResponseType projectData : projectsDataList) {
      projectsDataSet.add(projectData.getDescription());
    }
    for (String projectName : projectsDataSet) {
      projectsNamesList.add(new SelectItem(projectName, projectName));
    }
    setProjectNamesList(projectsNamesList);
  }

  public void setBankList(List<SelectItem> bankList) {
    this.bankList = bankList;
  }

  public List<SelectItem> getBankList() {

    return bankList;
  }

  public void setBanksDataList(List<G_1> banksDataList) {
    this.banksDataList = banksDataList;
  }

  public List<G_1> getBanksDataList() {
    return banksDataList;
  }

  public void setBankNameLov(RichSelectOneChoice bankNameLov) {
    this.bankNameLov = bankNameLov;
  }

  public RichSelectOneChoice getBankNameLov() {
    return bankNameLov;
  }

  public void bankNameLovVcl(ValueChangeEvent valueChangeEvent) {
    String bankName = (String)valueChangeEvent.getNewValue();
    loadLovsByBankName(bankName);
    AdfFacesContext.getCurrentInstance().addPartialTarget(branchNameLov);
    AdfFacesContext.getCurrentInstance().addPartialTarget(accntNameLov);
    AdfFacesContext.getCurrentInstance().addPartialTarget(accountNumberLov);
    AdfFacesContext.getCurrentInstance().addPartialTarget(crrncyCodeLov);
  }

  public void loadLovsByBankName(String bankName) {
    branchNameList = new ArrayList<SelectItem>();
    if (accountNameList != null) {
      accountNameList.clear();
    }
    if (accountNumberList != null) {
      accountNumberList.clear();
    }
    if (currencyCodeList != null) {
      currencyCodeList.clear();
    }
    SortedSet<String> branchDataSet = new TreeSet<String>();
    for (G_1 bankData : banksDataList) {
      if (bankName.equals(bankData.getBANK_NAME())) {
        if (!branchDataSet.contains(bankData.getBANK_BRANCH_NAME())) {
          branchDataSet.add(bankData.getBANK_BRANCH_NAME());
        }
      }
    }
    for (String branchName : branchDataSet) {
      branchNameList.add(new SelectItem(branchName, branchName));
    }
  }

  public void setBranchNameList(List<SelectItem> branchNameList) {
    this.branchNameList = branchNameList;
  }

  public List<SelectItem> getBranchNameList() {
    return branchNameList;
  }

  public void setBranchNameLov(RichSelectOneChoice branchNameLov) {
    this.branchNameLov = branchNameLov;
  }

  public RichSelectOneChoice getBranchNameLov() {
    return branchNameLov;
  }

  public void branchNameLovVcl(ValueChangeEvent valueChangeEvent) {
    String branchName = (String)valueChangeEvent.getNewValue();
    String bankName =
      (String)JSFUtils.resolveExpression("#{bindings.BankName.inputValue}");
    loadLovsByBranchName(bankName, branchName);
    AdfFacesContext.getCurrentInstance().addPartialTarget(accntNameLov);
    AdfFacesContext.getCurrentInstance().addPartialTarget(accountNumberLov);
    AdfFacesContext.getCurrentInstance().addPartialTarget(crrncyCodeLov);
  }

  public void loadLovsByBranchName(String bankName, String branchName) {
    accountNameList = new ArrayList<SelectItem>();
    if (currencyCodeList != null) {
      currencyCodeList.clear();
    }
    if (accountNumberList != null) {
      accountNumberList.clear();
    }
    SortedSet<String> accountDataSet = new TreeSet<String>();
    for (G_1 bankData : banksDataList) {
      if (bankName.equals(bankData.getBANK_NAME()) &&
          branchName.equals(bankData.getBANK_BRANCH_NAME())) {
        if (!accountDataSet.contains(bankData.getBANK_ACCOUNT_NAME())) {
          accountDataSet.add(bankData.getBANK_ACCOUNT_NAME());
        }
      }
    }
    for (String accountName : accountDataSet) {
      accountNameList.add(new SelectItem(accountName, accountName));
    }
  }

  public void setAccountNameList(List<SelectItem> accountNameList) {
    this.accountNameList = accountNameList;
  }

  public List<SelectItem> getAccountNameList() {
    return accountNameList;
  }

  public void setAccntNameLov(RichSelectOneChoice accntNameLov) {
    this.accntNameLov = accntNameLov;
  }

  public RichSelectOneChoice getAccntNameLov() {
    return accntNameLov;
  }

  public void accountNameVcl(ValueChangeEvent valueChangeEvent) {
    String branchName =
      (String)JSFUtils.resolveExpression("#{bindings.BranchName.inputValue}");
    String bankName =
      (String)JSFUtils.resolveExpression("#{bindings.BankName.inputValue}");
    String accountName = (String)valueChangeEvent.getNewValue();
    loadLovsByAccountName(bankName, branchName, accountName);
    AdfFacesContext.getCurrentInstance().addPartialTarget(accountNumberLov);
  }

  public void loadLovsByAccountName(String bankName, String branchName,
                                    String accountName) {
    currencyCodeList = new ArrayList<SelectItem>();
    SortedSet<String> currencyDataSet = new TreeSet<String>();
    for (G_1 bankData : banksDataList) {
      if (bankName.equals(bankData.getBANK_NAME()) &&
          branchName.equals(bankData.getBANK_BRANCH_NAME()) &&
          accountName.equals(bankData.getBANK_ACCOUNT_NAME())) {
        if (!currencyDataSet.contains(bankData.getCURRENCY_CODE())) {
          currencyDataSet.add(bankData.getCURRENCY_CODE());
        }
      }
    }
    for (String currencyCode : currencyDataSet) {
      currencyCodeList.add(new SelectItem(currencyCode, currencyCode));
    }
    AdfFacesContext.getCurrentInstance().addPartialTarget(crrncyCodeLov);

    accountNumberList = new ArrayList<SelectItem>();
    SortedSet<String> accountNumberSet = new TreeSet<String>();
    for (G_1 bankData : banksDataList) {
      if (bankName.equals(bankData.getBANK_NAME()) &&
          branchName.equals(bankData.getBANK_BRANCH_NAME()) &&
          accountName.equals(bankData.getBANK_ACCOUNT_NAME())) {
        if (!accountNumberSet.contains(bankData.getBANK_ACCOUNT_NUM())) {
          accountNumberSet.add(bankData.getBANK_ACCOUNT_NUM());
        }
      }
    }
    for (String accountNumber : accountNumberSet) {
      accountNumberList.add(new SelectItem(accountNumber, accountNumber));
    }
  }

  public void setCrrncyCodeLov(RichSelectOneChoice crrncyCodeLov) {
    this.crrncyCodeLov = crrncyCodeLov;
  }

  public RichSelectOneChoice getCrrncyCodeLov() {
    return crrncyCodeLov;
  }

  public void setCurrencyCodeList(List<SelectItem> currencyCodeList) {
    this.currencyCodeList = currencyCodeList;
  }

  public List<SelectItem> getCurrencyCodeList() {
    return currencyCodeList;
  }

  public void setAccountNumberList(List<SelectItem> accountNumberList) {
    this.accountNumberList = accountNumberList;
  }

  public List<SelectItem> getAccountNumberList() {
    return accountNumberList;
  }

  public void setAccountNumberLov(RichSelectOneChoice accountNumberLov) {
    this.accountNumberLov = accountNumberLov;
  }

  public RichSelectOneChoice getAccountNumberLov() {
    return accountNumberLov;
  }


  public void setHiddenProjectOutputBind(RichOutputText hiddenProjectOutputBind) {
    this.hiddenProjectOutputBind = hiddenProjectOutputBind;
  }

  public RichOutputText getHiddenProjectOutputBind() throws InvalidParametersException_Exception,
                                                            AccessDeniedException_Exception,
                                                            OperationFailedException_Exception,
                                                            JAXBException {
    getProjectsList();
    AdfFacesContext.getCurrentInstance().addPartialTarget(projectNameLov);
    return hiddenProjectOutputBind;
  }

  public void setProjectNameLov(RichSelectOneChoice projectNameLov) {
    this.projectNameLov = projectNameLov;
  }

  public RichSelectOneChoice getProjectNameLov() {
    return projectNameLov;
  }

  public void setProjectNamesList(List<SelectItem> projectNamesList) {
    this.projectNamesList = projectNamesList;
  }

  public List<SelectItem> getProjectNamesList() {
    return projectNamesList;
  }

  public void projectNameLovVcl(ValueChangeEvent valueChangeEvent) {
    loadLovsByProjectName(valueChangeEvent.getNewValue().toString());
    AdfFacesContext.getCurrentInstance().addPartialTarget(projectNumberLov);
  }

  public void loadLovsByProjectName(String projectName) {
    projectNumberList = new ArrayList<SelectItem>();
    SortedSet<String> projectNumberDataSet = new TreeSet<String>();
    for (ProjectReportResponseType projectData : projectsDataList) {
      if (projectName.equals(projectData.getDescription())) {
        projectNumberDataSet.add(projectData.getValue());
      }
    }
    for (String projectNumber : projectNumberDataSet) {
      projectNumberList.add(new SelectItem(projectNumber, projectNumber));
    }
  }

  public void setProjectNumberList(List<SelectItem> projectNumberList) {
    this.projectNumberList = projectNumberList;
  }

  public List<SelectItem> getProjectNumberList() {
    return projectNumberList;
  }

  public void setProjectNumberLov(RichSelectOneChoice projectNumberLov) {
    this.projectNumberLov = projectNumberLov;
  }

  public RichSelectOneChoice getProjectNumberLov() {
    return projectNumberLov;
  }

  public void legalEntityVcl(ValueChangeEvent valueChangeEvent) {
    DCBindingContainer bindings = getBindings();
    DCIteratorBinding businessUnitBinding =
      bindings.findIteratorBinding("FacilityBusinessUnitVO1Iterator");
    ViewObject businessUnitVo = businessUnitBinding.getViewObject();
    System.out.println("NEW VALUE--->>>" + valueChangeEvent.getNewValue());
    if (valueChangeEvent.getNewValue() != null) {
      businessUnitVo.setNamedWhereClauseParam("idBindVar",
                                              valueChangeEvent.getNewValue());
      businessUnitVo.executeQuery();
    } else {
      businessUnitVo.executeEmptyRowSet();
    }

    AdfFacesContext.getCurrentInstance().addPartialTarget(buBindVar);
    System.out.println("NEW VALUE--->>>" + valueChangeEvent.getNewValue());
  }

  public void setBuBindVar(RichSelectOneChoice buBindVar) {
    this.buBindVar = buBindVar;
  }

  public RichSelectOneChoice getBuBindVar() {
    return buBindVar;
  }

  public void setPrcntgeBind(RichInputText prcntgeBind) {
    this.prcntgeBind = prcntgeBind;
  }

  public RichInputText getPrcntgeBind() {
    return prcntgeBind;
  }

  public void completeBtnActnLsnr(ActionEvent actionEvent) {
    String facNum =
      (String)JSFUtils.resolveExpression("#{bindings.FacilityNumber.inputValue}");
    if (facNum == null || "".equals(facNum)) {
      showMessage("Please enter a facility number");
      throw new AbortProcessingException("Invalid");
    }
    saveFacility(actionEvent);
    AdfFacesContext.getCurrentInstance().addPartialTarget(isCompleteBind);
  }

  public void setIsCompleteBind(RichInputText isCompleteBind) {
    this.isCompleteBind = isCompleteBind;
  }

  public RichInputText getIsCompleteBind() {
    return isCompleteBind;
  }

  public void facilityAmendPopupFetchlLstnr(PopupFetchEvent popupFetchEvent) {
    DCBindingContainer bindings = getBindings();
    DCIteratorBinding facilityLovBinding =
      bindings.findIteratorBinding("FacilityLovVo1Iterator");
    ViewObject facilityLovVo = facilityLovBinding.getViewObject();
    facilityLovVo.executeQuery();
    RowSetIterator rsIterator = facilityLovVo.createRowSetIterator(null);
    HashMap<Integer, String> lovMap = new HashMap<Integer, String>();
    inverseFacTypeMap = new HashMap<String, Integer>();
    while (rsIterator.hasNext()) {
      Row r = rsIterator.next();
      oracle.jbo.domain.Number var =
        (oracle.jbo.domain.Number)(r.getAttribute("Id"));

      lovMap.put(var.intValue(), (String)r.getAttribute("LookupValue"));
    }
    rsIterator.closeRowSetIterator();
    System.out.println("MAP---->>>>>" + lovMap);

    List<SelectItem> facilityList = new ArrayList<SelectItem>();

    DCIteratorBinding financialVOBinding =
      bindings.findIteratorBinding("FacilityFinancialVO2Iterator");
    ViewObject financialVo = financialVOBinding.getViewObject();
    RowSetIterator rsIteratorFinVo = financialVo.createRowSetIterator(null);
    while (rsIteratorFinVo.hasNext()) {
      Row r = rsIteratorFinVo.next();
      Integer facValue =
        Integer.parseInt(r.getAttribute("FacilityType").toString());
      facilityList.add(new SelectItem(lovMap.get(facValue),
                                      lovMap.get(facValue)));
      inverseFacTypeMap.put(lovMap.get(facValue), facValue);

    }
    rsIteratorFinVo.closeRowSetIterator();


    setFacilityList(facilityList);
  }

  public void amendmntFacDateVcl(ValueChangeEvent valueChangeEvent) {
    Date amendFacDate = (Date)valueChangeEvent.getNewValue();
    Date actualFacDate =
      (Date)JSFUtils.resolveExpression("#{bindings.FacilityDate.inputValue}");
    Date actualExpDate =
      (Date)JSFUtils.resolveExpression("#{bindings.ExpiryDate.inputValue}");
    Date extndExpDate =
      (Date)JSFUtils.resolveExpression("#{bindings.ExtendedExpiryDate.inputValue}");
    if (extndExpDate != null) {
      actualExpDate = extndExpDate;
    }
    if ((amendFacDate != null && !"".equals(amendFacDate)) &&
        (actualFacDate != null && !"".equals(actualFacDate))) {
      java.sql.Date sqlAmendFacDate = amendFacDate.dateValue();
      java.sql.Date sqlActualFacDate = actualFacDate.dateValue();
      if (sqlAmendFacDate.before(sqlActualFacDate)) {
        ((UIXEditableValue)valueChangeEvent.getComponent()).resetValue();
        AdfFacesContext.getCurrentInstance().addPartialTarget(amendFacDateBind);
        showMessage("New facility date can't be before the actual facility date");
        return;
      }
    }

    if ((amendFacDate != null && !"".equals(amendFacDate)) &&
        (actualExpDate != null && !"".equals(actualExpDate))) {
      java.sql.Date sqlAmendFacDate = amendFacDate.dateValue();
      java.sql.Date sqlActualExpDate = actualExpDate.dateValue();
      if (sqlActualExpDate.before(sqlAmendFacDate)) {
        ((UIXEditableValue)valueChangeEvent.getComponent()).resetValue();
        AdfFacesContext.getCurrentInstance().addPartialTarget(amendFacDateBind);
        showMessage("New facility date can't be after the actual expiry date");
        return;
      }
    }
  }

  public void setAmendFacDateBind(RichInputDate amendFacDateBind) {
    this.amendFacDateBind = amendFacDateBind;
  }

  public RichInputDate getAmendFacDateBind() {
    return amendFacDateBind;
  }

  public void setAmendExpDateBind(RichInputDate amendExpDateBind) {
    this.amendExpDateBind = amendExpDateBind;
  }

  public RichInputDate getAmendExpDateBind() {
    return amendExpDateBind;
  }

  public void amendmntExpDateVcl(ValueChangeEvent valueChangeEvent) {
    DCBindingContainer bindings = getBindings();
    DCIteratorBinding facExtVoBinding =
      bindings.findIteratorBinding("FacilityExtnVO2Iterator");
    ViewObject facExtVo = facExtVoBinding.getViewObject();
    Row currentRow = facExtVo.getCurrentRow();
    Date amendFacilityDate = null;
    if (currentRow != null &&
        currentRow.getAttribute("FacilityDate") != null) {
      amendFacilityDate = (Date)currentRow.getAttribute("FacilityDate");
    }
    Date amendExpDate = (Date)valueChangeEvent.getNewValue();
    Date actualFacDate =
      (Date)JSFUtils.resolveExpression("#{bindings.FacilityDate.inputValue}");
    Date actualExpDate =
      (Date)JSFUtils.resolveExpression("#{bindings.ExpiryDate.inputValue}");
    Date extndExpDate =
      (Date)JSFUtils.resolveExpression("#{bindings.ExtendedExpiryDate.inputValue}");
    if (extndExpDate != null) {
      actualExpDate = extndExpDate;
    }
    if ((amendExpDate != null && !"".equals(amendExpDate)) &&
        (actualFacDate != null && !"".equals(actualFacDate))) {
      java.sql.Date sqlAmendExpDate = amendExpDate.dateValue();
      java.sql.Date sqlActualFacDate = actualFacDate.dateValue();
      if (sqlAmendExpDate.before(sqlActualFacDate)) {
        ((UIXEditableValue)valueChangeEvent.getComponent()).resetValue();
        AdfFacesContext.getCurrentInstance().addPartialTarget(amendExpDateBind);
        showMessage("New expiry date can't be before the actual facility date");
        return;
      }
    }

    if ((amendExpDate != null && !"".equals(amendExpDate)) &&
        (actualExpDate != null && !"".equals(actualExpDate))) {
      java.sql.Date sqlAmendExpDate = amendExpDate.dateValue();
      java.sql.Date sqlActualExpDate = actualExpDate.dateValue();
      if (sqlActualExpDate.before(sqlAmendExpDate)) {
        ((UIXEditableValue)valueChangeEvent.getComponent()).resetValue();
        AdfFacesContext.getCurrentInstance().addPartialTarget(amendExpDateBind);
        showMessage("New expiry date can't be after the actual expiry date");
        return;
      }
    }

    if ((amendExpDate != null && !"".equals(amendExpDate)) &&
        (amendFacilityDate != null && !"".equals(amendFacilityDate))) {
      java.sql.Date sqlAmendExpDate = amendExpDate.dateValue();
      java.sql.Date sqlAmendFacDate = amendFacilityDate.dateValue();
      if (sqlAmendExpDate.before(sqlAmendFacDate)) {
        ((UIXEditableValue)valueChangeEvent.getComponent()).resetValue();
        AdfFacesContext.getCurrentInstance().addPartialTarget(amendExpDateBind);
        showMessage("New expiry date can't be before the new facility date");
      }
    }
  }

  public void setFacilityFinancialTableBind(RichTable facilityFinancialTableBind) {
    this.facilityFinancialTableBind = facilityFinancialTableBind;
  }

  public RichTable getFacilityFinancialTableBind() {
    return facilityFinancialTableBind;
  }

  public void facFinFacTypeVcl(ValueChangeEvent vce) {
    DCBindingContainer bindings = getBindings();
    DCIteratorBinding facilityFinBinding =
      bindings.findIteratorBinding("FacilityFinancialVO2Iterator");
    ViewObject facilityFinancialVo = facilityFinBinding.getViewObject();
    String facTypeValue = "";
    if (vce.getNewValue() != null) {
      JSFUtils.setExpressionValue("#{row.bindings.FacilityType.inputValue}",
                                  vce.getNewValue()); //Updating Model Values
      facTypeValue =
          JSFUtils.resolveExpression("#{row.bindings.FacilityType.attributeValue}").toString();
    }

    Row[] facFinRows =
      facilityFinancialVo.getFilteredRows("FacilityType", facTypeValue);
    if (facFinRows.length > 1) {
      ((UIXEditableValue)vce.getComponent()).resetValue();
      AdfFacesContext.getCurrentInstance().addPartialTarget(facFinFacTypeBind);
      showMessage("Facility type already defined for the bank facility");
      return;
    }
  }

  public void setFacFinFacTypeBind(RichSelectOneChoice facFinFacTypeBind) {
    this.facFinFacTypeBind = facFinFacTypeBind;
  }

  public RichSelectOneChoice getFacFinFacTypeBind() {
    return facFinFacTypeBind;
  }

}

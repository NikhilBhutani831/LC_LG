package com.in.fsa.Pojo;

import com.oracle.xmlns.oxp.service.publicreportservice.AccessDeniedException_Exception;

import com.oracle.xmlns.oxp.service.publicreportservice.ExternalReportWSSService;
import com.oracle.xmlns.oxp.service.publicreportservice.ExternalReportWSSService_Service;

import com.sun.xml.ws.developer.WSBindingProvider;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import javax.net.ssl.SSLSession;

import javax.net.ssl.TrustManager;

import javax.net.ssl.X509TrustManager;

import javax.xml.ws.BindingProvider;

import weblogic.wsee.jws.jaxws.owsm.SecurityPolicyFeature;

import com.oracle.xmlns.oxp.service.publicreportservice.ReportResponse;
import com.oracle.xmlns.oxp.service.publicreportservice.InvalidParametersException_Exception;
import com.oracle.xmlns.oxp.service.publicreportservice.OperationFailedException_Exception;
import com.oracle.xmlns.oxp.service.publicreportservice.ReportRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class ReportsService {
  String ReportData;

  public DATA_DS RunReport_Reponse() throws InvalidParametersException_Exception,
                                            AccessDeniedException_Exception,
                                            OperationFailedException_Exception,
                                            JAXBException {
    importCertificates();
    SecurityPolicyFeature[] securityFeatures =
      new SecurityPolicyFeature[] { new SecurityPolicyFeature("oracle/wss_username_token_over_ssl_client_policy") };
    //new SecurityPolicyFeature[] { new SecurityPolicyFeature("oracle/wss_saml_token_bearer_over_ssl_client_policy") };

    ExternalReportWSSService_Service externalreportWSSService_Service =
      new ExternalReportWSSService_Service();
    ExternalReportWSSService externalReportWSSService =
      externalreportWSSService_Service.getExternalReportWSSService(securityFeatures);

    WSBindingProvider wsbp = (WSBindingProvider)externalReportWSSService;
    wsbp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
                                 "WIP.PAAS");
    wsbp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
                                 "Jeddah@112233");
    ReportResponse reportResponse;
    ReportRequest reportRequest = new ReportRequest();
    reportRequest.setReportAbsolutePath("/Custom/CashManagement_BankSetup_Report.xdo");
    reportRequest.setSizeOfDataChunkDownload(-1);
    reportRequest.setAttributeFormat("xml");

    reportResponse = externalReportWSSService.runReport(reportRequest, "");
    ReportData = new String(reportResponse.getReportBytes());


    JAXBContext context = JAXBContext.newInstance(DATA_DS.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    InputStream is = new ByteArrayInputStream(reportResponse.getReportBytes());
    DATA_DS reportsData = (DATA_DS)unmarshaller.unmarshal(is);
    System.out.println("ARRAY SIZE--->>" +
                       reportsData.getG_1().get(0).getBANK_ACCOUNT_NAME());
    return reportsData;
  }

  public ProjectReportResponse RunProjectReport_Reponse() throws InvalidParametersException_Exception,
                                                                 AccessDeniedException_Exception,
                                                                 OperationFailedException_Exception,
                                                                 JAXBException {
    importCertificates();
    SecurityPolicyFeature[] securityFeatures =
      new SecurityPolicyFeature[] { new SecurityPolicyFeature("oracle/wss_username_token_over_ssl_client_policy") };
    //new SecurityPolicyFeature[] { new SecurityPolicyFeature("oracle/wss_saml_token_bearer_over_ssl_client_policy") };

    ExternalReportWSSService_Service externalreportWSSService_Service =
      new ExternalReportWSSService_Service();
    ExternalReportWSSService externalReportWSSService =
      externalreportWSSService_Service.getExternalReportWSSService(securityFeatures);

    WSBindingProvider wsbp = (WSBindingProvider)externalReportWSSService;
    wsbp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
                                 "WIP.PAAS");
    wsbp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
                                 "Jeddah@112233");
    ReportResponse reportResponse;
    ReportRequest reportRequest = new ReportRequest();
    reportRequest.setReportAbsolutePath("/Custom/Projects_VS_Report.xdo");
    reportRequest.setSizeOfDataChunkDownload(-1);
    reportRequest.setAttributeFormat("xml");

    reportResponse = externalReportWSSService.runReport(reportRequest, "");
    ReportData = new String(reportResponse.getReportBytes());
    System.out.println(ReportData);

    JAXBContext context = JAXBContext.newInstance(ProjectReportResponse.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    InputStream is = new ByteArrayInputStream(reportResponse.getReportBytes());
    ProjectReportResponse reportsData =
      (ProjectReportResponse)unmarshaller.unmarshal(is);
    System.out.println("PROJECT RESPONSE SIZE--->>>>" +
                       reportsData.getG_1().size());
    return reportsData;
  }

  public void importCertificates() {
    try {
      TrustManager[] trustAllCerts =
        new TrustManager[] { new X509TrustManager() {
          public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                         String authType) throws java.security.cert.CertificateException {
          }

          public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                         String authType) throws java.security.cert.CertificateException {
          }

          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            // or we can return null too
            return new java.security.cert.X509Certificate[0];
          }
        } };

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
          public boolean verify(String string, SSLSession sslSession) {
            return true;
          }
        });
    } catch (NoSuchAlgorithmException e) {
      System.out.println(e);
    } catch (KeyManagementException e) {
      System.out.println(e);
    }
  }

  public static void main(String[] args) throws InvalidParametersException_Exception,
                                                AccessDeniedException_Exception,
                                                OperationFailedException_Exception,
                                                JAXBException {
    ReportsService externalReportServiceFacade = new ReportsService();
    // externalReportServiceFacade.importCertificates();
    externalReportServiceFacade.RunProjectReport_Reponse();

  }


}

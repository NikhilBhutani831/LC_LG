package com.in.fsa.Pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "G_1", propOrder = {
    "BANK_ACCOUNT_NUM",
    "BANK_ACCOUNT_NAME",
    "CURRENCY_CODE",
    "BANK_NAME",
    "BANK_BRANCH_NAME"
})

public class G_1 {
  @XmlElement(required = true, nillable = true)
  private String BANK_ACCOUNT_NUM;
  @XmlElement(required = true, nillable = true)
      private String BANK_ACCOUNT_NAME;
  @XmlElement(required = true, nillable = true)
      private String CURRENCY_CODE;
  @XmlElement(required = true, nillable = true)
      private String BANK_NAME;
  @XmlElement(required = true, nillable = true)
      private String BANK_BRANCH_NAME;

      public String getBANK_ACCOUNT_NUM ()
      {
          return BANK_ACCOUNT_NUM;
      }

      public void setBANK_ACCOUNT_NUM (String BANK_ACCOUNT_NUM)
      {
          this.BANK_ACCOUNT_NUM = BANK_ACCOUNT_NUM;
      }

      public String getBANK_ACCOUNT_NAME ()
      {
          return BANK_ACCOUNT_NAME;
      }

      public void setBANK_ACCOUNT_NAME (String BANK_ACCOUNT_NAME)
      {
          this.BANK_ACCOUNT_NAME = BANK_ACCOUNT_NAME;
      }

      public String getCURRENCY_CODE ()
      {
          return CURRENCY_CODE;
      }

      public void setCURRENCY_CODE (String CURRENCY_CODE)
      {
          this.CURRENCY_CODE = CURRENCY_CODE;
      }

      public String getBANK_NAME ()
      {
          return BANK_NAME;
      }

      public void setBANK_NAME (String BANK_NAME)
      {
          this.BANK_NAME = BANK_NAME;
      }

      public String getBANK_BRANCH_NAME ()
      {
          return BANK_BRANCH_NAME;
      }

      public void setBANK_BRANCH_NAME (String BANK_BRANCH_NAME)
      {
          this.BANK_BRANCH_NAME = BANK_BRANCH_NAME;
      }

}

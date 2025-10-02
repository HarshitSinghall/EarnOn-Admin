package com.developers.EarnOn_Admin.Models;

public class referalModel {
    String refered_by;
    String amount;
    String uperName;

    String code;

    public referalModel(String refered_by, String amount, String uperName, String code) {
        this.refered_by = refered_by;
        this.amount = amount;
        this.uperName = uperName;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getrefered_by() {
        return refered_by;
    }

    public void setrefered_by(String refered_by) {
        this.refered_by = refered_by;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUperName() {
        return uperName;
    }

    public void setUperName(String uperName) {
        this.uperName = uperName;
    }
}

package com.developers.EarnOn_Admin.Models;

public class leadModel {
    String number;
    String name;
    String pName;
    String status;
    String reg_no;

    public leadModel(String number, String name, String pName, String status, String reg_no) {
        this.number = number;
        this.name = name;
        this.pName = pName;
        this.status = status;
        this.reg_no = reg_no;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }
}

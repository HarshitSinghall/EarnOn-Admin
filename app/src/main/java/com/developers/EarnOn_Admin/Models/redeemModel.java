package com.developers.EarnOn_Admin.Models;

public class redeemModel {
    String number;
    String date;
    String msg;
    String status;
    String amount;
    String push_id;
    String reg_no;

    public redeemModel(String number, String date, String msg, String status, String amount, String push_id, String reg_no) {
        this.number = number;
        this.date = date;
        this.msg = msg;
        this.status = status;
        this.amount = amount;
        this.push_id = push_id;
        this.reg_no = reg_no;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }
}

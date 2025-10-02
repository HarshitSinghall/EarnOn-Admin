package com.developers.EarnOn_Admin.Models;

public class queryModel {
    String msg;
    String rply;
    String img_url;
    String status;
    String reg_no;
    String id;

    public queryModel(String msg, String rply, String img_url, String status, String reg_no, String id) {
        this.msg = msg;
        this.rply = rply;
        this.img_url = img_url;
        this.status = status;
        this.reg_no = reg_no;
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRply() {
        return rply;
    }

    public void setRply(String rply) {
        this.rply = rply;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

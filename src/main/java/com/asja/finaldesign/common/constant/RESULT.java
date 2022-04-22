package com.asja.finaldesign.common.constant;

public enum RESULT {

    SUCCESS(0,"success"),
    PARAM_ERROR(-1,"parameter error");

    private Integer errorCode;
    private String msg;

    RESULT(Integer errorCode, String msg){
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}


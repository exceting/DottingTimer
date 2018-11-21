/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.ui.resp;

/**
 * @author sunqinwen
 * @version \: DataResult.java,v 0.1 2018-11-19 10:48
 */
public class DataResult {

    public static int SUCCESS = 0;

    public static int FAIL = -500;

    private int code;

    private Object data;

    private String msg;

    public DataResult() {
    }

    public DataResult(int code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static DataResult success(Object t) {
        return new DataResult(SUCCESS, t, "success");
    }

    public static DataResult success(Object t, String msg) {
        return new DataResult(SUCCESS, t, msg);
    }

    public static DataResult fail() {
        return new DataResult(FAIL, null, "fail");
    }

    public static DataResult fail(String msg) {
        return new DataResult(FAIL, null, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

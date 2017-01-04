package com.ping.pingword.Retrofit;

import com.ping.pingword.bean.Basic;

import java.util.List;

/**
 * Created 有道翻译.
 */
public class RequestWord {
    private String query;
    private int errorCode;
    private List<String> translation;
    private Basic basic;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }
}

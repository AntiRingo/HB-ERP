package com.hongbang.pojo;

import javax.servlet.http.HttpSession;
import java.sql.Date;

public class UserSession {
    private String sessionId;
    private String ip;
    private Date loginTime;
    private HttpSession httpSession; // 用于关联实际会话（可选）

    public UserSession(String sessionId, String ip, Date loginTime, HttpSession httpSession) {
        this.sessionId = sessionId;
        this.ip = ip;
        this.loginTime = loginTime;
        this.httpSession = httpSession;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "sessionId='" + sessionId + '\'' +
                ", ip='" + ip + '\'' +
                ", loginTime=" + loginTime +
                ", httpSession=" + httpSession +
                '}';
    }

    // 会话失效方法（需关联实际HttpSession时）
    public void invalidate() {
        if (httpSession != null) {
            httpSession.invalidate(); // 调用实际会话的失效方法
            httpSession = null; // 重要！切断引用
        }
    }
}

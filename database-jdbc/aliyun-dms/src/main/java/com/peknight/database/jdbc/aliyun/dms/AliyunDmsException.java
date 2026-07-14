package com.peknight.database.jdbc.aliyun.dms;

import java.sql.SQLException;

/**
 * DMS API 异常封装为 SQLException
 */
public class AliyunDmsException extends SQLException {

    private final String dmsErrorCode;
    private final String dmsRequestId;

    public AliyunDmsException(String message) {
        super(message);
        this.dmsErrorCode = null;
        this.dmsRequestId = null;
    }

    public AliyunDmsException(String message, Throwable cause) {
        super(message, cause);
        this.dmsErrorCode = null;
        this.dmsRequestId = null;
    }

    public AliyunDmsException(String dmsErrorCode, String message, String requestId) {
        super(message);
        this.dmsErrorCode = dmsErrorCode;
        this.dmsRequestId = requestId;
    }

    public String getDmsErrorCode() {
        return dmsErrorCode;
    }

    public String getDmsRequestId() {
        return dmsRequestId;
    }
}

package com.peknight.database.jdbc.aliyun.dms;

import java.net.URLDecoder;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * DMS JDBC Driver
 *
 * URL 格式: jdbc:aliyun-dms://&lt;regionId&gt;/&lt;databaseId&gt;
 * 默认 regionId 为 cn-hangzhou，可省略: jdbc:aliyun-dms:///&lt;databaseId&gt;
 * Properties: user=AccessKeyId, password=AccessKeySecret
 * 查询参数（?key=value）可覆盖 URL 中的 regionId
 */
public class AliyunDmsDriver implements Driver {

    static final String URL_PREFIX = "jdbc:aliyun-dms://";

    static {
        try {
            DriverManager.registerDriver(new AliyunDmsDriver());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register DmsDriver", e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }

        // 合并 Properties 到 merged
        Properties merged = new Properties();
        if (info != null) {
            merged.putAll(info);
        }

        String remainder = url.substring(URL_PREFIX.length());

        // 提取查询参数（?后面的部分），查询参数可覆盖 URL path 中的值
        int queryIdx = remainder.indexOf('?');
        if (queryIdx >= 0) {
            parseQueryParams(remainder.substring(queryIdx + 1), merged);
            remainder = remainder.substring(0, queryIdx);
        }

        // 解析 URL path: <regionId>/<databaseId>
        // 类似 MySQL/PG: jdbc:mysql://host/database
        String pathRegionId;
        String databaseIdStr;
        int slashIdx = remainder.indexOf('/');
        if (slashIdx >= 0) {
            pathRegionId = remainder.substring(0, slashIdx);
            databaseIdStr = remainder.substring(slashIdx + 1);
        } else {
            // 没有 /，整个作为 databaseId，regionId 为空
            pathRegionId = "";
            databaseIdStr = remainder;
        }

        if (databaseIdStr.endsWith("/")) {
            databaseIdStr = databaseIdStr.substring(0, databaseIdStr.length() - 1);
        }

        if (databaseIdStr.isEmpty()) {
            throw new SQLException("Missing databaseId in URL: " + url);
        }

        long databaseId;
        try {
            databaseId = Long.parseLong(databaseIdStr);
        } catch (NumberFormatException e) {
            throw new SQLException("Invalid databaseId in URL: " + url);
        }

        // regionId 优先级: 查询参数/Properties > URL path > 默认值
        String regionId = merged.getProperty("regionId");
        if (regionId == null || regionId.isEmpty()) {
            regionId = pathRegionId;
        }
        if (regionId.isEmpty()) {
            regionId = "cn-hangzhou";
        }

        String accessKeyId = merged.getProperty("user");
        String accessKeySecret = merged.getProperty("password");

        if (accessKeyId == null || accessKeyId.isEmpty()) {
            throw new SQLException("AccessKeyId (user) is required");
        }
        if (accessKeySecret == null || accessKeySecret.isEmpty()) {
            throw new SQLException("AccessKeySecret (password) is required");
        }

        AliyunDmsClient aliyunDmsClient = new AliyunDmsClient(accessKeyId, accessKeySecret, regionId);
        return new AliyunDmsConnection(aliyunDmsClient, databaseId);
    }

    private void parseQueryParams(String queryString, Properties props) {
        if (queryString == null || queryString.isEmpty()) {
            return;
        }
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int eqIdx = pair.indexOf('=');
            if (eqIdx > 0) {
                String key = decode(pair.substring(0, eqIdx));
                String value = decode(pair.substring(eqIdx + 1));
                props.setProperty(key, value);
            }
        }
    }

    private String decode(String value) {
        return URLDecoder.decode(value, UTF_8);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url != null && url.startsWith(URL_PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        DriverPropertyInfo userProp = new DriverPropertyInfo("user", info != null ? info.getProperty("user") : null);
        userProp.required = true;

        DriverPropertyInfo passwordProp = new DriverPropertyInfo("password", null);
        passwordProp.required = true;

        DriverPropertyInfo regionProp = new DriverPropertyInfo("regionId", "cn-hangzhou");
        regionProp.required = false;
        regionProp.description = "Region ID, also can be specified in URL path: jdbc:aliyun-dms://<regionId>/<databaseId>";

        return new DriverPropertyInfo[]{ userProp, passwordProp, regionProp };
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 1;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}

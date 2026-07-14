package com.peknight.database.jdbc.aliyun.dms;

import com.aliyun.dms_enterprise20181101.Client;
import com.aliyun.dms_enterprise20181101.models.*;
import com.aliyun.teaopenapi.models.Config;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 封装阿里云 DMS SDK Client
 */
public class AliyunDmsClient1 {

    private final Client client;
    private final String regionId;

    public AliyunDmsClient1(String accessKeyId, String accessKeySecret, String regionId) throws AliyunDmsException1 {
        try {
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret)
                    .setRegionId(regionId);
            this.client = new Client(config);
            this.regionId = regionId;
        } catch (Exception e) {
            throw new AliyunDmsException1("Failed to initialize DMS client", e);
        }
    }

    public String getRegionId() {
        return regionId;
    }

    /**
     * 执行 SQL 脚本，返回结果
     */
    public ExecuteResult1 executeScript(long databaseId, String sql) throws SQLException {
        try {
            if (databaseId > Integer.MAX_VALUE) {
                throw new SQLException("databaseId exceeds integer range: " + databaseId);
            }
            ExecuteScriptRequest request = new ExecuteScriptRequest()
                    .setDbId((int) databaseId)
                    .setLogic(false)
                    .setScript(sql);

            ExecuteScriptResponse response = client.executeScript(request);
            ExecuteScriptResponseBody body = response.getBody();

            if (body.getSuccess() == null || !body.getSuccess()) {
                throw new AliyunDmsException1(
                        body.getErrorCode(),
                        body.getErrorMessage(),
                        body.getRequestId()
                );
            }

            List<ExecuteScriptResponseBody.ExecuteScriptResponseBodyResults> results = body.getResults();
            if (results == null || results.isEmpty()) {
                return new ExecuteResult1(Collections.emptyList(), Collections.emptyList(), 0);
            }

            // 取第一个结果（单语句执行）
            ExecuteScriptResponseBody.ExecuteScriptResponseBodyResults result = results.getFirst();
            if (result.getSuccess() == null || !result.getSuccess()) {
                throw new AliyunDmsException1("DMS ExecuteScript failed: " + result.getMessage());
            }

            List<String> columnNames = result.getColumnNames() == null ?
                    Collections.emptyList() : result.getColumnNames();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> rows = result.getRows() == null ?
                    Collections.emptyList() : (List<Map<String, Object>>) (List<?>) result.getRows();

            long rowCount = result.getRowCount() != null ? result.getRowCount() : rows.size();
            return new ExecuteResult1(columnNames, rows, rowCount);
        } catch (AliyunDmsException1 e) {
            throw e;
        } catch (Exception e) {
            throw new AliyunDmsException1("DMS API call failed: " + e.getMessage(), e);
        }
    }

    /**
     * 搜索数据库
     */
    public List<SearchDatabaseResponseBody.SearchDatabaseResponseBodySearchDatabaseListSearchDatabase> searchDatabase(String searchKey) throws SQLException {
        try {
            SearchDatabaseRequest request = new SearchDatabaseRequest()
                    .setSearchKey(searchKey)
                    .setPageNumber(1)
                    .setPageSize(100);

            SearchDatabaseResponse response = client.searchDatabase(request);
            SearchDatabaseResponseBody.SearchDatabaseResponseBodySearchDatabaseList wrapper =
                    response.getBody().getSearchDatabaseList();
            if (wrapper == null || wrapper.getSearchDatabase() == null) {
                return Collections.emptyList();
            }
            return wrapper.getSearchDatabase();
        } catch (Exception e) {
            throw new AliyunDmsException1("SearchDatabase failed: " + e.getMessage(), e);
        }
    }

    /**
     * 列出表
     */
    public List<ListTablesResponseBody.ListTablesResponseBodyTableListTable> listTables(long databaseId, String searchName) throws SQLException {
        try {
            ListTablesRequest request = new ListTablesRequest()
                    .setDatabaseId(String.valueOf(databaseId))
                    .setPageNumber(1)
                    .setPageSize(200);
            if (searchName != null && !searchName.isEmpty()) {
                request.setSearchName(searchName);
            }

            ListTablesResponse response = client.listTables(request);
            ListTablesResponseBody.ListTablesResponseBodyTableList wrapper =
                    response.getBody().getTableList();
            if (wrapper == null || wrapper.getTable() == null) {
                return Collections.emptyList();
            }
            return wrapper.getTable();
        } catch (Exception e) {
            throw new AliyunDmsException1("ListTables failed: " + e.getMessage(), e);
        }
    }

    /**
     * 列出列
     */
    public List<ListColumnsResponseBody.ListColumnsResponseBodyColumnListColumn> listColumns(long tableId) throws SQLException {
        try {
            ListColumnsRequest request = new ListColumnsRequest()
                    .setTableId(String.valueOf(tableId))
                    .setLogic(false);

            ListColumnsResponse response = client.listColumns(request);
            ListColumnsResponseBody.ListColumnsResponseBodyColumnList wrapper =
                    response.getBody().getColumnList();
            if (wrapper == null || wrapper.getColumn() == null) {
                return Collections.emptyList();
            }
            return wrapper.getColumn();
        } catch (Exception e) {
            throw new AliyunDmsException1("ListColumns failed: " + e.getMessage(), e);
        }
    }
}

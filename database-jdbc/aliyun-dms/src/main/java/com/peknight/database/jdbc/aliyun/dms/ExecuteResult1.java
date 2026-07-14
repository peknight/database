package com.peknight.database.jdbc.aliyun.dms;

import java.util.List;
import java.util.Map;

/**
 * ExecuteScript 结果封装
 */
public class ExecuteResult1 {
    private final List<String> columnNames;
    private final List<Map<String, Object>> rows;
    private final long rowCount;

    public ExecuteResult1(List<String> columnNames, List<Map<String, Object>> rows, long rowCount) {
        this.columnNames = columnNames;
        this.rows = rows;
        this.rowCount = rowCount;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public long getRowCount() {
        return rowCount;
    }
}

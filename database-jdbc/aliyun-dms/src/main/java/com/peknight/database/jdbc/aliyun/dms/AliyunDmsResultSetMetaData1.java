package com.peknight.database.jdbc.aliyun.dms;

import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.List;

/**
 * DMS JDBC ResultSetMetaData
 *
 * DMS ExecuteScript 不返回列类型信息，统一使用 VARCHAR
 */
public class AliyunDmsResultSetMetaData1 implements ResultSetMetaData {

    private final List<String> columnNames;

    public AliyunDmsResultSetMetaData1(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column - 1);
    }

    @Override
    public String getColumnLabel(int column) {
        return columnNames.get(column - 1);
    }

    @Override
    public int getColumnType(int column) {
        return Types.VARCHAR;
    }

    @Override
    public String getColumnTypeName(int column) {
        return "VARCHAR";
    }

    @Override
    public String getColumnClassName(int column) {
        return String.class.getName();
    }

    // ====== 返回默认值的方法 ======

    @Override
    public int isNullable(int column) {
        return columnNullableUnknown;
    }

    @Override
    public boolean isSigned(int column) {
        return false;
    }

    @Override
    public int getPrecision(int column) {
        return 0;
    }

    @Override
    public int getColumnDisplaySize(int column) {
        return 0;
    }

    @Override
    public int getScale(int column) {
        return 0;
    }

    @Override
    public String getTableName(int column) {
        return "";
    }

    @Override
    public String getSchemaName(int column) {
        return "";
    }

    @Override
    public String getCatalogName(int column) {
        return "";
    }

    @Override
    public boolean isAutoIncrement(int column) {
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) {
        return true;
    }

    @Override
    public boolean isSearchable(int column) {
        return true;
    }

    @Override
    public boolean isCurrency(int column) {
        return false;
    }

    @Override
    public boolean isReadOnly(int column) {
        return true;
    }

    @Override
    public boolean isWritable(int column) {
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws java.sql.SQLException {
        if (iface.isInstance(this)) {
            return iface.cast(this);
        }
        throw new java.sql.SQLException("Cannot unwrap to " + iface.getName());
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return iface.isInstance(this);
    }
}

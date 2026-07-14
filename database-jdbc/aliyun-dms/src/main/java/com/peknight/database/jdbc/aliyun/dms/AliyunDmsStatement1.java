package com.peknight.database.jdbc.aliyun.dms;

import java.sql.*;

/**
 * DMS JDBC Statement
 */
public class AliyunDmsStatement1 implements Statement {

    protected final AliyunDmsConnection1 connection;
    protected AliyunDmsResultSet1 currentResultSet;
    protected long updateCount = -1;
    protected volatile boolean closed = false;

    public AliyunDmsStatement1(AliyunDmsConnection1 connection) {
        this.connection = connection;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        checkClosed();
        closeCurrentResultSet();
        ExecuteResult1 result = connection.getDmsClient().executeScript(connection.getDatabaseId(), sql);

        currentResultSet = new AliyunDmsResultSet1(this, result.getColumnNames(), result.getRows());
        updateCount = -1;
        return currentResultSet;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        checkClosed();
        closeCurrentResultSet();
        ExecuteResult1 result = connection.getDmsClient().executeScript(connection.getDatabaseId(), sql);

        currentResultSet = null;
        updateCount = result.getRowCount();
        return (int) updateCount;
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        checkClosed();
        closeCurrentResultSet();
        ExecuteResult1 result = connection.getDmsClient().executeScript(connection.getDatabaseId(), sql);

        if (result.getColumnNames() != null && !result.getColumnNames().isEmpty()) {
            currentResultSet = new AliyunDmsResultSet1(this, result.getColumnNames(), result.getRows());
            updateCount = -1;
            return true;
        } else {
            currentResultSet = null;
            updateCount = result.getRowCount();
            return false;
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        checkClosed();
        return currentResultSet;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        checkClosed();
        return (int) updateCount;
    }

    @Override
    public void close() throws SQLException {
        closed = true;
        if (currentResultSet != null) {
            currentResultSet.close();
            currentResultSet = null;
        }
    }

    protected void checkClosed() throws SQLException {
        if (closed) {
            throw new SQLException("Statement is closed");
        }
    }

    /**
     * 关闭当前关联的 ResultSet（执行新查询前调用，防止资源泄漏）。
     */
    protected void closeCurrentResultSet() throws SQLException {
        if (currentResultSet != null) {
            currentResultSet.close();
            currentResultSet = null;
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    // ====== 简化实现 ======

    @Override
    public int getMaxFieldSize() throws SQLException { return 0; }

    @Override
    public void setMaxFieldSize(int max) throws SQLException { }

    @Override
    public int getMaxRows() throws SQLException { return 400; }

    @Override
    public void setMaxRows(int max) throws SQLException { }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException { }

    @Override
    public int getQueryTimeout() throws SQLException { return 0; }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException { }

    @Override
    public void cancel() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cancel not supported");
    }

    @Override
    public SQLWarning getWarnings() throws SQLException { return null; }

    @Override
    public void clearWarnings() throws SQLException { }

    @Override
    public void setCursorName(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cursor not supported");
    }

    @Override
    public boolean getMoreResults() throws SQLException { return false; }

    @Override
    public void setFetchDirection(int direction) throws SQLException { }

    @Override
    public int getFetchDirection() throws SQLException { return ResultSet.FETCH_FORWARD; }

    @Override
    public void setFetchSize(int rows) throws SQLException { }

    @Override
    public int getFetchSize() throws SQLException { return 0; }

    @Override
    public int getResultSetConcurrency() throws SQLException { return ResultSet.CONCUR_READ_ONLY; }

    @Override
    public int getResultSetType() throws SQLException { return ResultSet.TYPE_SCROLL_INSENSITIVE; }

    @Override
    public int getResultSetHoldability() throws SQLException { return ResultSet.HOLD_CURSORS_OVER_COMMIT; }

    @Override
    public boolean isClosed() throws SQLException { return closed; }

    @Override
    public void setPoolable(boolean poolable) throws SQLException { }

    @Override
    public boolean isPoolable() throws SQLException { return false; }

    @Override
    public void closeOnCompletion() throws SQLException { }

    @Override
    public boolean isCloseOnCompletion() throws SQLException { return false; }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return execute(sql);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return execute(sql);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return execute(sql);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return executeUpdate(sql);
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException { return false; }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLFeatureNotSupportedException("Generated keys not supported");
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("Batch not supported");
    }

    @Override
    public void clearBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException("Batch not supported");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException("Batch not supported");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return iface.cast(this);
        }
        throw new SQLException("Cannot unwrap to " + iface.getName());
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }
}

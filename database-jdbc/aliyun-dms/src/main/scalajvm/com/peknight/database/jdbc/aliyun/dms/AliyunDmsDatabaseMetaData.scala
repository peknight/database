package com.peknight.database.jdbc.aliyun.dms

import java.sql.{Connection, DatabaseMetaData, ResultSet, RowIdLifetime}

case class AliyunDmsDatabaseMetaData() extends DatabaseMetaData:
  def allProceduresAreCallable(): Boolean = ???

  def allTablesAreSelectable(): Boolean = ???

  def getURL: String = ???

  def getUserName: String = ???

  def isReadOnly: Boolean = ???

  def nullsAreSortedHigh(): Boolean = ???

  def nullsAreSortedLow(): Boolean = ???

  def nullsAreSortedAtStart(): Boolean = ???

  def nullsAreSortedAtEnd(): Boolean = ???

  def getDatabaseProductName: String = ???

  def getDatabaseProductVersion: String = ???

  def getDriverName: String = ???

  def getDriverVersion: String = ???

  def getDriverMajorVersion: Int = ???

  def getDriverMinorVersion: Int = ???

  def usesLocalFiles(): Boolean = ???

  def usesLocalFilePerTable(): Boolean = ???

  def supportsMixedCaseIdentifiers(): Boolean = ???

  def storesUpperCaseIdentifiers(): Boolean = ???

  def storesLowerCaseIdentifiers(): Boolean = ???

  def storesMixedCaseIdentifiers(): Boolean = ???

  def supportsMixedCaseQuotedIdentifiers(): Boolean = ???

  def storesUpperCaseQuotedIdentifiers(): Boolean = ???

  def storesLowerCaseQuotedIdentifiers(): Boolean = ???

  def storesMixedCaseQuotedIdentifiers(): Boolean = ???

  def getIdentifierQuoteString: String = ???

  def getSQLKeywords: String = ???

  def getNumericFunctions: String = ???

  def getStringFunctions: String = ???

  def getSystemFunctions: String = ???

  def getTimeDateFunctions: String = ???

  def getSearchStringEscape: String = ???

  def getExtraNameCharacters: String = ???

  def supportsAlterTableWithAddColumn(): Boolean = ???

  def supportsAlterTableWithDropColumn(): Boolean = ???

  def supportsColumnAliasing(): Boolean = ???

  def nullPlusNonNullIsNull(): Boolean = ???

  def supportsConvert(): Boolean = ???

  def supportsConvert(fromType: Int, toType: Int): Boolean = ???

  def supportsTableCorrelationNames(): Boolean = ???

  def supportsDifferentTableCorrelationNames(): Boolean = ???

  def supportsExpressionsInOrderBy(): Boolean = ???

  def supportsOrderByUnrelated(): Boolean = ???

  def supportsGroupBy(): Boolean = ???

  def supportsGroupByUnrelated(): Boolean = ???

  def supportsGroupByBeyondSelect(): Boolean = ???

  def supportsLikeEscapeClause(): Boolean = ???

  def supportsMultipleResultSets(): Boolean = ???

  def supportsMultipleTransactions(): Boolean = ???

  def supportsNonNullableColumns(): Boolean = ???

  def supportsMinimumSQLGrammar(): Boolean = ???

  def supportsCoreSQLGrammar(): Boolean = ???

  def supportsExtendedSQLGrammar(): Boolean = ???

  def supportsANSI92EntryLevelSQL(): Boolean = ???

  def supportsANSI92IntermediateSQL(): Boolean = ???

  def supportsANSI92FullSQL(): Boolean = ???

  def supportsIntegrityEnhancementFacility(): Boolean = ???

  def supportsOuterJoins(): Boolean = ???

  def supportsFullOuterJoins(): Boolean = ???

  def supportsLimitedOuterJoins(): Boolean = ???

  def getSchemaTerm: String = ???

  def getProcedureTerm: String = ???

  def getCatalogTerm: String = ???

  def isCatalogAtStart: Boolean = ???

  def getCatalogSeparator: String = ???

  def supportsSchemasInDataManipulation(): Boolean = ???

  def supportsSchemasInProcedureCalls(): Boolean = ???

  def supportsSchemasInTableDefinitions(): Boolean = ???

  def supportsSchemasInIndexDefinitions(): Boolean = ???

  def supportsSchemasInPrivilegeDefinitions(): Boolean = ???

  def supportsCatalogsInDataManipulation(): Boolean = ???

  def supportsCatalogsInProcedureCalls(): Boolean = ???

  def supportsCatalogsInTableDefinitions(): Boolean = ???

  def supportsCatalogsInIndexDefinitions(): Boolean = ???

  def supportsCatalogsInPrivilegeDefinitions(): Boolean = ???

  def supportsPositionedDelete(): Boolean = ???

  def supportsPositionedUpdate(): Boolean = ???

  def supportsSelectForUpdate(): Boolean = ???

  def supportsStoredProcedures(): Boolean = ???

  def supportsSubqueriesInComparisons(): Boolean = ???

  def supportsSubqueriesInExists(): Boolean = ???

  def supportsSubqueriesInIns(): Boolean = ???

  def supportsSubqueriesInQuantifieds(): Boolean = ???

  def supportsCorrelatedSubqueries(): Boolean = ???

  def supportsUnion(): Boolean = ???

  def supportsUnionAll(): Boolean = ???

  def supportsOpenCursorsAcrossCommit(): Boolean = ???

  def supportsOpenCursorsAcrossRollback(): Boolean = ???

  def supportsOpenStatementsAcrossCommit(): Boolean = ???

  def supportsOpenStatementsAcrossRollback(): Boolean = ???

  def getMaxBinaryLiteralLength: Int = ???

  def getMaxCharLiteralLength: Int = ???

  def getMaxColumnNameLength: Int = ???

  def getMaxColumnsInGroupBy: Int = ???

  def getMaxColumnsInIndex: Int = ???

  def getMaxColumnsInOrderBy: Int = ???

  def getMaxColumnsInSelect: Int = ???

  def getMaxColumnsInTable: Int = ???

  def getMaxConnections: Int = ???

  def getMaxCursorNameLength: Int = ???

  def getMaxIndexLength: Int = ???

  def getMaxSchemaNameLength: Int = ???

  def getMaxProcedureNameLength: Int = ???

  def getMaxCatalogNameLength: Int = ???

  def getMaxRowSize: Int = ???

  def doesMaxRowSizeIncludeBlobs(): Boolean = ???

  def getMaxStatementLength: Int = ???

  def getMaxStatements: Int = ???

  def getMaxTableNameLength: Int = ???

  def getMaxTablesInSelect: Int = ???

  def getMaxUserNameLength: Int = ???

  def getDefaultTransactionIsolation: Int = ???

  def supportsTransactions(): Boolean = ???

  def supportsTransactionIsolationLevel(level: Int): Boolean = ???

  def supportsDataDefinitionAndDataManipulationTransactions(): Boolean = ???

  def supportsDataManipulationTransactionsOnly(): Boolean = ???

  def dataDefinitionCausesTransactionCommit(): Boolean = ???

  def dataDefinitionIgnoredInTransactions(): Boolean = ???

  def getProcedures(catalog: String, schemaPattern: String, procedureNamePattern: String): ResultSet = ???

  def getProcedureColumns(catalog: String, schemaPattern: String, procedureNamePattern: String, columnNamePattern: String): ResultSet = ???

  def getTables(catalog: String, schemaPattern: String, tableNamePattern: String, types: Array[String]): ResultSet = ???

  def getSchemas: ResultSet = ???

  def getCatalogs: ResultSet = ???

  def getTableTypes: ResultSet = ???

  def getColumns(catalog: String, schemaPattern: String, tableNamePattern: String, columnNamePattern: String): ResultSet = ???

  def getColumnPrivileges(catalog: String, schema: String, table: String, columnNamePattern: String): ResultSet = ???

  def getTablePrivileges(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet = ???

  def getBestRowIdentifier(catalog: String, schema: String, table: String, scope: Int, nullable: Boolean): ResultSet = ???

  def getVersionColumns(catalog: String, schema: String, table: String): ResultSet = ???

  def getPrimaryKeys(catalog: String, schema: String, table: String): ResultSet = ???

  def getImportedKeys(catalog: String, schema: String, table: String): ResultSet = ???

  def getExportedKeys(catalog: String, schema: String, table: String): ResultSet = ???

  def getCrossReference(parentCatalog: String, parentSchema: String, parentTable: String, foreignCatalog: String, foreignSchema: String, foreignTable: String): ResultSet = ???

  def getTypeInfo: ResultSet = ???

  def getIndexInfo(catalog: String, schema: String, table: String, unique: Boolean, approximate: Boolean): ResultSet = ???

  def supportsResultSetType(`type`: Int): Boolean = ???

  def supportsResultSetConcurrency(`type`: Int, concurrency: Int): Boolean = ???

  def ownUpdatesAreVisible(`type`: Int): Boolean = ???

  def ownDeletesAreVisible(`type`: Int): Boolean = ???

  def ownInsertsAreVisible(`type`: Int): Boolean = ???

  def othersUpdatesAreVisible(`type`: Int): Boolean = ???

  def othersDeletesAreVisible(`type`: Int): Boolean = ???

  def othersInsertsAreVisible(`type`: Int): Boolean = ???

  def updatesAreDetected(`type`: Int): Boolean = ???

  def deletesAreDetected(`type`: Int): Boolean = ???

  def insertsAreDetected(`type`: Int): Boolean = ???

  def supportsBatchUpdates(): Boolean = ???

  def getUDTs(catalog: String, schemaPattern: String, typeNamePattern: String, types: Array[Int]): ResultSet = ???

  def getConnection: Connection = ???

  def supportsSavepoints(): Boolean = ???

  def supportsNamedParameters(): Boolean = ???

  def supportsMultipleOpenResults(): Boolean = ???

  def supportsGetGeneratedKeys(): Boolean = ???

  def getSuperTypes(catalog: String, schemaPattern: String, typeNamePattern: String): ResultSet = ???

  def getSuperTables(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet = ???

  def getAttributes(catalog: String, schemaPattern: String, typeNamePattern: String, attributeNamePattern: String): ResultSet = ???

  def supportsResultSetHoldability(holdability: Int): Boolean = ???

  def getResultSetHoldability: Int = ???

  def getDatabaseMajorVersion: Int = ???

  def getDatabaseMinorVersion: Int = ???

  def getJDBCMajorVersion: Int = ???

  def getJDBCMinorVersion: Int = ???

  def getSQLStateType: Int = ???

  def locatorsUpdateCopy(): Boolean = ???

  def supportsStatementPooling(): Boolean = ???

  def getRowIdLifetime: RowIdLifetime = ???

  def getSchemas(catalog: String, schemaPattern: String): ResultSet = ???

  def supportsStoredFunctionsUsingCallSyntax(): Boolean = ???

  def autoCommitFailureClosesAllResultSets(): Boolean = ???

  def getClientInfoProperties: ResultSet = ???

  def getFunctions(catalog: String, schemaPattern: String, functionNamePattern: String): ResultSet = ???

  def getFunctionColumns(catalog: String, schemaPattern: String, functionNamePattern: String, columnNamePattern: String): ResultSet = ???

  def getPseudoColumns(catalog: String, schemaPattern: String, tableNamePattern: String, columnNamePattern: String): ResultSet = ???

  def generatedKeyAlwaysReturned(): Boolean = ???

  def unwrap[T](iface: Class[T]): T = ???

  def isWrapperFor(iface: Class[?]): Boolean = ???
end AliyunDmsDatabaseMetaData
object AliyunDmsDatabaseMetaData:
  def apply(connection: AliyunDmsConnection): AliyunDmsDatabaseMetaData = AliyunDmsDatabaseMetaData()
end AliyunDmsDatabaseMetaData

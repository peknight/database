package com.peknight.database.jdbc.aliyun.dms

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.traverse.*
import com.peknight.codec.number.Number

import java.sql.{Array as _, *}

/**
 * DMS JDBC DatabaseMetaData
 *
 * 实现 getTables() 和 getColumns()，其他方法返回空结果或抛不支持
 */
case class AliyunDmsDatabaseMetaData(connection: AliyunDmsConnection) extends DatabaseMetaData:
  def allProceduresAreCallable(): Boolean = false

  def allTablesAreSelectable(): Boolean = true

  def getURL: String = s"jdbc:aliyun-dms://${connection.client.regionId}/${connection.databaseId}"

  def getUserName: String = null

  def isReadOnly: Boolean = true

  def nullsAreSortedHigh(): Boolean = false

  def nullsAreSortedLow(): Boolean = true

  def nullsAreSortedAtStart(): Boolean = false

  def nullsAreSortedAtEnd(): Boolean = false

  def getDatabaseProductName: String = "Alibaba Cloud DMS"

  def getDatabaseProductVersion: String = "1.0"

  def getDriverName: String = "jdbc-aliyun-dms"

  def getDriverVersion: String = "0.1.0"

  def getDriverMajorVersion: Int = 0

  def getDriverMinorVersion: Int = 1

  def usesLocalFiles(): Boolean = false

  def usesLocalFilePerTable(): Boolean = false

  def supportsMixedCaseIdentifiers(): Boolean = true

  def storesUpperCaseIdentifiers(): Boolean = false

  def storesLowerCaseIdentifiers(): Boolean = false

  def storesMixedCaseIdentifiers(): Boolean = true

  def supportsMixedCaseQuotedIdentifiers(): Boolean = true

  def storesUpperCaseQuotedIdentifiers(): Boolean = false

  def storesLowerCaseQuotedIdentifiers(): Boolean = false

  def storesMixedCaseQuotedIdentifiers(): Boolean = true

  def getIdentifierQuoteString: String = "`"

  def getSQLKeywords: String = ""

  def getNumericFunctions: String = ""

  def getStringFunctions: String = ""

  def getSystemFunctions: String = ""

  def getTimeDateFunctions: String = ""

  def getSearchStringEscape: String = "\\"

  def getExtraNameCharacters: String = ""

  def supportsAlterTableWithAddColumn(): Boolean = false

  def supportsAlterTableWithDropColumn(): Boolean = false

  def supportsColumnAliasing(): Boolean = true

  def nullPlusNonNullIsNull(): Boolean = true

  def supportsConvert(): Boolean = false

  def supportsConvert(fromType: Int, toType: Int): Boolean = false

  def supportsTableCorrelationNames(): Boolean = true

  def supportsDifferentTableCorrelationNames(): Boolean = false

  def supportsExpressionsInOrderBy(): Boolean = true

  def supportsOrderByUnrelated(): Boolean = true

  def supportsGroupBy(): Boolean = true

  def supportsGroupByUnrelated(): Boolean = true

  def supportsGroupByBeyondSelect(): Boolean = true

  def supportsLikeEscapeClause(): Boolean = true

  def supportsMultipleResultSets(): Boolean = false

  def supportsMultipleTransactions(): Boolean = false

  def supportsNonNullableColumns(): Boolean = true

  def supportsMinimumSQLGrammar(): Boolean = true

  def supportsCoreSQLGrammar(): Boolean = true

  def supportsExtendedSQLGrammar(): Boolean = false

  def supportsANSI92EntryLevelSQL(): Boolean = true

  def supportsANSI92IntermediateSQL(): Boolean = false

  def supportsANSI92FullSQL(): Boolean = false

  def supportsIntegrityEnhancementFacility(): Boolean = false

  def supportsOuterJoins(): Boolean = true

  def supportsFullOuterJoins(): Boolean = true

  def supportsLimitedOuterJoins(): Boolean = true

  def getSchemaTerm: String = "schema"

  def getProcedureTerm: String = "procedure"

  def getCatalogTerm: String = "catalog"

  def isCatalogAtStart: Boolean = true

  def getCatalogSeparator: String = "."

  def supportsSchemasInDataManipulation(): Boolean = false

  def supportsSchemasInProcedureCalls(): Boolean = false

  def supportsSchemasInTableDefinitions(): Boolean = false

  def supportsSchemasInIndexDefinitions(): Boolean = false

  def supportsSchemasInPrivilegeDefinitions(): Boolean = false

  def supportsCatalogsInDataManipulation(): Boolean = false

  def supportsCatalogsInProcedureCalls(): Boolean = false

  def supportsCatalogsInTableDefinitions(): Boolean = false

  def supportsCatalogsInIndexDefinitions(): Boolean = false

  def supportsCatalogsInPrivilegeDefinitions(): Boolean = false

  def supportsPositionedDelete(): Boolean = false

  def supportsPositionedUpdate(): Boolean = false

  def supportsSelectForUpdate(): Boolean = false

  def supportsStoredProcedures(): Boolean = false

  def supportsSubqueriesInComparisons(): Boolean = true

  def supportsSubqueriesInExists(): Boolean = true

  def supportsSubqueriesInIns(): Boolean = true

  def supportsSubqueriesInQuantifieds(): Boolean = true

  def supportsCorrelatedSubqueries(): Boolean = true

  def supportsUnion(): Boolean = true

  def supportsUnionAll(): Boolean = true

  def supportsOpenCursorsAcrossCommit(): Boolean = false

  def supportsOpenCursorsAcrossRollback(): Boolean = false

  def supportsOpenStatementsAcrossCommit(): Boolean = false

  def supportsOpenStatementsAcrossRollback(): Boolean = false

  def getMaxBinaryLiteralLength: Int = 0

  def getMaxCharLiteralLength: Int = 0

  def getMaxColumnNameLength: Int = 0

  def getMaxColumnsInGroupBy: Int = 0

  def getMaxColumnsInIndex: Int = 0

  def getMaxColumnsInOrderBy: Int = 0

  def getMaxColumnsInSelect: Int = 0

  def getMaxColumnsInTable: Int = 0

  def getMaxConnections: Int = 0

  def getMaxCursorNameLength: Int = 0

  def getMaxIndexLength: Int = 0

  def getMaxSchemaNameLength: Int = 0

  def getMaxProcedureNameLength: Int = 0

  def getMaxCatalogNameLength: Int = 0

  def getMaxRowSize: Int = 0

  def doesMaxRowSizeIncludeBlobs(): Boolean = false

  def getMaxStatementLength: Int = 0

  def getMaxStatements: Int = 0

  def getMaxTableNameLength: Int = 0

  def getMaxTablesInSelect: Int = 0

  def getMaxUserNameLength: Int = 0

  def getDefaultTransactionIsolation: Int = Connection.TRANSACTION_NONE

  def supportsTransactions(): Boolean = false

  def supportsTransactionIsolationLevel(level: Int): Boolean = level == Connection.TRANSACTION_NONE

  def supportsDataDefinitionAndDataManipulationTransactions(): Boolean = false

  def supportsDataManipulationTransactionsOnly(): Boolean = false

  def dataDefinitionCausesTransactionCommit(): Boolean = false

  def dataDefinitionIgnoredInTransactions(): Boolean = false

  def getProcedures(catalog: String, schemaPattern: String, procedureNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("PROCEDURE_CAT", "PROCEDURE_SCHEM", "PROCEDURE_NAME", "REMARKS", "PROCEDURE_TYPE", "SPECIFIC_NAME")

  def getProcedureColumns(catalog: String, schemaPattern: String, procedureNamePattern: String, columnNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("PROCEDURE_CAT", "PROCEDURE_SCHEM", "PROCEDURE_NAME", "COLUMN_NAME", "COLUMN_TYPE", "DATA_TYPE", "TYPE_NAME")

  def getTables(catalog: String, schemaPattern: String, tableNamePattern: String, types: Array[String]): ResultSet =
    val tableType: String = "TABLE"
    val typeFilter: Set[String] = Option(types).map(_.toSet).getOrElse(Set.empty)
    val columnNames: Vector[String] = Vector("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS")
    connection.client.listTables[IO](connection.databaseId, Option(tableNamePattern))
      .flatMap(tables => AliyunDmsResultSet(None, columnNames, tables
        .filter(table => typeFilter.isEmpty || typeFilter.contains(tableType))
        .map(table => Map(
          "TABLE_NAME" -> Option(table.getTableName).getOrElse(""),
          "TABLE_TYPE" -> tableType,
          "REMARKS" -> ""
        ))
      ))
      .unsafeRunSync()

  def getSchemas: ResultSet = AliyunDmsResultSet.empty("TABLE_SCHEM", "TABLE_CATALOG")

  def getCatalogs: ResultSet = AliyunDmsResultSet.empty("TABLE_CAT")

  def getTableTypes: ResultSet =
    AliyunDmsResultSet(None, Vector("TABLE_TYPE"), List(Map("TABLE_TYPE" -> "TABLE"))).unsafeRunSync()

  def getColumns(catalog: String, schemaPattern: String, tableNamePattern: String, columnNamePattern: String): ResultSet =
    val columnNames: Vector[String] = Vector("TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME",
      "COLUMN_SIZE", "NULLABLE", "REMARKS", "ORDINAL_POSITION")
    val io =
      for
        tables <- connection.client.listTables[IO](connection.databaseId, Option(tableNamePattern))
        rows <- tables
          .traverse { table =>
            Option(table.getTableId).filter(_.nonEmpty).flatMap(Number.fromString).flatMap(_.toLong) match
              case Some(tableId) => connection.client.listColumns[IO](tableId)
                .map(columns => columns.zipWithIndex
                  .filter((column, _) => !Option(columnNamePattern).exists(p => p.nonEmpty && !"%".equals(p)) ||
                    Option(column.getColumnName).exists(columnName => toRegex(columnNamePattern).matches(columnName)))
                  .map((column, index) => Map[String, Any](
                    "TABLE_NAME" -> Option(table.getTableName).getOrElse(""),
                    "COLUMN_NAME" -> Option(column.getColumnName).getOrElse(""),
                    "DATA_TYPE" -> Types.VARCHAR,
                    "TYPE_NAME" -> Option(column.getColumnType).getOrElse("VARCHAR"),
                    "COLUMN_SIZE" -> 0,
                    "NULLABLE" -> (
                      if Option(column.getNullable).exists(_.booleanValue()) then DatabaseMetaData.columnNullable
                      else DatabaseMetaData.columnNoNulls
                    ),
                    "REMARKS" -> Option(column.getDescription).getOrElse(""),
                    "ORDINAL_POSITION" -> (index + 1)
                  )))
              case _ => IO(Nil)
          }
          .map(_.flatten)
        res <- AliyunDmsResultSet(None, columnNames, rows)
      yield
        res
    io.unsafeRunSync()

  def getColumnPrivileges(catalog: String, schema: String, table: String, columnNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "GRANTOR", "GRANTEE", "PRIVILEGE", "IS_GRANTABLE")

  def getTablePrivileges(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "GRANTOR", "GRANTEE", "PRIVILEGE", "IS_GRANTABLE")

  def getBestRowIdentifier(catalog: String, schema: String, table: String, scope: Int, nullable: Boolean): ResultSet =
    AliyunDmsResultSet.empty("SCOPE", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH")

  def getVersionColumns(catalog: String, schema: String, table: String): ResultSet =
    AliyunDmsResultSet.empty("SCOPE", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH")

  def getPrimaryKeys(catalog: String, schema: String, table: String): ResultSet =
    AliyunDmsResultSet.empty("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "KEY_SEQ", "PK_NAME")

  def getImportedKeys(catalog: String, schema: String, table: String): ResultSet =
    AliyunDmsResultSet.empty("PKTABLE_CAT", "PKTABLE_SCHEM", "PKTABLE_NAME", "PKCOLUMN_NAME",
      "FKTABLE_CAT", "FKTABLE_SCHEM", "FKTABLE_NAME", "FKCOLUMN_NAME",
      "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE", "FK_NAME", "PK_NAME", "DEFERRABILITY")

  def getExportedKeys(catalog: String, schema: String, table: String): ResultSet =
    AliyunDmsResultSet.empty("PKTABLE_CAT", "PKTABLE_SCHEM", "PKTABLE_NAME", "PKCOLUMN_NAME",
      "FKTABLE_CAT", "FKTABLE_SCHEM", "FKTABLE_NAME", "FKCOLUMN_NAME",
      "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE", "FK_NAME", "PK_NAME", "DEFERRABILITY")

  def getCrossReference(parentCatalog: String, parentSchema: String, parentTable: String, foreignCatalog: String, foreignSchema: String, foreignTable: String): ResultSet =
    AliyunDmsResultSet.empty("PKTABLE_CAT", "PKTABLE_SCHEM", "PKTABLE_NAME", "PKCOLUMN_NAME",
      "FKTABLE_CAT", "FKTABLE_SCHEM", "FKTABLE_NAME", "FKCOLUMN_NAME",
      "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE", "FK_NAME", "PK_NAME", "DEFERRABILITY")

  def getTypeInfo: ResultSet =
    AliyunDmsResultSet.empty("TYPE_NAME", "DATA_TYPE", "PRECISION", "LITERAL_PREFIX", "LITERAL_SUFFIX",
      "CREATE_PARAMS", "NULLABLE", "CASE_SENSITIVE", "SEARCHABLE", "UNSIGNED_ATTRIBUTE",
      "FIXED_PREC_SCALE", "AUTO_INCREMENT", "LOCAL_TYPE_NAME", "MINIMUM_SCALE", "MAXIMUM_SCALE",
      "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "NUM_PREC_RADIX")

  def getIndexInfo(catalog: String, schema: String, table: String, unique: Boolean, approximate: Boolean): ResultSet =
    AliyunDmsResultSet.empty("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "NON_UNIQUE", "INDEX_QUALIFIER",
      "INDEX_NAME", "TYPE", "ORDINAL_POSITION", "COLUMN_NAME", "ASC_OR_DESC",
      "CARDINALITY", "PAGES", "FILTER_CONDITION")

  def supportsResultSetType(`type`: Int): Boolean = `type` == ResultSet.TYPE_SCROLL_INSENSITIVE

  def supportsResultSetConcurrency(`type`: Int, concurrency: Int): Boolean =
    `type` == ResultSet.TYPE_SCROLL_INSENSITIVE && concurrency == ResultSet.CONCUR_READ_ONLY

  def ownUpdatesAreVisible(`type`: Int): Boolean = false

  def ownDeletesAreVisible(`type`: Int): Boolean = false

  def ownInsertsAreVisible(`type`: Int): Boolean = false

  def othersUpdatesAreVisible(`type`: Int): Boolean = false

  def othersDeletesAreVisible(`type`: Int): Boolean = false

  def othersInsertsAreVisible(`type`: Int): Boolean = false

  def updatesAreDetected(`type`: Int): Boolean = false

  def deletesAreDetected(`type`: Int): Boolean = false

  def insertsAreDetected(`type`: Int): Boolean = false

  def supportsBatchUpdates(): Boolean = false

  def getUDTs(catalog: String, schemaPattern: String, typeNamePattern: String, types: Array[Int]): ResultSet =
    AliyunDmsResultSet.empty("TYPE_CAT", "TYPE_SCHEM", "TYPE_NAME", "CLASS_NAME", "DATA_TYPE", "REMARKS", "BASE_TYPE")

  def getConnection: Connection = connection

  def supportsSavepoints(): Boolean = false

  def supportsNamedParameters(): Boolean = false

  def supportsMultipleOpenResults(): Boolean = false

  def supportsGetGeneratedKeys(): Boolean = false

  def getSuperTypes(catalog: String, schemaPattern: String, typeNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("TYPE_CAT", "TYPE_SCHEM", "TYPE_NAME", "SUPERTYPE_CAT", "SUPERTYPE_SCHEM", "SUPERTYPE_NAME")

  def getSuperTables(catalog: String, schemaPattern: String, tableNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "SUPERTABLE_NAME")

  def getAttributes(catalog: String, schemaPattern: String, typeNamePattern: String, attributeNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("TYPE_CAT", "TYPE_SCHEM", "TYPE_NAME", "ATTR_NAME", "DATA_TYPE", "ATTR_TYPE_NAME")

  def supportsResultSetHoldability(holdability: Int): Boolean = holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT

  def getResultSetHoldability: Int = ResultSet.HOLD_CURSORS_OVER_COMMIT

  def getDatabaseMajorVersion: Int = 1

  def getDatabaseMinorVersion: Int = 0

  def getJDBCMajorVersion: Int = 4

  def getJDBCMinorVersion: Int = 2

  def getSQLStateType: Int = DatabaseMetaData.sqlStateSQL

  def locatorsUpdateCopy(): Boolean = false

  def supportsStatementPooling(): Boolean = false

  def getRowIdLifetime: RowIdLifetime = RowIdLifetime.ROWID_UNSUPPORTED

  def getSchemas(catalog: String, schemaPattern: String): ResultSet =
    AliyunDmsResultSet.empty("TABLE_SCHEM", "TABLE_CATALOG")

  def supportsStoredFunctionsUsingCallSyntax(): Boolean = false

  def autoCommitFailureClosesAllResultSets(): Boolean = false

  def getClientInfoProperties: ResultSet =
    AliyunDmsResultSet.empty("NAME", "MAX_LEN", "DEFAULT_VALUE", "DESCRIPTION")

  def getFunctions(catalog: String, schemaPattern: String, functionNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("FUNCTION_CAT", "FUNCTION_SCHEM", "FUNCTION_NAME", "REMARKS", "FUNCTION_TYPE", "SPECIFIC_NAME")

  def getFunctionColumns(catalog: String, schemaPattern: String, functionNamePattern: String, columnNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("FUNCTION_CAT", "FUNCTION_SCHEM", "FUNCTION_NAME", "COLUMN_NAME", "COLUMN_TYPE", "DATA_TYPE", "TYPE_NAME")

  def getPseudoColumns(catalog: String, schemaPattern: String, tableNamePattern: String, columnNamePattern: String): ResultSet =
    AliyunDmsResultSet.empty("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "COLUMN_SIZE")

  def generatedKeyAlwaysReturned(): Boolean = false

  def unwrap[T](iface: Class[T]): T = handleUnwrap(iface)

  def isWrapperFor(iface: Class[?]): Boolean = iface.isInstance(this)
end AliyunDmsDatabaseMetaData

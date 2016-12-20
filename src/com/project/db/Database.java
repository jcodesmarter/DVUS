/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author RavirajS
 */
public interface Database {          
    
    
    public Connection getConnection() throws ClassNotFoundException, SQLException;

    public boolean testConnection();
    
    public void testMetaData() throws ClassNotFoundException, SQLException;
    
    public Vector<String> getTablesList() throws ClassNotFoundException, SQLException;
    
    public Vector<String> getColumnsList(String tableName, boolean WITH_TABLE_NAME) throws ClassNotFoundException, SQLException;

    public Vector<String> getColumnsList(Vector<String> tableNameList, boolean WITH_TABLE_NAME) throws ClassNotFoundException, SQLException;
    
    public HashMap<String, String> getColumnProperties(String tableName, String columnName) throws ClassNotFoundException, SQLException;

    public HashMap<String, String> getPrimaryKeys() throws SQLException, ClassNotFoundException;

    public Vector<String> getForeignKeys() throws ClassNotFoundException, SQLException;
    
    public HashMap<String, String> getForeignKeyReference(String constraintName) throws SQLException, ClassNotFoundException;

    public Vector<String> getUniqueKeys() throws ClassNotFoundException, SQLException;

    public Vector<String> getUniqueKeyColumns(String constraintName) throws SQLException, ClassNotFoundException;

    public String getTableNameForConstraint(String constraintName) throws SQLException, ClassNotFoundException;

    //public String modifyColumnQuery(String tableName, String columnName, String dataType, String columnSize, String decimalDigit, String isNull, String isAutoIncrement, String defaultValue);
    public String modifyColumnQuery(String tableName, String columnName, String dataType, String defaultValue, String isNullable, String key, String extra);
    
    //public String addColumnQuery(String tableName, String columnName, String dataType, String columnSize, String isNull, String isAutoIncrement, String defaultValue) throws ClassNotFoundException, SQLException;;
    public String addColumnQuery(String tableName, String columnName, String dataType, String defaultValue, String isNullable, String key, String extra);

    public String getCreateQueriesForMissingTables(String tableName, Database master) throws ClassNotFoundException, SQLException;

    public Vector<String> getQueriesToAddModifyUniqueKeys(String tableName, String constraintName, Vector<String> columnList, boolean dropRequired);

    public String getAlterQueriesToAddPrimaryKeys(String tableName, String columnName) throws SQLException, ClassNotFoundException;

    public String getAlterQueriesToAddForeignKey(Database master, String tableName, String foreignKeyName) throws SQLException, ClassNotFoundException;
    
    public String getVendor();

    public void execute(Vector<String> queryContainer);
}

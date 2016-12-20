/*
 * To change this template, choose Tools|Templates
 * and open the template in the editor.
 */
package com.project.db;

import java.sql.DatabaseMetaData;
import com.project.logger.Logs;
import com.project.ui.MainPanel;
import com.project.util.Helper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author RavirajS
 */
public class Mysql implements Database {

    private Connection CONNECTION;
    private String IPADDRESS;
    private String PORT;
    private String DATABASE;
    private String USERNAME;
    private String PASSWORD;

    public Mysql(String ipaddress, String port, String database, String username, String password) {
        IPADDRESS = ipaddress;
        PORT = port;
        DATABASE = database;
        USERNAME = username;
        PASSWORD = password;
        if (MainPanel.DEBUG) {
            Logs.write("Using Connection: MySQL|" + IPADDRESS + "|" + PORT + "|" + DATABASE);
        }

    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        CONNECTION = DriverManager.getConnection("jdbc:mysql://" + IPADDRESS + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
        return CONNECTION;

    }

    @Override
    public boolean testConnection() {
        Connection con;
        boolean test = true;
        try {
            con = getConnection();
            con.close();
        } catch (ClassNotFoundException cnfe) {
            Logs.write("MySQL connection driver is missing", cnfe);
            JOptionPane.showMessageDialog(null, cnfe.getMessage(), "Connection Failed", JOptionPane.ERROR_MESSAGE);
            test = false;
        } catch (SQLException sqle) {
            Logs.write("MySQL connection failure", sqle);
            JOptionPane.showMessageDialog(null, sqle.getMessage(), "Connection Failed", JOptionPane.ERROR_MESSAGE);
            test = false;
        }

        return test;

    }

    @Override
    public void testMetaData() throws ClassNotFoundException, SQLException {
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        //ResultSet rs = stmt.executeQuery("SHOW INDEXES FROM with_foreign_key_constraints");
        //ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM with_foreign_key_constraints");
        //ResultSet rs = stmt.executeQuery("SELECT * FROM information_schema.KEY_COLUMN_USAGE");
        //ResultSet rs = stmt.executeQuery("SELECT * FROM information_schema.TABLE_CONSTRAINTS");


        DatabaseMetaData dbMeta = getConnection().getMetaData();

        //Tables List
        //ResultSet rs = dbMeta.getTables(null, null, "%", new String[]{"TABLE"});

        //Tables / views / Synonym list
        //ResultSet rs = dbMeta.getTables(null, null, "%", new String[]{"TABLE","VIEW","SYSTEM TABLE","ALIAS","SYNONYM"});        

        //Columns List for tables
        ResultSet rs = dbMeta.getColumns(null, null, "%", "%");

        //Primary Keys for tables        
        //ResultSet rs = dbMeta.getPrimaryKeys(null, null, "with_primary_key_constraints");


        ResultSetMetaData rsmd = rs.getMetaData();
        int cnt = rsmd.getColumnCount();

        StringBuffer sbuff = new StringBuffer();
        for (int n = 1; n <= cnt; n++) {
            sbuff.append(rsmd.getColumnName(n));
            if (n != cnt) {
                sbuff.append(" \t ");
            }
        }
        Logs.write_nodate(sbuff.toString());

        while (rs.next()) {
            sbuff = new StringBuffer();
            for (int i = 1; i <= cnt; i++) {
                sbuff.append(rs.getString(i));
                if (i != cnt) {
                    sbuff.append(" \t ");
                }
            }
            Logs.write_nodate(sbuff.toString());
        }


    }

    @Override
    public Vector<String> getTablesList() throws ClassNotFoundException, SQLException {
        Connection con = getConnection();
        DatabaseMetaData dbMeta = con.getMetaData();
        Vector<String> tables = new Vector<String>();

        ResultSet rs = dbMeta.getTables(null, null, "%", new String[]{"TABLE"});
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        if (MainPanel.DEBUG) {
            Logs.write("GET TABLES LIST {" + DATABASE + "}: " + tables.toString());
        }
        rs.close();
        con.close();
        return tables;
    }

    @Override
    public Vector<String> getColumnsList(String tableName, boolean WITH_TABLE_NAME) throws ClassNotFoundException, SQLException {
        Connection con = getConnection();
        DatabaseMetaData dbMeta = con.getMetaData();
        Vector<String> columnForTable = new Vector<String>();

        ResultSet rs = dbMeta.getColumns(null, null, tableName, "%");

        while (rs.next()) {
            if (WITH_TABLE_NAME) {
                columnForTable.add(rs.getString("TABLE_NAME") + "." + rs.getString("COLUMN_NAME"));
            } else {
                columnForTable.add(rs.getString("COLUMN_NAME"));
            }
        }
        if (MainPanel.DEBUG) {
            Logs.write("GET COLUMNS LIST {" + DATABASE + "} [" + tableName + "]: " + columnForTable.toString());
        }
        rs.close();
        con.close();
        return columnForTable;
    }
//    @Override
//    public HashMap<String, String> getColumnProperties(String tableName, String columnName) throws ClassNotFoundException, SQLException {
//        DatabaseMetaData dbMeta = getConnection().getMetaData();
//        HashMap<String, String> columnPropsForTable = new HashMap<String, String>();
//
//        ResultSet rs = dbMeta.getColumns(null, null, tableName, columnName);
//        if (MainPanel.DEBUG) {
//            Logs.write("Working on Table {" + DATABASE + "}: " + tableName + "|Column: " + columnName);
//
//            while (rs.next()) {
//                //add TABLE_NAME
//                //columnPropsForTable.put("TABLE_NAME", rs.getString("TABLE_NAME"));
//                //columnPropsForTable.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
//                columnPropsForTable.put("DATA_TYPE", rs.getString("TYPE_NAME"));
//                if (MainPanel.DEBUG) {
//                    Logs.write("DATA_TYPE:" + rs.getString("TYPE_NAME"));
//                }
//                columnPropsForTable.put("COLUMN_SIZE", rs.getString("COLUMN_SIZE"));
//                if (MainPanel.DEBUG) {
//                    Logs.write("COLUMN_SIZE:" + rs.getString("COLUMN_SIZE"));
//                }
//                String decimalDigits = rs.getString("DECIMAL_DIGITS");
//                if (decimalDigits == null) {
//                    decimalDigits = "null";
//                }
//                columnPropsForTable.put("DECIMAL_DIGITS", decimalDigits);
//                if (MainPanel.DEBUG) {
//                    Logs.write("DECIMAL_DIGITS:" + decimalDigits);
//                }
//
//                columnPropsForTable.put("NULLABLE", rs.getString("NULLABLE"));
//                if (MainPanel.DEBUG) {
//                    Logs.write("NULLABLE:" + rs.getString("NULLABLE"));
//                }
//                columnPropsForTable.put("IS_NULLABLE", rs.getString("IS_NULLABLE"));
//                if (MainPanel.DEBUG) {
//                    Logs.write("IS_NULLABLE:" + rs.getString("IS_NULLABLE"));
//                }
//                String defaultValue = rs.getString("COLUMN_DEF");
//                if (defaultValue == null) {
//                    defaultValue = "null";
//                }
//                columnPropsForTable.put("DEFAULT_VALUE", defaultValue);
//                if (MainPanel.DEBUG) {
//                    Logs.write("DEFAULT_VALUE:" + defaultValue);
//                }
//                columnPropsForTable.put("IS_AUTOINCREMENT", rs.getString("IS_AUTOINCREMENT"));
//                if (MainPanel.DEBUG) {
//                    Logs.write("IS_AUTOINCREMENT:" + rs.getString("IS_AUTOINCREMENT"));
//                }
//            }
//        }
//        return columnPropsForTable;
//    }

    @Override
    public HashMap<String, String> getColumnProperties(String tableName, String columnName) throws ClassNotFoundException, SQLException {
        HashMap<String, String> map = new HashMap<String, String>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT column_type, column_default, is_nullable, column_key, extra from INFORMATION_SCHEMA.COLUMNS");
        query.append(" WHERE table_schema = '" + DATABASE + "' AND table_name = '" + tableName + "' AND column_name = '" + columnName + "'");
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query.toString());
        if (rs.next()) {
            map.put("DATA_TYPE", rs.getString(1));
            map.put("DEFAULT_VALUE", rs.getString(2));
            map.put("IS_NULLABLE", rs.getString(3));
            map.put("KEY", rs.getString(4));
            map.put("EXTRA", rs.getString(5));
        }
        if (MainPanel.DEBUG) {
            Set<String> key = map.keySet();
            Iterator<String> itr = key.iterator();
            while (itr.hasNext()) {
                String head = itr.next();
                Logs.write("GET COLUMN PROPERTIES: " + tableName + "|" + columnName + "|" + head + "=" + map.get(head));
            }
        }
        rs.close();
        stmt.close();
        con.close();
        return map;
    }

    public Vector<String> getColumnsList(Vector<String> tableNameList, boolean WITH_TABLE_NAME) throws ClassNotFoundException, SQLException {
        Connection con = getConnection();
        DatabaseMetaData dbMeta = con.getMetaData();
        Vector<String> columnForTable = new Vector<String>();

        Enumeration<String> tables = tableNameList.elements();
        while (tables.hasMoreElements()) {
            String tableName = tables.nextElement();
            ResultSet rs = dbMeta.getColumns(null, null, tableName, "%");

            while (rs.next()) {
                if (WITH_TABLE_NAME) {
                    columnForTable.add(rs.getString("TABLE_NAME") + "." + rs.getString("COLUMN_NAME"));
                } else {
                    columnForTable.add(rs.getString("COLUMN_NAME"));
                }
            }

            if (MainPanel.DEBUG) {
                Logs.write("GET COLUMNS LIST {" + DATABASE + "} [" + tableName + "]: " + columnForTable.toString());
            }
            rs.close();
            con.close();
        }
        return columnForTable;
    }

    @Override
    public HashMap<String, String> getPrimaryKeys() throws SQLException, ClassNotFoundException {
        HashMap<String, String> map = new HashMap<String, String>();
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        StringBuilder query = new StringBuilder();
        query.append("SELECT table_name, column_name FROM INFORMATION_SCHEMA.key_column_usage WHERE table_schema = '" + DATABASE + "' AND constraint_name = 'PRIMARY'");
        ResultSet rs = stmt.executeQuery(query.toString());



        while (rs.next()) {
            map.put(rs.getString(1), rs.getString(2));


        }
        if (MainPanel.DEBUG) {
            Set<String> tablesList = map.keySet();
            Iterator<String> itr = tablesList.iterator();
            Logs.write("Listing Primary Keys");


            while (itr.hasNext()) {
                String table = itr.next();
                Logs.write("Get Primary Keys {" + DATABASE + "}: " + table + "|" + map.get(table));


            }
        }
        rs.close();
        stmt.close();
        con.close();
        return map;


    }

    @Override
    public Vector<String> getForeignKeys() throws ClassNotFoundException, SQLException {
        Vector<String> foreignKeyList = new Vector<String>();
        Connection con =getConnection();
        Statement stmt = con.createStatement();
        StringBuilder query = new StringBuilder();
        query.append("SELECT constraint_name FROM INFORMATION_SCHEMA.key_column_usage");
        query.append(" WHERE table_schema = '" + DATABASE + "' AND");
        query.append(" constraint_name in (SELECT constraint_name FROM INFORMATION_SCHEMA.table_constraints");
        query.append(" WHERE table_schema = '" + DATABASE + "' AND");
        query.append(" constraint_type = 'FOREIGN KEY')");
        ResultSet rs = stmt.executeQuery(query.toString());


        if (rs.next()) {
            foreignKeyList.add(rs.getString(1));


        }
        if (MainPanel.DEBUG) {
            Logs.write("Get Foreign Keys {" + DATABASE + "}: " + foreignKeyList.toString());


        }
        rs.close();
        stmt.close();
        con.close();
        return foreignKeyList;


    }

    @Override
    public HashMap<String, String> getForeignKeyReference(String constraintName) throws SQLException, ClassNotFoundException {
        HashMap<String, String> referenceDetails = new HashMap<String, String>();
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        StringBuilder query = new StringBuilder();
        query.append("SELECT table_name, column_name, referenced_table_name, referenced_column_name FROM INFORMATION_SCHEMA.key_column_usage");
        query.append(" WHERE constraint_schema = '" + DATABASE + "' AND constraint_name = '" + constraintName + "'");
        ResultSet rs = stmt.executeQuery(query.toString());


        if (rs.next()) {
            referenceDetails.put("TABLE_NAME", rs.getString(1));
            referenceDetails.put("COLUMN_NAME", rs.getString(2));
            referenceDetails.put("REFERENCE_TABLE_NAME", rs.getString(3));
            referenceDetails.put("REFERENCE_COLUMN_NAME", rs.getString(4));



            if (MainPanel.DEBUG) {
                Logs.write("Get Foreign Key References {" + DATABASE + "}: " + rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getString(3) + "|" + rs.getString(4));


            }
        }
        rs.close();
        stmt.close();
        con.close();
        return referenceDetails;


    }

    @Override
    public Vector<String> getUniqueKeys() throws ClassNotFoundException, SQLException {
        Vector<String> uniqueKeyList = new Vector<String>();
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        StringBuilder query = new StringBuilder();
        query.append("SELECT constraint_name FROM INFORMATION_SCHEMA.key_column_usage");
        query.append(" WHERE table_schema = '" + DATABASE + "' AND");
        query.append(" constraint_name in (SELECT constraint_name FROM INFORMATION_SCHEMA.table_constraints");
        query.append(" WHERE table_schema = '" + DATABASE + "' AND");
        query.append(" constraint_type = 'UNIQUE')");
        ResultSet rs = stmt.executeQuery(query.toString());


        while (rs.next()) {
            uniqueKeyList.add(rs.getString(1));


        }
        if (MainPanel.DEBUG) {
            Logs.write("Get Unique Keys {" + DATABASE + "}: " + uniqueKeyList.toString());


        }
        rs.close();
        stmt.close();
        con.close();
        return uniqueKeyList;


    }

    @Override
    public Vector<String> getUniqueKeyColumns(String constraintName) throws SQLException, ClassNotFoundException {
        Vector<String> columns = new Vector<String>();
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        StringBuilder query = new StringBuilder();
        query.append("SELECT column_name FROM INFORMATION_SCHEMA.key_column_usage WHERE constraint_schema = '" + DATABASE + "' AND constraint_name = '" + constraintName + "';");
        ResultSet rs = stmt.executeQuery(query.toString());


        while (rs.next()) {
            columns.add(rs.getString(1));


        }
        if (MainPanel.DEBUG) {
            Logs.write("Unique Key Columns {" + DATABASE + "}: " + constraintName + "|" + columns.toString());


        }
        rs.close();
        stmt.close();
        con.close();
        return columns;


    }

    @Override
    public String getTableNameForConstraint(String constraintName) throws SQLException, ClassNotFoundException {
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        StringBuilder query = new StringBuilder();
        query.append("SELECT table_name FROM INFORMATION_SCHEMA.table_constraints WHERE constraint_schema = '" + DATABASE + "' AND constraint_name = '" + constraintName + "'");
        ResultSet rs = stmt.executeQuery(query.toString());


        if (rs.next()) {
            if (MainPanel.DEBUG) {
                Logs.write("Get Table Name For Constraint {" + DATABASE + "}: " + constraintName + "|" + rs.getString(1));


            }
            return rs.getString(1);


        }
        rs.close();
        stmt.close();
        con.close();
        return "";


    }

//    @Override
//    public String modifyColumnQuery(String tableName, String columnName, String dataType, String columnSize, String decimalDigits, String isNull, String isAutoIncrement, String defaultValue) {
//        StringBuilder query = new StringBuilder();
//        query.append("ALTER TABLE " + tableName);
//        query.append(" MODIFY COLUMN " + columnName);
//        query.append(" " + dataType);
//
//
//        if (!getListOfDataTypesNotRequireLength().contains(dataType)) {
//
//            if (getListOfDecimalDataTypes().contains(dataType)) {
//                columnSize = columnSize + "," + decimalDigits;
//
//
//            }
//            query.append("(" + columnSize + ")");
//
//
//        }
//        if (defaultValue != null) {
//                query.append(" DEFAULT '" + defaultValue + "'");
//
//
//        }
//        if (isNull.equalsIgnoreCase("Yes")) {
//            query.append(" NOT NULL");
//
//
//        }
//        if (isAutoIncrement.equalsIgnoreCase("Yes")) {
//            query.append(" AUTO_INCREMENT");
//
//
//        }
//
//        return query.toString();
//
//
//    }
//    @Override
//    public String addColumnQuery(String tableName, String columnName, String dataType, String columnSize, String isNull, String isAutoIncrement, String defaultValue) {
//        StringBuilder query = new StringBuilder();
//        query.append("ALTER TABLE " + tableName);
//        query.append(" ADD COLUMN " + columnName);
//        query.append(" " + dataType);
//        query.append("(" + columnSize + ")");
//
//
//        if (defaultValue != null) {
//            query.append(" DEFAULT '" + defaultValue + "'");
//
//
//        }
//        if (isNull.equalsIgnoreCase("Yes")) {
//            query.append(" NOT NULL");
//
//
//        }
//        if (isAutoIncrement.equalsIgnoreCase("Yes")) {
//            query.append(" AUTO_INCREMENT");
//
//
//        }
//
//        return query.toString();
//
//
//    }
    @Override
    public String modifyColumnQuery(String tableName, String columnName, String dataType, String defaultValue, String isNullable, String key, String extra) {
        StringBuilder query = new StringBuilder();
        query.append("ALTER TABLE " + tableName);
        query.append(" MODIFY COLUMN " + columnName);
        query.append(" " + dataType);
        if (defaultValue != null) {
            if (!getListOfKeyVariables().contains(defaultValue.toUpperCase())) {
                defaultValue = "'" + defaultValue + "'";
            }
            query.append(" DEFAULT " + defaultValue);
        }
        if (!isNullable.equalsIgnoreCase("Yes")) {
            query.append(" NOT NULL");
        }
        if (key != null) {
            if (key.equalsIgnoreCase("PRI")) {
                query.append(" PRIMARY KEY ");
            }
        }
        if (extra != null) {
            if (!extra.equalsIgnoreCase("")) {
                query.append(" " + extra);
            }
        }
        return query.toString();


    }

    @Override
    public String addColumnQuery(String tableName, String columnName, String dataType, String defaultValue, String isNullable, String key, String extra) {
        StringBuilder query = new StringBuilder();
        query.append("ALTER TABLE " + tableName);
        query.append(" ADD COLUMN " + columnName);
        query.append(" " + dataType);
        if (defaultValue != null) {
            if (!getListOfKeyVariables().contains(defaultValue.toUpperCase())) {
                defaultValue = "'" + defaultValue + "'";
            }
            query.append(" DEFAULT " + defaultValue);
        }
        if (!isNullable.equalsIgnoreCase("Yes")) {
            query.append(" NOT NULL");
        }
        if (key != null) {
            if (key.equalsIgnoreCase("PRI")) {
                query.append(" PRIMARY KEY ");
            }
        }
        if (extra != null) {
            if (!extra.equalsIgnoreCase("")) {
                query.append(" " + extra);
            }
        }
        return query.toString();

    }

//    @Override
//    public String getCreateQueriesForMissingTables(String tableName, Database master) throws ClassNotFoundException, SQLException {
//        StringBuilder query = new StringBuilder();
//        query.append("CREATE TABLE " + tableName + "(");
//        Vector<String> columnList = master.getColumnsList(tableName, false);
//        Enumeration<String> columns = columnList.elements();
//
//
//        int i = 0;
//        while (columns.hasMoreElements()) {
//            if (i != 0) {
//                query.append(", ");
//            } else {
//                i++;
//            }
//            String column = columns.nextElement();
//            HashMap<String, String> columnMap = master.getColumnProperties(tableName, column);
//            query.append(" " + column);
//            String dataType = columnMap.get("DATA_TYPE");
//            String dataLength = "";
//            if (!getListOfDataTypesNotRequireLength().contains(dataType)) {
//                if (getListOfDecimalDataTypes().contains(dataType)) {
//                    dataLength = "(" + columnMap.get("COLUMN_SIZE") + "," + columnMap.get("DECIMAL_DIGITS") + ")";
//
//                } else {
//                    dataLength = "(" + columnMap.get("COLUMN_SIZE") + ")";
//                }
//            }
//            query.append(" " + dataType + dataLength);
//            String defaultValue = columnMap.get("DEFAULT_VALUE");
//            if (defaultValue == null) {
//                defaultValue = "null";
//            }
//                query.append(" DEFAULT '" + defaultValue + "'");
//            String isNull = columnMap.get("IS_NULLABLE");
//            if (isNull.equalsIgnoreCase("Yes")) {
//                query.append(" NOT NULL");
//
//
//            } //String isAutoIncrement = columnMap.get("IS_AUTOINCREMENT");
//            //if (isAutoIncrement.equalsIgnoreCase("Yes")) {
//            //    query.append(" AUTO_INCREMENT");
//            //}
//        }
//        query.append(")");
//        return query.toString();
//    }
    @Override
    public String getCreateQueriesForMissingTables(String tableName, Database master) throws ClassNotFoundException, SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE " + tableName + "(");
        Vector<String> columnList = master.getColumnsList(tableName, false);
        Enumeration<String> columns = columnList.elements();


        int i = 0;
        while (columns.hasMoreElements()) {
            if (i != 0) {
                query.append(", ");
            } else {
                i++;
            }
            String column = columns.nextElement();
            HashMap<String, String> columnMap = master.getColumnProperties(tableName, column);
            query.append(" " + column);
            String dataType = columnMap.get("DATA_TYPE");
            query.append(" " + dataType);
            String defaultValue = columnMap.get("DEFAULT_VALUE");
            if (defaultValue != null) {
                if (!getListOfKeyVariables().contains(defaultValue.toUpperCase())) {
                    defaultValue = "'" + defaultValue + "'";
                }
                query.append(" DEFAULT " + defaultValue);
            }
            String isNull = columnMap.get("IS_NULLABLE");
            if (!isNull.equalsIgnoreCase("Yes")) {
                query.append(" NOT NULL");
            }
            String key = columnMap.get("KEY");
            if (key != null) {
                if (key.equalsIgnoreCase("PRI")) {
                    query.append(" PRIMARY KEY ");

                }
            }
            String extra = columnMap.get("EXTRA");
            if (extra != null) {
                if (!extra.equalsIgnoreCase("")) {
                    query.append(" " + extra);
                }
            }
            //String isAutoIncrement = columnMap.get("IS_AUTOINCREMENT");
            //if (isAutoIncrement.equalsIgnoreCase("Yes")) {
            //    query.append(" AUTO_INCREMENT");
            //}
        }
        query.append(")");
        return query.toString();
    }

    @Override
    public Vector<String> getQueriesToAddModifyUniqueKeys(String tableName, String constraintName, Vector<String> columnList, boolean dropRequired) {
        //ALTER TABLE form_pattern drop index field_name_2
        //ALTER TABLE form_pattern drop constraint field_name_2
        //ALTER TABLE form_pattern drop index field_name;
        //alter table form_pattern add CONSTRAINT field_name unique(field_name, component_type);
        Vector<String> queries = new Vector<String>();


        if (dropRequired) {
            queries.add("ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraintName);


        }
        queries.add("ALTER table " + tableName + " ADD CONSTRAINT " + constraintName + " UNIQUE(" + Helper.convertVetorToCSV(columnList) + ")");


        return queries;


    }

    @Override
    public String getAlterQueriesToAddPrimaryKeys(String tableName, String columnName) throws SQLException, ClassNotFoundException {
        //ALTER TABLE Persons ADD PRIMARY KEY (P_Id)
        StringBuilder query = new StringBuilder();
        query.append("ALTER TABLE " + tableName + " ADD PRIMARY KEY (" + columnName + ")");


        return query.toString();


    }

    @Override
    public String getAlterQueriesToAddForeignKey(Database master, String tableName, String foreignKeyName) throws SQLException, ClassNotFoundException {
        //ALTER TABLE Orders ADD CONSTRAINT fk_PerOrders FOREIGN KEY (P_Id) REFERENCES Persons(P_Id)
        HashMap<String, String> refProps = master.getForeignKeyReference(foreignKeyName);
        StringBuilder query = new StringBuilder();
        query.append("ALTER TABLE " + refProps.get("TABLE_NAME"));
        query.append(" ADD CONSTRAINT " + foreignKeyName);
        query.append(" FOREIGN KEY (" + refProps.get("COLUMN_NAME") + ")");
        query.append(" REFERENCES " + refProps.get("REFERENCE_TABLE_NAME") + "(" + refProps.get("REFERENCE_COLUMN_NAME") + ")");
        return query.toString();


    }

    @Override
    public String getVendor() {
        return "Mysql";


    }

    private Vector<String> getListOfDataTypesNotRequireLength() {
        Vector<String> temp = new Vector();
        temp.add("DATE");
        temp.add("TIME");
        temp.add("DATETIME");
        temp.add("TIMESTAMP");
        temp.add("YEAR");
        temp.add("TINYTEXT");
        temp.add("TINYBLOB");
        temp.add("TEXT");
        temp.add("BLOB");
        temp.add("MEDIUMTEXT");
        temp.add("MEDIUMBLOB");
        temp.add("LONGTEXT");
        temp.add("LONGBLOB");
        temp.add("ENUM");
        temp.add("SET");


        return temp;


    }

    private Vector<String> getListOfDecimalDataTypes() {
        Vector<String> temp = new Vector();
        temp.add("FLOAT");
        temp.add("DOUBLE");
        temp.add("DECIMAL");


        return temp;

    }

    private Vector<String> getListOfKeyVariables() {
        Vector<String> temp = new Vector();
        temp.add("CURRENT_TIMESTAMP");
        temp.add("NULL");
        return temp;

    }

    public void execute(Vector<String> queryContainer) {
        StringBuilder msg = new StringBuilder();
        Enumeration<String> queries = queryContainer.elements();
        String query = "";
        int errorCounts = 0;
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            stmt.setQueryTimeout(3600);
            while (queries.hasMoreElements()) {
                try {
                    query = queries.nextElement();
                    int res = stmt.executeUpdate(query);
                    if (res > 0) {
                        Logs.write(query + " --> Successfully Executed");
                    } else if (res == 0) {
                        Logs.write(query + " --> No Change Affected");
                    }
                } catch (SQLException sqle) {
                    errorCounts++;
                    Logs.write(query + " --> Execution Failed", sqle);
                }
            }
            stmt.close();
            con.close();
            msg.append("Upgrade Completed");
            if (errorCounts == 0) {
                msg.append(" Successfully\n");
            } else {
                msg.append("With Errors\n");
                msg.append("Error counts: " + errorCounts);
            }

        } catch (ClassNotFoundException cnfe) {
            msg.append("" + cnfe.getMessage() + "\n");
            msg.append("Couldn't Execute Queries\n");
            Logs.write("Driver not found", cnfe);
        } catch (SQLException sqle) {
            msg.append("" + sqle.getMessage() + "\n");
            msg.append("Couldn't Execute Queries\n");
            Logs.write("SQL Error", sqle);
        }
        JOptionPane.showMessageDialog(null, msg.toString());

    }
}

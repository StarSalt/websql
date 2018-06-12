package org.singledog.websql.user.entity;

/**
 * Created by Adam on 2017/9/7.
 */
public enum DbType {

    MYSQL("com.mysql.jdbc.Driver"),
    SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    DB2("COM.ibm.db2.jdbc.app.DB2Driver"),
    ORACLE("oracle.jdbc.driver.OracleDriver");

    private String driver;

    private DbType(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }

}

package org.singledog.websql.query;

import java.sql.Connection;

/**
 * Created by adam on 9/17/17.
 */
public interface ConnectionCallback<T> {

    public T doWithConn(Connection connection) throws Exception;

}

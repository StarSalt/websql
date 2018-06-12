package org.singledog.websql.web.param;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Adam on 2017/12/8.
 */
public class CollectParam  {

    @NotEmpty
    private String title;
    @NotEmpty
    private String sql;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}

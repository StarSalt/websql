package org.singledog.websql.web.param;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Adam on 2017/9/20.
 */
public class QueryParam {

    @NotNull
    private Long did;// database id
    @NotEmpty
    private String sql;
    /**
     * 收藏夹ID
     */
    private Long cid;

    private Integer sel;

    public Integer getSel() {
        return sel;
    }

    public void setSel(Integer sel) {
        this.sel = sel;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
}

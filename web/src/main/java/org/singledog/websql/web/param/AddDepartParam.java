package org.singledog.websql.web.param;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by adam on 9/17/17.
 */
public class AddDepartParam {

    @NotEmpty
    private String name;
    private Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

package org.singledog.websql.user.entity;

import java.io.Serializable;

/**
 * @author wupeng  * @version 1.0  * @date 2015-10-08  * @modify  * @copyright Navi Tsp
 */
public interface Identified<ID> extends Serializable {
    public void generateID();

    public void setId(ID id);

    public ID getId();
}


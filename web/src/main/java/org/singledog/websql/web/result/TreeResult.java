package org.singledog.websql.web.result;

import org.singledog.websql.user.entity.Depart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 9/17/17.
 */
public class TreeResult {

    private Long id;
    private String text;
    private String state;
    private List<TreeResult> children = new ArrayList<>( );

    public TreeResult() {
    }

    public TreeResult(Depart depart) {
        this.id = depart.getId();
        this.text = depart.getDepartName();
    }

    public TreeResult(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<TreeResult> getChildren() {
        return children;
    }

    public void setChildren(List<TreeResult> children) {
        this.children = children;
    }
}

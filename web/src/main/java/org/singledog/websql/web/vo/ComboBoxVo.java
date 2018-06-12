package org.singledog.websql.web.vo;

/**
 * Created by Adam on 2017/9/15.
 */
public class ComboBoxVo {

    private String id;
    private String text;
    private boolean selected = false;
    private String desc;

    public ComboBoxVo() {
    }

    public ComboBoxVo(String id, String text) {
        this(id, text, false);
    }

    public ComboBoxVo(String id, String text, boolean selected) {
        this.id = id;
        this.text = text;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

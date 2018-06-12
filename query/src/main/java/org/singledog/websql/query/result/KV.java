package org.singledog.websql.query.result;

/**
 * Created by Adam on 2017/9/20.
 */
public class KV {

    private String k;
    private String v;

    public KV() {
    }

    public KV(String k, String v) {
        this.k = k;
        this.v = v;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}

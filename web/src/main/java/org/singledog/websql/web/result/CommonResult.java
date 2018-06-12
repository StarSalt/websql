package org.singledog.websql.web.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.MDC;

@ApiModel
public class CommonResult<T> {

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int httpCode;
    @ApiModelProperty("返回码")
    private int code;
    @ApiModelProperty("返回信息")
    private String message;
    @ApiModelProperty("返回数据")
    private T data;
    private String rid;

    public CommonResult() {
        super();
        this.rid = MDC.get("rid");
    }

    public CommonResult(CommonResult t) {
        this(t.getHttpCode(), t.getCode(), t.getMessage(), null);
    }

    public CommonResult(int httpCode, int code, String message, T data) {
        super();
        this.httpCode = httpCode;
        this.code = code;
        this.message = message;
        this.data = data;
        this.rid = MDC.get("rid");
    }

    public CommonResult<T> fillResult(ReturnResult result) {
        this.code = result.code();
        this.message = result.message();
        this.httpCode = result.httpCode();
        return this;
    }

    public static <T> CommonResult<T> newInstance(ReturnResult result, Class<T> clazz) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.fillResult(result == null ? ResultCode.SUCCESS : result);
        return commonResult;
    }

    public int getCode() {
        return code;
    }

    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    public boolean isSuccess() {
        return this.getCode() == ResultCode.SUCCESS.code();
    }

    public CommonResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public T getData() {
        return data;
    }

    public CommonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

}
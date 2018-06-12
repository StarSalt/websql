package org.singledog.websql.user.util;

import org.apache.ibatis.utils.StringUtils;

/**
 * Created by Adam on 2017/9/15.
 */
public class StringUtil {

    public static String wrapLike(String var) {
        return "%".concat(var).concat("%");
    }

    public static String wrapLikeLeft(String var) {
        return "%".concat(var);
    }

    public static String wrapLikeRight(String var) {
        return var.concat("%");
    }

    public static String emptyToNull(String var) {
        return StringUtils.isEmpty(var) ? null : var;
    }
}

package org.singledog.websql.user.service;

/**
 * Created by Adam on 2017/8/14.
 */
public interface SaltGenerator {

    String randomSalt();

    String wrapSalt(String raw, String salt);

}

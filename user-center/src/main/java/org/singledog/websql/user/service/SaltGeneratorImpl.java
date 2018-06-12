package org.singledog.websql.user.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Adam on 2017/8/14.
 */
@Service
public class SaltGeneratorImpl implements SaltGenerator {

    public static final int length = 8;

    @Override
    public String randomSalt() {
        char[] chs = new char[length];
        for (int i = 0; i < length; i++) {
            chs[i] = (char) ThreadLocalRandom.current().nextInt(33, 127);
        }

        return new String(chs);
    }

    @Override
    public String wrapSalt(String raw, String salt) {
        return salt + raw;
    }
}

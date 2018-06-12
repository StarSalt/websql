package org.singledog.websql.user.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Adam on 2017/9/14.
 */
public class RandomUtil {

    public static String randomNumber(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(String.valueOf(random.nextInt(10)));
        }

        return builder.toString();
    }

}

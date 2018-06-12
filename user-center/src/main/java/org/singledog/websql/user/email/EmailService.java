package org.singledog.websql.user.email;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.IOUtils;
import org.singledog.websql.user.entity.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Adam on 2017/8/15.
 */
@Service
public class EmailService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static LoadingCache<String, String> localCache;

    @Autowired
    private MailSendBackend mailSendBackend;

    public String sendEmail(String email, Functions functions, Map<String, String> params) {
        try {
            String content = localCache.get(functions.name());
            content = replace(content, params);
            mailSendBackend.sendHtmlEmail(email, functions.getSubject(), content);
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    private String replace(String rawContent, Map<String, String> params) {
        if (!CollectionUtils.isEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = "${".concat(entry.getKey()).concat("}");
                while (rawContent.contains(key))
                    rawContent = rawContent.replace(key, entry.getValue());
            }
        }

        return rawContent;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        localCache = CacheBuilder.newBuilder()
                .concurrencyLevel(1)
                .expireAfterWrite(10, TimeUnit.DAYS)
                .initialCapacity(Functions.values().length)
                .maximumSize(Functions.values().length)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        String path = "template/" + key + ".html";
                        ClassPathResource resource = new ClassPathResource(path);
                        if (!resource.exists() || !resource.isReadable()) {
                            throw new IllegalArgumentException("resource not exists or can not read ! " + path);
                        }

                        try (InputStream is = resource.getInputStream()){
                            return IOUtils.toString(is, "UTF-8");
                        }
                    }
                });
    }
}

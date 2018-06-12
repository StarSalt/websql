package org.singledog.websql.user.audit;

import org.singledog.websql.user.event.LoggerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * Created by Adam on 2017/9/12.
 */
@Service
public class OperateLogEventListener implements ApplicationListener<LoggerEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OperateLogEventListener.class);

    @Autowired
    private OperateLogService operateLogService;

    @Override
    public void onApplicationEvent(LoggerEvent event) {
        logger.info("received event : {}", event);
        int i = operateLogService.save(event.toOperateLog());
        if (i == 1)
            logger.info("save event success !");
        else
            logger.error("save event failed !");
    }
}

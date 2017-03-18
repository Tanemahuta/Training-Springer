package ch.eike.springer.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * Created by Tanemahuta on 18.03.17.
 */
@Component
@Slf4j
public class DefaultTaskExecutor extends ThreadPoolTaskScheduler {

    public static final int POOL_SIZE = 2;

    /**
     * Default constructor, sets the pool size to {@link #POOL_SIZE}
     */
    public DefaultTaskExecutor() {
        setThreadGroupName(getClass().getName());
        setPoolSize(POOL_SIZE);
        log.info("Task executor initialized.");
    }

}

package org.gitee.ztkyn.web.config.ttl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import static java.lang.Thread.currentThread;
import static org.gitee.ztkyn.web.config.ttl.TimeThreadLocal.TIME;

/**
 * 拦截器
 *
 * @author w.dehai
 */
@Slf4j
public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("     java interceptor in: tid: " + currentThread().getId() + ", time: " + TIME.get());
        return true;
    }
}

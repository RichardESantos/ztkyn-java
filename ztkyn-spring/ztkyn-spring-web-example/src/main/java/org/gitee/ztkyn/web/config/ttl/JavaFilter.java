package org.gitee.ztkyn.web.config.ttl;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static org.gitee.ztkyn.web.config.ttl.TimeThreadLocal.TIME;

@Slf4j
@WebFilter
@Component
public class JavaFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long time = currentTimeMillis();
        TIME.set(time);
        log.info("          java filter in: tid: " + currentThread().getId() + ", time: " + time);
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("         java filter out: tid: " + currentThread().getId() + ", time: " + TIME.get());
    }
}

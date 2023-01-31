package org.gitee.ztkyn.web.config.ttl;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static java.lang.Thread.currentThread;
import static org.gitee.ztkyn.web.config.ttl.TimeThreadLocal.TIME;

@Slf4j
@Component
public class SpringFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		log.info("        spring filter in: tid: " + currentThread().getId() + ", time: " + TIME.get());
		filterChain.doFilter(httpServletRequest, httpServletResponse);
		log.info("       spring filter out: tid: " + currentThread().getId() + ", time: " + TIME.get());
	}

}

package cl.wom.api.controller.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Order(2)
public class LoggingFilter implements Filter {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		// Set trace id to logger
		MDC.put("trace-id", UUID.randomUUID().toString().substring(0, 8));

		filterChain.doFilter(request, response);

		if (request instanceof ContentCachingRequestWrapper) {
			ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
			String payload = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
			log.info("Request [{}: {}] \n{}", requestWrapper.getMethod(), requestWrapper.getRequestURI(), payload);

			Enumeration<String> headerNames = requestWrapper.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				log.info("- Header [{} : {}]", headerName, requestWrapper.getHeader(headerName));
			}

		}

		if (response instanceof ContentCachingResponseWrapper) {
			ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
			String payload = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
			log.info("Response [ {} ] \n{}", responseWrapper.getStatus(), payload);
		}

		MDC.clear();

	}

}
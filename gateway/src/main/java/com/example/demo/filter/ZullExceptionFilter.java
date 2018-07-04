package com.example.demo.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class ZullExceptionFilter extends ZuulFilter {
	protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return -1;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		if (ctx.getThrowable() != null)
			return true;
		else
			return false;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.set(SEND_ERROR_FILTER_RAN);
		Throwable cause = ctx.getThrowable().getCause();
		Map<String, Object> res = new HashMap<>();
		if (isRateLimitException(cause.getStackTrace())) {
			ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ctx.set("error.exception", "Too Many");
			try {
				ctx.getResponse().getWriter().write("too many request per rule");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return res;
	}

	private boolean isRateLimitException(StackTraceElement[] stacks) {
		for (StackTraceElement e : stacks) {
			if (null != e.getClassName() && e.getClassName().contains("RateLimitPreFilter")) {
				return true;
			}
		}
		return false;
	}

}

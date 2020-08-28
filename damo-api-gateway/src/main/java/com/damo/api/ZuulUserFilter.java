package com.damo.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;

@Component
public class ZuulUserFilter extends ZuulFilter {
	
    private static Logger logger = LoggerFactory.getLogger(ZuulUserFilter.class);
	
    @Override
	public Object run() throws ZuulException {
    	logger.info("run");
		return null;
	}

	@Override
	public boolean shouldFilter() {
    	logger.info("shouldFilter");
		return true;
	}

	@Override
	public int filterOrder() {
    	logger.info("filterOrder");
		return 5;
	}

	@Override
	public String filterType() {
    	logger.info("filterType");
    	return "pre";
	}

}

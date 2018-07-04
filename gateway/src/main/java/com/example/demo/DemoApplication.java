package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.annotation.Bean;

import com.example.demo.filter.ZullExceptionFilter;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.DefaultRateLimitKeyGenerator;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitKeyGenerator;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.RateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitUtils;

@SpringBootApplication
@EnableZuulProxy
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public ZullExceptionFilter requestCheckFilter() {
		return new ZullExceptionFilter();
	}
	
	 @Bean
	  public RateLimitKeyGenerator ratelimitKeyGenerator(RateLimitProperties properties, RateLimitUtils rateLimitUtils) {
	      return new DefaultRateLimitKeyGenerator(properties, rateLimitUtils) {
	          @Override
	          public String key(HttpServletRequest request, Route route, RateLimitProperties.Policy policy) {
	        	  String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	        	  String ip = request.getRemoteHost();
	        	  String idendity = request.getParameter("idendity");
	        	  String mobile = request.getParameter("mobile");
	        	  String videoId = request.getParameter("videoId");
	              return String.format("%s:%s:%s:%s:%s",today,idendity,ip,mobile,videoId);
	              //super.key(request, route, policy) + ":" + request.getMethod();
	          }
	      };
	  }

	@Bean
	public RateLimiterErrorHandler rateLimitErrorHandler() {
		return new DefaultRateLimiterErrorHandler() {
			@Override
			public void handleSaveError(String key, Exception e) {
				System.out.println("=handleSaveError=");
				throw new RuntimeException(e);
			}

			@Override
			public void handleFetchError(String key, Exception e) {
				System.out.println("=handleFetchError=");
				throw new RuntimeException(e);
			}

			@Override
			public void handleError(String msg, Exception e) {
				System.out.println("=handleError=");
				throw new RuntimeException(e);
			}
		};
	}
}

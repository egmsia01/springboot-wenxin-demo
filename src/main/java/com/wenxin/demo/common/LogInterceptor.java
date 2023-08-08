package com.wenxin.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 请求响应日志 AOP
 *
 **/
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    private final Map<String, List<Long>> ipRequestRecords = new ConcurrentHashMap<>();
    private final Map<String, Integer> ipHourlyRequestCounts = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public LogInterceptor() {
        // 设置定时任务，每小时清理一次请求记录
        scheduler.scheduleAtFixedRate(this::clearExpiredRecords, 1, 1, TimeUnit.HOURS);
    }

    @Around("execution(* com.wenxin.demo.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        String ip = httpServletRequest.getRemoteAddr();
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest2 = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 生成请求唯一 id
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest2.getRequestURI();
        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";
        // 输出请求日志
        log.info("request start，id: {}, path: {}, ip: {}, params: {}", requestId, url,
                httpServletRequest2.getRemoteHost(), reqParam);
        // 检查小时内请求次数
        int hourlyRequestCount = ipHourlyRequestCounts.getOrDefault(ip, 0);
        if (hourlyRequestCount >= 50) {
            log.warn("IP {} has been blacklisted due to excessive requests.", ip);
            return null; // You can customize the response when an IP is blacklisted.
        }

        // 记录请求时间
        long currentTimeMillis = System.currentTimeMillis();
        List<Long> requests = ipRequestRecords.computeIfAbsent(ip, k -> new ArrayList<>());
        requests.add(currentTimeMillis);

        // 清理1分钟前的请求记录
        requests.removeIf(timestamp -> timestamp < currentTimeMillis - 60000);

        // 检查1分钟内请求次数
        if (requests.size() > 10) {
            log.warn("IP {} has made too many requests in the last minute.", ip);
            return null; // You can customize the response when an IP is rate limited.
        }

        // 执行原方法
        return point.proceed();
    }

    private void clearExpiredRecords() {
        long currentTimeMillis = System.currentTimeMillis();
        ipRequestRecords.forEach((ip, requests) -> {
            requests.removeIf(timestamp -> timestamp < currentTimeMillis - 3600000);
            ipHourlyRequestCounts.put(ip, requests.size());
        });
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}



package com.mujakjung.global.config.datasource;

import com.mujakjung.global.enums.DataSourceType;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Slf4j
@Order(0)
public class DataSourceRoutingAspect {

    @Around("@annotation(transactional)")
    public Object setDataSource(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        try {
            // 해당 트랜잭션이 readOnly인 경우 READ DataSource를 사용하도록 설정
            if (transactional.readOnly()) {
                DataSourceContextHolder.setDataSourceType(DataSourceType.READ);
                log.info("Set DataSource to READ");
            } else {
                DataSourceContextHolder.setDataSourceType(DataSourceType.WRITE);
                log.info("Set DataSource to WRITE");
            }
            return joinPoint.proceed();
        } finally {
            // 메소드 실행 후에는 반드시 컨텍스트를 비워줍니다.
            log.info("Clear DataSource Context");
            DataSourceContextHolder.clear();
        }
    }
}

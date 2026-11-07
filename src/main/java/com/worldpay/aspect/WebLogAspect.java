package com.worldpay.aspect;
import com.worldpay.filter.JWTAuthenticationFilter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.log.SysLogDao;
import com.worldpay.entity.system.SysLogEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * @category 日志统一管理
 * @author csz
 *
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Autowired
    private SysLogDao sysLogDao;

    private static final ThreadLocal<SysLogEntity> LOG_HOLDER = new ThreadLocal<>();

    @Pointcut("execution(public * com.worldpay.service..*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null) {
        	 HttpServletRequest request = attributes.getRequest();
             UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);

             SysLogEntity entity = new SysLogEntity();
             if (token != null) {
                 entity.setUserName(token.getName());
             }

             log.info("##################### 请求开始 ####################");
             log.info("URL : " + request.getRequestURL().toString());
             log.info("HTTP_METHOD : " + request.getMethod());
             log.info("IP : " + request.getRemoteAddr());

             entity.setReqUrl(request.getRequestURL().toString());
             entity.setIpUrl(request.getRemoteAddr());
             entity.setBegTime(new Date());

             MethodSignature signature = (MethodSignature) joinPoint.getSignature();
             Method method = signature.getMethod();
             SysLogAnnotation sysLogAnnotation = method.getAnnotation(SysLogAnnotation.class);

             if (sysLogAnnotation != null) {
                 entity.setModuleName(sysLogAnnotation.module());
                 entity.setReqType(sysLogAnnotation.type());
                 entity.setModuleInfo(sysLogAnnotation.remark());
             }

             Object[] args = joinPoint.getArgs();
             if (args != null && args.length > 0 && args[0] != null) {
                 try {
                     String parameterJson = JSON.toJSONString(args[0]);
                     log.info("PARAMETER：" + request.getRequestURL() + "-[" + parameterJson + "]");
                     entity.setReqParamet("[" + parameterJson + "]");
                 } catch (JSONException e) {
                     log.error("PARAMETER：" + request.getRequestURL() + "-[]");
                     entity.setReqParamet("[]");
                 }
             }

             Enumeration<String> enu = request.getParameterNames();
             while (enu.hasMoreElements()) {
                 String name = enu.nextElement();
                 log.info("name:{" + name + "},value:{" + request.getParameter(name) + "}");
             }

             LOG_HOLDER.set(entity); // 存入线程副本
        }
       
    }
    
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint point, Object ret) throws Throwable {
        SysLogEntity entity = LOG_HOLDER.get();
        if (entity == null) {
            return;
        }

        String type = point.getSignature().toLongString().split(" ")[1];
        if (type != null && !"void".equals(type) && !"String".equals(type)) {
            try {
                if (ret != null) {
                    String responseStr;
                    
                    if (ret instanceof String) {
                        // 字符串类型直接使用
                        responseStr = (String) ret;
                        log.info("RESPONSE (String) : " + responseStr);
                    } 
                    else if (ret instanceof ResponseBase) {
                        // ResponseBase 类型，获取其中的信息
                        ResponseBase base = (ResponseBase) ret;
                        responseStr = JSON.toJSONString(ret);
                        log.info("RESPONSE (ResponseBase) : " + point.getSignature().toString().substring(point.getSignature().toString().indexOf(" ")) + "-" + base.getMsg());
                    }
                    else if (ret instanceof Number) {
                        // 数字类型
                        responseStr = String.valueOf(ret);
                        log.info("RESPONSE (Number) : " + responseStr);
                    }
                    else {
                        // 其他类型，尝试序列化，如果失败就用 toString
                        try {
                            responseStr = JSON.toJSONString(ret);
                            log.info("RESPONSE (Object) : " + ret.getClass().getSimpleName() + " - " + responseStr);
                        } catch (Exception e) {
                            responseStr = ret.toString();
                            log.info("RESPONSE (Object toString) : " + responseStr);
                        }
                    }
                    
                    entity.setResData(responseStr);
                }
            } catch (Exception e) {
                log.error("处理响应数据时发生错误", e);
                entity.setResData(ret == null ? "" : ret.toString());
            }

            // 设置结束时间
            entity.setEndTime(new Date());

            // 插入日志到数据库
            if (entity.getModuleName() != null && !entity.getModuleName().isEmpty()) {
                try {
                    sysLogDao.insert(entity);
                } catch (Exception e) {
                    log.error("插入日志失败", e);
                }
            }
        }

        log.info("##################### 请求结束 ####################");

        // 清理当前线程副本，防止内存泄漏
        LOG_HOLDER.remove();
    }

}

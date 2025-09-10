package com.liuyang1.impl.components;

import com.liuyang1.impl.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 扫描项目中所有 controller uri 路径
 */
@Component
@Slf4j
public class ControllerScanner {
    @Autowired
    private RequestMappingHandlerMapping requestHandlerMapping;

    public static Set<String> CONTROLLER_URIS = new HashSet<>();

    @PostConstruct
    public void printAllEndpoints() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestHandlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
//            HandlerMethod handlerMethod = entry.getValue();

            Set<String> patterns = requestMappingInfo.getPatternValues();
//            Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();

//            log.info("Controller: " + handlerMethod.getBeanType().getName());
//            log.info("Method: " + handlerMethod.getMethod().getName());
//            log.info("URI Patterns: " + patterns);
//            log.info("HTTP Methods: " + methods);

            for (String uri : patterns) {
                if (uri.equals("/error")) {
                    continue;
                }
                CONTROLLER_URIS.add(uri);
            }
        }

        log.info("all uri Patterns: " + GsonUtils.toJson(CONTROLLER_URIS));
    }
}

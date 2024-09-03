package com.liuyang1.spring_learning.groovyscript;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.DigestUtils;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@Slf4j
public class GroovyScriptApplication {
    private static Random rand = new Random();

    private static Map<String, Script> SCRIPT_CACHE = new ConcurrentHashMap<>();

    private final static String GROOVY_SCRIPT = "package scripts\n" +
            "\n" +
            "println(\"current qps is: \" + qps);\n" +
            "\n" +
            "if (qps <= 15) {\n" +
            "    return 100;\n" +
            "} else if (qps <= 20) {\n" +
            "    return 70;\n" +
            "} else if (qps <= 25) {\n" +
            "    return 30;\n" +
            "} else if (qps <= 30) {\n" +
            "    return 20;\n" +
            "} else {\n" +
            "    return 0;\n" +
            "}";

    private final static String QPS_SCRIPT = "package scripts;\n" +
            "\n" +
            "int decideQps(int qps) {\n" +
            "    println(\"current qps is: \" + qps);\n" +
            "\n" +
            "    if (qps <= 15) {\n" +
            "        return 100;\n" +
            "    } else if (qps <= 20) {\n" +
            "        return 70;\n" +
            "    } else if (qps <= 25) {\n" +
            "        return 30;\n" +
            "    } else if (qps <= 30) {\n" +
            "        return 20;\n" +
            "    } else {\n" +
            "        return 0;\n" +
            "    }\n" +
            "}\n";

    public static void main(String[] args) {
        SpringApplication.run(GroovyScriptApplication.class, args);
        for (int i = 1; i <= 100_000; i++) {
            printMemoryUsage(String.valueOf(i));
//            usingGroovyShell();
            basicGroovyScriptUsage2();
        }

//        basicGroovyScriptUsage();
//        usingGroovyShell();
//        usingScriptEngineManager();
    }

    public static void printMemoryUsage(String extra) {
        // 获取Java虚拟机中的运行时环境
        Runtime runtime = Runtime.getRuntime();

        // 总内存
        double totalMemoryMb = Math.round(runtime.totalMemory() * 1.0 / 1024 / 1024);
        // 剩余内存
        double freeMemoryMb = Math.round(runtime.freeMemory() * 1.0 / 1024 / 1024);
        // 已使用内存
        double usedMemoryMb = totalMemoryMb - freeMemoryMb;

        log.info("{} - memory usage: {}/{}", extra, usedMemoryMb, totalMemoryMb);
    }

    public static void basicGroovyScriptUsage() {
        // file 模式下，如何bind 变量？
        // 答：invokeMethod 的时候传递方法参数即可
//        String filePath = "src/main/resources/scripts/Hello.groovy";
//        File groovyFile = new File(filePath);
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        try {
            Class groovyClass = groovyClassLoader.parseClass(QPS_SCRIPT);
            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();

            int qps = 5 + rand.nextInt(36);
            Object result = groovyObject.invokeMethod("decideQps", qps);
            log.info("groovy exec result: {}", result);
        } catch (Exception e) {
            log.error("groovy exec error: {}", e.getMessage());
        }
    }

    /**
     * 生产环境方案：Script 去重，不要重复 parse groovy 脚本，这样会导致内存泄漏
     * 不会内存泄漏，运行速度快
     */
    public static void basicGroovyScriptUsage2() {
        long startTime = System.currentTimeMillis();
        Script script;

        String scriptMd5 = DigestUtils.md5DigestAsHex(QPS_SCRIPT.getBytes(StandardCharsets.UTF_8)).substring(8);
        if (SCRIPT_CACHE.containsKey(scriptMd5)) {
            script = SCRIPT_CACHE.get(scriptMd5);
        } else {
            script = new GroovyShell().parse(QPS_SCRIPT);
            SCRIPT_CACHE.put(scriptMd5, script);
        }

        int qps = 5 + rand.nextInt(36);
        int result = (int) script.invokeMethod("decideQps", qps);
        log.info("groovy exec result: {}, cost: {}", result, System.currentTimeMillis() - startTime);
    }

    public static void usingGroovyShell() {
        long startTime = System.currentTimeMillis();
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        int qps = 5 + rand.nextInt(36);
        shell.setVariable("qps", qps);
//        String script = GROOVY_SCRIPT;
        final Object eval = shell.evaluate(GROOVY_SCRIPT);
        log.info("shell exec result: {}, cost: {}", eval.toString(), System.currentTimeMillis() - startTime);
    }

    public static void usingScriptEngineManager() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        Bindings bindings = engine.createBindings();
        bindings.put("date", new Date());

        try {
            engine.eval("def getTime() {return date.getTime();}", bindings);
            engine.eval("def sayHello(name, age) {return 'Hello, I am ' + name + ',age ' + age;}");
            Long time = (Long) ((Invocable) engine).invokeFunction("getTime");
            log.info("scriptEngineManager, time: {}", time);
            String message = (String) ((Invocable) engine).invokeFunction("sayHello", "张三", 22);
            log.info("scriptEngineManager, sayHello: {}", message);
        } catch (ScriptException | NoSuchMethodException e) {
            log.error("scriptEngineManager exec error: {}", e.getMessage());
        }

    }
}

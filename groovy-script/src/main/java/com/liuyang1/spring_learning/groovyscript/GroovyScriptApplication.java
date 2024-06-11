package com.liuyang1.spring_learning.groovyscript;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@SpringBootApplication
@Slf4j
public class GroovyScriptApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroovyScriptApplication.class, args);

        basicGroovyScriptUsage();
        usingGroovyShell();
        usingScriptEngineManager();
    }

    public static void basicGroovyScriptUsage() {
        // todo- file 模式下，如何bind 变量？
        String filePath = "src/main/resources/scripts/Hello.groovy";
        File groovyFile = new File(filePath);
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        try {
            Class groovyClass = groovyClassLoader.parseClass(groovyFile);
            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
            Object result = groovyObject.invokeMethod("say", "Hello ABC");
            log.info("groovy exec result: {}", result);
        } catch (IOException e) {
            log.error("groovy exec error: {}", e.getMessage());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void usingGroovyShell() {
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);

        String script = "Runtime.getRuntime().availableProcessors()";
        final Object eval = shell.evaluate(script);
        log.info("shell exec result: {}", eval.toString());
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

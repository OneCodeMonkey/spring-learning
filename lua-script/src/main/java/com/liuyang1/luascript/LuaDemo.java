package com.liuyang1.luascript;

import lombok.extern.slf4j.Slf4j;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LuaDemo {
    public void execute() {
        String dirPath = System.getProperty("user.dir");
        log.info("path: " + dirPath);
        String path = dirPath + "/src/main/java/com/liuyang1/luascript/luaScripts/test.lua";

        Globals globals = JsePlatform.standardGlobals();

        LuaValue chunk = globals.loadfile(path);
        chunk.call();
    }
}

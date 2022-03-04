package com.liuyang1.luascript;

import lombok.extern.slf4j.Slf4j;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.springframework.stereotype.Service;

import java.util.List;

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

    static class getAirTemperature extends OneArgFunction {
        float lowerBound = 10, higherBound = 40;

        @Override
        public LuaValue call(LuaValue luaValue) {
            if (luaValue.isstring() && LuaValue.valueOf("ac_temp").equals(luaValue)) {
                double curSetTemp = lowerBound + Math.random() * (higherBound - lowerBound + 1);

                return LuaValue.valueOf(curSetTemp);
            } else {
                return LuaValue.NIL;
            }
        }
    }

    static class getNluResult extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            String nluResult = "{\"data\":{\"vehicleId\":\"100020002\",\"traceId\":\"100020003\",\"answer\":[],\"domain\":\"air_conditioner\",\"intentList\":[{\"intent\":\"air_conditioner_temp_turn_down_value\",\"slots\":{\"number\":\"3\",\"temperature\":\"3\",\"ordinal\":\"3\"}}],\"utterance\":\"空调温度调高3度\",\"tts\":\"好的，已为您调高温度\",\"extend\":\"origin intent: i_car_control_temp_turn_up_value ,raw result: domain: air_conditioner  intent: air_conditioner_temp_turn_up_value  slots: {\\\\\\\"number\\\\\\\":\\\\\\\"3\\\\\\\",\\\\\\\"temperature\\\\\\\":\\\\\\\"3\\\\\\\",\\\\\\\"ordinal\\\\\\\":\\\\\\\"3\\\\\\\"}\"},\"errno\":0,\"errmsg\":\"success\"}";
            String str = "{\"data\":{\"vehicleId\":\"100110002\",\"traceId\":\"100110002\",\"answer\":[],\"domain\":\"air_conditioner\",\"intentList\":[{\"intent\":\"air_conditioner_turn_on\",\"slots\":{}}],\"utterance\":\"打开空调\",\"tts\":\"好的，已为您打开空调\",\"extend\":\"origin intent: i_car_control_turn_on_air_conditioner ,raw result: domain: air_conditioner \\tintent: air_conditioner_turn_on \\tslots: {}\"},\"errno\":0,\"errmsg\":\"success\"}";

            return LuaValue.valueOf(nluResult);
        }
    }

    class testReturnTrue extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            return LuaValue.valueOf(true);
        }
    }

    class testReturnFalse extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            return LuaValue.valueOf(false);
        }
    }

    class testReturnNull extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            return LuaValue.NIL;
        }
    }

    public void loadJavaFuncInLua() {
        Globals globals = JsePlatform.standardGlobals();

        // register java function to lua
        globals.set("carPropertyGet", new LuaTable());
        globals.get("carPropertyGet").set("GetAirConditionerProperty", new getAirTemperature());
        globals.get("carPropertyGet").set("LuaGetNluResult", new getNluResult());

        globals.get("carPropertyGet").set("TestReturnTrue", new testReturnTrue());
        globals.get("carPropertyGet").set("TestReturnFalse", new testReturnFalse());
        globals.get("carPropertyGet").set("TestReturnNull", new testReturnNull());

        String dirPath = System.getProperty("user.dir");
        String path = dirPath + "/src/main/java/com/liuyang1/luascript/luaScripts/test.lua";
        LuaValue chunk = globals.loadfile(path);

        LuaValue luaRawResult = chunk.call();
        System.out.println(luaRawResult);
    }
}


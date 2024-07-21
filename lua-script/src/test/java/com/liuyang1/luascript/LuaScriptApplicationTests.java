//package com.liuyang1.luascript;
//
//import com.liuyang1.luascript.utils.concurrent.BaseRspDTO;
//import com.liuyang1.luascript.utils.concurrent.ConcurrentUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.Callable;
//
//@SpringBootTest
//class LuaScriptApplicationTests {
//
//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    public void testConcurrent() {
//        Callable<BaseRspDTO<Object>> task1 = () -> {
//            BaseRspDTO<Object> result = new BaseRspDTO<>();
//            result.setKey("task1");
//            result.setData("task1 result1...");
//            Thread.sleep(2 * 1000);
//            return result;
//        };
//
//        Callable<BaseRspDTO<Object>> task2 = () -> {
//            BaseRspDTO<Object> result = new BaseRspDTO<>();
//            result.setKey("task2");
//            result.setData("task2 result1...");
//            Thread.sleep(3 * 1000);
//            return result;
//        };
//
//        Callable<BaseRspDTO<Object>> task3 = () -> {
//            BaseRspDTO<Object> result = new BaseRspDTO<>();
//            result.setKey("task3");
//            result.setData("task3 result1...");
//            Thread.sleep(4 * 1000);
//            return result;
//        };
//
//        List<Callable<BaseRspDTO<Object>>> tasks = Arrays.asList(task1, task2, task3);
//        List<BaseRspDTO<Object>> resultList = ConcurrentUtils.getResultList(tasks, 1500L);
//        System.out.println("concurrent1 result: ");
//        for (BaseRspDTO<Object> resultItem : resultList) {
//            switch (resultItem.getKey()) {
//                case "task1":
//                    System.out.println("task1 result=" + resultItem.getData());
//                    break;
//                case "task2":
//                    System.out.println("task2 result=" + resultItem.getData());
//                    break;
//                case "task3":
//                    System.out.println("task3 result=" + resultItem.getData());
//                    break;
//            }
//        }
//        System.out.println("concurrent1 finished!");
//
//        List<BaseRspDTO<Object>> resultList2 = ConcurrentUtils.getResultList(tasks, 2100L);
//        System.out.println("concurrent2 result: ");
//        for (BaseRspDTO<Object> resultItem : resultList2) {
//            switch (resultItem.getKey()) {
//                case "task1":
//                    System.out.println("task1 result=" + resultItem.getData());
//                    break;
//                case "task2":
//                    System.out.println("task2 result=" + resultItem.getData());
//                    break;
//                case "task3":
//                    System.out.println("task3 result=" + resultItem.getData());
//                    break;
//            }
//        }
//        System.out.println("concurrent2 finished!");
//    }
//}

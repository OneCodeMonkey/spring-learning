package com.liuyang1.luascript;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

@SpringBootTest
public class IntegerCacheTest {
    /**
     * 通过缓存改 IntegerCache 容易误操作
     */
    @Test
    public void testIntegerCache() {
        Class<?> cls = Integer.class.getDeclaredClasses()[0];
        try {
            Field myCache = cls.getDeclaredField("cache");
            myCache.setAccessible(true);
            Integer[] newCache = (Integer[]) myCache.get(cls);
            newCache[132] = newCache[133];

            int a = 2, b = a + a;
            System.out.printf("%d+%d=%d\n", a, a, b);       // 2 + 2 = 5
            for (Integer i : newCache) {
                System.out.println("item: " + i);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}

package com.liuyang1.spring_learning.mybatis;

import com.liuyang1.spring_learning.mybatis.entity.User;
import com.liuyang1.spring_learning.mybatis.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest
@Slf4j
public class MybatisBasicUsageTest {
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/doc_manage";
    public static final String USER = "root";
    public static final String PASS = "yourPassword";

    /**
     * 1.基础JDBC连接方式
     */
    @Test
    public void testJdbcConnection() {
        try {
            // 注册 jdbc driver类。因为 class 加载的过程中，触发了其 static 静态方法，它会注册 driver 类到DriverManager
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(URL, USER, PASS);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from hc_user limit 20");
            while (resultSet.next()) {
                log.info("id: {}, username: {}, email: {}", resultSet.getInt("id"), resultSet.getString("Username"),
                        resultSet.getString("Email"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void testMybatisBasic() {
        SqlSession session = getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        long startTime = System.currentTimeMillis();
        // 获取数据，转化对象
        User user = userMapper.selectUserById(1);
        log.info("result: {}", user);
        long cost = System.currentTimeMillis() - startTime;
        log.info("cost1: {} ms", cost);
        // 清除一级缓存
//        session.clearCache();

        for (int i = 1; i < 20; i++) {
            startTime = System.currentTimeMillis();
            user = userMapper.selectUserById(i);
            cost = System.currentTimeMillis() - startTime;
            log.info("cost {}: {} ms", i, cost);
        }

        for (int i = 21; i < 40; i++) {
            startTime = System.currentTimeMillis();
            user = userMapper.selectUserById(i);
            cost = System.currentTimeMillis() - startTime;
            log.info("cost {}: {} ms", i, cost);
        }
    }

    private SqlSession getSession() {
        final String source = "mybatis-config.xml";
        InputStream inputStream = null;
        SqlSession session = null;
        try {
            // 读取配置文件
            inputStream = Resources.getResourceAsStream(source);
            // 创建 sqlSessionFactory
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            // 创建 sqlSession
            session = sqlSessionFactory.openSession();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }

        return session;
    }
}

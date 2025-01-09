package com.liuyang1.springlearning.distributedtrans.distri_trans;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@Slf4j
public class DistriTransApplication implements CommandLineRunner {
    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private TransRocketMQTemplate transRocketMQTemplate;

    private final String normalTopic = "testTopic", transTopic = "transTopic";

    public static void main(String[] args) {
        SpringApplication.run(DistriTransApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        // Send message
        for (int i = 0; i < 100; i++) {
            SendResult sendResult = rocketMQTemplate.syncSend(normalTopic, "Hello " + i + ", World! date is : " + new Date());
            System.out.printf("syncSend1 to topic %s sendResult=%s %n", normalTopic, sendResult);
        }

        // receive message
        //This is an example of pull consumer using rocketMQTemplate.
        while (true) {
            List<String> messages = rocketMQTemplate.receive(String.class);
            System.out.printf("receive from rocketMQTemplate, messages=%s %n", messages);
            if (messages.isEmpty()) {
                break;
            }
        }

        // 发送事务消息
        sendTransMessage("this is a transaction message", "transTest");

        // 接收事务消息
        while (true) {
            List<String> messages = transRocketMQTemplate.receive(String.class);
            System.out.printf("receive Transaction Message, messages=%s %n", messages);
            if (messages.isEmpty()) {
                break;
            }
        }
    }


    private void sendTransMessage(String text, String messageTag) {
        TransactionListener transactionListener = new TransactionListenerImpl();
        ((TransactionMQProducer) rocketMQTemplate.getProducer()).setTransactionListener(transactionListener);
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 100; i++) {
            try {
                String finalMessageTag = messageTag;
                Message<String> msg = new Message<>() {
                    @Override
                    public String getPayload() {
                        return text;
                    }

                    @Override
                    public MessageHeaders getHeaders() {
                        HashMap<String, Object> header = new HashMap<>();
                        header.put("tag", finalMessageTag);
                        return new MessageHeaders(header);
                    }
                };
                log.info("msg to be sent: {}", msg);
                TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(transTopic, msg, new HashMap<>());
                log.info("sendResult: {}", sendResult);
//                System.out.printf("%s%n", sendResult);

                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TransactionListenerImpl implements TransactionListener {
        private AtomicInteger transactionIndex = new AtomicInteger(0);

        private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

        /**
         * 模拟本地事务的执行结果
         *
         * @param msg
         * @param arg
         * @return
         */
        @Override
        public LocalTransactionState executeLocalTransaction(org.apache.rocketmq.common.message.Message msg, Object arg) {
            int value = transactionIndex.getAndIncrement();
            int status = value % 3;
            localTrans.put(msg.getTransactionId(), status);
            return LocalTransactionState.UNKNOW;
        }

        /**
         * 本地事务结果回查，用来确认本地事务是否提交成功
         *
         * @param msg
         * @return
         */
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            Integer status = localTrans.get(msg.getTransactionId());
            if (null != status) {
                switch (status) {
                    case 0:
                        return LocalTransactionState.UNKNOW;
                    case 1:
                        log.info("local transaction success, commit message: {}", msg);
                        return LocalTransactionState.COMMIT_MESSAGE;
                    case 2:
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    default:
                        return LocalTransactionState.COMMIT_MESSAGE;
                }
            }
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    }
}

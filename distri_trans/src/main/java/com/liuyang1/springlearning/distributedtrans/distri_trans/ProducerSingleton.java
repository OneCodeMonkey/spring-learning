package com.liuyang1.springlearning.distributedtrans.distri_trans;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.ProducerBuilder;
import org.apache.rocketmq.client.apis.producer.TransactionChecker;

public class ProducerSingleton {
    // 单例对象
    private static volatile Producer TRANSACTIONAL_PRODUCER;

    private final String normalTopic = "testTopic", transTopic = "transTopic";

    private static final String ACCESS_KEY = "yourAccessKey";
    private static final String SECRET_KEY = "yourSecretKey";
    private static final String ENDPOINTS = "localhost:9876";

    public static Producer getTransactionalInstance(TransactionChecker checker, String... topics) throws ClientException {
        if (null == TRANSACTIONAL_PRODUCER) {
            synchronized (ProducerSingleton.class) {
                if (null == TRANSACTIONAL_PRODUCER) {
                    TRANSACTIONAL_PRODUCER = buildProducer(checker, topics);
                }
            }
        }
        return TRANSACTIONAL_PRODUCER;
    }

    private static Producer buildProducer(TransactionChecker checker, String... topics) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        // Credential provider is optional for client configuration.
        // This parameter is necessary only when the server ACL is enabled. Otherwise,
        // it does not need to be set by default.
//        SessionCredentialsProvider sessionCredentialsProvider =
//                new StaticSessionCredentialsProvider(ACCESS_KEY, SECRET_KEY);
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(ENDPOINTS)
                // On some Windows platforms, you may encounter SSL compatibility issues. Try turning off the SSL option in
                // client configuration to solve the problem please if SSL is not essential.
                // .enableSsl(false)
//                .setCredentialProvider(sessionCredentialsProvider)
                .build();
        final ProducerBuilder builder = provider.newProducerBuilder()
                .setClientConfiguration(clientConfiguration)
                // Set the topic name(s), which is optional but recommended. It makes producer could prefetch
                // the topic route before message publishing.
                .setTopics(topics);
        if (checker != null) {
            // Set the transaction checker.
            builder.setTransactionChecker(checker);
        }
        return builder.build();
    }
}

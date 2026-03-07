package org.example.service;

import org.example.config.RabbitMQConfig;
import org.example.model.CreditResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Service
public class CreditResponseListener {

    private final ConcurrentHashMap<String, CreditResponse> responses = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CountDownLatch> latches = new ConcurrentHashMap<>();
    private final RabbitLogService logService;

    public CreditResponseListener(RabbitLogService logService) {
        this.logService = logService;
    }

    public CountDownLatch createLatch(String userId) {
        CountDownLatch latch = new CountDownLatch(1);
        latches.put(userId, latch);
        return latch;
    }

    public CreditResponse getResponse(String userId) {
        CreditResponse response = responses.remove(userId);
        latches.remove(userId);
        return response;
    }

    @RabbitListener(queues = RabbitMQConfig.CREDIT_RESPONSE_QUEUE)
    public void receiveResponse(CreditResponse response) {
        String status = response.isApproved() ? "APPROVED" : "REJECTED";
        String msg = "userId=" + response.getUserId() + " | " + status + " | " + response.getMessage();
        System.out.println("[RENTAL] Received credit response -> " + msg);
        logService.addLog("RECEIVED", RabbitMQConfig.CREDIT_RESPONSE_QUEUE, msg);
        responses.put(response.getUserId(), response);
        CountDownLatch latch = latches.get(response.getUserId());
        if (latch != null) {
            latch.countDown();
        }
    }
}

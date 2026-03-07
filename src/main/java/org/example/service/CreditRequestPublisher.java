package org.example.service;

import org.example.config.RabbitMQConfig;
import org.example.model.CreditRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CreditRequestPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitLogService logService;

    public CreditRequestPublisher(RabbitTemplate rabbitTemplate, RabbitLogService logService) {
        this.rabbitTemplate = rabbitTemplate;
        this.logService = logService;
    }

    public void sendCreditRequest(CreditRequest request) {
        String msg = "userId=" + request.getUserId() + " | amount=" + request.getAmount() + "EUR | type=" + request.getOperationType();
        System.out.println("[RENTAL] Sending credit request -> " + msg);
        logService.addLog("SENT", RabbitMQConfig.CREDIT_REQUEST_QUEUE, msg);
        rabbitTemplate.convertAndSend(RabbitMQConfig.CREDIT_REQUEST_QUEUE, request);
    }
}

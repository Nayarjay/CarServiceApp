package com.bank.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Récupère les noms des files et de l'échange depuis le fichier
    // application.properties
    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.request.queue}")
    private String requestQueue;

    @Value("${app.rabbitmq.request.routingkey}")
    private String requestRoutingKey;

    @Value("${app.rabbitmq.response.queue}")
    private String responseQueue;

    @Value("${app.rabbitmq.response.routingkey}")
    private String responseRoutingKey;

    // Crée la file d'attente pour recevoir les demandes de crédit (Request)
    @Bean
    public Queue requestQueue() {
        return new Queue(requestQueue, true);
    }

    // Crée la file d'attente pour envoyer les réponses à la location (Response)
    @Bean
    public Queue responseQueue() {
        return new Queue(responseQueue, true);
    }

    // Crée le "centre de tri" (Exchange) qui va diriger les messages vers les
    // bonnes files
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    // Relie la file de "Demande" au centre de tri avec une clé spécifique (le code
    // postal du message)
    @Bean
    public Binding requestBinding() {
        return BindingBuilder.bind(requestQueue()).to(exchange()).with(requestRoutingKey);
    }

    // Relie la file de "Réponse" au centre de tri
    @Bean
    public Binding responseBinding() {
        return BindingBuilder.bind(responseQueue()).to(exchange()).with(responseRoutingKey);
    }

    // Définit que les messages seront envoyés au format JSON (lisible par tous les
    // services)
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Configure l'outil principal qui sert à envoyer les messages (le pistolet à
    // messages)
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter()); // On lui dit de toujours parler en JSON
        return template;
    }
}

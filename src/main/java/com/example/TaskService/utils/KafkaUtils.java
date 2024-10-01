package com.example.TaskService.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaUtils {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String, Object> template;

    public void send(String topic, Object message){
        String json = null;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        CompletableFuture<SendResult<String, Object>> send = template.send(topic, json);

        send.whenComplete((stringObjectSendResult, throwable) ->{
                if(throwable != null) {
                    log.info("Message not sent");
                }else{
                    log.info("Message sent");
                }
            }
        );
    }
}

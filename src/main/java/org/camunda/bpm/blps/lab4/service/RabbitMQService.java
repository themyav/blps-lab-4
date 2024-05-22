package org.camunda.bpm.blps.lab4.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.blps.lab4.model.User;
import org.camunda.bpm.blps.lab4.model.Vacancy;
import org.camunda.bpm.blps.lab4.util.EmailNotification;
import org.camunda.bpm.blps.lab4.util.ModeratorComment;
import org.camunda.bpm.blps.lab4.util.enums.Result;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RabbitMQService {
    @Autowired
    private Queue queue;

    @Autowired
    private MqttClient mqttClient;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private void send(String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttClient.publish(queue.getName(), mqttMessage);
        System.out.println(" [x] Sent '" + message + "' via MQTT");
    }

    public void sendUserStats(User user, List<Vacancy>vacancies){
        StringBuilder messageBody = new StringBuilder("Уважаемый(ая) " + user.getFirstName() + " " + user.getLastName() + "!\n" +
                "статистика ваших вакансий за последнюю неделю:\n");
        for(Vacancy vacancy : vacancies) messageBody.append(vacancy.toString()).append("\n");
        try {
            String message = objectMapper.writeValueAsString(new EmailNotification(user.getEmail(), "Статистика ваших объявлений", messageBody.toString()));
            send(message);
        } catch (JsonProcessingException | MqttException e) {
            throw new RuntimeException(e);
        }


    }

    public Result sendUserNotification(Vacancy vacancy, ModeratorComment moderatorComment) {
        User user = userService.findUserById(vacancy.getAuthorId());
        if (user == null) return Result.USER_NOT_FOUND;
        String message;
        try {
            if (vacancy.isPublished()) {
                message = objectMapper.writeValueAsString(new EmailNotification(user.getEmail(), "Ваша вакансия опубликована", vacancy.toString()));
            } else {
                message = objectMapper.writeValueAsString(new EmailNotification(user.getEmail(),
                        "Ваша вакансия не прошла модерацию",
                        vacancy + "Комментарий модератора:\n" + moderatorComment.getComment()));
            }
            send(message);
            return Result.OK;
        } catch (JsonProcessingException e) {
            return Result.JSON_ERROR;
        } catch (MqttException e) {
            return Result.MQTT_ERROR;
        }

    }

    public Result sendModeratorNotification(Vacancy vacancy) {

        List<User> moderators = userService.getModerators();
        AtomicReference<Result> result = new AtomicReference<>(Result.MQTT_ERROR);
        moderators.forEach(moderator -> {
            try {
                String message = objectMapper.writeValueAsString(new EmailNotification(moderator.getEmail(), "У вас новая вакансия для модерации!", vacancy.toString()));
                send(message);
                result.set(Result.OK); //хотя бы 1 модератору пришло письмо
            } catch (MqttException | JsonProcessingException e) {
                throw new RuntimeException(e); //TODO может умнее
            }
        });
        return result.get();
    }
}

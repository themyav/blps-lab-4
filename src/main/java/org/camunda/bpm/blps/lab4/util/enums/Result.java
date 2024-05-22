package org.camunda.bpm.blps.lab4.util.enums;

public enum Result {



    OK(0, "упешное выполнение операции"),
    USER_NOT_FOUND(-1, "пользователь не найден"),
    NOT_ENOUGH_BALANCE(1, "не достаточно средств на балансе"),
    NO_VACANCY_DESCRIPTION(2, "не указано описание вакансии"),
    NO_VACANCY_TITLE(3, "не указано название вакансии"),
    NO_VACANCY_AUTHOR(4, "не указан автор вакансии"),
    UNKNOWN_ERROR(5, "ошибка во время выполнения операции"),

    VACANCY_NOT_FOUND(6, "вакансия не найдена"),
    VACANCY_NOT_APPROVED(7, "Вакансия не прошла модерацию и сохранена как черновик"),

    MQTT_ERROR(8, "ошибка отправки по mqtt"),
    JSON_ERROR(9, "ошибка сериализации объекта");
        private int code;


         Result(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
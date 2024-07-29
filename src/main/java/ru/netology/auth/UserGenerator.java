package ru.netology.auth;

import com.github.javafaker.Faker;
import lombok.Value;

public class UserGenerator {
    private UserGenerator() {
    }

    static Faker faker = new Faker();

    public static String generateLogin() {
        return faker.name().username();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String status) {
            return new UserInfo(generateLogin(), generatePassword(), status);
        }
    }

    @Value
    public static class UserInfo {
        String login;
        String password;
        String status;
    }
}
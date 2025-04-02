package com.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

class JsonPropertySelectorTest {
    static Jsonb jsonb;

    @BeforeAll
    static void init() {
        JsonbConfig config = new JsonbConfig().withFormatting(true);
        jsonb = JsonbBuilder.create(config);
    }

    @BeforeEach
    void setup() {


    }

    @Test
    void test() {
        // フラット構造のJavaオブジェクト
        Person person = new Person();
        person.setFirstName("taro");
        person.setLastName("sato");
        person.setAge(20);

        JsonObject jsonObj = JsonPropertySelector.properties("firstName,age,test").apply(person);
        System.out.println("result -> " + jsonObj);
    }

/**
 * フラット構造のJavaオブジェクト
 */
    public static class Person {
        private String firstName;
        private String lastName;
        private Integer age;

        public Person() {
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

    }

}

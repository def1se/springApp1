package ru.ashitok.spring.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class Person {
    private int personId;

    @Pattern(regexp = "[А-Я][а-я]*\\s[А-Я][а-я]*\\s[А-Я][а-я]*", message = "Ваше фио должно иметь формат: Фамилия Имя Отчество")
    private String fio;

    @Min(value = 1900, message = "Год должен быть больше, чем 1900")
    private int yearOfBirth;

    public Person(){}

    public Person(int personId, String fio, int yearOfBirth) {
        this.personId = personId;
        this.fio = fio;
        this.yearOfBirth = yearOfBirth;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", fio='" + fio + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}

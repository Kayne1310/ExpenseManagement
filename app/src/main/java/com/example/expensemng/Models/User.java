package com.example.expensemng.Models;

public class User {

    private  String userName;
    private String password;
    private String email;
    private int age;

    public String getUserName() {
        return userName;
    }

    public User(int age, String password, String userName, String email) {
        this.age = age;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }

    public User(String userName, String email, int age) {
        this.userName = userName;
        this.email = email;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

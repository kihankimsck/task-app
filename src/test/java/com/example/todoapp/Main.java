package com.example.todoapp;

public class Main {
    public static void main(String[] args) {
        Person person1 = new Person(); // 1
        Person person2 = new Person(); // 1
        if (person1.equals(person2)) {
            System.out.println(person1);
            System.out.println("equal!");
        } else {
            System.out.println(person2);
            System.out.println("not equal");
        }

        User user1 = User.getInstance();
        User user2 = User.getInstance();
        if (user1.equals(user2)) {
            System.out.println("equal2!");
        } else {
            System.out.println("not equal2");
        }
    }
}

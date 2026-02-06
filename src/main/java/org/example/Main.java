package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Calculator calc = new Calculator();
        int sum = calc.add(5,5);
        System.out.println(sum);
    }
}
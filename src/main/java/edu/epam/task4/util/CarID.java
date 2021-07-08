package edu.epam.task4.util;

public class CarID {
    private static long counter = 0;

    private CarID() {
    }

    public static long generateID() {
        return ++counter;
    }
}

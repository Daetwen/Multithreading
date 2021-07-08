package edu.epam.task4.creator;

import edu.epam.task4.entity.Cars;

import java.util.ArrayList;
import java.util.List;

public class CarCreator {

    public static List<Cars> createCars(List<int[]> characteristicsForWork) {
        List<Cars> carsList = new ArrayList<>();
        for (int[] characteristics : characteristicsForWork) {
            int weight = characteristics[0];
            int area = characteristics[1];
            Cars car = new Cars(weight, area);
            carsList.add(car);
        }
        return carsList;
    }
}

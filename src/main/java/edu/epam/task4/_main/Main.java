package edu.epam.task4._main;

import edu.epam.task4.creator.CarCreator;
import edu.epam.task4.entity.Cars;
import edu.epam.task4.exception.FerryCarException;
import edu.epam.task4.parser.CarParser;
import edu.epam.task4.reader.MultithreadingFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger logger = LogManager.getLogger();
    private static final String FILE_PATH = "src/main/resources/info.txt";

    public static void main(String[] args) {
        try {
            List<String> characteristics;
            List<int[]> characteristicsForWork;
            MultithreadingFileReader multithreadingFileReader = new MultithreadingFileReader();
            CarParser carParser = new CarParser();
            characteristics = multithreadingFileReader.readCharacteristicsOfCars(FILE_PATH);
            characteristicsForWork = carParser.parseListToCharacteristicsArrays(characteristics);
            List<Cars> carsList = CarCreator.createCars(characteristicsForWork);
            carsList.forEach(logger::info);

            ExecutorService executorService = Executors.newFixedThreadPool(carsList.size());
            carsList.forEach(executorService::submit);
            executorService.shutdown();
        } catch (FerryCarException e) {
            logger.error("Error in main: ", e);
        }
    }
}

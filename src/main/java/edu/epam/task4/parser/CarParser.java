package edu.epam.task4.parser;

import edu.epam.task4.exception.FerryCarException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarParser {
    private static final Logger logger = LogManager.getLogger();
    private static final String SPLIT_REGEX = "\\s+";
    private static final int NUMBERS_COUNT = 2;

    public List<int[]> parseListToCharacteristicsArrays(List<String> array) throws FerryCarException {
        List<int[]> result = new ArrayList<>();
        if (array != null && !array.isEmpty()) {
            int i = -1;
            while (++i < array.size()) {
                int[] numArray = Arrays.stream(array.get(i).split(SPLIT_REGEX))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                if (numArray.length == NUMBERS_COUNT) {
                    result.add(numArray);
                }
            }
        } else {
            logger.error("Invalid List of string for parsing.");
            throw new FerryCarException("Invalid List of string for parsing.");
        }
        return result;
    }
}

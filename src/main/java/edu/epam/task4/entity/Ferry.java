package edu.epam.task4.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ferry {
    private static final Logger logger = LogManager.getLogger();
    private static final int MAX_WEIGHT = 20;
    private static final int MAX_AREA = 50;
    private static Ferry instance;
    private static final ReentrantLock locker = new ReentrantLock();
    private static final AtomicBoolean create = new AtomicBoolean(false);
    private final Queue<Condition> carQueue = new LinkedList<>();
    private final Queue<Cars> carQueue2 = new LinkedList<>();

    private int currentWeight = 0;
    private int currentArea = 0;

    private Ferry() {

    }

    public static Ferry getInstance() {
        if (!create.get()) {
            try {
                locker.lock();
                if (instance == null) {
                    instance = new Ferry();
                    create.set(true);
                }
            } finally {
                locker.unlock();
            }
        }
        return instance;
    }

    public void addCar(Cars car) {
        try {
            locker.lock();
            if (currentWeight + car.getWeight() <= MAX_WEIGHT
                    && currentArea + car.getArea() <= MAX_AREA) {
                currentWeight += car.getWeight();
                currentArea += car.getArea();
                logger.info(car + ", Current weight = " + currentWeight + ", Current area = " + currentArea);
            }
        } finally {
            locker.unlock();
        }
    }

    public void removeCar(Cars car) {
        try {
            locker.lock();
            currentWeight -= car.getWeight();
            currentArea -= car.getArea();
            logger.info(car + ", Current weight = " + currentWeight + ", Current area = " + currentArea);
        } finally {
            locker.unlock();
        }
    }

    public void startTransfer(Cars car) {
        try {
            locker.lock();

            if (currentArea + car.getArea() > MAX_AREA
                    || currentWeight + car.getWeight() > MAX_WEIGHT) {
                try {
                    car.setState(CarState.EXPECTS);
                    logger.info(car + " was added in the queue");
                    Condition condition = locker.newCondition();
                    carQueue.add(condition);
                    carQueue2.add(car);
                    condition.await();
                } catch (InterruptedException e) {
                    logger.error("Caught an exception: ", e);
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            locker.unlock();
        }
    }

    public void endTransfer() {
        try {
            locker.lock();
            if (currentArea == 0 && currentWeight == 0) {
                Condition condition = carQueue.peek();
                Cars car = carQueue2.peek();
                int localWeight = 0;
                int localArea = 0;
                while (condition != null && car != null) {
                    if (car.getWeight() + localWeight <= MAX_WEIGHT && car.getArea() + localArea <= MAX_AREA) {
                        localWeight += car.getWeight();
                        localArea += car.getArea();
                        carQueue.poll();
                        carQueue2.poll();
                        condition.signal();
                    } else {
                        break;
                    }
                    condition = carQueue.peek();
                    car = carQueue2.peek();
                }
            }
        } finally {
            locker.unlock();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Ferry ferry = (Ferry) obj;
        return currentWeight == ferry.currentWeight
                && currentArea == ferry.currentArea
                && carQueue.equals(ferry.carQueue)
                && carQueue2.equals(ferry.carQueue2);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        result = result * prime + Integer.hashCode(currentWeight);
        result = result * prime + Integer.hashCode(currentArea);
        result = result * prime + carQueue.hashCode();
        result = result * prime + carQueue2.hashCode();

        return result;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Ferry: (MAX_WEIGHT = ")
                .append(MAX_WEIGHT)
                .append(", MAX_AREA = ")
                .append(MAX_AREA)
                .append(")");
        return stringBuffer.toString();
    }
}

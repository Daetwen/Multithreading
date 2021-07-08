package edu.epam.task4.entity;

import edu.epam.task4.util.CarID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Cars implements Callable<Cars> {
    private static final long TIME_STOP_MSEC = 1750;
    private static final Logger logger = LogManager.getLogger();
    private long carId;
    private int weight;
    private int area;
    private CarState state;
    private CarType type;

    public Cars(int weight, int area) {
        this.carId = CarID.generateID();
        this.weight = weight;
        this.area = area;
        this.type = weight > 3.5 ? CarType.CARGO : CarType.PASSENGER;
        this.state = CarState.NEW;
    }

    public long getCarId() {
        return carId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setState(CarState state) {
        this.state = state;
    }

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    @Override
    public Cars call() {
        Ferry ferry = Ferry.getInstance();
        try {
            ferry.startTransfer(this);
            this.state = CarState.TRANSPORTED;
            ferry.addCar(this);
            TimeUnit.MILLISECONDS.sleep(TIME_STOP_MSEC);

            this.state = CarState.DELIVERED;
            ferry.removeCar(this);
            ferry.endTransfer();
        } catch (InterruptedException e) {
            logger.error("Exception in work: ", e);
            Thread.currentThread().interrupt();
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cars car = (Cars) obj;
        return weight == car.weight
                && area == car.area
                && state == car.state
                && type == car.type;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        result = result * prime + Integer.hashCode(weight);
        result = result * prime + Integer.hashCode(area);
        result = result * prime + state.hashCode();
        result = result * prime + type.hashCode();

        return result;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Car: (id = ")
                .append(carId)
                .append(") [ weight = ")
                .append(weight)
                .append(", area = ")
                .append(area)
                .append(", state - ")
                .append(state)
                .append("]");
        return stringBuffer.toString();
    }
}

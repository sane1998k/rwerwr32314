package com.automat.manager.requests;

public class PutOrdersRequest {
    private String counterpartyId;
    private String driverId;
    private String carId;
    private int balloonCount;

    public PutOrdersRequest(String counterpartyId, String driverId, String carId, int balloonCount) {
        this.counterpartyId = counterpartyId;
        this.driverId = driverId;
        this.carId = carId;
        this.balloonCount = balloonCount;
    }

    public String getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(String counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public int getBalloonCount() {
        return balloonCount;
    }

    public void setBalloonCount(int balloonCount) {
        this.balloonCount = balloonCount;
    }
}

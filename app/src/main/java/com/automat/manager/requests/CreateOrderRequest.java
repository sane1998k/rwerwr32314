package com.automat.manager.requests;

public class CreateOrderRequest {
    private String gasTypeId;
    private String counterpartyId;
    private int balloonCount;

    public String getGasTypeId() {
        return gasTypeId;
    }

    public void setGasTypeId(String gasTypeId) {
        this.gasTypeId = gasTypeId;
    }

    public String getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(String counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public int getBalloonCount() {
        return balloonCount;
    }

    public void setBalloonCount(int balloonCount) {
        this.balloonCount = balloonCount;
    }
}

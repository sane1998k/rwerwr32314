package com.automat.manager.responses;

import com.automat.manager.classes.Balloon;

public class GetReturningsBody implements Balloon {
    private String id;
    private String orderId;

    private String creationDate;
    private String creationUserName;
    private String creationUserid;

    private String counterpartyName;
    private String counterPartyId;
    private int balloonCount;
    private int doneCount;
    private int givenCount;

    private String carName;
    private String carId;
    private String driverName;
    private String driverId;

    private boolean isСollected;
    private String collectedDate;

    private boolean isDone;
    private String doneDate;

    private boolean isLost;
    private boolean isCanceled;
    private boolean isRemove;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationUserName() {
        return creationUserName;
    }

    public void setCreationUserName(String creationUserName) {
        this.creationUserName = creationUserName;
    }

    public String getCreationUserid() {
        return creationUserid;
    }

    public void setCreationUserid(String creationUserid) {
        this.creationUserid = creationUserid;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public int getBalloonCount() {
        return balloonCount;
    }

    public void setBalloonCount(int balloonCount) {
        this.balloonCount = balloonCount;
    }

    public int getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(int doneCount) {
        this.doneCount = doneCount;
    }

    public int getGivenCount() {
        return givenCount;
    }

    public void setGivenCount(int givenCount) {
        this.givenCount = givenCount;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public boolean isСollected() {
        return isСollected;
    }

    public void setСollected(boolean сollected) {
        isСollected = сollected;
    }

    public String getCollectedDate() {
        return collectedDate;
    }

    public void setCollectedDate(String collectedDate) {
        this.collectedDate = collectedDate;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    @Override
    public String getIBalloonId() {
        return null;
    }

    @Override
    public boolean getIBalloonIsCheck() {
        return false;
    }

    @Override
    public String getINameScan() {
        return null;
    }

    @Override
    public String getINameBalloon() {
        return null;
    }
}

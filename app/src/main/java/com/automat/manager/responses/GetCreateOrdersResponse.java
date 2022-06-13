package com.automat.manager.responses;

import java.io.Serializable;

public class GetCreateOrdersResponse implements Serializable {
    private String id;

    private String creationDate;
    private String creationUserName;
    private String creationUserId;

    private String counterpartyName;
    private String counterpartyId;

    private String gasTypeId;
    private String gasTypeName;
    private Integer balloonCount;
    private String carName;
    private String carId;
    private String driverName;
    private String driverId;
    private boolean isСollected;
    private String сollectedDate;
    private boolean isDone;
    private String doneDate;
    private boolean isReturned;
    private boolean isLost;
    private boolean isCanceled;
    private boolean isRemove;

    public GetCreateOrdersResponse() {
    }

    public GetCreateOrdersResponse(String id, String creationDate, String counterpartyName, String gasTypeName, int balloonCount, String driverName, String carName) {
        this.id = id;
        this.creationDate = creationDate;
        this.counterpartyName = counterpartyName;
        this.gasTypeName = gasTypeName;
        this.balloonCount = balloonCount;
        this.carName = carName;
        this.driverName = driverName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreationUserId() {
        return creationUserId;
    }

    public void setCreationUserId(String creationUserId) {
        this.creationUserId = creationUserId;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(String counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public String getGasTypeId() {
        return gasTypeId;
    }

    public void setGasTypeId(String gasTypeId) {
        this.gasTypeId = gasTypeId;
    }

    public String getGasTypeName() {
        return gasTypeName;
    }

    public void setGasTypeName(String gasTypeName) {
        this.gasTypeName = gasTypeName;
    }

    public int getBalloonCount() {
        return balloonCount != null ? balloonCount : 0;
    }

    public void setBalloonCount(int balloonCount) {
        this.balloonCount = balloonCount;
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

    public String getСollectedDate() {
        return сollectedDate;
    }

    public void setСollectedDate(String сollectedDate) {
        this.сollectedDate = сollectedDate;
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

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
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
}

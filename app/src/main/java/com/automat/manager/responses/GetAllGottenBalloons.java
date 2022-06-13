package com.automat.manager.responses;

import com.automat.manager.classes.Balloon;

public class GetAllGottenBalloons implements Balloon {
    private String id;
    private String creationDate;
    private String creationUserId;
    private String creationUserName;
    private String orderId;
    private String balloonId;
    private String balloonName;
    private boolean isChecked;
    private String checkedDate;
    private String checkedLocationAddress;
    private String checkedLocationCoordinates;
    private boolean isLost;
    private String lostDate;
    private String isHandwritten;
    private String isRemove;

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

    public String getCreationUserId() {
        return creationUserId;
    }

    public void setCreationUserId(String creationUserId) {
        this.creationUserId = creationUserId;
    }

    public String getCreationUserName() {
        return creationUserName;
    }

    public void setCreationUserName(String creationUserName) {
        this.creationUserName = creationUserName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBalloonId() {
        return balloonId;
    }

    public void setBalloonId(String balloonId) {
        this.balloonId = balloonId;
    }

    public String getBalloonName() {
        return balloonName;
    }

    public void setBalloonName(String balloonName) {
        this.balloonName = balloonName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(String checkedDate) {
        this.checkedDate = checkedDate;
    }

    public String getCheckedLocationAddress() {
        return checkedLocationAddress;
    }

    public void setCheckedLocationAddress(String checkedLocationAddress) {
        this.checkedLocationAddress = checkedLocationAddress;
    }

    public String getCheckedLocationCoordinates() {
        return checkedLocationCoordinates;
    }

    public void setCheckedLocationCoordinates(String checkedLocationCoordinates) {
        this.checkedLocationCoordinates = checkedLocationCoordinates;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

    public String getLostDate() {
        return lostDate;
    }

    public void setLostDate(String lostDate) {
        this.lostDate = lostDate;
    }

    public String getIsHandwritten() {
        return isHandwritten;
    }

    public void setIsHandwritten(String isHandwritten) {
        this.isHandwritten = isHandwritten;
    }

    public String getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(String isRemove) {
        this.isRemove = isRemove;
    }

    @Override
    public String toString() {
        return "GetAllGottenBalloons{" +
                "id='" + id + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", creationUserId='" + creationUserId + '\'' +
                ", creationUserName='" + creationUserName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", balloonId='" + balloonId + '\'' +
                ", balloonName='" + balloonName + '\'' +
                ", isChecked=" + isChecked +
                ", checkedDate='" + checkedDate + '\'' +
                ", checkedLocationAddress='" + checkedLocationAddress + '\'' +
                ", checkedLocationCoordinates='" + checkedLocationCoordinates + '\'' +
                ", isLost=" + isLost +
                ", lostDate='" + lostDate + '\'' +
                ", isHandwritten='" + isHandwritten + '\'' +
                ", isRemove='" + isRemove + '\'' +
                '}';
    }

    @Override
    public String getIBalloonId() {
        return balloonId;
    }

    @Override
    public boolean getIBalloonIsCheck() {
        return isChecked;
    }

    @Override
    public String getINameScan() {
        return getCreationUserName();
    }

    @Override
    public String getINameBalloon() {
        return getBalloonName();
    }
}


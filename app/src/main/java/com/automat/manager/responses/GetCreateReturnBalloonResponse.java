package com.automat.manager.responses;

public class GetCreateReturnBalloonResponse {
    private String id;
    private String creationDate;
    private String creationUserId;
    private String creationUserName;
    private String orderId;
    private String orderDetailsId;
    private String balloonId;
    private String balloonName;

    private String volumeId;
    private String volumeName;
    private boolean isChecked;
    private String checkedDate;
    private String checkedLocationAddress;
    private String checkedLocationCoordinates;
    private boolean isLost;
    private String lostDate;
    private boolean isHandwritten;
    private boolean isRemove;

    public String getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(String orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
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

    public boolean isHandwritten() {
        return isHandwritten;
    }

    public void setHandwritten(boolean handwritten) {
        isHandwritten = handwritten;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }
}

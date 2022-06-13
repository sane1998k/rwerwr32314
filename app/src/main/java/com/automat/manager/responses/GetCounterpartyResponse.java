package com.automat.manager.responses;

import com.automat.manager.classes.GetterArray;
import com.automat.manager.interfaces.Test;

public class GetCounterpartyResponse implements Test {
    private String id;
    private String name;
    private String inn;
    private String kpp;
    private String directorName;
    private String directorPost;
    private String equipment;
    private boolean isRemove;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getDirectorPost() {
        return directorPost;
    }

    public void setDirectorPost(String directorPost) {
        this.directorPost = directorPost;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    @Override
    public String toString() {
        return "GetCounterpartyResponse{" +
                "name='" + name + '\'' +
                '}';
    }


    @Override
    public String getIdCounterparty() {
        return getId();
    }

    @Override
    public String getNameCounterparty() {
        return getName();
    }
}

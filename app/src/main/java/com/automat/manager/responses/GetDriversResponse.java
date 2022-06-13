package com.automat.manager.responses;

import com.automat.manager.interfaces.IGetName;

public class GetDriversResponse implements IGetName {
    private String id;
    private String name;
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

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    @Override
    public String getNameI() {
        return getName();
    }
}

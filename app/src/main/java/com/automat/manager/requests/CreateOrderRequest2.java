package com.automat.manager.requests;

import java.util.ArrayList;

public class CreateOrderRequest2 {
    public String counterpartyId;
    public ArrayList<OrderDetail> orderDetails;

    public CreateOrderRequest2(String counterpartyId, ArrayList<OrderDetail> orderDetails) {
        this.counterpartyId = counterpartyId;
        this.orderDetails = orderDetails;
    }

    class Counterparty{
        public String id;
        public String name;
        public String inn;
        public String kpp;
        public String directorName;
        public String directorPost;
        public String equipment;
        public boolean isRemove;
    }

    class GasType{
        public String id;
        public String name;
        public boolean isRemove;
    }

    public class OrderDetail{
        public GasType gasType;
        public Counterparty counterparty;
        public Volume volume;
        public int balloonCount;
    }

     class Volume{
        public String id;
        public String value;
        public boolean isRemove;
    }
}





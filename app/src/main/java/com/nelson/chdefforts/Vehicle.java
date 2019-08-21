package com.nelson.chdefforts;

public class Vehicle {
    String vehicleno;
    String startreading;
    String endreading;
    String amount;
    String id;


    public Vehicle(String id,String vehicleno, String startreading, String endreading, String amount) {
        this.id = id;
        this.vehicleno = vehicleno;
        this.startreading = startreading;
        this.endreading = endreading;
        this.amount = amount;
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public String getStartreading() {
        return startreading;
    }

    public String getEndreading() {
        return endreading;
    }

    public String getAmount() {
        return amount;
    }
}


package com.delluna.dellunahotel.models;

public class Service {
  public String serviceId;
  public String serviceName;
  public String description;
  public double pricePerStay;

  public String getServiceName() {
    return serviceName;
}

public double getPricePerStay() {
    return pricePerStay;
}
}

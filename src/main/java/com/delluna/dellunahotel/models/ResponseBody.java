package com.delluna.dellunahotel.models;

import java.util.ArrayList;

public class ResponseBody {
  public boolean isSuccess = false;
  public ArrayList<String> properties;
  public ArrayList<String> data;

  public ResponseBody() {
    properties = new ArrayList<String>();
    data = new ArrayList<String>();
  }

  public ArrayList<String> getProperties() {
    return properties;
  }

  public String getProperty(String property) {
    int i = properties.indexOf(property);
    if (i == -1) return null;
    return data.get(i);
  }
}

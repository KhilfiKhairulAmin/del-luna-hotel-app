package com.delluna.dellunahotel.models;

public class Room {
    public String roomNum;
    public int floorLevel;
    public String roomTypeId;

    @Override
    public String toString() {
        return "Room{" +
               "roomNum='" + roomNum + '\'' +
               ", floorLevel=" + floorLevel +
               ", roomTypeId='" + roomTypeId + '\'' +
               '}';
    }
}

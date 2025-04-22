package com.delluna.dellunahotel.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.delluna.dellunahotel.models.Room;
import com.delluna.dellunahotel.models.RoomType;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class RoomService {
    private static final String ROOMS_FILE_PATH = "src/main/resources/com/delluna/dellunahotel/database/rooms.json";
    private static final String ROOM_TYPES_FILE_PATH = "src/main/resources/com/delluna/dellunahotel/database/roomTypes.json";

    private List<Room> rooms = new ArrayList<>();
    private List<RoomType> roomTypes = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RoomService() {
        loadAllData();
    }

    // File operations
    private void loadAllData() {
        loadRoomTypes();
        loadRooms();
    }

    private void loadRoomTypes() {
        try (Reader reader = new FileReader(ROOM_TYPES_FILE_PATH)) {
            Type type = new TypeToken<ArrayList<RoomType>>(){}.getType();
            roomTypes = gson.fromJson(reader, type);
            if (roomTypes == null) roomTypes = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            roomTypes = new ArrayList<>();
        }
        System.out.println(roomTypes.size());
        System.out.println(rooms.size());
    }

    private void loadRooms() {
        try (Reader reader = new FileReader(ROOMS_FILE_PATH)) {
            Type type = new TypeToken<ArrayList<Room>>(){}.getType();
            List<Room> loadedRooms = gson.fromJson(reader, type);
            if (loadedRooms != null) {
                rooms = loadedRooms;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveRooms() {
        try (Writer writer = new FileWriter(ROOMS_FILE_PATH)) {
            gson.toJson(rooms, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveRoomTypes() {
        try (Writer writer = new FileWriter(ROOM_TYPES_FILE_PATH)) {
            gson.toJson(roomTypes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Room operations
    public Room createRoom(Room room) {
        if (getRoomByNumber(room.roomNum) != null) {
            return null;
        }
        rooms.add(room);
        saveRooms();
        return room;
    }

    public Room getRoomByNumber(String roomNum) {
        return rooms.stream()
                .filter(r -> r.roomNum.equals(roomNum))
                .findFirst()
                .orElse(null);
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public boolean deleteRoom(String roomNum) {
        boolean removed = rooms.removeIf(r -> r.roomNum.equals(roomNum));
        if (removed) saveRooms();
        return removed;
    }

    public Room updateRoom(Room updatedRoom) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).roomNum.equals(updatedRoom.roomNum)) {
                rooms.set(i, updatedRoom);
                saveRooms();
                return updatedRoom;
            }
        }
        return null;
    }

    public List<Room> getRoomsByFloor(int floor) {
        return rooms.stream()
                .filter(r -> r.floorLevel == floor)
                .collect(Collectors.toList());
    }

    public List<Room> getRoomsByType(String typeId) {
        return rooms.stream()
                .filter(r -> r.roomTypeId != null && r.roomTypeId.equals(typeId))
                .collect(Collectors.toList());
    }

    // RoomType operations
    public RoomType createRoomType(RoomType roomType) {
        if (getRoomTypeById(roomType.typeId) != null) return null;
        roomTypes.add(roomType);
        saveRoomTypes();
        return roomType;
    }

    public RoomType getRoomTypeById(String typeId) {
        return roomTypes.stream()
                .filter(rt -> rt.typeId.equals(typeId))
                .findFirst()
                .orElse(null);
    }

    public List<RoomType> getAllRoomTypes() {
        return new ArrayList<>(roomTypes);
    }

    public boolean deleteRoomType(String typeId) {
        if (!getRoomsByType(typeId).isEmpty()) return false;

        boolean removed = roomTypes.removeIf(rt -> rt.typeId.equals(typeId));
        if (removed) saveRoomTypes();
        return removed;
    }

    public RoomType updateRoomType(RoomType updatedType) {
        for (int i = 0; i < roomTypes.size(); i++) {
            if (roomTypes.get(i).typeId.equals(updatedType.typeId)) {
                roomTypes.set(i, updatedType);
                saveRoomTypes();
                return updatedType;
            }
        }
        return null;
    }

    // Advanced queries
    public List<Room> getAvailableRooms() {
        return rooms.stream()
                .filter(room -> room.roomTypeId != null)
                .collect(Collectors.toList());
    }

    public List<RoomType> getRoomTypesByPriceRange(double min, double max) {
        return roomTypes.stream()
                .filter(rt -> rt.pricePerNight >= min && rt.pricePerNight <= max)
                .collect(Collectors.toList());
    }

    public List<Room> getRoomsByPriceRange(double min, double max) {
        Set<String> typeIdsInRange = roomTypes.stream()
                .filter(rt -> rt.pricePerNight >= min && rt.pricePerNight <= max)
                .map(rt -> rt.typeId)
                .collect(Collectors.toSet());

        return rooms.stream()
                .filter(r -> r.roomTypeId != null && typeIdsInRange.contains(r.roomTypeId))
                .collect(Collectors.toList());
    }
}

package com.delluna.dellunahotel.services;

import com.delluna.dellunahotel.models.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ServiceService {
    private static final String SERVICES_FILE_PATH = "src/main/resources/com/delluna/dellunahotel/services.json";

    private List<Service> services = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ServiceService() {
        loadServices();
    }

    private void loadServices() {
        try (Reader reader = new FileReader(SERVICES_FILE_PATH)) {
            Type type = new TypeToken<ArrayList<Service>>() {}.getType();
            List<Service> loadedServices = gson.fromJson(reader, type);
            if (loadedServices != null) {
                services = loadedServices;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveServices() {
        try (Writer writer = new FileWriter(SERVICES_FILE_PATH)) {
            gson.toJson(services, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create
    public Service createService(Service service) {
        if (getServiceById(service.serviceId) != null) {
            return null;
        }
        services.add(service);
        saveServices();
        return service;
    }

    // Read
    public List<Service> getAllServices() {
        return new ArrayList<>(services);
    }

    public Service getServiceById(String serviceId) {
        return services.stream()
                .filter(s -> s.serviceId.equals(serviceId))
                .findFirst()
                .orElse(null);
    }

    // Update
    public Service updateService(Service updatedService) {
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).serviceId.equals(updatedService.serviceId)) {
                services.set(i, updatedService);
                saveServices();
                return updatedService;
            }
        }
        return null;
    }

    // Delete
    public boolean deleteService(String serviceId) {
        boolean removed = services.removeIf(s -> s.serviceId.equals(serviceId));
        if (removed) saveServices();
        return removed;
    }
}

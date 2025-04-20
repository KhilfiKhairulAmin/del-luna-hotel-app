package com.delluna.dellunahotel.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.prefs.Preferences;

public class ApiClient {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Preferences prefs = Preferences.userNodeForPackage(ApiClient.class);
    private static final String JWT_TOKEN_KEY = "jwt_token";

    /**
     * Sets the JWT token in preferences
     * @param token JWT token to store
     */
    public static void setAuthToken(String token) {
        // Save to Preferences
        prefs.put(JWT_TOKEN_KEY, token);

        // Also save to a file
        try {
            FileWriter writer = new FileWriter("auth_token.txt");
            writer.write(token);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets the current JWT token
     * @return Current JWT token or null if not set
     */
    public static String getAuthToken() {
        try {
            // Read the contents of the auth_token.txt file
            return Files.readString(Path.of("auth_token.txt")).trim(); // .trim() to remove any extra newlines or spaces
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if there's an error reading the file
        }
    }

    /**
     * Clears the stored JWT token
     */
    public static void clearAuthToken() {
        prefs.remove(JWT_TOKEN_KEY);
    }

    /**
     * Creates a request builder with common headers and auth if available
     */
    private static HttpRequest.Builder createRequestBuilder(String url) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json");

        String token = getAuthToken();
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        return builder;
    }

    /**
     * Makes an asynchronous GET request
     * @param url API endpoint URL
     * @param responseType Class of the expected response
     * @return CompletableFuture with the parsed response
     */
    public static <T> CompletableFuture<T> getAsync(String url, Class<T> responseType) {
        HttpRequest request = createRequestBuilder(url)
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        return gson.fromJson(response.body(), responseType);
                    } else {
                        throw new ApiException("API request failed with status: " + response.statusCode(), response.statusCode());
                    }
                });
    }

    /**
     * Makes a POST request with JavaFX Service wrapper for UI thread safety
     * @param url API endpoint URL
     * @param requestBody Object to be sent as JSON
     * @param responseType Class of the expected response
     * @return Service that can be bound to UI components
     */
    public static <T, R> Service<R> postService(String url, T requestBody, Class<R> responseType) {
        Service<R> service = new Service<>() {
            @Override
            protected Task<R> createTask() {
                return new Task<>() {
                    @Override
                    protected R call() throws Exception {
                        String jsonBody = gson.toJson(requestBody);
                        HttpRequest request = createRequestBuilder(url)
                                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                                .header("Content-Type", "application/json")
                                .build();

                        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        return gson.fromJson(response.body(), responseType);
                    }
                };
            }
        };
        service.start();
        return service;
    }

    /**
     * Makes a PUT request with JavaFX Service wrapper
     * @param url API endpoint URL
     * @param requestBody Object to be sent as JSON
     * @param responseType Class of the expected response
     * @return Service that can be bound to UI components
     */
    public static <T, R> Service<R> putService(String url, T requestBody, Class<R> responseType) {
        Service<R> service = new Service<>() {
            @Override
            protected Task<R> createTask() {
                return new Task<>() {
                    @Override
                    protected R call() throws Exception {
                        String jsonBody = gson.toJson(requestBody);
                        HttpRequest request = createRequestBuilder(url)
                                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                                .header("Content-Type", "application/json")
                                .build();

                        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        if (response.statusCode() >= 200 && response.statusCode() < 300) {
                            return gson.fromJson(response.body(), responseType);
                        } else {
                            throw new ApiException("API request failed with status: " + response.statusCode(), response.statusCode());
                        }
                    }
                };
            }
        };
        service.start();
        return service;
    }

    /**
     * Makes a DELETE request
     * @param url API endpoint URL
     * @return CompletableFuture with the status code
     */
    public static CompletableFuture<Integer> deleteAsync(String url) {
        HttpRequest request = createRequestBuilder(url)
                .DELETE()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode);
    }

    /**
     * Makes a login request and stores the JWT token if successful
     * @param url Login endpoint URL
     * @param credentials Login credentials object
     * @param responseType Expected response type
     * @return Service that can be bound to UI components
     */
    public static <T, R> Service<R> loginService(String url, T credentials, Class<R> responseType) {
        Service<R> service = new Service<>() {
            @Override
            protected Task<R> createTask() {
                return new Task<>() {
                    @Override
                    protected R call() throws Exception {
                        String jsonBody = gson.toJson(credentials);
                        HttpRequest request = HttpRequest.newBuilder()
                                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                                .uri(URI.create(url))
                                .header("Content-Type", "application/json")
                                .build();

                        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        if (response.statusCode() >= 200 && response.statusCode() < 300) {
                            // Store the token if the response contains one
                            System.out.println("HER2e23423ERER");
                            if (response.headers().firstValue("Authorization").isPresent()) {
                                System.out.println("HERERER");
                                String authHeader = response.headers().firstValue("Authorization").get();
                                if (authHeader.startsWith("Bearer ")) {
                                    System.out.println(authHeader);
                                    setAuthToken(authHeader.substring(7));
                                }
                            }
                            System.out.println("HERE2");
                            return gson.fromJson(response.body(), responseType);
                        } else {
                            return gson.fromJson(response.body(), responseType);
                        }
                    }
                };
            }
        };
        service.start();
        return service;
    }

    public static String getGuestId() {
        String token = getAuthToken();
        return extractGuestIdFromToken(token);
    }

    /**
     * Custom exception for API errors
     */
    public static class ApiException extends RuntimeException {
        private final int statusCode;

        public ApiException(String message, int statusCode) {
            super(message);
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }

    public static String extractGuestIdFromToken(String token) {
    if (token == null || token.isEmpty()) return null;

    try {
        // Split the token into parts: header.payload.signature
        String[] parts = token.split("\\.");
        if (parts.length != 3) return null;

        // Decode the payload (part[1]) from Base64URL
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

        // Parse the payload JSON
        JsonObject payload = JsonParser.parseString(payloadJson).getAsJsonObject();

        // Extract guestId
        return payload.has("sub") ? payload.get("sub").getAsString() : null;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
}
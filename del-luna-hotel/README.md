# Del Luna Hotel Management System

## Overview
The Del Luna Hotel Management System is a JavaFX application designed to facilitate hotel booking management. It provides a user-friendly interface for selecting rooms and managing bookings.

## Project Structure
```
del-luna-hotel
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── delluna
│   │   │           └── dellunahotel
│   │   │               ├── controllers
│   │   │               ├── models
│   │   │               │   └── Booking.java
│   │   │               └── views
│   │   ├── resources
│   │   │   └── com
│   │   │       └── delluna
│   │   │           └── dellunahotel
│   │   │               ├── styles
│   │   │               └── views
│   │   │                   ├── SelectingRooms2.fxml
│   │   │                   └── BookingList.fxml
├── README.md
```

## Files Description

- **Booking.java**: Defines the `Booking` class with properties such as `bookingId`, `guestId`, `roomNum`, `serviceIds`, `checkInDate`, `checkOutDate`, `totalRoomCost`, `totalServiceCost`, `taxAmount`, `discountAmount`, `grandTotal`, and `pointsObtained`.

- **SelectingRooms2.fxml**: FXML layout for the room selection interface, structured using a `BorderPane` containing a `ScrollPane` and an `HBox` for the header.

- **BookingList.fxml**: FXML layout for displaying a list of bookings, following a similar structure to `SelectingRooms2.fxml`.

## Getting Started
To run the application, ensure you have Java and JavaFX set up in your development environment. Clone the repository and navigate to the project directory. Compile and run the application using your preferred IDE or command line.

## Future Enhancements
- Implement additional features for managing bookings.
- Enhance the user interface for better usability.
- Add database integration for persistent storage of bookings.
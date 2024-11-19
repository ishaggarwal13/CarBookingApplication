# Car Booking Application

## Project Overview

The **Car Booking Application** is an Android application that allows users to seamlessly book rides. The app includes functionalities such as user authentication (Login/Registration), searching for available drivers, viewing ride details, and confirming bookings. Built using **Kotlin** and Android SDK, the app utilizes key components like **Intents**, **RecyclerView**, **Google Maps API**, and **Coroutines** to provide a smooth and interactive experience.

### Key Features:
- **Splash Screen**: Welcomes users before navigating to the Login screen.
- **Login and Registration**: Secure login/registration functionality.
- **Main Activity**: Users can enter their pickup and drop-off locations, select a date, and search for available drivers.
- **Driver List Activity**: Displays available drivers in a **RecyclerView**, showing their details like name, price, and availability.
- **Driver Details**: Maps showing pickup and drop-off locations along with driver information.
- **Booking Confirmation**: Users can confirm bookings for a selected driver.

## Screenshots

### Splash Screen
![Splash Screen](Snapshots/splash_screen.png =300x)

### Login Page
![Login Page](Snapshots/Login.png =300x)

### Register Page
![Register Page](Snapshots/Register.png =300x)

### Main Activity
![Main Activity](Snapshots/MainPage.png =300x)

### Driver List
![Driver List](Snapshots/DriverList.png =300x)

### Driver Details
![Driver Details](Snapshots/DriverDetails.png =300x)

### Booking Confirmation
![Booking Confirmation](Snapshots/Booking.png =300x)

## Technology Stack

- **Android SDK** (Kotlin)
- **Google Maps API**
- **RecyclerView**
- **Firebase Authentication** (optional, if used)
- **Coroutines** for handling asynchronous tasks
- **Material Design** components for UI
- **Intents** for navigation between activities

## Installation

To get started with this project, follow these steps:

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/ishaggarwal13/CarBookingApplication.git
2. Open the project in Android Studio.
3. Make sure to add the required dependencies in the build.gradle file (if not already present).
4. Add your Google Maps API key in the AndroidManifest.xml file.
   ```bash
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
      package="com.example.carbookingapp">
      <application
        android:usesCleartextTraffic="true"
        android:apiKey="YOUR_GOOGLE_MAPS_API_KEY"
        android:label="Car Booking Application">
        <!-- Other app components -->
      </application>
    </manifest>
5. Build and run the application on an Android emulator or real device.


## Usage
**1. Splash Screen:** 
   - The app starts with a splash screen that automatically transitions to the Login Activity.

**2. Login and Registration:**
   - Login Page: Allows users to log in with existing credentials.
   - Registration Page: Allows new users to create an account by entering their name, email, and password.

**3. Main Activity:**
- Users enter:
   - Pickup Location
   - Drop-off Location
   - Travel Date (using the date picker)
   - Number of Passengers
- Once these details are provided, users can click the Search button to find available drivers.

**4. Driver List Activity:**
   - A list of available drivers is displayed in a RecyclerView with information such as name, price, and available time.
   - Users can click the Book button on a driver to view more details.

**6. Driver Details:**
   - This activity shows the selected driver’s details on a map, displaying the route from the pickup location to the drop-off location.

**7. Booking Confirmation:**
   - After selecting a driver, the user can confirm the booking and proceed to the final confirmation screen.


## Code Structure
The project is organized into the following modules:

1. **Splash Screen Activity:** Displays the splash screen and transitions to the login page.
2. **Login Activity: Handles user login.**
3. **Registration Activity:** Handles user registration.
4. **Main Activity:** Allows users to enter ride details.
5. **Driver List Activity:** Displays available drivers using a RecyclerView.
6. **Driver Details Activity:** Shows detailed driver information and the map route.
7. **Booking Confirmation Activity:** Displays a summary of the booking for confirmation.

## Future Scope

- **Real-Time Ride Tracking:** Integrate real-time tracking to allow users to track their booked ride.
- **Payment Integration:** Implement a payment gateway for in-app payments (e.g., Google Pay, Stripe).
- **Push Notifications:** Notify users about ride status, arrival times, or booking confirmations.
- **Rating System:** Allow users to rate their ride and provide feedback on drivers.
- **Advanced Search Filters:** Allow users to filter drivers by price, rating, or vehicle type.

## Contributing
If you'd like to contribute to this project, feel free to open issues or create pull requests. Please make sure to follow the coding style and write clear commit messages.

## Acknowledgements

- **Google Maps API:** For the integration of maps and location services.
- **Material Design:** For UI components and design guidelines.
- **Android SDK:** For building and running the app.


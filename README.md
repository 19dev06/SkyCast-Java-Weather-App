# SkyCast - Java Weather App

SkyCast is a desktop weather application built using Java and Swing. It allows users to search for a city and view its current weather information through a simple, modern interface.

## Features

- Search weather by city name
- Display current temperature in Celsius
- Show weather conditions
- Display feels-like temperature
- Show humidity
- Display wind speed
- Modern dark-themed graphical user interface
- Fetch real-time weather data using the OpenWeather API

## Technologies Used

- Java 21
- Java Swing
- Maven
- OpenWeather API
- JSON
- Eclipse IDE

## Requirements

- Java Development Kit (JDK) 21
- Eclipse IDE
- Maven
- An OpenWeather API key

## Setup and Installation

1. Install JDK 21 and Eclipse IDE.
2. Import the project into Eclipse as an existing Maven project.
3. Make sure the required dependencies are configured in `pom.xml`.
4. Create an API key through the OpenWeather website.
5. Add a Windows environment variable with the following details:

   - Variable name: `OPENWEATHER_API_KEY`
   - Variable value: Your actual OpenWeather API key

6. Restart Eclipse so it can access the environment variable.
7. Open `WeatherApp.java`.
8. Run the Java application.
9. Enter a city name and click the search button to view its weather.

## Project Structure

```text
WeatherApp/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── weather/
│                   └── WeatherApp.java
├── pom.xml
├── .gitignore
└── README.md
```

## Security

The application reads the OpenWeather API key from a Windows environment variable. Never publish your actual API key in source code or upload it to a public GitHub repository.

## Future Improvements

- Add weather icons
- Display a five-day weather forecast
- Add temperature unit conversion
- Improve error handling and user experience

## Author

**Dev Sharma**
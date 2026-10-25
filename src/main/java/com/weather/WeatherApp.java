
package com.weather;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class WeatherApp extends JFrame {

	private static final String API_KEY =
	        System.getenv("OPENWEATHER_API_KEY");
    private final Color background = new Color(11, 18, 32);
    private final Color panelColor = new Color(24, 36, 57);
    private final Color heroColor = new Color(35, 61, 96);
    private final Color accent = new Color(76, 169, 255);
    private final Color white = new Color(245, 248, 255);
    private final Color muted = new Color(169, 186, 209);

    private JTextField cityField;
    private JButton searchButton;

    private JLabel cityLabel;
    private JLabel temperatureLabel;
    private JLabel conditionLabel;
    private JLabel feelsLabel;
    private JLabel humidityValue;
    private JLabel windValue;
    private JLabel feelsValue;
    private JLabel statusLabel;

    public WeatherApp() {

        setTitle("SkyCast - Weather Dashboard");
        setSize(720, 620);
        setMinimumSize(new Dimension(560, 540));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel();
        root.setBackground(background);
        root.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel logo = new JLabel("  ☀  SkyCast");
        logo.setFont(new Font("SansSerif", Font.BOLD, 27));
        logo.setForeground(white);

        JLabel subtitle = new JLabel("WEATHER DASHBOARD");
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        subtitle.setForeground(muted);

        header.add(logo, BorderLayout.WEST);
        header.add(subtitle, BorderLayout.EAST);

        root.add(header);
        root.add(Box.createVerticalStrut(22));

        // Search section
        RoundedPanel searchPanel = new RoundedPanel(panelColor, 18);
        searchPanel.setLayout(new BorderLayout(12, 0));
        searchPanel.setBorder(
            BorderFactory.createEmptyBorder(12, 16, 12, 12)
        );
        searchPanel.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 60)
        );

        cityField = new JTextField();
        cityField.setText("Delhi");
        cityField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        cityField.setForeground(white);
        cityField.setCaretColor(white);
        cityField.setBackground(panelColor);
        cityField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(accent);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        searchPanel.add(cityField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        root.add(searchPanel);
        root.add(Box.createVerticalStrut(22));

        // Main weather card
        RoundedPanel weatherCard = new RoundedPanel(heroColor, 25);
        weatherCard.setLayout(new BorderLayout(10, 10));
        weatherCard.setBorder(
            BorderFactory.createEmptyBorder(24, 25, 24, 25)
        );
        weatherCard.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 220)
        );

        JPanel weatherInfo = new JPanel();
        weatherInfo.setOpaque(false);
        weatherInfo.setLayout(new BoxLayout(weatherInfo, BoxLayout.Y_AXIS));

        cityLabel = new JLabel("CURRENT WEATHER");
        cityLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        cityLabel.setForeground(muted);

        temperatureLabel = new JLabel("--°C");
        temperatureLabel.setFont(new Font("SansSerif", Font.BOLD, 56));
        temperatureLabel.setForeground(white);

        conditionLabel = new JLabel("Search for a city");
        conditionLabel.setFont(new Font("SansSerif", Font.PLAIN, 19));
        conditionLabel.setForeground(white);

        feelsLabel = new JLabel("Feels like --°C");
        feelsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        feelsLabel.setForeground(muted);

        weatherInfo.add(cityLabel);
        weatherInfo.add(Box.createVerticalStrut(10));
        weatherInfo.add(temperatureLabel);
        weatherInfo.add(Box.createVerticalStrut(4));
        weatherInfo.add(conditionLabel);
        weatherInfo.add(Box.createVerticalStrut(8));
        weatherInfo.add(feelsLabel);

        JLabel weatherIcon = new JLabel("☀");
        weatherIcon.setFont(new Font("SansSerif", Font.PLAIN, 75));
        weatherIcon.setForeground(new Color(255, 211, 115));

        weatherCard.add(weatherInfo, BorderLayout.CENTER);
        weatherCard.add(weatherIcon, BorderLayout.EAST);

        root.add(weatherCard);
        root.add(Box.createVerticalStrut(20));

        // Weather detail cards
        JPanel details = new JPanel(new GridLayout(1, 3, 12, 0));
        details.setOpaque(false);
        details.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 125)
        );

        humidityValue = new JLabel("--%");
        windValue = new JLabel("-- m/s");
        feelsValue = new JLabel("--°C");

        details.add(makeCard("Humidity", humidityValue, "💧"));
        details.add(makeCard("Wind Speed", windValue, "≋"));
        details.add(makeCard("Feels Like", feelsValue, "🌡"));

        root.add(details);
        root.add(Box.createVerticalStrut(15));

        // Status message
        statusLabel = new JLabel("Enter a city to get current weather.");
        statusLabel.setForeground(muted);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        root.add(statusLabel);

        setContentPane(root);

        searchButton.addActionListener(e -> searchWeather());
        cityField.addActionListener(e -> searchWeather());

        setVisible(true);
    }

    private JPanel makeCard(
            String title, JLabel valueLabel, String symbol) {

        RoundedPanel card = new RoundedPanel(panelColor, 18);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(
            BorderFactory.createEmptyBorder(15, 12, 15, 8)
        );

        JLabel iconLabel = new JLabel(symbol);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 23));
        iconLabel.setForeground(accent);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLabel.setForeground(muted);

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        valueLabel.setForeground(white);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);

        return card;
    }

    private void searchWeather() {
    	if (API_KEY == null || API_KEY.isBlank()) {
    	    statusLabel.setText("Please configure your API key.");
    	    return;
    	}

        String city = cityField.getText().trim();

        if (city.isEmpty()) {
            statusLabel.setText("Please enter a city name.");
            return;
        }
        searchButton.setEnabled(false);
        statusLabel.setText("Loading weather...");
        
        SwingWorker<JSONObject, Void> worker =
            new SwingWorker<JSONObject, Void>() {

            @Override
            protected JSONObject doInBackground() throws Exception {

                String encodedCity = URLEncoder.encode(
                    city, StandardCharsets.UTF_8
                );

                String url =
                    "https://api.openweathermap.org/data/2.5/weather"
                    + "?q=" + encodedCity
                    + "&appid=" + API_KEY
                    + "&units=metric";

                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

                HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
                );

                JSONObject data = new JSONObject(response.body());

                if (response.statusCode() != 200) {
                    throw new Exception(
                        data.optString("message", "Weather request failed")
                    );
                }

                return data;
            }

            @Override
            protected void done() {

                try {
                    JSONObject data = get();

                    String cityName = data.getString("name");
                    JSONObject main = data.getJSONObject("main");

                    double temperature = main.getDouble("temp");
                    double feelsLike = main.getDouble("feels_like");
                    int humidity = main.getInt("humidity");

                    String condition = data
                        .getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("description");

                    double windSpeed = data
                        .getJSONObject("wind")
                        .optDouble("speed", 0);

                    cityLabel.setText(cityName.toUpperCase()
                        + "  •  CURRENT WEATHER");

                    temperatureLabel.setText(
                        String.format("%.1f°C", temperature)
                    );

                    conditionLabel.setText(
                        capitalize(condition)
                    );

                    feelsLabel.setText(
                        String.format("Feels like %.1f°C", feelsLike)
                    );

                    humidityValue.setText(humidity + "%");

                    windValue.setText(
                        String.format("%.1f m/s", windSpeed)
                    );

                    feelsValue.setText(
                        String.format("%.1f°C", feelsLike)
                    );

                    statusLabel.setText("Weather updated successfully.");

                } catch (Exception ex) {

                    Throwable cause = ex.getCause();

                    String message = cause != null
                        ? cause.getMessage()
                        : ex.getMessage();

                    statusLabel.setText(
                        "Could not load weather: " + message
                    );

                } finally {
                    searchButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private String capitalize(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        return text.substring(0, 1).toUpperCase()
            + text.substring(1);
    }

    // Panel with rounded corners
    private static class RoundedPanel extends JPanel {

        private final Color color;
        private final int radius;

        RoundedPanel(Color color, int radius) {
            this.color = color;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);
            g2.fillRoundRect(
                0, 0, getWidth(), getHeight(),
                radius, radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WeatherApp());
    }
}

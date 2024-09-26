package com.umar.velora;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Locale;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

public class VeloraAppMain {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(VeloraAppMain::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Velora App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create CardLayout panel
        JPanel cardPanel = new JPanel(new CardLayout());

        // Create panels
        JPanel homePanel = createHomePanel(cardPanel);
        JPanel mainPanel = createMainPanel(cardPanel);

        cardPanel.add(homePanel, "Home");
        cardPanel.add(mainPanel, "Main");

        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "Home");

        frame.add(cardPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createHomePanel(JPanel cardPanel) {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Styling.BACKGROUND_COLOR);

        JLabel welcomeLabel = new JLabel("Velora", SwingConstants.CENTER);
        welcomeLabel.setFont(Styling.TITLE_FONT);
        welcomeLabel.setForeground(Styling.FOREGROUND_COLOR);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JFreeChart chart = ChartFactory.createLineChart(
            "",
            "", 
            "", 
            createDataset()
        );

        chart.setBackgroundPaint(Styling.BACKGROUND_COLOR);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Styling.BACKGROUND_COLOR);
        plot.getRenderer().setSeriesPaint(0, Styling.FOREGROUND_COLOR);

        chart.removeLegend();
        chart.setBorderVisible(false);
        plot.setOutlineVisible(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Styling.BACKGROUND_COLOR);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setPopupMenu(null);

        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        homePanel.add(chartPanel, BorderLayout.CENTER);

        JButton startButton = new JButton("Enter Application");
        startButton.setFont(Styling.BUTTON_FONT);
        startButton.setFocusPainted(false);
        startButton.setBackground(Styling.BUTTON_COLOR);
        startButton.setForeground(Styling.BUTTON_TEXT_COLOR);
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Styling.BACKGROUND_COLOR);
        buttonPanel.add(startButton);

        homePanel.add(buttonPanel, BorderLayout.SOUTH);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cardPanel.getLayout());
                cl.show(cardPanel, "Main");
            }
        });

        return homePanel;
    }

    private static DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1, "Values", "Day 1");
        dataset.addValue(3, "Values", "Day 2");
        dataset.addValue(2, "Values", "Day 3");
        dataset.addValue(5, "Values", "Day 4");
        dataset.addValue(4, "Values", "Day 5");
        return dataset;
    }

    private static JPanel createMainPanel(JPanel cardPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Styling.BACKGROUND_COLOR);

        String[] cryptoIds = {"solana", "dogecoin"};
        String[] currencies = {"usd", "cad", "inr", "rub", "eur"};

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Styling.BACKGROUND_COLOR);

        JLabel currencyLabel = new JLabel("Select Currency:");
        currencyLabel.setForeground(Styling.FOREGROUND_COLOR);
        currencyLabel.setFont(Styling.DEFAULT_FONT);
        topPanel.add(currencyLabel);

        JComboBox<String> currencyComboBox = new JComboBox<>(currencies);
        currencyComboBox.setBackground(Styling.BUTTON_COLOR);
        currencyComboBox.setForeground(Styling.BUTTON_TEXT_COLOR);
        currencyComboBox.setFont(Styling.DEFAULT_FONT);
        topPanel.add(currencyComboBox);

        JButton backButton = new JButton("Back to Home");
        backButton.setBackground(Styling.BUTTON_COLOR);
        backButton.setForeground(Styling.BUTTON_TEXT_COLOR);
        backButton.setFont(Styling.DEFAULT_FONT);
        topPanel.add(backButton);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Cryptocurrency", "Price (USD)"}, 0);
        JTable table = new JTable(model);
        table.setBackground(Styling.BACKGROUND_COLOR);
        table.setForeground(Styling.FOREGROUND_COLOR);
        table.setGridColor(Styling.FOREGROUND_COLOR);
        table.setFont(Styling.DEFAULT_FONT);
        table.setRowHeight(25);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Styling.BUTTON_COLOR);
        header.setForeground(Styling.BUTTON_TEXT_COLOR);
        header.setFont(Styling.DEFAULT_FONT);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Styling.BACKGROUND_COLOR);

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setBackground(Styling.BUTTON_COLOR);
        refreshButton.setForeground(Styling.BUTTON_TEXT_COLOR);
        refreshButton.setFont(Styling.DEFAULT_FONT);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);

        CryptoDataFetcher dataFetcher = new CryptoDataFetcher();

        ActionListener refreshAction = e -> {
            String selectedCurrency = (String) currencyComboBox.getSelectedItem();
            Map<String, String> prices = dataFetcher.getCryptoPrices(cryptoIds, selectedCurrency);

            model.setRowCount(0);

            NumberFormat currencyFormat;
            switch (selectedCurrency) {
                case "usd":
                    currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                    break;
                case "cad":
                    currencyFormat = NumberFormat.getCurrencyInstance(Locale.CANADA);
                    break;
                case "inr":
                    currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                    break;
                case "rub":
                    currencyFormat = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
                    break;
                case "eur":
                    currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
                    break;
                default:
                    currencyFormat = NumberFormat.getCurrencyInstance();
                    break;
            }

            for (String id : cryptoIds) {
                String name = id.replace("-", " ").toUpperCase();
                String priceStr = prices.get(id);
                String formattedPrice = "N/A";
                if (!priceStr.equals("N/A")) {
                    double priceValue = Double.parseDouble(priceStr);
                    formattedPrice = currencyFormat.format(priceValue);
                }
                model.addRow(new Object[]{name, formattedPrice});
            }

            model.setColumnIdentifiers(new Object[]{"Cryptocurrency", "Price (" + selectedCurrency.toUpperCase() + ")"});
        };

        refreshAction.actionPerformed(null);
        refreshButton.addActionListener(refreshAction);
        currencyComboBox.addActionListener(refreshAction);

        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (cardPanel.getLayout());
            cl.show(cardPanel, "Home");
        });

        new Timer(60000, refreshAction).start();

        return panel;
    }
}

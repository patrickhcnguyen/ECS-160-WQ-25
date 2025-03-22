package com.ecs160.hw1.config;

public class AnalyzerConfig {
    private static AnalyzerConfig instance;
    private final boolean isWeighted;
    private final String jsonFilePath;

    private AnalyzerConfig(boolean isWeighted, String jsonFilePath) {
        this.isWeighted = isWeighted;
        this.jsonFilePath = jsonFilePath;
    }

    public static AnalyzerConfig getInstance(boolean isWeighted, String jsonFilePath) {
        if (instance == null) {
            synchronized (AnalyzerConfig.class) {
                if (instance == null) {
                    instance = new AnalyzerConfig(isWeighted, jsonFilePath);
                }
            }
        }
        return instance;
    }

    public boolean isWeighted() {
        return isWeighted;
    }

    public String getJsonFilePath() {
        return jsonFilePath;
    }
} 
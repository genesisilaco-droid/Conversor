package com.example.app_conversion;

/**
 * Categorías de conversión. Cada una tiene su nombre, un emoji para la imagen,
 * un color de fondo y su lista de unidades.
 * La PRIMERA unidad de cada lista es la unidad base (factor 1).
 * Para añadir una unidad nueva basta con añadir una línea: no hace falta ningún if.
 */
public enum Category {

    DISTANCE("Distancia", "📏", 0xFFF8BBD0,
            new Unit("Metros", 1),
            new Unit("Kilómetros", 1000),
            new Unit("Centímetros", 0.01),
            new Unit("Milímetros", 0.001),
            new Unit("Millas", 1609.344),
            new Unit("Yardas", 0.9144),
            new Unit("Pies", 0.3048),
            new Unit("Pulgadas", 0.0254)),

    TEMPERATURE("Temperatura", "🌡️", 0xFFFFCCBC,
            new Unit("Celsius", 1, 0),
            new Unit("Fahrenheit", 5.0 / 9.0, -160.0 / 9.0),
            new Unit("Kelvin", 1, -273.15)),

    MASS("Masa y peso", "⚖️", 0xFFD1C4E9,
            new Unit("Gramos", 1),
            new Unit("Kilogramos", 1000),
            new Unit("Miligramos", 0.001),
            new Unit("Toneladas", 1_000_000),
            new Unit("Libras", 453.59237),
            new Unit("Onzas", 28.349523125)),

    VOLUME("Volumen", "🧪", 0xFFB3E5FC,
            new Unit("Litros", 1),
            new Unit("Mililitros", 0.001),
            new Unit("Metros cúbicos", 1000),
            new Unit("Galones (EE. UU.)", 3.785411784),
            new Unit("Tazas", 0.24)),

    TIME("Tiempo", "⏱️", 0xFFC8E6C9,
            new Unit("Segundos", 1),
            new Unit("Minutos", 60),
            new Unit("Horas", 3600),
            new Unit("Días", 86400),
            new Unit("Semanas", 604800)),

    DATA("Información digital", "💾", 0xFFFFF9C4,
            new Unit("Bits", 1),
            new Unit("Bytes", 8),
            new Unit("Kilobytes", 8.0 * 1024),
            new Unit("Megabytes", 8.0 * 1024 * 1024),
            new Unit("Gigabytes", 8.0 * 1024 * 1024 * 1024)),

    SPEED("Velocidad", "🚀", 0xFFFFE0B2,
            new Unit("Metros por segundo", 1),
            new Unit("Kilómetros por hora", 1 / 3.6),
            new Unit("Millas por hora", 0.44704),
            new Unit("Nudos", 0.514444));

    public final String label;
    public final String emoji;
    public final int color;
    public final Unit[] units;

    Category(String label, String emoji, int color, Unit... units) {
        this.label = label;
        this.emoji = emoji;
        this.color = color;
        this.units = units;
    }

    /** Fórmula genérica: origen -> unidad base -> destino. Sirve para TODAS las unidades. */
    public double convert(double value, int from, int to) {
        return units[to].fromBase(units[from].toBase(value));
    }

    /** Comprueba si el valor tiene sentido (no hay distancias negativas ni nada por debajo del cero absoluto). */
    public boolean isValid(double value, int from) {
        if (this == TEMPERATURE) {
            return units[from].toBase(value) >= -273.15;
        }
        return value >= 0;
    }

    /** El Spinner de categorías muestra este texto. */
    @Override
    public String toString() {
        return label;
    }
}
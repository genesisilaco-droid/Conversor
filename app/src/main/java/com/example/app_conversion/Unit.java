package com.example.app_conversion;

/**
 * Una unidad de medida.
 * Se guarda cómo pasar a la unidad base de su categoría:
 *     valorBase = valor * factor + offset
 * Para casi todas las unidades offset = 0 (solo multiplicar).
 * La temperatura usa también offset (0 ºC = 32 ºF = 273.15 K).
 */
public class Unit {

    private final String name;
    private final double factor;
    private final double offset;

    public Unit(String name, double factor) {
        this(name, factor, 0);
    }

    public Unit(String name, double factor, double offset) {
        this.name = name;
        this.factor = factor;
        this.offset = offset;
    }

    /** De esta unidad a la unidad base. */
    public double toBase(double value) {
        return value * factor + offset;
    }

    /** De la unidad base a esta unidad. */
    public double fromBase(double baseValue) {
        return (baseValue - offset) / factor;
    }

    /** El Spinner usa toString() para mostrar el nombre. */
    @Override
    public String toString() {
        return name;
    }
}
# ✿ Multi-Converter ✿

App Android para convertir valores entre distintas unidades de medida. Práctica del Sprint 2 (Actividad 2), hecha en grupo por **Erik** y **Andrea**.

## Qué hace
1. **Pantalla 1 – Categoría:** eliges qué quieres convertir (distancia, temperatura, masa, volumen, tiempo, información digital o velocidad). La imagen de la categoría se dibuja con la clase `Bitmap` y cambia al elegir otra.
2. **Pantalla 2 – Conversión:** escribes un valor, eliges la unidad de origen y la de destino y pulsas *Convertir*. Puedes intercambiar las unidades, copiar el resultado y volver para elegir otra categoría.

Los avisos y errores (campo vacío, número no válido, unidades iguales, valores imposibles) se muestran con **Snackbar**.

## Cómo funciona la conversión
Cada unidad guarda cómo pasar a la unidad base de su categoría:

```
valorBase = valor × factor + offset
```

Para convertir se pasa de la unidad de origen a la base y de la base a la de destino. La misma fórmula sirve para todas las unidades, sin `if` ni `switch` por cada pareja. La temperatura funciona igual gracias al `offset`. Para añadir una unidad nueva basta con una línea en `Category.java`.

## Archivos principales
| Archivo | Para qué sirve |
|---|---|
| `Unit.java` | Una unidad: nombre, factor y offset |
| `Category.java` | Las categorías y sus unidades, más la fórmula genérica |
| `MainActivity.java` | Pantalla 1: Spinner de categorías, imagen Bitmap e Intent |
| `ConversionActivity.java` | Pantalla 2: conversión, validación y Snackbar |

## Elementos usados
Button · EditText · TextView · Spinner · Snackbar · Bitmap · segunda Activity con Intent

## Cómo ejecutarla
Abrir la carpeta en Android Studio, sincronizar Gradle y pulsar ▶ Run. El APK se genera en `app/build/outputs/apk/debug/`.

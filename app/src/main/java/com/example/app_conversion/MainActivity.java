package com.example.app_conversion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Primera Activity: el usuario elige la categoría de conversión.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "extra_category";

    private ImageView ivCategoria;
    private TextView tvNombreCategoria, tvUnidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivCategoria = findViewById(R.id.ivCategoria);
        tvNombreCategoria = findViewById(R.id.tvNombreCategoria);
        tvUnidades = findViewById(R.id.tvUnidades);
        Spinner spCategoria = findViewById(R.id.spCategoria);
        Button btnContinuar = findViewById(R.id.btnContinuar);

        // Spinner con todas las categorías del enum
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                R.layout.item_spinner, Category.values());
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spCategoria.setAdapter(adapter);

        // Cada vez que cambia la categoría, cambia la imagen (Bitmap) y los textos
        spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mostrarCategoria(Category.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Abrir la segunda Activity pasando la categoría con un Intent
        btnContinuar.setOnClickListener(v -> {
            Category elegida = (Category) spCategoria.getSelectedItem();
            Intent intent = new Intent(this, ConversionActivity.class);
            intent.putExtra(EXTRA_CATEGORY, elegida.name());
            startActivity(intent);
        });
    }

    private void mostrarCategoria(Category categoria) {
        ivCategoria.setImageBitmap(crearImagen(categoria, 420));
        tvNombreCategoria.setText(categoria.label);
        tvUnidades.setText(categoria.units.length + " unidades disponibles");

        // Pequeña animación al cambiar de imagen
        ivCategoria.setAlpha(0f);
        ivCategoria.setScaleX(0.8f);
        ivCategoria.setScaleY(0.8f);
        ivCategoria.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(250).start();
    }

    /**
     * Crea la imagen de la categoría con la clase Bitmap:
     * un círculo de color con un anillo blanco y el emoji de la categoría encima.
     */
    private Bitmap crearImagen(Category categoria, int tamano) {
        Bitmap bitmap = Bitmap.createBitmap(tamano, tamano, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float centro = tamano / 2f;

        // Círculo de fondo con el color de la categoría
        paint.setColor(categoria.color);
        canvas.drawCircle(centro, centro, centro, paint);

        // Anillo blanco decorativo
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(tamano * 0.03f);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centro, centro, centro * 0.86f, paint);
        paint.setStyle(Paint.Style.FILL);

        // Emoji centrado
        paint.setTextSize(tamano * 0.48f);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float y = centro - (fm.ascent + fm.descent) / 2f;
        canvas.drawText(categoria.emoji, centro, y, paint);

        return bitmap;
    }
}

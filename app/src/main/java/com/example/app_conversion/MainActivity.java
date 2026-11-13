package com.example.app_conversion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

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
        Spinner spCategoria = findViewById(R.id.spCategoria);
        Button btnContinuar = findViewById(R.id.btnContinuar);

        // Spinner con todas las categorías del enum
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Category.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);

        // Cada vez que cambia la categoría, cambia la imagen (Bitmap)
        spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ivCategoria.setImageBitmap(crearImagen(Category.values()[position], 400));
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

    /**
     * Crea la imagen de la categoría con la clase Bitmap:
     * un círculo de color con el emoji de la categoría dibujado encima.
     */
    private Bitmap crearImagen(Category categoria, int tamano) {
        Bitmap bitmap = Bitmap.createBitmap(tamano, tamano, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        float centro = tamano / 2f;
        paint.setColor(categoria.color);
        canvas.drawCircle(centro, centro, centro, paint);

        paint.setTextSize(tamano * 0.5f);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float y = centro - (fm.ascent + fm.descent) / 2f;
        canvas.drawText(categoria.emoji, centro, y, paint);

        return bitmap;
    }
}

package com.example.app_conversion;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

/**
 * Segunda Activity: convierte valores dentro de la categoría elegida en MainActivity.
 */
public class ConversionActivity extends AppCompatActivity {

    private Category category;
    private EditText etValor;
    private Spinner spOrigen, spDestino;
    private TextView tvResultado;
    private final DecimalFormat formato = new DecimalFormat("#,##0.######");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conversion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Recibir la categoría que manda la primera Activity por el Intent
        String nombre = getIntent().getStringExtra(MainActivity.EXTRA_CATEGORY);
        if (nombre == null) {
            finish();
            return;
        }
        category = Category.valueOf(nombre);

        // 2. Enlazar las vistas
        TextView tvTitulo = findViewById(R.id.tvTitulo);
        etValor = findViewById(R.id.etValor);
        spOrigen = findViewById(R.id.spOrigen);
        spDestino = findViewById(R.id.spDestino);
        tvResultado = findViewById(R.id.tvResultado);
        Button btnConvertir = findViewById(R.id.btnConvertir);
        Button btnVolver = findViewById(R.id.btnVolver);

        tvTitulo.setText("✿ " + category.label + " ✿");

        // 3. Rellenar los Spinner con las unidades de ESA categoría
        ArrayAdapter<Unit> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, category.units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrigen.setAdapter(adapter);
        spDestino.setAdapter(adapter);
        spDestino.setSelection(1);

        btnConvertir.setOnClickListener(v -> convertir());
        btnVolver.setOnClickListener(v -> finish()); // vuelve a la primera Activity
    }

    private void convertir() {
        ocultarTeclado();
        String texto = etValor.getText().toString().trim().replace(',', '.');

        if (texto.isEmpty()) {
            mensaje("Escribe un valor para convertir");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            mensaje("El valor no es un número válido");
            return;
        }

        int origen = spOrigen.getSelectedItemPosition();
        int destino = spDestino.getSelectedItemPosition();

        if (origen == destino) {
            mensaje("Elige dos unidades distintas");
            return;
        }
        if (!category.isValid(valor, origen)) {
            mensaje("Ese valor no es posible en " + category.label.toLowerCase());
            return;
        }

        double resultado = category.convert(valor, origen, destino);
        tvResultado.setText(formato.format(valor) + " " + category.units[origen]
                + "\n= " + formato.format(resultado) + " " + category.units[destino]);
        mensaje("¡Conversión hecha! ♡");
    }

    /** Todos los mensajes emergentes con Snackbar (nunca Toast). */
    private void mensaje(String texto) {
        Snackbar.make(findViewById(R.id.main), texto, Snackbar.LENGTH_SHORT).show();
    }

    private void ocultarTeclado() {
        View foco = getCurrentFocus();
        if (foco != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(foco.getWindowToken(), 0);
        }
    }
}

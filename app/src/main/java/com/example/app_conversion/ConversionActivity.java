package com.example.app_conversion;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
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

    private static final int COLOR_ERROR = Color.parseColor("#C2185B");
    private static final int COLOR_OK = Color.parseColor("#7E57C2");

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
        TextView tvSubtitulo = findViewById(R.id.tvSubtitulo);
        etValor = findViewById(R.id.etValor);
        spOrigen = findViewById(R.id.spOrigen);
        spDestino = findViewById(R.id.spDestino);
        tvResultado = findViewById(R.id.tvResultado);
        Button btnConvertir = findViewById(R.id.btnConvertir);
        Button btnIntercambiar = findViewById(R.id.btnIntercambiar);
        Button btnVolver = findViewById(R.id.btnVolver);

        tvTitulo.setText(category.emoji + " " + category.label);
        tvSubtitulo.setText("Convierte entre " + category.units.length + " unidades ♡");

        // 3. Rellenar los Spinner con las unidades de ESA categoría
        ArrayAdapter<Unit> adapter = new ArrayAdapter<>(this,
                R.layout.item_spinner, category.units);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spOrigen.setAdapter(adapter);
        spDestino.setAdapter(adapter);
        spDestino.setSelection(1);

        btnConvertir.setOnClickListener(v -> convertir());
        btnIntercambiar.setOnClickListener(v -> intercambiar());
        btnVolver.setOnClickListener(v -> finish()); // vuelve a la primera Activity
    }

    private void convertir() {
        ocultarTeclado();
        String texto = etValor.getText().toString().trim().replace(',', '.');

        if (texto.isEmpty()) {
            error("Escribe un valor para convertir");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            error("El valor no es un número válido");
            return;
        }

        int origen = spOrigen.getSelectedItemPosition();
        int destino = spDestino.getSelectedItemPosition();

        if (origen == destino) {
            error("Elige dos unidades distintas");
            return;
        }
        if (!category.isValid(valor, origen)) {
            error("Ese valor no es posible en " + category.label.toLowerCase());
            return;
        }

        double resultado = category.convert(valor, origen, destino);
        String textoResultado = formato.format(resultado) + " " + category.units[destino];
        tvResultado.setText(formato.format(valor) + " " + category.units[origen]
                + "\n= " + textoResultado);

        // Animación del resultado
        tvResultado.setScaleX(0.9f);
        tvResultado.setScaleY(0.9f);
        tvResultado.animate().scaleX(1f).scaleY(1f).setDuration(200).start();

        // Snackbar de éxito con acción para copiar el resultado
        Snackbar snackbar = crearSnackbar("¡Conversión hecha! ♡", COLOR_OK);
        snackbar.setAction("Copiar", v -> copiar(textoResultado));
        snackbar.setActionTextColor(Color.parseColor("#FFE4EF"));
        snackbar.show();
    }

    /** Intercambia la unidad de origen y la de destino. */
    private void intercambiar() {
        int origen = spOrigen.getSelectedItemPosition();
        spOrigen.setSelection(spDestino.getSelectedItemPosition());
        spDestino.setSelection(origen);
        if (!etValor.getText().toString().trim().isEmpty()) {
            convertir();
        }
    }

    private void copiar(String texto) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Resultado", texto));
        crearSnackbar("Resultado copiado ✨", COLOR_OK).show();
    }

    /** Todos los mensajes emergentes con Snackbar (nunca Toast). */
    private void error(String texto) {
        crearSnackbar(texto, COLOR_ERROR).show();
    }

    private Snackbar crearSnackbar(String texto, int color) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main), texto, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(color);
        snackbar.setTextColor(Color.WHITE);
        return snackbar;
    }

    private void ocultarTeclado() {
        View foco = getCurrentFocus();
        if (foco != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(foco.getWindowToken(), 0);
        }
    }
}

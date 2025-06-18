package com.example.lab6;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Linea1Fragment extends Fragment {

    private EditText tarjeta, fecha, estEntrada, estSalida, tiempoViaje;
    private Button btnGuardar;
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private Linea1Adapter adapter;
    private List<Linea1> listaMovimientos;

    private Linea1 movedit = null; //objeto a editar

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linea1, container, false);

        db = FirebaseFirestore.getInstance();

        // Referencias
        tarjeta = view.findViewById(R.id.etIdTarjeta);
        fecha = view.findViewById(R.id.etFecha);
        estEntrada = view.findViewById(R.id.etEstacionEntrada);
        estSalida = view.findViewById(R.id.etEstacionSalida);
        tiempoViaje = view.findViewById(R.id.etTiempoViaje);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        recyclerView = view.findViewById(R.id.recyclerLinea1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaMovimientos = new ArrayList<>();

        adapter = new Linea1Adapter(listaMovimientos, movimiento -> {
            // Rellenado de formulario
            movedit = movimiento;
            tarjeta.setText(movimiento.getIdTarjeta());
            fecha.setText(movimiento.getFecha());
            estEntrada.setText(movimiento.getEstacionEntrada());
            estSalida.setText(movimiento.getEstacionSalida());
            tiempoViaje.setText(movimiento.getTiempoViaje());
            btnGuardar.setText("Actualizar");
        });

        recyclerView.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> {
            if (movedit != null) {
                actualizarMovimiento();
            } else {
                guardarMovimiento();
            }
        });

        cargarMovimientos();
        return view;
    }

    private void guardarMovimiento() {
        String idTarjeta = tarjeta.getText().toString().trim();
        String f = fecha.getText().toString().trim();
        String entrada = estEntrada.getText().toString().trim();
        String salida = estSalida.getText().toString().trim();
        String tiempo = tiempoViaje.getText().toString().trim();

        if (TextUtils.isEmpty(idTarjeta) || TextUtils.isEmpty(f) || TextUtils.isEmpty(entrada) || TextUtils.isEmpty(salida) || TextUtils.isEmpty(tiempo)) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Linea1 movimiento = new Linea1(idTarjeta, f, entrada, salida, tiempo);

        db.collection("movimientos-linea1")
                .add(movimiento)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Movimiento guardado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarMovimientos();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error:", e);
                });
    }

    private void actualizarMovimiento() {
        String idTarjeta = tarjeta.getText().toString().trim();
        String f = fecha.getText().toString().trim();
        String entrada = estEntrada.getText().toString().trim();
        String salida = estSalida.getText().toString().trim();
        String tiempo = tiempoViaje.getText().toString().trim();

        if (TextUtils.isEmpty(idTarjeta) || TextUtils.isEmpty(f) || TextUtils.isEmpty(entrada) || TextUtils.isEmpty(salida) || TextUtils.isEmpty(tiempo)) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Linea1 actualizado = new Linea1(idTarjeta, f, entrada, salida, tiempo);
        actualizado.setId(movedit.getId());

        db.collection("movimientos-linea1")
                .document(actualizado.getId())
                .set(actualizado)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Movimiento actualizado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarMovimientos();
                    movedit = null;
                    btnGuardar.setText("Guardar");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Update error", e);
                });
    }

    private void cargarMovimientos() {
        db.collection("movimientos-linea1")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    listaMovimientos.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Linea1 m = doc.toObject(Linea1.class);
                        m.setId(doc.getId());
                        listaMovimientos.add(m);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al leer", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Lectura fallida", e);
                });
    }

    private void limpiarCampos() {
        tarjeta.setText("");
        fecha.setText("");
        estEntrada.setText("");
        estSalida.setText("");
        tiempoViaje.setText("");
    }
}

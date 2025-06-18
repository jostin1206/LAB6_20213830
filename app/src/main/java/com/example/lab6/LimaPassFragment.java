package com.example.lab6;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class LimaPassFragment extends Fragment {

    private EditText tarjeta, fecha, entrada, salida;
    private Button btnGuardar;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;

    private List<LimaPass> listaMovimientos;
    private LimaPassAdapter adapter;

    private LimaPass movimientoEditando = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_limapass, container, false);

        db = FirebaseFirestore.getInstance();

        tarjeta = view.findViewById(R.id.etIdTarjetaLima);
        fecha = view.findViewById(R.id.etFechaLima);
        entrada = view.findViewById(R.id.etParaderoEntrada);
        salida = view.findViewById(R.id.etParaderoSalida);
        btnGuardar = view.findViewById(R.id.btnGuardarLima);

        recyclerView = view.findViewById(R.id.recyclerLimaPass);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaMovimientos = new ArrayList<>();

        adapter = new LimaPassAdapter(listaMovimientos, mov -> {
            movimientoEditando = mov;
            tarjeta.setText(mov.getIdTarjeta());
            fecha.setText(mov.getFecha());
            entrada.setText(mov.getParaderoEntrada());
            salida.setText(mov.getParaderoSalida());
            btnGuardar.setText("Actualizar");
        });

        recyclerView.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> {
            if (movimientoEditando != null) {
                actualizarMovimiento();
            } else {
                guardarMovimiento();
            }
        });

        cargarMovimientos();

        return view;
    }

    private void guardarMovimiento() {
        String id = tarjeta.getText().toString().trim();
        String f = fecha.getText().toString().trim();
        String e = entrada.getText().toString().trim();
        String s = salida.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(f) || TextUtils.isEmpty(e) || TextUtils.isEmpty(s)) {
            Toast.makeText(getContext(), "Completa todos los campos, por favor", Toast.LENGTH_SHORT).show();
            return;
        }

        LimaPass movimiento = new LimaPass(id, f, e, s);

        db.collection("movimientos-limapass")
                .add(movimiento)
                .addOnSuccessListener(docRef -> {
                    //Toast.makeText(getContext(), "Movimiento guardado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarMovimientos();
                })
                .addOnFailureListener(e1 -> {
                    Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error:", e1);
                });
    }

    private void actualizarMovimiento() {
        String id = tarjeta.getText().toString().trim();
        String f = fecha.getText().toString().trim();
        String e = entrada.getText().toString().trim();
        String s = salida.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(f) || TextUtils.isEmpty(e) || TextUtils.isEmpty(s)) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        LimaPass actualizado = new LimaPass(id, f, e, s);
        actualizado.setId(movimientoEditando.getId());

        db.collection("movimientos-limapass")
                .document(actualizado.getId())
                .set(actualizado)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Movimiento actualizado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarMovimientos();
                    movimientoEditando = null;
                    btnGuardar.setText("Guardar");
                })
                .addOnFailureListener(e1 -> {
                    Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error al actualizar", e1);
                });
    }

    private void cargarMovimientos() {
        db.collection("movimientos-limapass")
                .get()
                .addOnSuccessListener(snapshot -> {
                    listaMovimientos.clear();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        LimaPass mov = doc.toObject(LimaPass.class);
                        mov.setId(doc.getId());
                        listaMovimientos.add(mov);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(listaMovimientos.size() - 1);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al leer", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Lectura fallida", e);
                });
    }

    private void limpiarCampos() {
        tarjeta.setText("");
        fecha.setText("");
        entrada.setText("");
        salida.setText("");
    }
}

package com.example.lab6;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Linea1Adapter extends RecyclerView.Adapter<Linea1Adapter.Linea1ViewHolder> {

    private List<Linea1> listaMovimientos;
    private OnEditarClickListener listener;

    public interface OnEditarClickListener {
        void onEditarClick(Linea1 movimiento);
    }

    public Linea1Adapter(List<Linea1> listaMovimientos, OnEditarClickListener listener) {
        this.listaMovimientos = listaMovimientos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Linea1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_linea1, parent, false);
        return new Linea1ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Linea1ViewHolder h, int position) {
        Linea1 movimiento = listaMovimientos.get(position);

        h.tvIdTarjeta.setText("ID: " + movimiento.getIdTarjeta());
        h.tvEntrada.setText("Entrada: " + movimiento.getEstacionEntrada());
        h.tvSalida.setText("Salida: " + movimiento.getEstacionSalida());
        h.tvFecha.setText("Fecha: " + movimiento.getFecha());
        h.tvTiempo.setText("Tiempo de viaje: " + movimiento.getTiempoViaje() + " min");

        h.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar")
                    .setMessage("¿Seguro que deseas eliminar este movimiento?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        FirebaseFirestore.getInstance()
                                .collection("movimientos-linea1")
                                .document(movimiento.getId())
                                .delete()
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(v.getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                                    listaMovimientos.remove(h.getAdapterPosition());
                                    notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(v.getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        h.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(movimiento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMovimientos.size();
    }

    public static class Linea1ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdTarjeta, tvEntrada, tvSalida, tvFecha, tvTiempo;
        Button btnEliminar, btnEditar;

        public Linea1ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdTarjeta = itemView.findViewById(R.id.tvIdTarjeta);
            tvEntrada = itemView.findViewById(R.id.tvEntrada);
            tvSalida = itemView.findViewById(R.id.tvSalida);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvTiempo = itemView.findViewById(R.id.tvTiempo);
            btnEliminar = itemView.findViewById(R.id.btnEliminarLinea1);
            btnEditar = itemView.findViewById(R.id.btnEditarLinea1);
        }
    }
}

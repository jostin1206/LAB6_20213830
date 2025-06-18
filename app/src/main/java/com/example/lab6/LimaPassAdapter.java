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

public class LimaPassAdapter extends RecyclerView.Adapter<LimaPassAdapter.LimaPassViewHolder> {

    private List<LimaPass> lista;
    private OnEditarClickListener listener;

    public interface OnEditarClickListener {
        void onEditarClick(LimaPass movimiento);
    }

    public LimaPassAdapter(List<LimaPass> lista, OnEditarClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LimaPassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_limapass, parent, false);
        return new LimaPassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LimaPassViewHolder holder, int position) {
        LimaPass mov = lista.get(position);

        holder.tvIdTarjeta.setText("ID: " + mov.getIdTarjeta());
        holder.tvEntrada.setText("Entrada: " + mov.getParaderoEntrada());
        holder.tvSalida.setText("Salida: " + mov.getParaderoSalida());
        holder.tvFecha.setText("Fecha: " + mov.getFecha());

        holder.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar")
                    .setMessage("¿Deseas eliminar este movimiento?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        FirebaseFirestore.getInstance()
                                .collection("movimientos-limapass")
                                .document(mov.getId())
                                .delete()
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(v.getContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                                    lista.remove(holder.getAdapterPosition());
                                    notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(v.getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(mov);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class LimaPassViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdTarjeta, tvEntrada, tvSalida, tvFecha;
        Button btnEliminar, btnEditar;

        public LimaPassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdTarjeta = itemView.findViewById(R.id.tvIdTarjetaLima);
            tvEntrada = itemView.findViewById(R.id.tvEntradaLima);
            tvSalida = itemView.findViewById(R.id.tvSalidaLima);
            tvFecha = itemView.findViewById(R.id.tvFechaLima);
            btnEliminar = itemView.findViewById(R.id.btnEliminarLima);
            btnEditar = itemView.findViewById(R.id.btnEditarLima);
        }
    }
}

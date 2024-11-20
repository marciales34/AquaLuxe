package com.snt.aqualuxe.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snt.aqualuxe.R;

import java.util.List;

public class AutolavadoAdapter extends RecyclerView.Adapter<AutolavadoAdapter.AutolavadoViewHolder> {
    private List<Autolavado> autolavadoList;
    private Context context;
    private OnItemClickListener listener;

    // Interface para manejar clics
    public interface OnItemClickListener {
        void onItemClick(Autolavado autolavado);
    }

    public AutolavadoAdapter(List<Autolavado> autolavadoList, Context context, OnItemClickListener listener) {
        this.autolavadoList = autolavadoList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AutolavadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_autolavado, parent, false);
        return new AutolavadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AutolavadoViewHolder holder, int position) {
        Autolavado autolavado = autolavadoList.get(position);

        holder.textAutolavado.setText(autolavado.getNombre_autolavado());
        holder.textUbicacion.setText(autolavado.getDireccion());

        // Configurar clic en en tolo el item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(autolavado);
            }
        });

        holder.btnDetalles.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(autolavado);
            }
        });
    }

    @Override
    public int getItemCount() {
        return autolavadoList.size();
    }

    public class AutolavadoViewHolder extends RecyclerView.ViewHolder {
        TextView textAutolavado;
        TextView textUbicacion;
        Button btnDetalles;

        public AutolavadoViewHolder(@NonNull View itemView) {
            super(itemView);
            textAutolavado = itemView.findViewById(R.id.text_autolavado);
            textUbicacion = itemView.findViewById(R.id.text_ubicacion);
            btnDetalles = itemView.findViewById(R.id.btn_detalles);
        }
    }
}
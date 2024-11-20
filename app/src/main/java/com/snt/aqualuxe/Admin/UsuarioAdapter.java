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

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {
    private List<Usuario> usuarioList;
    private Context context;
    private OnItemClickListener listener;

    // Interface para manejar clics
    public interface OnItemClickListener {
        void onItemClick(Usuario usuario);
    }

    public UsuarioAdapter(List<Usuario> usuarioList, Context context, OnItemClickListener listener) {
        this.usuarioList = usuarioList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = usuarioList.get(position);

        holder.textNombre.setText(usuario.getNombre());
        holder.textTelefono.setText(usuario.getTelefono());


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(usuario);
            }
        });

        holder.btnDetalles.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(usuario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre;
        TextView textTelefono;
        Button btnDetalles;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.text_nombre);
            textTelefono = itemView.findViewById(R.id.text_telefono);
            btnDetalles = itemView.findViewById(R.id.btn_detalles);
            // Comprobaciones de null
            if (textNombre == null) {
                throw new NullPointerException("textNombre es null");
            }
            if (textTelefono == null) {
                throw new NullPointerException("textTelefono es null");
            }
            if (btnDetalles == null) {
                throw new NullPointerException("btnDetalles es null");
            }
        }
    }
}
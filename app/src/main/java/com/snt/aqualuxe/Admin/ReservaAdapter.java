package com.snt.aqualuxe.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snt.aqualuxe.R;
import com.snt.aqualuxe.Servicio;
import com.snt.aqualuxe.serviciosVehiculo.Vehiculo;

import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {
    private List<ReservaView> reservaList;
    private Context context;


    public ReservaAdapter(List<ReservaView> reservaList, Context context) {
        this.reservaList = reservaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        ReservaView reserva = reservaList.get(position);

        holder.textFecha.setText(reserva.getFecha());
        holder.textServicio.setText(reserva.getServicio());
        holder.textHora.setText(reserva.getHora());
        holder.textPlaca.setText(reserva.getVehiculo());
        holder.textAutolavado.setText(reserva.getAutolavado());

    }

    @Override
    public int getItemCount() {
        return reservaList.size();
    }

    public class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView textFecha;
        TextView textHora;
        TextView textPlaca;
        TextView textAutolavado;
        TextView textServicio;


        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            textFecha = itemView.findViewById(R.id.text_fecha);
            textHora = itemView.findViewById(R.id.text_hora);
            textPlaca = itemView.findViewById(R.id.text_placa);
            textAutolavado = itemView.findViewById(R.id.text_autolavado);
            textServicio = itemView.findViewById(R.id.text_servicio);

        }
    }
}
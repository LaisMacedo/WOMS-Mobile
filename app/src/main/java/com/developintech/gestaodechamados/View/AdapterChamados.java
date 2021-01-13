package com.developintech.gestaodechamados.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developintech.gestaodechamados.Controler.Tools;
import com.developintech.gestaodechamados.Model.Chamado;
import com.developintech.gestaodechamados.R;

import java.util.ArrayList;

public class AdapterChamados extends RecyclerView.Adapter<AdapterChamados.ChamadoViewHolder> {

    private Context context;
    private ArrayList<Chamado> listaDeChamados;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public AdapterChamados(Context context, ArrayList<Chamado> listaDeChamados) {
        this.context = context;
        this.listaDeChamados = listaDeChamados;
    }

    @Override
    public ChamadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_listachamados, null);
        ChamadoViewHolder holder = new ChamadoViewHolder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChamadoViewHolder holder, int position) {
        Chamado chamado = listaDeChamados.get(position);
        String distanciaFormatada;
        double distance;

        if (chamado.getDistancia()<1000) {      distance = chamado.getDistancia();           distanciaFormatada = String.format("%.0f", distance) + "m";}
        else if (chamado.getDistancia()>1000){  distance = chamado.getDistancia()/1000;      distanciaFormatada = String.format("%.1f", distance) + "Km";}
        else { distanciaFormatada = "ND"; }

        if (chamado.getStatus().equalsIgnoreCase("cancelado"))
            holder.textViewStatus.setTextColor(Color.rgb(188, 24, 24)); //vermelho
        if (chamado.getStatus().equalsIgnoreCase("em espera"))
            holder.textViewStatus.setTextColor(Color.rgb(255, 169, 33)); //laranja
        if (chamado.getStatus().equalsIgnoreCase("finalizado"))
            holder.textViewStatus.setTextColor(Color.rgb(79, 175, 65)); //verde
        if (chamado.getStatus().equalsIgnoreCase("em deslocamento"))
            holder.textViewStatus.setTextColor(Color.rgb(4, 158, 224)); //azul claro
        if ( chamado.getStatus().equalsIgnoreCase("em execucao"))
            holder.textViewStatus.setTextColor(Color.rgb(7, 64, 196)); //azul escuro

        holder.textViewServico.setText(chamado.getServ_nome());
        holder.textViewBairro.setText(chamado.getEnd_bairro());
        holder.textViewCidade.setText(chamado.getEnd_cidade());
        holder.textViewStatus.setText(chamado.getStatus());
        holder.textViewData.setText(chamado.getData_chamado());
        holder.textViewHora.setText(chamado.getHora_chamado());
        holder.textViewDistancia.setText(distanciaFormatada);
        holder.imageViewIcon.setImageDrawable(context.getResources().getDrawable(Tools.getIdIconResource(chamado)));//chamado.getIcon_mobile()));
    }

    @Override
    public int getItemCount() {
        return listaDeChamados.size();
    }

    class ChamadoViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewIcon;
        TextView textViewServico, textViewBairro, textViewCidade, textViewStatus, textViewData, textViewHora, textViewDistancia;

        public ChamadoViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewServico = itemView.findViewById(R.id.textViewNome);
            textViewBairro = itemView.findViewById(R.id.textViewCargo);
            textViewCidade = itemView.findViewById(R.id.textViewCidade);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewData = itemView.findViewById(R.id.textViewData);
            textViewHora = itemView.findViewById(R.id.textViewHora);
            textViewDistancia = itemView.findViewById(R.id.textViewDistancia);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }

    }
}
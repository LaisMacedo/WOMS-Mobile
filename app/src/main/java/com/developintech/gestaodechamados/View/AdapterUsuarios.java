package com.developintech.gestaodechamados.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.developintech.gestaodechamados.Model.Usuario;
import com.developintech.gestaodechamados.R;

import java.util.ArrayList;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.UsuarioViewHolder> {


    private Context context;
    private ArrayList<Usuario> listaDeUsuarios;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onLigarClick(String numero);
        void onMsgClick(String numero);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public AdapterUsuarios(Context context, ArrayList<Usuario> listaDeUsuarios) {
        this.context = context;
        this.listaDeUsuarios = listaDeUsuarios;
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_listausuarios, null);
        UsuarioViewHolder holder = new UsuarioViewHolder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = listaDeUsuarios.get(position);

        holder.textViewNome.setText(usuario.getUser_nome());

        holder.textViewCargo.setText(usuario.getUser_cargo());

        if (usuario.getUser_cargo().equalsIgnoreCase("administrador"))
            holder.textViewCargo.setTextColor(Color.rgb(160, 0, 0));
        if (usuario.getUser_cargo().equalsIgnoreCase("técnico"))
            holder.textViewCargo.setTextColor(Color.rgb(0, 89, 191));
        if (usuario.getUser_cargo().equalsIgnoreCase("atendente"))
            holder.textViewCargo.setTextColor(Color.rgb(209, 2, 122));

        holder.tlfUser = usuario.getUser_tlf();

        holder.msgUser = usuario.getUser_msg();
    }

    @Override
    public int getItemCount() {
        return listaDeUsuarios.size();
    }

    class UsuarioViewHolder extends RecyclerView.ViewHolder {
                String tlfUser, msgUser;
                TextView textViewNome, textViewCargo;
                ImageButton buttonLigar, buttonMsg;

        public UsuarioViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewCargo = itemView.findViewById(R.id.textViewCargo);
            buttonLigar = itemView.findViewById(R.id.buttonLigar);
            buttonMsg = itemView.findViewById(R.id.buttonMsg);

            buttonLigar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onLigarClick(tlfUser);
                        }
                    }
                }
            });

            buttonMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onMsgClick(msgUser);
                        }
                    }
                }
            });

        }

    }
}
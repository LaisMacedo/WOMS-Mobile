package com.developintech.gestaodechamados.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Usuario {

    @SerializedName("usu_id")
    public int user_id;

    @SerializedName("usu_nome")
    public String user_nome;

    @SerializedName("cargo_nome")
    public String user_cargo;

    @SerializedName("usu_celular")
    public String user_tlf;

    @SerializedName("usu_whatsapp")
    public String user_msg;

    public static Comparator<Usuario> comparadorNome = new Comparator<Usuario>() {
        @Override
        public int compare(Usuario usr1, Usuario usr2) {
            return usr1.getUser_nome().compareToIgnoreCase(usr2.getUser_nome());
        }
    };

    public static Comparator<Usuario> comparadorCargo = new Comparator<Usuario>() {
        @Override
        public int compare(Usuario usr1, Usuario usr2) {
            return usr1.getUser_cargo().compareToIgnoreCase(usr2.getUser_cargo());
        }
    };

    //CONSTRUTOR SOMENTE COM CHAMADO ID
    public Usuario(int user_id) {
        setUser_id(user_id);
    }

    //CONSTRUTOR COM TODOS OS CAMPOS MENOS MSG
    public Usuario(int user_id, String user_nome, String user_cargo, String user_tlf) {
        setUser_id(user_id);
        setUser_nome(user_nome);
        setUser_cargo(user_cargo);
        setUser_tlf(user_tlf);
    }

    //CONSTRUTOR COM TODOS OS CAMPOS
    public Usuario(int user_id, String user_nome, String user_cargo, String user_tlf, String user_msg) {
        setUser_id(user_id);
        setUser_nome(user_nome);
        setUser_cargo(user_cargo);
        setUser_tlf(user_tlf);
        setUser_msg(user_msg);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_nome() {
        return user_nome;
    }

    public void setUser_nome(String user_nome) {
        this.user_nome = user_nome;
    }

    public String getUser_cargo() {
        return user_cargo;
    }

    public void setUser_cargo(String user_cargo) {
        this.user_cargo = user_cargo;
    }

    public String getUser_tlf() {
        return user_tlf;
    }

    public void setUser_tlf(String user_tlf) {
        this.user_tlf = user_tlf;
    }

    public String getUser_msg() {
        return user_msg;
    }

    public void setUser_msg(String user_msg) {
        this.user_msg = user_msg;
    }
}
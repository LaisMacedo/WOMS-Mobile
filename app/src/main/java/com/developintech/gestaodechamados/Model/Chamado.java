package com.developintech.gestaodechamados.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Chamado {
    @SerializedName("cham_id")
    public int chamado_id;

    @SerializedName("est_nome")
    public String status;

    @SerializedName("serv_nome")
    public String serv_nome;

    @SerializedName("serv_descricao")
    public String serv_descricao;

    @SerializedName("cli_nome")
    public String cli_nome;

    @SerializedName("cli_celular")
    public String cli_celular;

    @SerializedName("cli_telefone")
    public String cli_telefone;

    @SerializedName("cli_email")
    public String cli_email;

    @SerializedName("end_logradouro")
    public String end_logradouro;

    @SerializedName("end_numero")
    public String end_numero;

    @SerializedName("end_complemento")
    public String end_complemento;

    @SerializedName("end_bairro")
    public String end_bairro;

    @SerializedName("end_cidade")
    public String end_cidade;

    @SerializedName("end_estado")
    public String end_estado;

    @SerializedName("end_latitude")
    public double end_latitude;

    @SerializedName("end_longitude")
    public double end_longitude;

    @SerializedName("data_chamado")
    public String data_chamado;

    @SerializedName("hora_chamado")
    public String hora_chamado;

    @SerializedName("icon_mobile")
    public String icon_mobile;

    public double distancia;


    public static Comparator<Chamado> comparador = new Comparator<Chamado>() {
        @Override
        public int compare(Chamado c1, Chamado c2) {
            return c1.getCli_nome().compareToIgnoreCase(c2.getCli_nome());
        }
    };

    //CONSTRUTOR SOMENTE COM CHAMADO ID

    public Chamado(int chamado_id){
        setChamado_id(chamado_id);
    }

    //CONSTRUTOR COM TODOS OS CAMPOS EXETO DISTANCIA (NAO VEM DO BANCO)

    public Chamado(int chamado_id, String status, String serv_nome, String serv_descricao, String cli_nome, String cli_celular,
                   String cli_telefone, String cli_email, String end_logradouro, String end_numero, String end_complemento, String end_bairro,
                   String end_cidade, String end_estado, double end_latitude, double end_longitude, String data_chamado, String hora_chamado, String icon_mobile){

        setChamado_id(chamado_id);
        setStatus(status);
        setServ_nome(serv_nome);
        setServ_descricao(serv_descricao);
        setCli_nome(cli_nome);
        setCli_celular(cli_celular);
        setCli_telefone(cli_telefone);
        setCli_email(cli_email);
        setEnd_logradouro(end_logradouro);
        setEnd_numero(end_numero);
        setEnd_complemento(end_complemento);
        setEnd_bairro(end_bairro);
        setEnd_cidade(end_cidade);
        setEnd_estado(end_estado);
        setEnd_latitude(end_latitude);
        setEnd_longitude(end_longitude);
        setData_chamado(data_chamado);
        setHora_chamado(hora_chamado);
        setIcon_mobile(icon_mobile);
    }


    //GETERS AND SETTERS

    public int getChamado_id() {
        return chamado_id;
    }

    public void setChamado_id(int chamado_id) {
        this.chamado_id = chamado_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServ_nome() {
        return serv_nome;
    }

    public void setServ_nome(String serv_nome) {
        this.serv_nome = serv_nome;
    }

    public String getServ_descricao() {
        return serv_descricao;
    }

    public void setServ_descricao(String serv_descricao) {
        this.serv_descricao = serv_descricao;
    }

    public String getCli_nome() {
        return cli_nome;
    }

    public void setCli_nome(String cli_nome) {
        this.cli_nome = cli_nome;
    }

    public String getCli_celular() {
        return cli_celular;
    }

    public void setCli_celular(String cli_celular) {
        this.cli_celular = cli_celular;
    }

    public String getCli_telefone() {
        return cli_telefone;
    }

    public void setCli_telefone(String cli_telefone) {
        this.cli_telefone = cli_telefone;
    }

    public String getCli_email() {
        return cli_email;
    }

    public void setCli_email(String cli_email) {
        this.cli_email = cli_email;
    }

    public String getEnd_logradouro() {
        return end_logradouro;
    }

    public void setEnd_logradouro(String end_logradouro) {
        this.end_logradouro = end_logradouro;
    }

    public String getEnd_numero() {
        return end_numero;
    }

    public void setEnd_numero(String end_numero) {
        this.end_numero = end_numero;
    }

    public String getEnd_complemento() {
        return end_complemento;
    }

    public void setEnd_complemento(String end_complemento) {
        this.end_complemento = end_complemento;
    }

    public String getEnd_bairro() {
        return end_bairro;
    }

    public void setEnd_bairro(String end_bairro) {
        this.end_bairro = end_bairro;
    }

    public String getEnd_cidade() {
        return end_cidade;
    }

    public void setEnd_cidade(String end_cidade) {
        this.end_cidade = end_cidade;
    }

    public String getEnd_estado() {
        return end_estado;
    }

    public void setEnd_estado(String end_estado) {
        this.end_estado = end_estado;
    }

    public double getEnd_latitude() {
        return end_latitude;
    }

    public void setEnd_latitude(double end_latitude) {
        this.end_latitude = end_latitude;
    }

    public double getEnd_longitude() {
        return end_longitude;
    }

    public void setEnd_longitude(double end_longitude) {
        this.end_longitude = end_longitude;
    }

    public String getData_chamado() {
        return data_chamado;
    }

    public void setData_chamado(String data_chamado) {
        this.data_chamado = data_chamado;
    }

    public String getHora_chamado() {
        return hora_chamado;
    }

    public void setHora_chamado(String hora_chamado) {
        this.hora_chamado = hora_chamado;
    }

    public String getIcon_mobile() {
        return icon_mobile;
    }

    public void setIcon_mobile(String icon_mobile) {
        this.icon_mobile = icon_mobile;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}

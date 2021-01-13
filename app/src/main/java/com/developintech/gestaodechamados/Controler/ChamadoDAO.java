package com.developintech.gestaodechamados.Controler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.developintech.gestaodechamados.Model.Chamado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChamadoDAO {

        public static final String TABELA = "CHAMADOS";

        public static final String CHAMID    = "CHAMID";
        public static final String STATUS    = "STATUS";
        public static final String SERVNOME  = "SERVNOME";
        public static final String SERVDESC  = "SERVDESC";
        public static final String CLINOME   = "CLINOME";
        public static final String CLICEL    = "CLICEL";
        public static final String CLITEL    = "CLITEL";
        public static final String CLIMAIL   = "CLIMAIL";
        public static final String ENDLOG    = "ENDLOG";
        public static final String ENDNUM    = "ENDNUM";
        public static final String ENDCOMP   = "ENDCOMP";
        public static final String ENDBAIRRO = "ENDBAIRRO";
        public static final String ENDCIDADE = "ENDCIDADE";
        public static final String ENDESTADO = "ENDESTADO";
        public static final String LATITUDE  = "LATITUDE";
        public static final String LONGITUDE = "LONGITUDE";
        public static final String DATA      = "DATA";
        public static final String HORA      = "HORA";
        public static final String ICON      = "ICON";
        public static final String DISTANCIA = "DISTANCIA";


    private ConexaoDatabase conexao;

        public List<Chamado> lista;

    public ChamadoDAO(ConexaoDatabase conexaoDatabase){
            conexao = conexaoDatabase;
        lista   = new ArrayList<Chamado>();
    }

    public void criarTabela(SQLiteDatabase database){

        String sql = "CREATE TABLE " + TABELA + "(" +
                CHAMID   + " INTEGER PRIMARY KEY NOT NULL, " +
                STATUS   + " TEXT NOT NULL, " +
                SERVNOME + " TEXT NOT NULL, " +
                SERVDESC + " TEXT, " +
                CLINOME + " TEXT NOT NULL, " +
                CLICEL + " TEXT, " +
                CLITEL + " TEXT, " +
                CLIMAIL + " TEXT, " +
                ENDLOG + " TEXT NOT NULL, " +
                ENDNUM + " TEXT, " +
                ENDCOMP + " TEXT, " +
                ENDBAIRRO + " TEXT, " +
                ENDCIDADE + " TEXT, " +
                ENDESTADO + " TEXT, " +
                LATITUDE + " REAL, " +
                LONGITUDE + " REAL, " +
                DATA + " TEXT, " +
                HORA + " TEXT, " +
                ICON + " TEXT, " +
                DISTANCIA + " REAL)";

        database.execSQL(sql);
    }

    public void apagarTabela(SQLiteDatabase database){
        String sql = "DROP TABLE IF EXISTS " + TABELA;
        database.execSQL(sql);
    }

    public void deletaDadosTabela(SQLiteDatabase database){
        String sql = "DELETE FROM " + TABELA;
        database.execSQL(sql);
    }

    public boolean inserir(Chamado chamado){

        ContentValues values = new ContentValues();

        values.put(CHAMID, chamado.getChamado_id());
        values.put(STATUS, chamado.getStatus());
        values.put(SERVNOME, chamado.getServ_nome());
        values.put(SERVDESC, chamado.getServ_descricao());
        values.put(CLINOME, chamado.getCli_nome());
        values.put(CLICEL, chamado.getCli_celular());
        values.put(CLITEL, chamado.getCli_telefone());
        values.put(CLIMAIL, chamado.getCli_email());
        values.put(ENDLOG, chamado.getEnd_logradouro());
        values.put(ENDNUM, chamado.getEnd_numero());
        values.put(ENDCOMP, chamado.getEnd_complemento());
        values.put(ENDBAIRRO, chamado.getEnd_bairro());
        values.put(ENDCIDADE, chamado.getEnd_cidade());
        values.put(ENDESTADO, chamado.getEnd_estado());
        values.put(LATITUDE, chamado.getEnd_latitude());
        values.put(LONGITUDE, chamado.getEnd_longitude());
        values.put(DATA, chamado.getData_chamado());
        values.put(HORA, chamado.getHora_chamado());
        values.put(ICON, chamado.getIcon_mobile());

        conexao.getWritableDatabase().insert(TABELA,null, values);

        lista.add(chamado);

        ordenarLista();

        return true;
    }

    /**public boolean alterar(Pessoa pessoa){
        ContentValues values = new ContentValues();
        values.put(NOME, pessoa.getNome());
        values.put(IDADE, pessoa.getIdade());
        String[] args = {String.valueOf(pessoa.getId())};
        conexao.getWritableDatabase().update(TABELA, values, ID + " = ?", args);
        ordenarLista();
        return true;
    }*/

    public boolean apagar(Chamado chamado){

        String[] args = {String.valueOf(chamado.getChamado_id())};

        conexao.getWritableDatabase().delete(TABELA,CHAMID + " = ?", args);

        lista.remove(chamado);

        return true;
    }

    public void carregarTudoParametro(int parametro){

        String sql = "SELECT * FROM " + TABELA;

        if      (parametro == 1){ sql += " ORDER BY " + CHAMID;   }                     // ORDER BY CHAMADO ID
        else if (parametro == 2){ sql += " ORDER BY " + CLINOME;  }                     // ORDER BY NOME CLIENTE
        else if (parametro == 3){ sql += " ORDER BY " + STATUS;   }                     // ORDER BY STATUS
        else if (parametro == 4){ sql += " ORDER BY " + SERVNOME; }                     // ORDER BY SERVIÇO
        else if (parametro == 5){ sql += " ORDER BY " + DISTANCIA;}                     // ORDER BY DISTANCIA
        else if (parametro == 6){ sql += " GROUP BY " + DATA + " ORDER BY " + HORA; }   // ORDER BY DATA HORA

        lista.clear();

        Cursor cursor = conexao.getReadableDatabase().rawQuery(sql, null);

        int columChamId = cursor.getColumnIndex(CHAMID);
        int columStatus = cursor.getColumnIndex(STATUS);
        int columServNome = cursor.getColumnIndex(SERVNOME);
        int columServDesc = cursor.getColumnIndex(SERVDESC);
        int columCliNome = cursor.getColumnIndex(CLINOME);
        int columCliCel = cursor.getColumnIndex(CLICEL);
        int columCliTel = cursor.getColumnIndex(CLITEL);
        int columCliMail = cursor.getColumnIndex(CLIMAIL);
        int columEndLog = cursor.getColumnIndex(ENDLOG);
        int columEndNum = cursor.getColumnIndex(ENDNUM);
        int columEndComp = cursor.getColumnIndex(ENDCOMP);
        int columEndBairro = cursor.getColumnIndex(ENDBAIRRO);
        int columEndCidade = cursor.getColumnIndex(ENDCIDADE);
        int columEndEstado = cursor.getColumnIndex(ENDESTADO);
        int columLatitude = cursor.getColumnIndex(LATITUDE);
        int columLongitude = cursor.getColumnIndex(LONGITUDE);
        int columData = cursor.getColumnIndex(DATA);
        int columHora = cursor.getColumnIndex(HORA);
        int columIcon = cursor.getColumnIndex(ICON);
        int columDist = cursor.getColumnIndex(DISTANCIA);

        while(cursor.moveToNext()){
            Chamado chamado = new Chamado(cursor.getInt(columChamId));

            chamado.setStatus(cursor.getString(columStatus));
            chamado.setServ_nome(cursor.getString(columServNome));
            chamado.setServ_descricao(cursor.getString(columServDesc));
            chamado.setCli_nome(cursor.getString(columCliNome));
            chamado.setCli_celular(cursor.getString(columCliCel));
            chamado.setCli_telefone(cursor.getString(columCliTel));
            chamado.setCli_email(cursor.getString(columCliMail));
            chamado.setEnd_logradouro(cursor.getString(columEndLog));
            chamado.setEnd_numero(cursor.getString(columEndNum));
            chamado.setEnd_complemento(cursor.getString(columEndComp));
            chamado.setEnd_bairro(cursor.getString(columEndBairro));
            chamado.setEnd_cidade(cursor.getString(columEndCidade));
            chamado.setEnd_estado(cursor.getString(columEndEstado));
            chamado.setEnd_latitude(cursor.getDouble(columLatitude));
            chamado.setEnd_longitude(cursor.getDouble(columLongitude));
            chamado.setData_chamado(cursor.getString(columData));
            chamado.setHora_chamado(cursor.getString(columHora));
            chamado.setIcon_mobile(cursor.getString(columIcon));
            chamado.setDistancia(0);
            lista.add(chamado);
        }
        cursor.close();
    }

    public Chamado chamdoPorId(int id){
        for (Chamado c: lista){
            if (c.getChamado_id() == id){
                return c;
            }
        }
        return null;
    }

    public void ordenarLista(){
        Collections.sort(lista, Chamado.comparador);
    }

    public void alteraStatus(int chamadoId, String statusStr) {

        String sql = "UPDATE " + TABELA +" SET " + STATUS + " = '" + statusStr + "' WHERE " + CHAMID + " = " + chamadoId + "";

        conexao.getWritableDatabase().execSQL(sql);

    }
}
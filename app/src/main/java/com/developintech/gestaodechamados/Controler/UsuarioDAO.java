package com.developintech.gestaodechamados.Controler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.developintech.gestaodechamados.Model.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioDAO {

    public static final String TABELA = "USUARIOS";

    public static final String USERID    = "USERID";
    public static final String USERNOME    = "USERNOME";
    public static final String USERCARGO  = "USERCARGO";
    public static final String USERTLF  = "USERTLF";
    public static final String USERMSG   = "USERMSG";

    private ConexaoDatabase conexao;

    public List<Usuario> lista;

    public UsuarioDAO(ConexaoDatabase conexaoDatabase){
        conexao = conexaoDatabase;
        lista   = new ArrayList<Usuario>();
    }

    public void criarTabela(SQLiteDatabase database){
        String sql_1 = "DROP TABLE IF EXISTS " + TABELA;

        String sql_2 = "CREATE TABLE " + TABELA + "(" +
                USERID   + " INTEGER PRIMARY KEY NOT NULL, " +
                USERNOME   + " TEXT NOT NULL, " +
                USERCARGO + " TEXT NOT NULL, " +
                USERTLF + " TEXT NOT NULL, " +
                USERMSG + " TEXT)";

        database.execSQL(sql_1);

        database.execSQL(sql_2);
    }

    public void apagarTabela(SQLiteDatabase database){
        String sql = "DROP TABLE IF EXISTS " + TABELA;
        database.execSQL(sql);
    }

    public void deletaDadosTabela(SQLiteDatabase database){
        String sql = "DELETE FROM " + TABELA ;
        database.execSQL(sql);
    }

    public boolean inserir(Usuario usuario){

        ContentValues values = new ContentValues();

        values.put(USERID, usuario.getUser_id());
        values.put(USERNOME, usuario.getUser_nome());
        values.put(USERCARGO, usuario.getUser_cargo());
        values.put(USERTLF, usuario.getUser_tlf());
        values.put(USERMSG, usuario.getUser_msg());

        conexao.getWritableDatabase().insert(TABELA,null, values);

        lista.add(usuario);

        ordenarListaCargo();

        return true;
    }

    public boolean apagar(Usuario usuario){

        String[] args = {String.valueOf(usuario.getUser_id())};

        conexao.getWritableDatabase().delete(TABELA,USERID + " = ?", args);

        lista.remove(usuario);

        return true;
    }

    public void carregarTudoCargo(){

        String sql = "SELECT * FROM " + TABELA + " ORDER BY " + USERCARGO;

        lista.clear();

        Cursor cursor = conexao.getReadableDatabase().rawQuery(sql, null);

        int columUserId = cursor.getColumnIndex(USERID);
        int columUserNome = cursor.getColumnIndex(USERNOME);
        int columUserCargo = cursor.getColumnIndex(USERCARGO);
        int columUserTlf = cursor.getColumnIndex(USERTLF);
        int columUserMsg = cursor.getColumnIndex(USERMSG);


        while(cursor.moveToNext()){
            Usuario usuario = new Usuario(cursor.getInt(columUserId));
            usuario.setUser_nome(cursor.getString(columUserNome));
            usuario.setUser_cargo(cursor.getString(columUserCargo));
            usuario.setUser_tlf(cursor.getString(columUserTlf));
            usuario.setUser_msg(cursor.getString(columUserMsg));
            lista.add(usuario);
        }
        cursor.close();
    }

    public void carregarTudoNome(){

        String sql = "SELECT * FROM " + TABELA + " ORDER BY " + USERNOME;

        lista.clear();

        Cursor cursor = conexao.getReadableDatabase().rawQuery(sql, null);

        int columUserId = cursor.getColumnIndex(USERID);
        int columUserNome = cursor.getColumnIndex(USERNOME);
        int columUserCargo = cursor.getColumnIndex(USERCARGO);
        int columUserTlf = cursor.getColumnIndex(USERTLF);
        int columUserMsg = cursor.getColumnIndex(USERMSG);


        while(cursor.moveToNext()){
            Usuario usuario = new Usuario(cursor.getInt(columUserId));
            usuario.setUser_nome(cursor.getString(columUserNome));
            usuario.setUser_cargo(cursor.getString(columUserCargo));
            usuario.setUser_tlf(cursor.getString(columUserTlf));
            usuario.setUser_msg(cursor.getString(columUserMsg));
            lista.add(usuario);
        }
        cursor.close();
    }

    public Usuario usuarioPorId(int id){
        for (Usuario usr: lista){
            if (usr.getUser_id() == id){
                return usr;
            }
        }
        return null;
    }

    public void ordenarListaNome(){
        Collections.sort(lista, Usuario.comparadorNome);
    }

    public void ordenarListaCargo(){
        Collections.sort(lista, Usuario.comparadorCargo);
    }
}

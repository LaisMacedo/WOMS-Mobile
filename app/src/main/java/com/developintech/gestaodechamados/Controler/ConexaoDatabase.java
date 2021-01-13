package com.developintech.gestaodechamados.Controler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexaoDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME    = "gestaoChamados.db";
    private static final int    DB_VERSION = 1;

    private static ConexaoDatabase instance;

    private Context context;
    public ChamadoDAO chamadoDAO;
    public UsuarioDAO usuarioDAO;

    public static ConexaoDatabase getInstance(Context contexto){
        if (instance == null){
            instance = new ConexaoDatabase(contexto);
        }
        return instance;
    }

    private ConexaoDatabase(Context contexto){
        super(contexto, DB_NAME, null, DB_VERSION);
        context = contexto;
        chamadoDAO = new ChamadoDAO(this);
        usuarioDAO = new UsuarioDAO(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        chamadoDAO.criarTabela(db);
        usuarioDAO.criarTabela(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        chamadoDAO.apagarTabela(db);
        usuarioDAO.apagarTabela(db);
        onCreate(db);
    }
}

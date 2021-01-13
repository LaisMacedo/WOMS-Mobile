package com.developintech.gestaodechamados.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.developintech.gestaodechamados.Controler.ConexaoDatabase;
import com.developintech.gestaodechamados.Controler.Tools;
import com.developintech.gestaodechamados.Model.Usuario;
import com.developintech.gestaodechamados.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;

import static com.developintech.gestaodechamados.Controler.Tools.BASE_URL;

public class ListaUsuariosActivity extends AppCompatActivity implements AsyncResponse {


    public static final String ID = "ID";

    ArrayList<Usuario> listaUsuarios;
    AdapterUsuarios adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout cLayout;
    public Boolean porNome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_usuarios);

        listaUsuarios = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        cLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutUsuario);

        // adiciona a ação de atualizar a lista com o gesto SwipeDown
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                atualizarSeguro();
            }
        });

        //atualiza o view de usuarios de acordo com a conectividade
        atualizarSeguro();
    }

    //verifica a conectividade antes de decidadir atualizar pela internet ou pelo banco local
    public void atualizarSeguro() {
        if (Tools.conectado(this)) {
            atualizarDBLocal(); //via internet
        } else {
            popularLista(); //via local db
        }
    }

    //carrega os dados do banco na view de usuarios
    private void popularLista(){
        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        porNome = pref.getBoolean("porNome",false);

        //Carrega listview com informações do Banco Local
        ConexaoDatabase conexao = ConexaoDatabase.getInstance(this);
        if(porNome){
            conexao.usuarioDAO.carregarTudoNome();
        }else {
            conexao.usuarioDAO.carregarTudoCargo();
        }
        if(listaUsuarios!=null){ listaUsuarios.clear(); }
        for(Usuario usr: conexao.usuarioDAO.lista) {
            listaUsuarios.add(usr);
        }

        adapter = new AdapterUsuarios(this, listaUsuarios);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterUsuarios.OnItemClickListener() {
            @Override
            public void onLigarClick(String numero) {
                if (numero.isEmpty()){
                    Tools.snackbarShort(cLayout, "Numero não cadastrado");
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + numero));
                    startActivity(intent);
                }
            }

            @Override
            public void onMsgClick(String numero) {
                if (numero.isEmpty()){
                    Tools.snackbarShort(cLayout, "Numero não cadastrado");
                } else {
                    Uri uri = Uri.parse("smsto:" + numero);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(intent);
                }

                /* TODO - Codigo para alterar para WhatsApp Mensager
                PackageManager pm=getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = "YOUR TEXT HERE";

                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Tools.snackbarShort(cLayout, "WhatsApp Não Instalado");
                }*/
            }
        });
        onItemsLoadComplete();
    }

    //aualiza fazendo um httpRequest (internet)
    private void atualizarDBLocal(){

        //busca das preferencias o email do usuario gravado localmente para realizar a consulta via web
        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        String storedEmail = pref.getString("email", null);
        porNome = pref.getBoolean("porNome",false);

        //cria a requisição http que recebe como resposta o json de usuarios exceto o proprio
        PostResponseAsyncTask loadingUsuarios = new PostResponseAsyncTask(this);
        loadingUsuarios.execute(BASE_URL+"/getusuarios.php?format=json&me=" + storedEmail);
    };

    @Override //continuação da função atualizarDBLocal
    public void processFinish (String result){
        //Lida com o resultado da requisição o arquivo converte os json inputs em um ArrayList<Usuarios>

        //se a lista nao estiver vazia a esvazia
        if(listaUsuarios!=null){ listaUsuarios.clear(); }

        //converte cada item do json em um objeto Usuario e por sua vez cada usuarios numa posição de um arraylist listaUsuarios
        listaUsuarios = new JsonConverter<Usuario>().toArrayList(result,Usuario.class);

        //cria conexao com o banco
        ConexaoDatabase conexao = ConexaoDatabase.getInstance(this);

        //apaga o conteudo da tabela anterior
        //conexao.usuarioDAO.deletaDadosTabela(conexao.getWritableDatabase());
        conexao.usuarioDAO.apagarTabela(conexao.getWritableDatabase());
        conexao.usuarioDAO.criarTabela(conexao.getWritableDatabase());

        for(Usuario usr: listaUsuarios){
            conexao.usuarioDAO.inserir(usr); //insere no banco cada usuarios
        }
        //quando termina popula a lista novamente a partir do banco
        popularLista();
    }

    //abre um chamado especificado pelo id do objeto
    public void abreUsuario(Usuario usr){
        Intent intent = new Intent(this, ListaChamadosActivity.class); /* TODO mudar a activity de destino */
        //intent.putExtra(ID, usr.getUser_id());
        startActivity(intent);
    }

    //o que fazer quando terminar de carregar os dados da view
    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
        Tools.snackbarShort(cLayout, "Dados Atualizados");
    }

    /* MENU */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usuarios_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemPorNome, menuItemPorCargo;
        menuItemPorNome = menu.findItem(R.id.menuItemPorNome);
        menuItemPorCargo = menu.findItem(R.id.menuItemPorCargo);

        if (porNome){
            menuItemPorNome.setVisible(false);
            menuItemPorCargo.setVisible(true);
        } else {
            menuItemPorNome.setVisible(true);
            menuItemPorCargo.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.menuItemVoltar:
            {
                abreChamados();
                return true;
            }
            case R.id.menuItemSobre:
            {
                abreSobre();
                return true;
            }
            case R.id.menuItemSair:
            {
                logout();
                return true;
            }
            case R.id.menuItemPorNome:
            {
                reordenaPorNome();
                return true;
            }
            case R.id.menuItemPorCargo:
            {
                reordenaPorCargo();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abreChamados() {
        Intent intent = new Intent(this, ListaChamadosActivity.class);
        startActivity(intent);
    }

    private void reordenaPorNome() {
        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("porNome", true);
        porNome = true;
        editor.commit();
        invalidateOptionsMenu();
        popularLista();
    }

    private void reordenaPorCargo() {
        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("porNome", false);
        porNome = false;
        editor.commit();
        invalidateOptionsMenu();
        popularLista();
    }

    private void abreSobre() {
        /* TODO > CRIAR PAGINA SOBRE E MUDAR O TARGET DO INTENT */
        Intent intent = new Intent(this, ListaUsuariosActivity.class);
        startActivity(intent);
    }

    private void logout() {
        //cria conexao com o banco
        ConexaoDatabase conexao = ConexaoDatabase.getInstance(this);

        //apaga o conteudo das tabelas Chamados e Usuarios
        conexao.chamadoDAO.apagarTabela(conexao.getWritableDatabase());
        //conexao.usuarioDAO.apagarTabela(conexao.getWritableDatabase());

        //Apaga as preferencias usadas para o login
        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        //vai para a tela de login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
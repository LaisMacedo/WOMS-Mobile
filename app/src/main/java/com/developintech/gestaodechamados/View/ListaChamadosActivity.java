package com.developintech.gestaodechamados.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.developintech.gestaodechamados.Controler.ConexaoDatabase;
import com.developintech.gestaodechamados.Controler.Tools;
import com.developintech.gestaodechamados.Model.Chamado;
import com.developintech.gestaodechamados.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;

import static com.developintech.gestaodechamados.Controler.Tools.BASE_URL;

public class ListaChamadosActivity extends AppCompatActivity implements AsyncResponse {

    public static final String ID = "ID";
    private double myLat, myLng;

    ArrayList<Chamado> listaChamados;
    AdapterChamados adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private CoordinatorLayout cLayout;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override //Função de Inicialização da Class
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chamados);

        listaChamados = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        cLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //cria o Gerenciador de localização do aparelho
        locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    myLat = location.getLatitude();
                    myLng = location.getLongitude(); }
                @Override
                public void onProviderDisabled(String provider) { Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); startActivity(intent); }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override
                public void onProviderEnabled(String provider) { }
        };

        // Verifica as permições do usuario, se nao tiver solicita! se tiver procegue
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},10);
            return;
        } else { configureRequestLocation(); }

        // adiciona a ação de atualizar a lista com o gesto SwipeDown
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                atualizarSeguro();
            }
        });

        //atualiza o view de chamados de acordo com a conectividade
        atualizarSeguro();
    }

    //verifica a conectividade antes de decidadir atualizar pela internet ou pelo banco local
    public void atualizarSeguro() {
        if (Tools.conectado(this)) {
            atualizarDBLocal();
        } else {
            popularLista();
        }
    }

    //carrega os dados do banco na view de chamados
    private void popularLista(){
        //Carrega listview com informações do Banco Local
        ConexaoDatabase conexao = ConexaoDatabase.getInstance(this);
        conexao.chamadoDAO.carregarTudoParametro(1);
        if(listaChamados!=null){ listaChamados.clear(); }
        for(Chamado c: conexao.chamadoDAO.lista) {
            listaChamados.add(c);
        }
        for(Chamado c: conexao.chamadoDAO.lista) {
            c.setDistancia(Tools.calculeDistanceAtoB(myLat,myLng,c.getEnd_latitude(),c.getEnd_longitude()));
        }

        adapter = new AdapterChamados(this, listaChamados);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterChamados.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                abreChamado(listaChamados.get(position));
            }
        });
        onItemsLoadComplete();
    }

    //aualiza fazendo um httpRequest (internet)
    private void atualizarDBLocal(){

        //busca das preferencias o email do usuario gravado localmente para realizar a consulta via web
        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        String storedEmail = pref.getString("email", null);

        //cria a requisição http que recebe como resposta o json de chamados
        PostResponseAsyncTask loadingChamados = new PostResponseAsyncTask(this);
        loadingChamados.execute(BASE_URL+"/getchamados.php?format=json&tecemail=" + storedEmail);

    };

    @Override //continuação da função atualizarDBLocal
    public void processFinish (String result){
    //Lida com o resultado da requisição o arquivo converte os json inputs em um ArrayList<Chamados>

        //se a lista nao estiver vazia a esvazia
        if(listaChamados!=null){ listaChamados.clear(); }

        //converte cada item do json em um objeto Chamado e por sua vez cada chamado numa posição de um arraylist listaChamados
        listaChamados = new JsonConverter<Chamado>().toArrayList(result,Chamado.class);

        //cria conexao com o banco
        ConexaoDatabase conexao = ConexaoDatabase.getInstance(this);

        //apaga o conteudo da tabela anterior e
        //conexao.chamadoDAO.deletaDadosTabela(conexao.getWritableDatabase());
        conexao.chamadoDAO.apagarTabela(conexao.getWritableDatabase());
        conexao.chamadoDAO.criarTabela(conexao.getWritableDatabase());

        /* TODO para cada chamado envia requisição para a API do google buscando a distancia e o tempo da tragetoria entre o local do aparelho e o local do chamado */

        for(Chamado c: listaChamados){
            conexao.chamadoDAO.inserir(c); //insere no banco cada chamado
        }
        //quando termina popula a lista novamente a partir do banco
        popularLista();
    }

    //abre um chamado especificado pelo id do objeto
    public void abreChamado(Chamado chamado){
        Intent intent = new Intent(this, ChamadoActivity.class);
        intent.putExtra(ID, chamado.getChamado_id());
        startActivity(intent);
    }

    //o que fazer quando terminar de carregar os dados da view
    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
        Tools.snackbarShort(cLayout, "Dados Atualizados");
    }

    @Override //recupera a resposta de pedido de permissões para o usuario
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureRequestLocation();
                return;
        }
    }

    @SuppressLint("MissingPermission") //recupera a localização de acordo com os criterios
    public void configureRequestLocation(){
        locationManager.requestLocationUpdates("gps", 10000, 100, locationListener);
    }



    /* MENU */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {

            case R.id.menu_item_usuarios:
            {
                abreUsuarios();
                return true;
            }

            case R.id.menu_item_sobre:
            {
                abreSobre();
                return true;
            }

            case R.id.menu_item_sair:
            {
                logout();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abreUsuarios() {
        Intent intent = new Intent(this, ListaUsuariosActivity.class);
        startActivity(intent);
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
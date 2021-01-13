package com.developintech.gestaodechamados.View;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.developintech.gestaodechamados.Controler.ConexaoDatabase;
import com.developintech.gestaodechamados.Controler.Tools;
import com.developintech.gestaodechamados.Model.Chamado;
import com.developintech.gestaodechamados.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.lang.reflect.Array;

import static com.developintech.gestaodechamados.Controler.Tools.BASE_URL;

public class ChamadoActivity extends FragmentActivity implements OnMapReadyCallback, AsyncResponse {

    private GoogleMap mMap;
    private TextView textViewCliNome, textViewEndereco, textViewServico, textViewDescricao, textViewChamStatus;
    private double latitude, longitude;
    private String telefone, celular, status;
    private int chamadoId;
    private ImageButton linkGps, linkCall, linkPhoto, linkStatus;
    private CoordinatorLayout cLayout;
    private String[] statusStrings = {"Em espera", "Em deslocamento", "Em execucao", "Designar técnico", "Finalizado", "Cancelado"};
    public int idMenuItemToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamado);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        chamadoId = bundle.getInt(ListaChamadosActivity.ID);

        textViewChamStatus = (TextView) findViewById(R.id.textViewChamStatus);
        textViewCliNome = (TextView) findViewById(R.id.textViewCliNome);
        textViewEndereco = (TextView) findViewById(R.id.textViewEndereco);
        textViewServico = (TextView) findViewById(R.id.textViewNome);
        textViewDescricao = (TextView) findViewById(R.id.textViewDescricao);
        linkCall = (ImageButton) findViewById(R.id.imageButton);
        linkGps = (ImageButton) findViewById(R.id.imageButton2);
        linkPhoto = (ImageButton) findViewById(R.id.imageButton3);
        linkStatus = (ImageButton) findViewById(R.id.imageButton4);

        //Tools.displayToast(this, "ID"+chamadoId); //for debug

        //PostResponseAsyncTask loadingChamados = new PostResponseAsyncTask(this);
        //loadingChamados.execute("http://developintech.com/gestaochamados/android_api/chamado-details.php?format=json&id=" + chamadoId);

        linkGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
                startActivity(intent);
            }
        });

        linkCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + telefone));
                startActivity(intent);
            }
        });

        linkPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreCamera();
            }
        });

        registerForContextMenu(linkStatus);
        linkStatus.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });

        carregaDadosDoBancoLocal();
    }

    private void abreCamera() {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }

    private void carregaDadosDoBancoLocal() {
        /*
        Menu menu;
        MenuItem emEspera, emDeslocamento, emExecucao, designarTecnico, finalizado, cancelado;
        menu = (Menu) findViewById(R.menu.status_menu);
        emEspera = findViewById(R.id.menuItemEmEspera);
         emDeslocamento = findViewById(R.id.menuItemEmDeslocamento);
        emExecucao = findViewById(R.id.menuItemEmExecucao);
         designarTecnico = findViewById(R.id.menuItemDesignarTecnico);
        finalizado = findViewById(R.id.menuItemFinalizado);
         cancelado = findViewById(R.id.menuItemCancelado);
        */
        //cria conexao com o banco local
        ConexaoDatabase conexao = ConexaoDatabase.getInstance(this);

        //Pega o chamado por ID no Banco SQLite
        Chamado c = conexao.chamadoDAO.chamdoPorId(chamadoId);

        telefone = c.getCli_telefone();
        celular = c.getCli_celular();
        status = c.getStatus();
        latitude = c.getEnd_latitude();
        longitude = c.getEnd_longitude();


        if (status.equalsIgnoreCase("em espera")) {
            textViewChamStatus.setTextColor(Color.rgb(255, 169, 33)); //laranja
            idMenuItemToRemove = R.id.menuItemEmEspera;
        }
        if (status.equalsIgnoreCase("em deslocamento")) {
            textViewChamStatus.setTextColor(Color.rgb(4, 158, 224)); //azul claro
            idMenuItemToRemove = R.id.menuItemEmDeslocamento;
        }
        if (status.equalsIgnoreCase("em execucao")) {
            textViewChamStatus.setTextColor(Color.rgb(7, 64, 196)); //azul escuro
            idMenuItemToRemove = R.id.menuItemEmExecucao;
        }
        if (status.equalsIgnoreCase("finalizado")) {
            textViewChamStatus.setTextColor(Color.rgb(79, 175, 65)); //verde
            idMenuItemToRemove = R.id.menuItemFinalizado;
        }
        if (status.equalsIgnoreCase("cancelado")) {
            textViewChamStatus.setTextColor(Color.rgb(188, 24, 24)); //vermelho
            idMenuItemToRemove = R.id.menuItemCancelado;
        }
        if (status.equalsIgnoreCase("designar técnico")) {
            idMenuItemToRemove = R.id.menuItemDesignarTecnico;
        }

        textViewChamStatus.setText(status);
        textViewCliNome.setText(c.getCli_nome());
        textViewEndereco.setText(c.getEnd_logradouro() + ", " + c.getEnd_numero() + ", " + c.getEnd_complemento() + " - " + c.getEnd_bairro() + ". " + c.getEnd_cidade() + " - " + c.getEnd_estado());
        textViewServico.setText(c.getServ_nome());
        textViewDescricao.setText(c.getServ_descricao());


        Tools.displayToast(this, "Carregando: " + latitude + " | " + longitude);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng localChamado = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(localChamado));
        mMap.addMarker(new MarkerOptions().position(localChamado).title("Cliente"));
        mMap.setMinZoomPreference(16);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        //menu.removeItem(idMenuItemToRemove);
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.status_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemEmEspera: {
                changeStatus(1);
                return true;
            }
            case R.id.menuItemEmDeslocamento: {
                changeStatus(2);
                return true;
            }
            case R.id.menuItemEmExecucao: {
                changeStatus(3);
                return true;
            }
            case R.id.menuItemDesignarTecnico: {
                changeStatus(4);
                return true;
            }
            case R.id.menuItemFinalizado: {
                changeStatus(5);
                return true;
            }
            case R.id.menuItemCancelado: {
                changeStatus(6);
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void changeStatus(int statusId) {
        PostResponseAsyncTask changeStatus = new PostResponseAsyncTask(this);
        ConexaoDatabase conexao = ConexaoDatabase.getInstance(this);

        conexao.chamadoDAO.alteraStatus(chamadoId, statusStrings[statusId-1]);
        changeStatus.execute(BASE_URL + "/changestatus.php?format=json&id=" + chamadoId + "&status=" + statusId);

    }

    @Override
    public void processFinish(String s) {
        if(s.contains("sucesso")){
            Snackbar.make(findViewById(android.R.id.content),"Status atualizado com sucesso!", Snackbar.LENGTH_SHORT).show();
            carregaDadosDoBancoLocal();
        } else {
            Snackbar.make(findViewById(android.R.id.content),"Ocorreu um erro na atualização!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
package com.developintech.gestaodechamados.Controler;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import com.developintech.gestaodechamados.Model.Chamado;
import com.developintech.gestaodechamados.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import java.util.HashMap;

public class Tools {

    private Location currentBestLocation = null;
    HashMap<String, Double> distAndTimeAtoB = new HashMap<>();
    Double distancia = 0.0, tempo = 0.0;



    /** PERSONAL GOOGLE API KEY - Change it before dump in production! {ò.Ó,}  *******************/

    public static final String google_api_key = "...";



    /** URL BASE DA API-WEB   ! não colocar '/' no final  ****************************************/

    public static final String BASE_URL = "...";



    /**  TOASTS & SNACKBARS          ...delicious  :P   ******************************************/

    //Exibe de forma simplificada um Toast na tela de contexto
    public static void displayToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    //Exibe de forma simplificada um SnackBar de curta duração na tela de contexto
    public static void snackbarShort(CoordinatorLayout cLayout, String msg) {
        Snackbar.make(cLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    //Exibe de forma simplificada um SnackBar de Longa duração na tela de contexto
    public static void snackbarLong(CoordinatorLayout cLayout, String msg) {
        Snackbar.make(cLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    //Exibe de forma simplificada um SnackBar de duração indefinida na tela de contexto
    public static void snackbarIndefinite(CoordinatorLayout cLayout, String msg) {
        Snackbar.make(cLayout, msg, Snackbar.LENGTH_INDEFINITE).show();
    }



    /** OTHER BORING STUFF          ...joking  ;)   **********************************************/

    //Verifica se o aparelho está conectado e devolve TRUE ou FALSE
    public static boolean conectado(Context contexto) {
        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) {
            return true;
        } else {
            return false;
        }
    }

    //Devolve o id do icone da lista de recursos com base no Chamado passado
    public static int getIdIconResource(Chamado chamado) {
        if (chamado.getIcon_mobile().contains("geral")) {
            if(chamado.getIcon_mobile().contains("42"))
                return R.drawable.geral_icon_42x42;
            if(chamado.getIcon_mobile().contains("52"))
                return R.drawable.geral_icon_52x52;
        } else if (chamado.getIcon_mobile().contains("hardware")) {
            if(chamado.getIcon_mobile().contains("42"))
                return R.drawable.hardware_icon_42x42;
            if(chamado.getIcon_mobile().contains("52"))
                return R.drawable.hardware_icon_52x52;
        } else if (chamado.getIcon_mobile().contains("redes")) {
            if(chamado.getIcon_mobile().contains("42"))
                return R.drawable.redes_icon_42x42;
            if(chamado.getIcon_mobile().contains("52"))
                return R.drawable.redes_icon_52x52;
        } else if (chamado.getIcon_mobile().contains("software")) {
            if(chamado.getIcon_mobile().contains("42"))
                return R.drawable.software_icon_42x42;
            if(chamado.getIcon_mobile().contains("52"))
                return R.drawable.software_icon_52x52;
        }
        return android.R.drawable.presence_away; //caso seja diferente do disponivel
    }

    //Devolve a distancia entre dois pares de coordenadas
    public static double calculeDistanceAtoB(double aLat, double aLng, double bLat, double bLng) {

        LatLng posicaoInicial = new LatLng(aLat, aLng);
        LatLng posicaiFinal = new LatLng(bLat, bLng);

        double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);

        return distance;
    }
}

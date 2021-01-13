package com.developintech.gestaodechamados.View;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import com.developintech.gestaodechamados.Controler.Tools;
        import com.developintech.gestaodechamados.R;

        import com.kosalgeek.android.md5simply.MD5;
        import com.kosalgeek.asynctask.AsyncResponse;
        import com.kosalgeek.asynctask.ExceptionHandler;
        import com.kosalgeek.asynctask.PostResponseAsyncTask;
        import java.util.HashMap;

        import static com.developintech.gestaodechamados.Controler.Tools.BASE_URL;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "LoginActivity";
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        tvRegister = (TextView)findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
                //startActivity(in);
            }
        });
    }

    private boolean emptyValidate(EditText etEmail, EditText etPassword){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        return (email.isEmpty() && password.isEmpty());
    }

    String password = "";
    @Override
    public void onClick(View v) {
        if(!Tools.conectado(this)){
            Tools.displayToast(this,"Sem Conexão de Internet!");
        } else if (emptyValidate(etEmail,etPassword)){
            Tools.displayToast(this,"Login ou Senha são campos obrigatorios!");
        } else { //se estiver conectado e os editTexts preenchidos executa
            final String email = etEmail.getText().toString();
            password =  MD5.encrypt(etPassword.getText().toString());

            HashMap<String, String> loginData = new HashMap<>();
            loginData.put("email", email);
            loginData.put("password", password);

            final PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, loginData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    Log.d(TAG, s);
                    if(s.contains("LoginSuccess")){
                        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putBoolean("porNome", false);
                        editor.commit();
                        Intent in = new Intent(getApplicationContext(), ListaChamadosActivity.class);
                        startActivity(in);
                    }
                    else{
                        Tools.displayToast(getApplicationContext(),"Something went wrong. Cannot login.");
                    }
                }
            });

            loginTask.setExceptionHandler(new ExceptionHandler() {
                @Override
                public void handleException(Exception e) {
                    if(e != null && e.getMessage() != null){
                        Log.d(TAG, e.getMessage());
                    }
                }
            });

            loginTask.execute(BASE_URL+"/login.php");
        }
    }
}
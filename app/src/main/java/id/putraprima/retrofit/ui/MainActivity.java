package id.putraprima.retrofit.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button loginButton, registerButton;
    EditText edtEmail, edtPassword;
    String email, password;
    private TextView mMainTxtAppName;
    private TextView mMainTxtAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.bntToRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        mMainTxtAppName = findViewById(R.id.mainTxtAppName);
        mMainTxtAppVersion = findViewById(R.id.mainTxtAppVersion);

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mMainTxtAppName.setText(preference.getString("appName", "default"));
        mMainTxtAppVersion.setText(preference.getString("appVersion", "default"));
    }

    public void handleLoginClick(View view) {
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        doLogin();
    }

    private void doLogin() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);

        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = service.doLogin(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(i);
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if (error.getError().getEmail()!= null && error.getError().getPassword()!=null){
                        Toast.makeText(MainActivity.this, "response message : " + error.getError().getPassword().get(0)
                                + error.getError().getEmail().get(0), Toast.LENGTH_SHORT).show();
                    }else if(error.getError().getEmail()!= null){
                        Toast.makeText(MainActivity.this, error.getError().getEmail().get(0), Toast.LENGTH_SHORT).show();
                    }else if (error.getError().getPassword()!=null){
                        Toast.makeText(MainActivity.this, error.getError().getPassword().get(0), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleRegister(View view) {
        Intent intent = new Intent(this, registerActivity.class);
        startActivity(intent);
    }


}

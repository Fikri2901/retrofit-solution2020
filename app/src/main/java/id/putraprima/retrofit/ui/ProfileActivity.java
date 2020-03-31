package id.putraprima.retrofit.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.UserInfo;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    public Context context;
    private TextView Name , Email , Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = getApplicationContext();

        getMe();
        Id = findViewById(R.id.iptId);
        Name = findViewById(R.id.iptName);
        Email = findViewById(R.id.iptEmail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1){
            getMe();
            return;
        }else if (requestCode == 2 && resultCode == 2){
            getMe();
            return;
        }
    }

    private void getMe() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        Toast.makeText(context, preference.getString("token",null), Toast.LENGTH_SHORT).show();
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class, "Bearer "+preference.getString("token",null));
        Call<Envelope<UserInfo>> call = service.me();
        call.enqueue(new Callback<Envelope<UserInfo>>() {
            @Override
            public void onResponse(Call<Envelope<UserInfo>> call, Response<Envelope<UserInfo>> response) {

                Id.setText(Integer.toString(response.body().getData().getId()));
                Name.setText(response.body().getData().getName());
                Email.setText(response.body().getData().getEmail());
            }

            @Override
            public void onFailure(Call<Envelope<UserInfo>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleUpdateProfile(View view) {
        Intent intent = new Intent(this, uProfileActivity.class);
        startActivityForResult(intent, 1);
    }

    public void handleUpdatePassword(View view) {
        Intent intent = new Intent(this, uPasswordActivity.class);
        startActivityForResult(intent, 2);
    }
}

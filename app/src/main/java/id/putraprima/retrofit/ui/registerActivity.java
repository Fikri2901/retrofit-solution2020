package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class registerActivity extends AppCompatActivity {

    private EditText username,Email,Password,Password_confirm;
    public RegisterRequest registerRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        Password_confirm = findViewById(R.id.password_confirm);

        register();
    }

    public void register(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<RegisterResponse>> call = service.doRegister(registerRequest);
        call.enqueue(new Callback<Envelope<RegisterResponse>>() {
            @Override
            public void onResponse(Call<Envelope<RegisterResponse>> call, Response<Envelope<RegisterResponse>> response) {
                if (response.code() == 302){
                    Toast.makeText(registerActivity.this, "Register Failed, Duplicate User", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 201){
                    Toast.makeText(registerActivity.this, response.body().getData().getName(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(registerActivity.this, response.body().getData().getEmail(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(registerActivity.this, "Register Successfull", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Envelope<RegisterResponse>> call, Throwable t) {
                Toast.makeText(registerActivity.this, "Error Request", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void handlerRegisterProcess(View view) {
        String name = username.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String password_confirm = Password_confirm.getText().toString();
        registerRequest = new RegisterRequest(name, email, password, password_confirm);

        boolean check;
        if (name.equals("")) {
            Toast.makeText(this, "Name is Empty!", Toast.LENGTH_SHORT).show();
            check = false;
        } else if(email.equals("")) {
            Toast.makeText(this, "Email is Empty!", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (password.equals("")) {
            Toast.makeText(this, "Password is Empty!", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (password.equals("")) {
            Toast.makeText(this, "Password Confirmation is Empty!", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password limit 8", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (!password_confirm.equals(password)) {
            Toast.makeText(this, "Confirm Password not Same!", Toast.LENGTH_SHORT).show();
            check = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Email Not Valid", Toast.LENGTH_SHORT).show();
            check = false;
        } else {
            check = true;
        }

        if (check == true) {
            register();
        }
    }
}

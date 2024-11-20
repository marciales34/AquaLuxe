package com.snt.aqualuxe.Clientes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.snt.aqualuxe.R;
import com.snt.aqualuxe.RetrofitClient;

public class Login extends AppCompatActivity {

    private GoogleSignInClient googleLoginCliente;
    private final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Configuración para iniciar sesión con Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleLoginCliente = GoogleSignIn.getClient(this, gso);

        // Botón de inicio de sesión con Google
        Button signInButton = findViewById(R.id.btn_google);
        signInButton.setOnClickListener(v -> signIn());

        // Botón para iniciar sesión normal
        Button loginButton = findViewById(R.id.InicioSesion);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la actividad de inicio de sesión
                Intent intent = new Intent(Login.this, InicioDeSesion.class);
                startActivity(intent);
            }
        });

        // Botón para registrar
        Button registrarButton = findViewById(R.id.Registrar);
        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la actividad de registro
                Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);
            }
        });
    }

    // Método para iniciar sesión con Google
    private void signIn() {
        Intent signInIntent = googleLoginCliente.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Procesar el resultado de la autenticación con Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // Manejar el resultado de la autenticación con Google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                Toast.makeText(this, "Verificado: " + account.getDisplayName(), Toast.LENGTH_LONG).show();

                // Redirigir a la actividad Home después del inicio de sesión
                Intent intent = new Intent(this, PaginaInicioCliente.class);
                startActivity(intent);
                finish();
            }
        } catch (ApiException e) {
            Log.w("SignIn", "SignInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Error de autenticación: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}



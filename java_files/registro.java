package com.example.paises_capitales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registro extends AppCompatActivity {

    EditText name;
    EditText password;
    private FirebaseAuth auth;
    SharedPreferences prefe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        name = (EditText) findViewById(R.id.nombre);
        password = (EditText) findViewById(R.id.pswd);


        prefe = getSharedPreferences("DatosShared", Context.MODE_PRIVATE);
        name.setText(prefe.getString("mail", ""));
        password.setText(prefe.getString("pwd",""));


    }
    public void crear(View v){


            final String nombre = name.getText().toString().trim();
            String psw = password.getText().toString();

        //autenticar usuario
            auth.createUserWithEmailAndPassword(nombre, psw)
                    .addOnCompleteListener(registro.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(registro.this, "Usuario creado: createUserWithEmail:onComplete:" + task.isSuccessful(),Toast.LENGTH_SHORT).show();
                                Intent data = new Intent();
                                data.putExtra("email",nombre);
                                setResult(RESULT_OK, data);
                                finish();
                            }
                            else{
                                Toast.makeText(registro.this, "Falló la autenticación "+ task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


    }
    public void entrar(View v){


       final String nombre = name.getText().toString().trim();
        final String psw = password.getText().toString();
        //autenticar usuario
        auth.signInWithEmailAndPassword(nombre, psw)
                .addOnCompleteListener(registro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        prefe = getSharedPreferences("DatosShared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefe.edit();
                        editor.putString("mail", name.getText().toString());
                        editor.putString("pwd",password.getText().toString());
                        editor.commit();

                        if (!task.isSuccessful()) {
                            // there was an error
                            if (psw.length() < 6) {
                                Toast.makeText(registro.this, "Contraseña demasiado corta, ingrese un mínimo de 6 caracteres", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(registro.this, "Falló la autenticación, revise su correo electrónico y contraseña o regístrese", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(registro.this, "Usuario correcto ", Toast.LENGTH_LONG).show();
                            Intent data = new Intent();
                            data.putExtra("email",nombre);
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    }
                });

    }

    public boolean match(String prueba, String exp) {
        Pattern patron;   //indicamos la ER
        Matcher mcher;   //compila ER vs Cadena

        patron = Pattern.compile(exp);
        mcher = patron.matcher(prueba);
        // mcher obtiene el resultado de la evaluacion

        if (mcher.find()) {
            return true;
        } else {
            return false;
        }
    }


}

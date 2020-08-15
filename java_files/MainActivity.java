package com.example.paises_capitales;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  ArrayList<String> p_c = new ArrayList<>();
  //int request_code = 1;
  int i = 0;
  int cont=0;
  int correctos =0;
    DatabaseReference mRootReference;
    String email;
    TextView player;
    TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = findViewById(R.id.jugador);
        score = findViewById(R.id.puntos);

       llenar_arr();
       clase_registro();
        mRootReference = FirebaseDatabase.getInstance().getReference();

    }
    public void clase_registro(){
        Intent intent = new Intent( this,registro.class);
        startActivityForResult(intent, 2);
    }

    public void clase_jugar(View v){

        cont = 1;  //inicializamos el contador de las preguntas para empezar a jugar
        Random r=new Random();
        i = r.nextInt(p_c.size());

           String arreglo[] = p_c.get(i).split(":");

           Intent intent = new Intent( v.getContext(),Jugar.class);
           intent.putExtra("PAIS", arreglo[0]);
           intent.putExtra("CAPITAL", arreglo[1]);
           intent.putExtra("indice",i);
            intent.putExtra("numero_pag",cont);
           intent.putStringArrayListExtra("ARREGLO", p_c);
           cont++;
           startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    if (cont < 11){

                        if (data.getStringExtra("resul").equals("bien")){
                            correctos++;
                        }
                        Random r=new Random();
                        i = r.nextInt(p_c.size());
                        String arreglo[] = p_c.get(i).split(":");

                        Intent intent = new Intent( this,Jugar.class);
                        intent.putExtra("PAIS", arreglo[0]);
                        intent.putExtra("CAPITAL", arreglo[1]);
                        intent.putExtra("indice",i);
                        intent.putExtra("numero_pag",cont);
                        intent.putStringArrayListExtra("ARREGLO", p_c);
                        cont++;
                        startActivityForResult(intent, 1);
                    }else{
                        player.setText("Jugador: "+email);
                        score.setText("Puntuaje: "+correctos+" correctos de 10");
                        cargarDatosFirebase(email);
                    }
                }
                break;
            case 2:     //si viene de la clase registro
                if (resultCode == RESULT_OK){
                    email = data.getStringExtra("email");
                }
                break;
        }
    }

    public void llenar_arr(){

        try{
        InputStreamReader ls = new InputStreamReader(this.getResources().openRawResource(R.raw.textooo));
       BufferedReader leer = new BufferedReader(ls);

            String linea = "";
            while(linea != null){
                linea = leer.readLine();
                if(linea!=null){
                   p_c.add(linea);
                }
            }

        }catch (IOException e){ e.printStackTrace();}
    }
    public void cargarDatosFirebase(String correo) {

        Map<String, Object> datosUsuario = new HashMap<>();
        String id = mRootReference.push().getKey();
        datosUsuario.put("email", correo);
        datosUsuario.put("progreso", correctos);
        correctos = 0;
        int indice = correo.indexOf("@");
        mRootReference.child(correo.substring(0, indice)).child(id).setValue(datosUsuario);
    }


}

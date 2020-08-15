package com.example.paises_capitales;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Jugar extends AppCompatActivity {

    TextView PAIS,num_pag;

    ImageView ima_bandera;
    Button botones [] = new Button[4];

    ArrayList<Integer> lista = new ArrayList<>();
    ArrayList<String> arre = new ArrayList<>();
    String country, capital;
    int index,cont_paginas;
    ProgressBar barra ;
    int progreso = 0;
    Handler handler = new Handler();
    DatabaseReference mRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        PAIS = (TextView) findViewById(R.id.pais);
        num_pag = (TextView) findViewById(R.id.num_pagina);
        ima_bandera = (ImageView) findViewById(R.id.bandera);

        botones[0] = (Button) findViewById(R.id.opcion_1);
        botones[1] = (Button) findViewById(R.id.opcion_2);
        botones[2] = (Button) findViewById(R.id.opcion_3);
        botones[3] = (Button) findViewById(R.id.opcion_4);
        barra = (ProgressBar) findViewById(R.id.progreso);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        country = intent.getStringExtra("PAIS");
        capital = intent.getStringExtra("CAPITAL");
        arre = intent.getStringArrayListExtra("ARREGLO");
        index = bundle.getInt("indice");
        cont_paginas = bundle.getInt("numero_pag");

       num_pag.setText(String.valueOf(cont_paginas));
        PAIS.setText(country);
        country = country.toLowerCase();
        country = country.replaceAll("\\s","");


        final int id = getResources().getIdentifier(getPackageName()+":drawable/"+country, null, null);
        ima_bandera.setImageResource(id);
        cambiar_botones();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progreso < 10){
                    progreso++;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            barra.setProgress(progreso);
                        }
                    });
                    try{
                         Thread.sleep(1000);
                    }catch (InterruptedException e){  e.printStackTrace();}
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < botones.length; i++) {
                            if (botones[i].getText().toString().equals(capital)){
                                botones[i].setBackgroundColor(Color.YELLOW);
                            }
                            botones[i].setEnabled(false);
                        }

                    }
                });
               String resultado = "mal";
                Intent data = new Intent();
                data.putExtra("resul",resultado);
                setResult(RESULT_OK, data);
                finish();

            }
        }).start();
    }
    public void respuesta(View v){
        switch (v.getId()) {
            case R.id.opcion_1:
                compara(botones[0].getText().toString(),0);
                break;
            case R.id.opcion_2:
                compara(botones[1].getText().toString(),1);
                break;
            case R.id.opcion_3:
                compara(botones[2].getText().toString(),2);
                break;
            case R.id.opcion_4:
                compara(botones[3].getText().toString(),3);
                break;
        }

       // finish();

    }
    public void compara(String texto, int boton){
        String resultado = "";
        if (texto.equals(capital)){
            botones[boton].setBackgroundColor(Color.GREEN);
            Toast.makeText(this, "CORRECTO", Toast.LENGTH_SHORT).show();
            resultado = "bien";
        }else{
            botones[boton].setBackgroundColor(Color.RED);
            Toast.makeText(this, "INCORRECTO", Toast.LENGTH_SHORT).show();
            resultado = "mal";
        }

        Intent data = new Intent();
        //data.setData(Uri.parse("hola"));
        data.putExtra("resul",resultado);
        setResult(RESULT_OK, data);
        finish();
    }
    public void cambiar_botones(){
        Random r=new Random();
        int btn_correcto = r.nextInt(3);
        botones[btn_correcto].setText(capital);
        arre.remove(index);

        lista.add(btn_correcto);
        for (int i = 0; i < 4; i++) {
            if (i != btn_correcto){
                agregar(i);
            }
        }

    }
    public void agregar(int indice){
        Random r=new Random();
        int index_random = r.nextInt(arre.size());
            String arreglo[] = arre.get(index_random).split(":");
            botones[indice].setText(arreglo[1]);
            arre.remove(index_random);
    }




}

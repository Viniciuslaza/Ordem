package com.example.ordems.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ordems.R;
import com.example.ordems.model.ModelOs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ConsultaActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tv_cliente, tv_atendente, tv_infe, tv_tipo,
                     tv_marca, tv_modelo, tv_cor, tv_ns, tv_inf, tv_laudo;

    private Button btn_voltar;
    private  int id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        this.btn_voltar = findViewById(R.id.btn_voltar);
        this.btn_voltar.setOnClickListener(this);

        Bundle extra = getIntent().getExtras();
        id = extra.getInt("id_os");
        ConsultaOsAsyncTask consultarOsAsyncTask = new ConsultaOsAsyncTask("consultaros", id);

        consultarOsAsyncTask.execute();

    }


        public class ConsultaOsAsyncTask extends AsyncTask<String, String, String>{

            String api_token, query;
            int api_id;


            HttpURLConnection conn;
            URL url = null;
            Uri.Builder builder;

            final String URL_WEB_SERVICES = "https://link8.com.br/Api/Api.php";

            final int READ_TIMEOUT = 10000; //MILISEGUNDOS
            final int CONNECTION_TIMEOUT = 30000;

            int response_code;

            public ConsultaOsAsyncTask(String token , int id) {
                this.api_token = token;
                this.api_id = api_id;
                this.builder = new Uri.Builder();
                builder.appendQueryParameter("api_token", api_token);
                builder.appendQueryParameter("api_cpf", String.valueOf(api_id));
            }


            @Override
            protected void onPreExecute() {
                // progressBar.setVisibility(View.VISIBLE);
                Log.i("APIConsultarOs", "onPreExecute()");
            }

            @Override
            protected String doInBackground(String... strings) {

                Log.i("APIConsultarOs", "doInBackground()");

                //GERAR O CONTEÚDO PARA A URL

                try {
                    url = new URL(URL_WEB_SERVICES);
                } catch (MalformedURLException e) {
                    Log.i("APIConsultarOs", "doInBackground() --> " + e.getMessage());
                } catch (Exception e) {
                    Log.i("APIConsultarOs", "doInBackground() --> " + e.getMessage());
                }

                //GERAR UMA REQUISIÇÃO HTTP - POST - RESULT SERÁ UM ARRAYJSON

                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("charset", "utf-8");

                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    conn.connect();

                } catch (ProtocolException e) {
                    Log.i("APIConsultarOs", "doInBackground() --> " + e.getMessage());
                } catch (IOException e) {
                    Log.i("APIConsultarOs", "doInBackground() --> " + e.getMessage());
                }

                //ADICIONAR O TOKEN E/ OU OUTROS PARAMETROS COMO POR EXEMPLO
                //O OBJETO A SER INCLUIDO, DELETADO OU ALTERADO.
                //CRUD COMPLETO

                try {
                    query = builder.build().getEncodedQuery();

                    OutputStream stream = conn.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(stream, "utf-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    stream.close();

                    conn.connect();
                } catch (UnsupportedEncodingException e) {
                    Log.i("APIConsultarOs", "doInBackground() --> " + e.getMessage());
                } catch (IOException e) {
                    Log.i("APIConsultarOs", "doInBackground() --> " + e.getMessage());
                }

                //RECEBER O RESPONSE - ARRAYJSON
                //HTTP - CODIGO DO RESPONSE | 200 | 404 | 503

                try {
                    response_code = conn.getResponseCode();
                    if (response_code == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(inputStream)
                        );

                        StringBuilder result = new StringBuilder();
                        String linha = null;
                        while ((linha = reader.readLine()) != null) {
                            result.append(linha);
                        }
                        return result.toString();
                    } else {
                        return "HTTP ERRO:" + response_code;
                    }

                } catch (IOException e) {
                    Log.i("APIConsultarOs", "doInBackground() --> " + e.getMessage());
                } finally {
                    conn.disconnect();
                }
                return "Processamento concluido";
            }

            @Override
            protected void onPostExecute(String result) {
                Log.i("v", "onPostExecute() --> Result: " + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.getBoolean("validaçao")) {
                        setarcampos(jsonObject.getString("nome_cliente"),
                                jsonObject.getInt("id_usuario"),
                                jsonObject.getString("data_previsao"),
                                jsonObject.getString("tipo"),
                                jsonObject.getString("marca"),
                                jsonObject.getString("modelo"),
                                jsonObject.getString("cor"),
                                jsonObject.getString("serie"),
                                jsonObject.getString("info_cliente"),
                                jsonObject.getString("info_tecnico"));

                        //goOsActivity();

                    } else {
                        Log.i("APIConsultarOs", "onPostExecute() --> Consulta Falhou");
                        Log.i("APIConsultarOs", "onPostExecute() --> " + jsonObject.getString("SQL"));
                        // erroActivity();
                    }
                } catch (JSONException e) {
                    Log.i("APIConsultarOs", "onPostExecute() --> " + e.getMessage());
                }

                //progressBar.setVisibility(View.GONE);
            }


        }
    public void setarcampos(String nome_cliente, int id_usuario, String data_previsao, String tipo, String marca, String modelo, String cor, String serie, String info_cliente, String info_tecnico){
        tv_cliente.setText(nome_cliente);
        tv_atendente.setText(String.valueOf(id_usuario));
        tv_infe.setText(data_previsao);
        tv_tipo.setText(tipo);
        tv_marca.setText(marca);
        tv_modelo.setText(modelo);
        tv_cor.setText(cor);
        tv_ns.setText(serie);
        tv_inf.setText(info_cliente);
        tv_laudo.setText(info_tecnico);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_voltar:
                Intent intent = new Intent(getApplicationContext(), OsActivity.class);
                startActivity(intent);
                finish();
        }

    }
}
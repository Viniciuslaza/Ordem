package com.example.ordems.View;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ordems.R;

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

    private Button btn_volt;
    private  int id;

    private  String nm_cliente, data_previsao, tipo, marca, modelo, cor, serie, info_cliente, info_tecnico;
    private int usuario_id ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        this.tv_cliente = findViewById(R.id.tv_cliente);
        this.tv_atendente = findViewById(R.id.tv_atendente);
        this.tv_infe = findViewById(R.id.tv_infe);
        this.tv_tipo = findViewById(R.id.tv_tipo);
        this.tv_marca = findViewById(R.id.tv_marca);
        this.tv_modelo = findViewById(R.id.tv_modelo);
        this.tv_cor = findViewById(R.id.tv_cor);
        this.tv_ns = findViewById(R.id.tv_ns);
        this.tv_inf = findViewById(R.id.tv_inf);
        this.tv_laudo = findViewById(R.id.tv_laudo);

        this.btn_volt = findViewById(R.id.btn_volt);
        this.btn_volt.setOnClickListener(this);

        Bundle extra = getIntent().getExtras();
        id = extra.getInt("id_os");
        ConsultaOsAsyncTask consultarOsAsyncTask = new ConsultaOsAsyncTask("consultaros", id);

        consultarOsAsyncTask.execute();

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_volt:


                finish();
        }

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
                this.api_id = id;
                this.builder = new Uri.Builder();
                builder.appendQueryParameter("api_token", api_token);
                builder.appendQueryParameter("api_id_os", String.valueOf(api_id));
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



                    if (jsonObject.getBoolean("validacao")) {
                        Log.i("v", "Nome do Cliente" + jsonObject.getString("nm_cliente") );
                               nm_cliente = jsonObject.getString("nm_cliente");
                               usuario_id  = jsonObject.getInt("usuario_id");
                               data_previsao = jsonObject.getString("data_previsao");
                               tipo = jsonObject.getString("tipo");
                               marca = jsonObject.getString("marca");
                               modelo = jsonObject.getString("modelo");
                               cor = jsonObject.getString("cor");
                               serie = jsonObject.getString("serie");
                               info_cliente = jsonObject.getString("info_cliente");
                               info_tecnico  = jsonObject.getString("info_tecnico");

                        setarcampos();

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
    public void setarcampos(){
        tv_cliente.setText("Nome: "+ nm_cliente);
        tv_atendente.setText("Atendente: "+String.valueOf(usuario_id));
        tv_infe.setText("Data previsão: "+data_previsao);
        tv_tipo.setText("Tipo: "+tipo);
        tv_marca.setText("Marca: "+marca);
        tv_modelo.setText("Modelo: " +modelo);
        tv_cor.setText("Cor: " +cor);
        tv_ns.setText("Serie: "+serie);
        tv_inf.setText("Informação do cliente: " +info_cliente);
        tv_laudo.setText("Informação tecnico: " +info_tecnico);

    }


}
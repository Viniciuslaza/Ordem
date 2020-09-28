package com.example.ordems.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ordems.Holder.AdapterOs;
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
import java.util.ArrayList;
import java.util.List;

public class OsActivity extends AppCompatActivity implements View.OnClickListener, AdapterOs.OnProjectListnner {
    private RecyclerView rv_ordems;
    private Button btn_voltar;
    private String cpf;
    private List<ModelOs> listaOs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);
        this.btn_voltar = findViewById(R.id.btn_voltar);
        this.btn_voltar.setOnClickListener(this);

        this.rv_ordems = findViewById(R.id.rv_ordems);

        Bundle extra = getIntent().getExtras();
        cpf = extra.getString("cpfUsuario");
        ListarOsAsyncTask listarOsAsyncTask = new ListarOsAsyncTask("listaros", cpf);

        listarOsAsyncTask.execute();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_voltar:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
        }
    }
//Aqui onde se faz o click
    @Override
    public void onProjectClick(int position) {
        int id_os = listaOs.get(position).getId();
        //Intent que vai passar o id os para a proxima tela(Activity)
        Intent intent = new Intent(this, ConsultaActivity.class);
        intent.putExtra("id_os", id_os);
        startActivity(intent);
    }

    public class ListarOsAsyncTask extends AsyncTask<String, String, String> {

        String api_token, query, api_cpf;


        HttpURLConnection conn;
        URL url = null;
        Uri.Builder builder;

        final String URL_WEB_SERVICES = "https://link8.com.br/Api/Api.php";

        final int READ_TIMEOUT = 10000; //MILISEGUNDOS
        final int CONNECTION_TIMEOUT = 30000;

        int response_code;

        public ListarOsAsyncTask(String token, String api_cpf) {
            this.api_token = token;
            this.api_cpf = api_cpf;
            this.builder = new Uri.Builder();
            builder.appendQueryParameter("api_token", api_token);
            builder.appendQueryParameter("api_cpf", String.valueOf(api_cpf));
        }

        @Override
        protected void onPreExecute() {
            // progressBar.setVisibility(View.VISIBLE);
            Log.i("APIListarOs", "onPreExecute()");
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i("APIListarOs", "doInBackground()");

            //GERAR O CONTEÚDO PARA A URL

            try {
                url = new URL(URL_WEB_SERVICES);
            } catch (MalformedURLException e) {
                Log.i("APIListarOs", "doInBackground() --> " + e.getMessage());
            } catch (Exception e) {
                Log.i("APIListarOs", "doInBackground() --> " + e.getMessage());
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
                Log.i("APIListarOs", "doInBackground() --> " + e.getMessage());
            } catch (IOException e) {
                Log.i("APIListarOs", "doInBackground() --> " + e.getMessage());
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
                Log.i("APIListarOs", "doInBackground() --> " + e.getMessage());
            } catch (IOException e) {
                Log.i("APIListarOs", "doInBackground() --> " + e.getMessage());
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
                Log.i("APIListarOs", "doInBackground() --> " + e.getMessage());
            } finally {
                conn.disconnect();
            }
            return "Processamento concluido";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("APIListarOs", "onPostExecute() --> Result: " + result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getBoolean("validaçao")) {
                    int lenght = jsonObject.getInt("lenght");
                    ModelOs os;
                    for (int i = 1; i <= lenght; i++) {
                        os = new ModelOs(jsonObject.getInt("Id" + i), jsonObject.getString("Equipamento" + i));
                        listaOs.add(os);

//                        Log.i("APIListarOs", "Id:" +jsonObject.getInt("Id"+i));
//                        Log.i("APIListarOs", "Equipamento:" +jsonObject.getString("Equipamento"+i));

                    }
                    configurarAdapter(listaOs);
                    //goOsActivity();

                } else {
                    Log.i("APIListarOs", "onPostExecute() --> Consulta Falhou");
                    Log.i("APIListarOs", "onPostExecute() --> " + jsonObject.getString("SQL"));
                    // erroActivity();
                }
            } catch (JSONException e) {
                Log.i("APIListarOs", "onPostExecute() --> " + e.getMessage());
            }

            //progressBar.setVisibility(View.GONE);
        }


    }

    public void configurarAdapter(List<ModelOs> osList) {

        //Configuraçao do Adapter
        AdapterOs adapterOs = new AdapterOs(osList, this);
        //config recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_ordems.setLayoutManager(layoutManager);
        rv_ordems.setHasFixedSize(true);
        rv_ordems.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rv_ordems.setAdapter(adapterOs);
    }


    }

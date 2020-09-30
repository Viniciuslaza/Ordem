package com.example.ordems.View;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ordems.R;
import com.example.ordems.View.ConsultaActivity;
import com.example.ordems.View.OsActivity;

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

public class MainActivity extends AppCompatActivity {


    private EditText et_cpf, et_senha;
    private Button btn_ver;
    private String Cpf;
    private int Senha;
    private TextView alertaCpf, alertaSenha;

    //testando
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.et_cpf = findViewById(R.id.et_cpf);
        this.btn_ver = findViewById(R.id.btn_ver);
        this.et_senha = findViewById(R.id.et_senha);
        this.alertaSenha = findViewById(R.id.alertasenha);
        this.alertaCpf = findViewById(R.id.alertaCpf);

        this.btn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    alertaCpf.setVisibility(View.GONE);
                    if (et_cpf.getText().toString().equals("")) {
                        et_cpf.requestFocus();
                        alertaCpf.setVisibility(View.VISIBLE);
                    }
                    else if(et_senha.getText().toString().equals("")){
                            et_senha.requestFocus();
                            alertaSenha.setVisibility(View.VISIBLE);
                    }
                    else {
                        Cpf = et_cpf.getText().toString();
                        Senha = Integer.parseInt(et_senha.getText().toString());
                        Log.i("Cpf", "Cpf:"+Cpf);
                        Log.i("Senha", "Senha:"+Senha);

                        ConsultarCpfAsyncTask task = new ConsultarCpfAsyncTask("validarCPF", Cpf, Senha);
                        task.execute();


                    }
                }

            }
        });
    }
    //
    public class ConsultarCpfAsyncTask extends AsyncTask<String, String, String> {

        String api_token, query, api_cpf, api_nm_cliente;
        int api_senha;

        HttpURLConnection conn;
        URL url = null;
        Uri.Builder builder;

        final String URL_WEB_SERVICES = "https://link8.com.br/Api/Api.php";

        final int READ_TIMEOUT = 10000; //MILISEGUNDOS
        final int CONNECTION_TIMEOUT = 30000;

        int response_code;

        public ConsultarCpfAsyncTask(String token, String api_cpf, int api_senha){
            this.api_token = token;
            this.api_cpf = api_cpf;
            this.api_senha = api_senha;
            this.builder = new Uri.Builder();
            builder.appendQueryParameter("api_token", api_token);
            builder.appendQueryParameter("api_cpf", String.valueOf(api_cpf));
            builder.appendQueryParameter("api_senha", String.valueOf(api_senha));
        }

        @Override
        protected void onPreExecute() {
            // progressBar.setVisibility(View.VISIBLE);
            Log.i("APIConsultarCpf", "onPreExecute()");
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i("APIConsultarCpf", "doInBackground()");

            //GERAR O CONTEÚDO PARA A URL

            try {
                url= new URL(URL_WEB_SERVICES);
            } catch (MalformedURLException e) {
                Log.i("APIConsultarCpf", "doInBackground() --> "+e.getMessage());
            } catch (Exception e){
                Log.i("APIConsultarCpf", "doInBackground() --> "+e.getMessage());
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
                Log.i("APIConsultarCpf", "doInBackground() --> "+e.getMessage());
            } catch (IOException e) {
                Log.i("APIConsultarCpf", "doInBackground() --> "+e.getMessage());
            }

            //ADICIONAR O TOKEN E/ OU OUTROS PARAMETROS COMO POR EXEMPLO
            //O OBJETO A SER INCLUIDO, DELETADO OU ALTERADO.
            //CRUD COMPLETO

            try{
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
                Log.i("APIConsultarCpf", "doInBackground() --> "+e.getMessage());
            } catch (IOException e) {
                Log.i("APIConsultarCpf", "doInBackground() --> "+e.getMessage());
            }

            //RECEBER O RESPONSE - ARRAYJSON
            //HTTP - CODIGO DO RESPONSE | 200 | 404 | 503

            try{
                response_code = conn.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream)
                    );

                    StringBuilder result = new StringBuilder();
                    String linha = null;
                    while((linha = reader.readLine()) != null){
                        result.append(linha);
                    }
                    return result.toString();
                }
                else{
                    return "HTTP ERRO:"+response_code;
                }

            } catch (IOException e) {
                Log.i("APIConsultarCpf", "doInBackground() --> "+e.getMessage());
            }
            finally {
                conn.disconnect();
            }
            return "Processamento concluido";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("APIConsultarCpf", "onPostExecute() --> Result: "+result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getBoolean("validacao")){
                    //
                    Log.i("ApiConsultarCpf", "CPF:" +jsonObject.getString("cpf"));
                    Log.i("ApiConsultarSenha", "senha:" +jsonObject.getInt("senha"));
                    goOsActivity();

                }
                else{
                    Log.i("APIConsultarCpf", "onPostExecute() --> Consulta Falhou");
                    Log.i("APIConsultarCpf", "onPostExecute() --> "+jsonObject.getString("SQL"));
                    erroActivity();
                }
            } catch (JSONException e) {
                Log.i("APIConsultarCpf", "onPostExecute() --> "+e.getMessage());
            }

            //progressBar.setVisibility(View.GONE);
        }
    }



    public void goOsActivity(){
        Intent intent = new Intent(getApplicationContext(), OsActivity.class);
        intent.putExtra("cpfUsuario", Cpf);

        startActivity(intent);
        finish();
    }
    public void erroActivity(){
        Toast.makeText(this, "Dados Invalidos!!", Toast.LENGTH_SHORT).show();
    }


}






package com.example.ordems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ordems.View.ConsultaActivity;
import com.example.ordems.View.OsActivity;

public class MainActivity extends AppCompatActivity {


    private EditText et_cpf;
    private Button btn_ver;
    private String Cpf;
    private TextView alertaCpf;

    //testando
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.et_cpf = findViewById(R.id.et_cpf);
        this.btn_ver = findViewById(R.id.btn_ver);
        this.alertaCpf = findViewById(R.id.alertaCpf);

        this.btn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    alertaCpf.setVisibility(View.GONE);
                    if (et_cpf.getText().toString().equals("")) {
                        et_cpf.requestFocus();
                        alertaCpf.setVisibility(View.VISIBLE);
                    } else {
                        Cpf = et_cpf.getText().toString();

                        Intent intent = new Intent(getApplicationContext(), OsActivity.class);
                        intent.putExtra("cpfUsuario", Cpf);

                        startActivity(intent);
                        finish();

                    }
                }

            }
        });
    }
}






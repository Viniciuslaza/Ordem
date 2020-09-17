package com.example.ordems.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ordems.MainActivity;
import com.example.ordems.R;

public class ConsultaActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tv_cliente, tv_atendente, tv_infe, tv_tipo,
                     tv_marca, tv_modelo, tv_cor, tv_ns, tv_inf, v_laudo;

    private Button btn_voltar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);


        this.btn_voltar = findViewById(R.id.btn_voltar);
        this.btn_voltar.setOnClickListener(this);

















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
}
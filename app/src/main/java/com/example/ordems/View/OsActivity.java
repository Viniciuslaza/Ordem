package com.example.ordems.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ordems.R;

public class OsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rv_ordems;
    private Button btn_voltar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);
        this.btn_voltar = findViewById(R.id.btn_voltar);
        this.btn_voltar.setOnClickListener(this);

        this.rv_ordems = findViewById(R.id.rv_ordems);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rv_ordems.setLayoutManager(linearLayoutManager);

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
package com.example.ordems.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordems.R;

public class ViewHolderOs extends RecyclerView.ViewHolder {
    public LinearLayout linearLayoutOs;
    public TextView tv_nome;
    public TextView tv_equipamento;

    public ViewHolderOs(@NonNull View itemView) {
        super(itemView);

        this.linearLayoutOs = itemView.findViewById(R.id.linear_layout_os);
        this.tv_nome = itemView.findViewById(R.id.tv_nome);
        this.tv_equipamento = itemView.findViewById(R.id.tv_equipamento);

    }
}

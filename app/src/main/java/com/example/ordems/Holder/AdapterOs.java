package com.example.ordems.Holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordems.R;
import com.example.ordems.model.ModelOs;

import java.util.List;

public class AdapterOs extends RecyclerView.Adapter<AdapterOs.MyViewHolder> {
   private List<ModelOs> listaOs;
   private OnProjectListnner myonprojectlistenner;

   public AdapterOs(List<ModelOs> listaOs, OnProjectListnner onProjectListnner){
       this.listaOs = listaOs;
       this.myonprojectlistenner = onProjectListnner;

       int a = 1;


   }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemlista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_oscliente, parent, false);

       return new MyViewHolder(itemlista, myonprojectlistenner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelOs os = listaOs.get(position);
        holder.tv_id.setText("Id:"+String.valueOf(os.getId()));
        holder.tv_equipamento.setText("Equipamento:"+os.getEquipamento());

    }


    @Override
    public int getItemCount() {
        return listaOs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_id;
        public TextView tv_equipamento;
        public LinearLayout linearLayoutOs;
        public OnProjectListnner onProjectListnner;


        public MyViewHolder(@NonNull View itemView, OnProjectListnner onProjectListnner) {
            super(itemView);


            this.linearLayoutOs = itemView.findViewById(R.id.linear_layout_os);
            this.tv_id = itemView.findViewById(R.id.tv_id);
            this.tv_equipamento = itemView.findViewById(R.id.tv_equipamento);
            this.onProjectListnner = onProjectListnner;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onProjectListnner.onProjectClick(getAdapterPosition());
        }
    }
    public interface OnProjectListnner{
        void onProjectClick(int position);
    }
}

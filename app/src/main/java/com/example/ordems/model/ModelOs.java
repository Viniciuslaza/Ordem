package com.example.ordems.model;

public class ModelOs {

        private int id;
       // private String nome;
        private String equipamento;

        public ModelOs() {

        }
        public ModelOs(int id, String equipamento){
            this.id = id;
            this.equipamento = equipamento;

        }

        public int getId()
        {
            return this.id;
        }

        public void setId(int id){
            this.id = id;
        }

//        public String getNome() {
//            return nome;
//        }
//
//        public void setNome(String nome) {
//            this.nome = nome;
//        }

        public String getEquipamento() {
            return equipamento;
        }

        public void setEquipamento(String equipamento) {
            this.equipamento = equipamento;
        }
    }



package com.company.servidor;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

    public class Mensaje {

        public Mensaje() {
            this.mjsCliente = Collections.synchronizedList(new ArrayList());
        }

        private List<String> mjsCliente;


        public synchronized String EnviarMensajes() throws InterruptedException {
            String textoConcate = "";
            for (int i = 0; i < mjsCliente.size(); i++) {
                textoConcate += mjsCliente.get(i);
            }
            return textoConcate;
        }

        public synchronized void guardarMensaje(String mensaje) throws InterruptedException {
            mjsCliente.add(mensaje);
        }

    }

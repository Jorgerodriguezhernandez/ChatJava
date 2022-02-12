package com.company.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class Cliente {
    public static void main(String[] args) throws IOException {

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Socket socket = new Socket("127.0.0.1", 8080);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        try {
            Scanner sc = new Scanner(System.in);
            List<String> mensajes = Collections.synchronizedList(new ArrayList());

            String nombre = NombreSaludo(ois,oos);

            String msjChatOld = (String) ois.readObject();
            StringTokenizer msjRecibidos = new StringTokenizer(msjChatOld, "~~~");

            // Muestro los mensajes que tengo en el servidor
            while (msjRecibidos.hasMoreTokens()){
                System.out.println(msjRecibidos.nextToken());
            }

            opcionMensaje(ois,oos);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(oos != null) oos.close();
            if (ois != null) ois.close();
            if (socket != null) socket.close();
            System.out.println("Conexion cerrada");
        }
    }

    private static String NombreSaludo(ObjectInputStream ois, ObjectOutputStream oos) {
    }

    private static void opcionMensaje(ObjectInputStream ois, ObjectOutputStream oos) {
    }
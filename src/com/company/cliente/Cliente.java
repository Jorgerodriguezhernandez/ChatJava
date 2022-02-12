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

    public static String NombreSaludo(ObjectInputStream ois, ObjectOutputStream oos){
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca su nombre: ");
        String nombre = sc.nextLine();
        try {
            oos.writeObject(nombre);
            String returnValue = (String) ois.readObject();
            System.out.println(returnValue);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return nombre;
    }



    public static void opcionMensaje(ObjectInputStream ois, ObjectOutputStream oos){
        String opcion = "";
        Scanner sc = new Scanner(System.in);
        while ( !opcion.equals("Good bye") ){
            try {
                System.out.println("Seleccione Bye para cerrar o escriba message: <texto> para mandar un mensaje: ");
                opcion = sc.nextLine();

                if (opcion.contains("message:")) {

                    oos.writeObject(opcion);

                    opcion = (String) ois.readObject();
                    StringTokenizer cachemsj = new StringTokenizer(opcion, "~~~");

                    while (cachemsj.hasMoreTokens()){
                        System.out.println(cachemsj.nextToken());
                    }

                }else if (opcion.equals("bye")) {
                    oos.writeObject(opcion);
                    opcion = (String) ois.readObject();
                }else{
                    System.out.println("Error en la lectura: valor no reconocido");
                }
            }catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }}
    }
}
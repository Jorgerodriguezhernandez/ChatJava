package com.company.servidor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Servidor {

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Encendiendo servidor....");
        Mensaje mensaje = new Mensaje();

        while(true){
            socket = serverSocket.accept();
            HiloConnect hilito = new HiloConnect(mensaje, socket);
            hilito.start();
        }
    }

    static class HiloConnect extends Thread{

        private final Mensaje buffer;
        private Socket socket;
        private ObjectInputStream ois = null;
        private ObjectOutputStream oos = null;

        public HiloConnect(Mensaje data,Socket socket){
            this.buffer = data;
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Conexión recibida desde => " + socket.getInetAddress());

            try {
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());

                // Saludo al nuevo usuario conectado al chat
                String nombre = (String) ois.readObject();
                oos.writeObject("Bienvenido " + nombre);


                //Devolucion de mensajes
                oos.writeObject(buffer.EnviarMensajes());
                System.out.println("Chat enviado !! ");

                String texto = "";

                while (!texto.equals("bye")) {

                    texto = (String) ois.readObject();

                    if (texto.equals("bye")) {
                        oos.writeObject("Good bye");
                        System.out.println("El usuaro => " + nombre + " Se va del chat");
                        //contains => solo si el string tiene la misma secuencia de chars
                    } else if (texto.contains("message:")) {

                        DateTimeFormatter diayhora = DateTimeFormatter.ofPattern("HH:mm:ss");
                        String tiempoactual = diayhora.format(LocalDateTime.now());
                        texto = texto.substring(9);
                        texto =  nombre +" "+ tiempoactual + " : " + texto + "~~~";
                        buffer.guardarMensaje(texto);
                        oos.writeObject(buffer.EnviarMensajes());
                    }
                }

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (oos != null) oos.close();
                    if (ois != null) ois.close();
                    if (socket != null) socket.close();
                    System.out.println("Servidor apagado....");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
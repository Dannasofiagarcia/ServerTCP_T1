package main;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

public class Servidor {
    public static void main(String[] args) {

        try {


            ServerSocket server = new ServerSocket((5001));
            while(true) {

            System.out.println("Esperando conexión...");
            Socket socket = server.accept();
            System.out.println("Conectado");

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader breader = new BufferedReader(isr);

            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bwriter = new BufferedWriter(osw);


                String mensajeRecibido = "";
                mensajeRecibido = breader.readLine();
                System.out.println("Mensaje recibido del cliente: "+ mensajeRecibido);

                if (mensajeRecibido.equalsIgnoreCase("remoteIpConfig")) {
                    bwriter.write("La dirección IP del servidor es: " + socket.getInetAddress() + "\n");
                    bwriter.flush();
                } else if (mensajeRecibido.equalsIgnoreCase("interface")) {
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    boolean stop = false;
                    while(interfaces.hasMoreElements() && stop == false) {
                        NetworkInterface netN = interfaces.nextElement();
                        //Mostramos las interfaces que estan encedidas y que nuestro
                        //computador tiene acceso
                        if (netN.isUp()) {

                            List<InterfaceAddress> list = netN.getInterfaceAddresses();

                            for (int i = 0; i < list.size() && stop == false; i++) {
                                if (list.get(i).getAddress().equals(socket.getInetAddress())) {
                                    bwriter.write("La interfaz desde donde el servidor se comunica es: " + netN.getName() + " " + list.get(i) + "\n");
                                    bwriter.flush();
                                    stop = true;
                                }
                            }
                        }
                    }
                } else if (mensajeRecibido.equalsIgnoreCase("whatTimeIsIt")) {
                    Calendar c = Calendar.getInstance();
                    String fecha = c.getTime().toString();

                    bwriter.write(fecha + "\n");
                    bwriter.flush();
                } else if (mensajeRecibido.equalsIgnoreCase(("RTT"))) {
                    String texto1024 = breader.readLine();
                    if (texto1024.getBytes().length == 1024) {
                        bwriter.write(texto1024 + "\n");
                        bwriter.flush();
                    } else {
                        System.out.println("Not working");
                    }
                } else if (mensajeRecibido.equalsIgnoreCase(("speed"))) {
                    String texto8192 = breader.readLine();
                    int size = texto8192.getBytes().length;
                    if (texto8192.getBytes().length == 8192) {
                        bwriter.write(texto8192 + "\n");
                        bwriter.flush();
                    } else {
                        System.out.println("Not working");
                    }
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

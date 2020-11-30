import dhcpxd.Red;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

public class DHCPServer {
        private static final int MAX_BUFFER_SIZE = 1024; // 1024 bytes
        private int listenPort = 67;//1337;
        private int tiempoPrestamo;
        public DHCPServer(int servePort) {
                listenPort = servePort;
                new DHCPServer();
        }
        public void leerArchivoConfiguracionServidor() {
        /**
         *
         * # de subredes Tiempo de arrendamiento - seg
         *
         * Subred-n Ip inicial Ip final Máscara de subred Getaway DNS
         *
         * EJ:
         *
         * 2 5
         *
         * 10.0.0.21 10.0.0.29 24 10.0.0.20 10.2.2.2 12
         *
         * 10.0.1.50 10.0.1.65 24 10.0.0.49 10.2.2.2 12
         *
         */
        ArrayList<Red> subRedes = new ArrayList<Red>();
        System.out.println("lllllll");
        try {
            // Flujo para leer el archivo de texto
            String archivoConfiguracion = "D:\\Documentos\\Comunicacion y redes\\Proyecto 2\\Proyecto\\DHCP\\DHCP\\Configuracion.txt";
            BufferedReader lectorArchivo = new BufferedReader(new FileReader(archivoConfiguracion));
            // Lectura de la cantidad de subredes
            String linea = lectorArchivo.readLine();
            int cantidadSubRedes = Integer.parseInt(linea);
            // Lectura del tiempo de arrendamiento de cada dirección Ip
            linea = lectorArchivo.readLine();
            this.tiempoPrestamo = Integer.parseInt(linea);
            // Lectura de la configuración de cada subred
            for (int i = 0; i < cantidadSubRedes; i++) {
                // Lectura de Ip inicial
                String ipInicial = lectorArchivo.readLine();
                // Lectura de Ip final
                String ipFinal = lectorArchivo.readLine();
                // Lectura de la máscara
                String mascara = lectorArchivo.readLine();
                // Lectura del Getaway
                String getaway = lectorArchivo.readLine();
                // Lecura del DNS
                String dns = lectorArchivo.readLine();
                // Se crea la red y se guarda
                subRedes.add(new Red(ipInicial, ipFinal, mascara, getaway, dns));

            }
        } catch (Exception e) {

        }
        System.out.println("");
        for (Red r : subRedes) {
            System.out.println(r.toString());
        }
    }

        public DHCPServer() {
                //System.out.println("Opening UDP Socket On Port: " + listenPort);

                DatagramSocket socket = null;
                DHCPmessage m = new DHCPmessage();
                try {
                        
                        socket = new DatagramSocket(listenPort);  

                        byte[] payload = new byte[MAX_BUFFER_SIZE];
                        int length = 6;
                        DatagramPacket p = new DatagramPacket(payload, payload.length);
                        //System.out.println("Success! Now listening on port " + listenPort + "...");
                        System.out.println("Listening on port " + listenPort + "...");
                        
                        //server is always listening
                        boolean listening = true;
                        while (listening) {
                                socket.receive(p);
                                System.out.println("p: "+p);
                                //Prepare Offer 
                                payload = m.DHCPOFFER(p.getData());
                                //System.out.println("Connection established from " + p.getAddress());
                                //System.out.println("DHCPOFFER: " + Arrays.toString(p.getData()));
                                //Send offer
                                p = new DatagramPacket(payload, payload.length,InetAddress.getByName("255.255.255.255") ,p.getPort());
                                socket.send(p);
                        }
                } catch (SocketException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

        }

        /**
         * @param args
         */
        public static void main(String[] args) {
                DHCPServer server;
                if (args.length >= 1) {
                        server = new DHCPServer(Integer.parseInt(args[0]));
                } else {
                        server = new DHCPServer();
                }

        }

}
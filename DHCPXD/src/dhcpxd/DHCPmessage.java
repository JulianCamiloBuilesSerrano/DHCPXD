
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;

public class DHCPmessage {

    /**
     * Atributos
     */
    private byte op; // Opción de mensaje
    private byte h_type; // Tipo de la dirección de hardware
    private byte h_len; // Tamaño de la dirección de hardware
    private byte h_ops; //
    private byte[] xid; // Id de la transacción
    private byte[] secs; // Segundos que pasan desde que el cliente empieza el proceso de adquiriri una
    // dirección o la renovación de una
    private byte[] flags; // Flags
    private byte[] ciaddr; // Dirección Ip del cliente - solo se llena si el cliente se encuentra en
    // proceso de renovar, reasignar o ... y puede responder mensajes ARP
    private byte[] yiaddr; // Dirección Ip del cliente (YOUR Ip Adress)
    private byte[] siaddr; // Dirección Ip del servidor
    private byte[] giaddr; //
    private byte[] chaddr; // Dirección de hardware del cliente
    private byte[] sname; // Nombre del servidor - opcional
    private byte[] file; //
    private byte[] options; // Opciones

    /**
     * Constructores
     */
    public DHCPmessage() {
        this.xid = new byte[4];
        this.secs = new byte[2];
        this.flags = new byte[2];
        this.ciaddr = new byte[4];
        this.yiaddr = new byte[4];
        this.siaddr = new byte[4];
        this.giaddr = new byte[4];
        this.chaddr = new byte[16];
        this.sname = new byte[64];
        this.file = new byte[128];
    }

    /**
     * Métodos
     *
     * @throws SocketException
     * @throws UnknownHostException
     */
    public byte[] DHCPOFFER(byte[] mensaje) throws UnknownHostException, SocketException {

        DHCPmessage m = convertirMensaje(mensaje);

        InetAddress ip = InetAddress.getByName("192.168.1.8");

        this.op = 2;
        this.h_type = 1;
        this.h_len = 6;
        this.h_ops = 0;
        this.xid = new byte[]{5,6,7,8};
        this.secs = new byte[]{0, 0};
        this.flags = new byte[]{0, 0};
        this.ciaddr = new byte[]{0, 0, 0, 0};
        this.yiaddr = ip.getAddress();
        this.siaddr = InetAddress.getLocalHost().getAddress();
        this.giaddr = new byte[]{0, 0, 0, 0};
        this.chaddr = m.getChaddr();
        this.sname = m.getSname();
        this.file = m.getFile();

        this.options = new byte[0];

        return convertirByte();
    }

    public DHCPmessage convertirMensaje(byte[] mensaje) {
        DHCPmessage convertido = new DHCPmessage();

        convertido.setOp(mensaje[0]);
        convertido.setH_type(mensaje[1]);
        convertido.setH_len(mensaje[2]);
        convertido.setH_ops(mensaje[3]);

        byte[] buffer;

        buffer = new byte[4];
        
        for (int i = 0; i < 4; i++) {
            buffer[i] = mensaje[4 + i];
        }
        System.out.println("");
        convertido.setXid(buffer);

        buffer = new byte[2];
        for (int i = 0; i < 2; i++) {
            buffer[i] = mensaje[8 + i];
        }
        convertido.setSecs(buffer);

        for (int i = 0; i < 2; i++) {
            buffer[i] = mensaje[10 + i];
        }
        convertido.setFlags(buffer);

        buffer = new byte[4];
        for (int i = 0; i < 4; i++) {
            buffer[i] = mensaje[12 + i];
        }
        convertido.setCiaddr(buffer);

        for (int i = 0; i < 4; i++) {
            buffer[i] = mensaje[16 + i];
        }
        convertido.setYiaddr(buffer);

        for (int i = 0; i < 4; i++) {
            buffer[i] = mensaje[20 + i];
        }
        convertido.setSiaddr(buffer);

        for (int i = 0; i < 4; i++) {
            buffer[i] = mensaje[24 + i];
        }
        convertido.setGiaddr(buffer);

        buffer = new byte[16];
        //System.out.println("MAC:");
        for (int i = 0; i < 16; i++) {
            //System.out.println(" "+ Arrays.toString(mensaje[28 + i]));
            buffer[i] = mensaje[28 + i];
        }
        convertido.setChaddr(buffer);

        buffer = new byte[64];
        for (int i = 0; i < 64; i++) {
            buffer[i] = mensaje[44 + i];
        }
        convertido.setSname(buffer);

        buffer = new byte[128];
        for (int i = 0; i < 128; i++) {
            buffer[i] = mensaje[108 + i];
        }
        convertido.setFile(buffer);

        buffer = new byte[0];
        int size = mensaje.length - 236;
   
        /*
		 * for (int i = 0; i < size - 2; i++) { buffer[i] = mensaje[size + i];
		 * System.out.println(size + i); }
         */
        convertido.setOptions(buffer);

        return convertido;
    }

    public static void main(String[] args) throws UnknownHostException, SocketException {

        DHCPmessage men = new DHCPmessage();
        men.DHCPSDISCOVER();

    }

    public byte[] DHCPSDISCOVER() throws UnknownHostException, SocketException {
        this.op = 1;
        this.h_type = 1;
        this.h_len = 6;
        this.h_ops = 0;
        this.xid = new byte[]{(byte) 10, (byte) 15, (byte) 23, (byte) 43};
        this.secs = new byte[]{0, 0};
        this.flags = new byte[]{0, 0};
        this.ciaddr = new byte[]{0, 0, 0, 0};
        this.yiaddr = new byte[]{0, 0, 0, 0};
        this.siaddr = new byte[]{0, 0, 0, 0};
        this.giaddr = new byte[]{0, 0, 0, 0};
        this.chaddr = getMAC();
        this.sname = new byte[sname.length];
        this.file = new byte[this.file.length];

        this.options = new byte[0];

        return convertirByte();
    }

    private byte[] convertirByte() {

        int constante = 236;
        int tamanoOpciones = this.options.length;
        byte[] mensaje = new byte[constante + tamanoOpciones];
        mensaje[0] = this.op;
        mensaje[1] = this.h_type;
        mensaje[2] = this.h_len;
        mensaje[3] = this.h_ops;
        for (int i = 0; i < 4; i++) {
            mensaje[4 + i] = this.xid[i];
        }
        for (int i = 0; i < 2; i++) {
            mensaje[8 + i] = this.secs[i];
        }
        for (int i = 0; i < 2; i++) {
            mensaje[10 + i] = this.flags[i];
        }
        for (int i = 0; i < 4; i++) {
            mensaje[12 + i] = this.ciaddr[i];
        }
        for (int i = 0; i < 4; i++) {
            mensaje[16 + i] = this.yiaddr[i];
        }
        for (int i = 0; i < 4; i++) {
            mensaje[20 + i] = this.siaddr[i];
        }
        for (int i = 0; i < 4; i++) {
            mensaje[24 + i] = this.giaddr[i];
        }
        for (int i = 0; i < 16; i++) {
            mensaje[28 + i] = this.chaddr[i];
        }
        for (int i = 0; i < 64; i++) {
            mensaje[44 + i] = this.sname[i];
        }
        for (int i = 0; i < 128; i++) {
            mensaje[108 + i] = this.file[i];
        }
        for (int i = 0; i < tamanoOpciones; i++) {
            mensaje[tamanoOpciones + i] = this.options[i];
        }

        return mensaje;
    }

    private byte[] getMAC() throws UnknownHostException, SocketException {

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface ni = networkInterfaces.nextElement();
            byte[] hardwareAddress = ni.getHardwareAddress();
            byte[] hardware = new byte[16];
            if (hardwareAddress != null) {
                String[] hexadecimalFormat = new String[hardwareAddress.length];
                for (int i = 0; i < hardwareAddress.length; i++) {
                    hexadecimalFormat[i] = String.format("%02X", hardwareAddress[i]);
                    hardware[i] = (byte) Integer.parseInt(hexadecimalFormat[i], 16);
                }
            }
            return hardware;
        }

        return null;
    }

    public byte[] DHCPACK(byte[] mensaje) {
        DHCPmessage m = convertirMensaje(mensaje);

        InetAddress ip;
        try {
            ip = InetAddress.getByName("10.10.10.0");
            this.op = 2;
            this.h_type = 1;
            this.h_len = 6;
            this.h_ops = 0;
            this.xid = m.getXid();
            this.secs = new byte[]{0, 0};
            this.flags = new byte[]{0, 0};
            this.ciaddr = new byte[]{0, 0, 0, 0};
            this.yiaddr = ip.getAddress();
            this.siaddr = InetAddress.getLocalHost().getAddress();
            this.giaddr = new byte[]{0, 0, 0, 0};
            this.chaddr = m.getChaddr();
            this.sname = m.getSname();
            this.file = m.getFile();

            this.options = new byte[0];
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertirByte();
    }

    public DHCPmessage DHCPNACK() {
        return null;
    }

    public DHCPmessage DHCPRQUEST() {
        return null;
    }

    public byte getOp() {
        return op;
    }

    public void setOp(byte op) {
        this.op = op;
    }

    public byte getH_type() {
        return h_type;
    }

    public void setH_type(byte h_type) {
        this.h_type = h_type;
    }

    public byte getH_len() {
        return h_len;
    }

    public void setH_len(byte h_len) {
        this.h_len = h_len;
    }

    public byte getH_ops() {
        return h_ops;
    }

    public void setH_ops(byte h_ops) {
        this.h_ops = h_ops;
    }

    public byte[] getXid() {
        return xid;
    }

    public void setXid(byte[] xid) {
        this.xid = xid;
    }

    public byte[] getSecs() {
        return secs;
    }

    public void setSecs(byte[] secs) {
        this.secs = secs;
    }

    public byte[] getFlags() {
        return flags;
    }

    public void setFlags(byte[] flags) {
        this.flags = flags;
    }

    public byte[] getCiaddr() {
        return ciaddr;
    }

    public void setCiaddr(byte[] ciaddr) {
        this.ciaddr = ciaddr;
    }

    public byte[] getYiaddr() {
        return yiaddr;
    }

    public void setYiaddr(byte[] yiaddr) {
        this.yiaddr = yiaddr;
    }

    public byte[] getSiaddr() {
        return siaddr;
    }

    public void setSiaddr(byte[] siaddr) {
        this.siaddr = siaddr;
    }

    public byte[] getGiaddr() {
        return giaddr;
    }

    public void setGiaddr(byte[] giaddr) {
        this.giaddr = giaddr;
    }

    public byte[] getChaddr() {
        return chaddr;
    }

    public void setChaddr(byte[] chaddr) {
        this.chaddr = chaddr;
    }

    public byte[] getSname() {
        return sname;
    }

    public void setSname(byte[] sname) {
        this.sname = sname;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getOptions() {
        return options;
    }

    public void setOptions(byte[] options) {
        this.options = options;
    }

}

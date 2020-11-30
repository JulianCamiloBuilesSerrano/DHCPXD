package dhcpxd;

public class Red {

    /**
     * Atributos
     */
    private String mascaraDeRed;
    private String ipInicial;
    private String ipFinal;
    private String getaway;
    private String DNS;
    private int tiempoPrestamo;

    /**
     * Constructores
     */
    public Red() {
    }

    public Red(String mascaraDeRed, String ipInicial, String ipFinal, String getaway, String dNS) {
        this.mascaraDeRed = mascaraDeRed;
        this.ipInicial = ipInicial;
        this.ipFinal = ipFinal;
        this.getaway = getaway;
        this.DNS = dNS;
    }

    /**
     * Métodos
     */

    public String getMascaraDeRed() {
        return mascaraDeRed;
    }

    public void setMascaraDeRed(String mascaraDeRed) {
        this.mascaraDeRed = mascaraDeRed;
    }

    public String getIpInicial() {
        return ipInicial;
    }

    public void setIpInicial(String ipInicial) {
        this.ipInicial = ipInicial;
    }

    public String getIpFinal() {
        return ipFinal;
    }

    public void setIpFinal(String ipFinal) {
        this.ipFinal = ipFinal;
    }

    public String getGetaway() {
        return getaway;
    }

    public void setGetaway(String getaway) {
        this.getaway = getaway;
    }

    public String getDNS() {
        return DNS;
    }

    public void setDNS(String dNS) {
        DNS = dNS;
    }

    public int getTiempoPrestamo() {
        return tiempoPrestamo;
    }

    public void setTiempoPrestamo(int tiempoPrestamo) {
        this.tiempoPrestamo = tiempoPrestamo;
    }

    @Override
    public String toString() {
        return "Ip inicial: "+this.ipInicial+ ", Ip final: " +this.ipFinal+", DNS: "+this.DNS+", GetWay: "+this.getaway+ ", MAscara: "+this.mascaraDeRed; //To change body of generated methods, choose Tools | Templates.
    }
   

}

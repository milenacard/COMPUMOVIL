package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo;

/**
 * Created by Daniel on 07/06/2017.
 */

public class Chat {
    private String iniciador;
    private String interesIniciador;
    private String receptor;
    private String interesReceptor;

    public Chat(String iniciador, String interesIniciador, String receptor, String interesReceptor) {
        this.iniciador = iniciador;
        this.interesIniciador = interesIniciador;
        this.receptor = receptor;
        this.interesReceptor = interesReceptor;
    }

    public Chat() {
    }

    public String getIniciador() {
        return iniciador;
    }

    public void setIniciador(String iniciador) {
        this.iniciador = iniciador;
    }

    public String getInteresIniciador() {
        return interesIniciador;
    }

    public void setInteresIniciador(String interesIniciador) {
        this.interesIniciador = interesIniciador;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getInteresReceptor() {
        return interesReceptor;
    }

    public void setInteresReceptor(String interesReceptor) {
        this.interesReceptor = interesReceptor;
    }
}

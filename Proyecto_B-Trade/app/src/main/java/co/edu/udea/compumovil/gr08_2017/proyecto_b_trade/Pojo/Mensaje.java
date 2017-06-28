package co.edu.udea.compumovil.gr08_2017.proyecto_b_trade.Pojo;

/**
 * Created by Daniel on 01/06/2017.
 */

public class Mensaje {

    private String emisor;
    private String chat;
    private String texto;

    public Mensaje() {
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Mensaje(String emisor, String chat, String texto) {

        this.emisor = emisor;
        this.chat = chat;
        this.texto = texto;

    }
}

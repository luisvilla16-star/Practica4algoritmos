package org.example.pratica4.Logica;

import java.util.ArrayList;
import org.example.pratica4.DeckOfCards.CartaInglesa;

public class Movimientos {

    private int origen;
    private int destino;
    private CartaInglesa carta;
    private String tipoOrigen;
    private String tipoDestino;

    // Cartas que se movieron juntas
    private ArrayList<CartaInglesa> cartasMovidas;

    public Movimientos(int origen, int destino, CartaInglesa carta,
                       String tipoOrigen, String tipoDestino) {

        this.origen = origen;
        this.destino = destino;
        this.carta = carta;
        this.tipoOrigen = tipoOrigen;
        this.tipoDestino = tipoDestino;

        cartasMovidas = new ArrayList<>();
        cartasMovidas.add(carta);
    }

    // Constructor para movimientos de varias cartas
    public Movimientos(int origen, int destino,
                       ArrayList<CartaInglesa> cartas,
                       String tipoOrigen, String tipoDestino) {

        this.origen = origen;
        this.destino = destino;
        this.tipoOrigen = tipoOrigen;
        this.tipoDestino = tipoDestino;

        cartasMovidas = new ArrayList<>(cartas);
        carta = cartas.get(0);
    }

    public int getOrigen() {
        return origen;
    }

    public void setOrigen(int origen) {
        this.origen = origen;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public CartaInglesa getCarta() {
        return carta;
    }

    public void setCarta(CartaInglesa carta) {
        this.carta = carta;
    }

    public String getTipoOrigen() {
        return tipoOrigen;
    }

    public void setTipoOrigen(String tipoOrigen) {
        this.tipoOrigen = tipoOrigen;
    }

    public String getTipoDestino() {
        return tipoDestino;
    }

    public void setTipoDestino(String tipoDestino) {
        this.tipoDestino = tipoDestino;
    }

    public ArrayList<CartaInglesa> getCartasMovidas() {
        return cartasMovidas;
    }
}
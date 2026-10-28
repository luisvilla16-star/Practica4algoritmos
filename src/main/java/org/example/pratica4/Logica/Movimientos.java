package org.example.pratica4.Logica;

import org.example.pratica4.DeckOfCards.CartaInglesa;

public class Movimientos {
    private int origen;
    private int destino;
    private CartaInglesa carta;
    private String tipoOrigen;
    private String tipoDestino;

    public Movimientos(int origen, int destino, CartaInglesa carta,
                       String tipoOrigen, String tipoDestino) {
        this.origen = origen;
        this.destino = destino;
        this.carta = carta;
        this.tipoOrigen = tipoOrigen;
        this.tipoDestino = tipoDestino;
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
}

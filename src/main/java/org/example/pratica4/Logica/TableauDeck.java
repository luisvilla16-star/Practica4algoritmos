package org.example.pratica4.Logica;

import java.util.ArrayList;
import org.example.pratica4.DeckOfCards.CartaInglesa;

public class TableauDeck {

    private ListaSimple<CartaInglesa> cartas;
    private CartaInglesa ultima;

    public TableauDeck() {
        cartas = new ListaSimple<>();
        ultima = null;
    }

    public void inicializar(CartaInglesa carta) {
        cartas.insetarFin(carta);
        ultima = carta;
    }

    public boolean sePuedeAgregarCarta(CartaInglesa carta) {

        if (carta == null) {
            return false;
        }

        if (cartas.getCantidad() == 0) {
            return carta.getValorBajo() == 13;
        }

        CartaInglesa ultimaCarta = verUltimaCarta();

        return ultimaCarta.getPalo() == carta.getPalo()
                && ultimaCarta.getValorBajo()
                == carta.getValorBajo() + 1;
    }

    public boolean agregarCarta(CartaInglesa carta) {

        if (!sePuedeAgregarCarta(carta)) {
            return false;
        }

        cartas.insetarFin(carta);
        ultima = carta;

        return true;
    }

    public void restaurarCarta(CartaInglesa carta) {
        if (carta != null) {
            cartas.insetarFin(carta);
            ultima = carta;
        }
    }

    public CartaInglesa verUltimaCarta() {
        return cartas.verUltimo();
    }

    public CartaInglesa removerUltimaCarta() {

        CartaInglesa carta = cartas.eliminarFin();

        ultima = cartas.verUltimo();

        return carta;
    }

    public boolean isEmpty() {
        return cartas.getCantidad() == 0;
    }

    public CartaInglesa getUltimaCarta() {
        return verUltimaCarta();
    }

    public ListaSimple<CartaInglesa> getCards() {
        return cartas;
    }

    public int getCantidad() {
        return cartas.getCantidad();
    }

    public CartaInglesa obtenerCarta(int posicion) {
        return cartas.obtener(posicion);
    }

    public void limpiar() {
        cartas = new ListaSimple<>();
        ultima = null;
    }

    // Comprobar si las cartas seleccionadas están ordenadas
    public boolean secuenciaValida(int inicio) {

        if (inicio < 0 || inicio >= getCantidad()) {
            return false;
        }

        for (int i = inicio; i < getCantidad() - 1; i++) {

            CartaInglesa inferior = cartas.obtener(i);
            CartaInglesa superior = cartas.obtener(i + 1);

            if (inferior.getPalo() != superior.getPalo()
                    || inferior.getValorBajo()
                    != superior.getValorBajo() + 1) {
                return false;
            }
        }

        return true;
    }

    // Obtener las cartas seleccionadas y las que están encima
    public ArrayList<CartaInglesa> obtenerSecuencia(int inicio) {

        ArrayList<CartaInglesa> secuencia = new ArrayList<>();

        if (inicio < 0 || inicio >= getCantidad()) {
            return secuencia;
        }

        for (int i = inicio; i < getCantidad(); i++) {
            secuencia.add(cartas.obtener(i));
        }

        return secuencia;
    }

    // Retirar una secuencia completa
    public ArrayList<CartaInglesa> removerSecuencia(int inicio) {

        ArrayList<CartaInglesa> secuencia = obtenerSecuencia(inicio);

        if (secuencia.isEmpty()) {
            return secuencia;
        }

        while (getCantidad() > inicio) {
            removerUltimaCarta();
        }

        return secuencia;
    }
}

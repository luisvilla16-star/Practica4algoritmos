package org.example.pratica4.Logica;

import org.example.pratica4.DeckOfCards.CartaInglesa;

public class TableauDeck {

    private ListaSimple<CartaInglesa> cartas = new ListaSimple<>();
    private CartaInglesa ultima;

    // Colocar las cartas al iniciar el juego
    public void inicializar(CartaInglesa carta) {
        carta.makeFaceUp();
        cartas.insetarFin(carta);
        ultima = carta;
    }

    // Revisar si se puede agregar una carta
    public boolean sePuedeAgregarCarta(CartaInglesa carta) {
        if (carta == null) {
            return false;
        }

        // Si la columna esta vacia, solo acepta un rey
        if (ultima == null) {
            return carta.getValorBajo() == 13;
        }

        // Deben ser del mismo palo y en orden descendente
        return ultima.getPalo() == carta.getPalo()
                && ultima.getValorBajo() == carta.getValorBajo() + 1;
    }

    // Agregar una carta respetando las reglas del juego
    public boolean agregarCarta(CartaInglesa carta) {
        if (!sePuedeAgregarCarta(carta)) {
            return false;
        }

        carta.makeFaceUp();
        cartas.insetarFin(carta);
        ultima = carta;
        return true;
    }

    // Restaurar una carta al deshacer, sin revisar las reglas
    public void restaurarCarta(CartaInglesa carta) {
        if (carta == null) {
            return;
        }

        carta.makeFaceUp();
        cartas.insetarFin(carta);
        ultima = carta;
    }

    // Ver la ultima carta sin eliminarla
    public CartaInglesa verUltimaCarta() {
        return ultima;
    }

    // Eliminar la ultima carta
    public CartaInglesa removerUltimaCarta() {
        if (ultima == null) {
            return null;
        }

        CartaInglesa carta = cartas.eliminarFin();
        ultima = cartas.verUltimo();
        return carta;
    }

    // Comprobar si la columna esta vacia
    public boolean isEmpty() {
        return ultima == null;
    }

    // Obtener la ultima carta
    public CartaInglesa getUltimaCarta() {
        return ultima;
    }

    // Obtener la lista de cartas
    public ListaSimple<CartaInglesa> getCards() {
        return cartas;
    }

    // Obtener la cantidad de cartas
    public int getCantidad() {
        return cartas.getCantidad();
    }

    // Obtener una carta por su posicion
    public CartaInglesa obtenerCarta(int posicion) {
        return cartas.obtener(posicion);
    }

    // Limpiar la columna
    public void limpiar() {
        cartas = new ListaSimple<>();
        ultima = null;
    }
}

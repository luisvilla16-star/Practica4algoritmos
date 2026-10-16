
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

        // Si la columna esta vacia
        if (ultima == null) {
            return true;
        }

        // Deben ser del mismo palo y en orden descendente
        if (ultima.getPalo() == carta.getPalo()) {
            if (ultima.getValor() == carta.getValor() + 1) {
                return true;
            }
        }

        return false;
    }

    // Agregar una carta a la columna
    public boolean agregarCarta(CartaInglesa carta) {

        if (sePuedeAgregarCarta(carta)) {
            carta.makeFaceUp();
            cartas.insetarFin(carta);
            ultima = carta;
            return true;
        }

        return false;
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

        // Actualizar la ultima carta
        ultima = cartas.eliminarFin();

        if (ultima != null) {
            cartas.insetarFin(ultima);
        }

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
}

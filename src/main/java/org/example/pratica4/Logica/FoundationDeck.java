
package org.example.pratica4.Logica;

import org.example.pratica4.DeckOfCards.CartaInglesa;
import org.example.pratica4.DeckOfCards.Palo;

public class FoundationDeck {

    private Palo palo;
    private ListaSimple<CartaInglesa> cartas = new ListaSimple<>();
    private CartaInglesa ultima;
    private int cantidad = 0;

    // Crear un monton indicando el palo
    public FoundationDeck(Palo palo) {
        this.palo = palo;
    }

    // Crear un monton con una carta
    public FoundationDeck(CartaInglesa carta) {

        palo = carta.getPalo();

        if (carta.getValorBajo() == 1) {
            cartas.insetarFin(carta);
            ultima = carta;
            cantidad++;
        }
    }

    // Agregar una carta al monton
    public boolean agregarCarta(CartaInglesa carta) {

        boolean agregado = false;

        if (carta != null && carta.tieneElMismoPalo(palo)) {

            if (ultima == null) {

                // Si esta vacio, solo acepta un As
                if (carta.getValorBajo() == 1) {

                    cartas.insetarFin(carta);
                    ultima = carta;
                    cantidad++;
                    agregado = true;
                }

            } else {

                // La siguiente carta debe ser mayor por uno
                if (ultima.getValorBajo() + 1 ==
                        carta.getValorBajo()) {

                    cartas.insetarFin(carta);
                    ultima = carta;
                    cantidad++;
                    agregado = true;
                }
            }
        }

        return agregado;
    }

    // Eliminar la ultima carta
    public CartaInglesa removerUltimaCarta() {

        if (cantidad == 0) {
            return null;
        }

        CartaInglesa carta = cartas.eliminarFin();
        cantidad--;

        if (cantidad == 0) {
            ultima = null;
        } else {
            ultima = cartas.eliminarFin();
            cartas.insetarFin(ultima);
        }

        return carta;
    }

    // Revisar si el monton esta vacio
    public boolean estaVacio() {
        return cantidad == 0;
    }

    // Obtener la ultima carta sin eliminarla
    public CartaInglesa getUltimaCarta() {
        return ultima;
    }

    // Mostrar las cartas
    @Override
    public String toString() {

        if (cantidad == 0) {
            return "---";
        }

        String resultado = "";

        for (int i = 0; i < cantidad; i++) {

            CartaInglesa carta = cartas.eliminarInicio();

            resultado += carta + " ";

            cartas.insetarFin(carta);
        }

        return resultado;
    }
}

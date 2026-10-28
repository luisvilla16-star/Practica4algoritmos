package org.example.pratica4.Logica;

import org.example.pratica4.DeckOfCards.CartaInglesa;
import org.example.pratica4.DeckOfCards.Palo;

public class FoundationDeck {

    private Palo palo;
    private ListaSimple<CartaInglesa> cartas = new ListaSimple<>();
    private CartaInglesa ultima;
    private int cantidad = 0;

    // Crear una fundacion indicando el palo
    public FoundationDeck(Palo palo) {
        this.palo = palo;
    }

    // Crear una fundacion con un As
    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();

        if (carta.getValorBajo() == 1) {
            cartas.insetarFin(carta);
            ultima = carta;
            cantidad++;
        }
    }

    // Agregar una carta respetando las reglas del juego
    public boolean agregarCarta(CartaInglesa carta) {
        if (carta == null || !carta.tieneElMismoPalo(palo)) {
            return false;
        }

        if (ultima == null) {
            if (carta.getValorBajo() != 1) {
                return false;
            }
        } else if (ultima.getValorBajo() + 1 != carta.getValorBajo()) {
            return false;
        }

        cartas.insetarFin(carta);
        ultima = carta;
        cantidad++;
        return true;
    }

    // Restaurar una carta al deshacer, sin revisar las reglas
    public void restaurarCarta(CartaInglesa carta) {
        if (carta == null) {
            return;
        }

        cartas.insetarFin(carta);
        ultima = carta;
        cantidad++;
    }

    // Eliminar la ultima carta
    public CartaInglesa removerUltimaCarta() {
        if (cantidad == 0) {
            return null;
        }

        CartaInglesa carta = cartas.eliminarFin();
        cantidad--;
        ultima = cartas.verUltimo();
        return carta;
    }

    // Revisar si la fundacion esta vacia
    public boolean estaVacio() {
        return cantidad == 0;
    }

    // Obtener la ultima carta sin eliminarla
    public CartaInglesa getUltimaCarta() {
        return ultima;
    }

    // Obtener la cantidad de cartas
    public int getCantidad() {
        return cantidad;
    }

    // Limpiar la fundacion
    public void limpiar() {
        cartas = new ListaSimple<>();
        ultima = null;
        cantidad = 0;
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

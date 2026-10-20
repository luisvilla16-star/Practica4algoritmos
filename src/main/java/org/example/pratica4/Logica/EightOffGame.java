
package org.example.pratica4.Logica;

import org.example.pratica4.DeckOfCards.CartaInglesa;
import org.example.pratica4.DeckOfCards.Mazo;
import org.example.pratica4.DeckOfCards.Palo;

public class EightOffGame {

    private Mazo mazo;
    private TableauDeck[] columnas;
    private FoundationDeck[] fundaciones;
    private CartaInglesa[] reservas;
    private Pila<Movimientos> historial;

    public EightOffGame() {
        columnas = new TableauDeck[8];
        fundaciones = new FoundationDeck[4];
        reservas = new CartaInglesa[8];
        historial = new Pila<>(100);

        for (int i = 0; i < 8; i++) {
            columnas[i] = new TableauDeck();
        }

        for (int i = 0; i < 4; i++) {
            fundaciones[i] = new FoundationDeck(Palo.values()[i]);
        }
    }

    // Iniciar el juego
    public void iniciarJuego() {
        historial = new Pila<>(100);

        for (int i = 0; i < 8; i++) {
            columnas[i].limpiar();
        }

        for (int i = 0; i < 4; i++) {
            fundaciones[i].limpiar();
        }

        for (int i = 0; i < 8; i++) {
            reservas[i] = null;
        }

        mazo = new Mazo();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                CartaInglesa carta = mazo.obtenerUnaCarta();

                if (carta != null) {
                    columnas[i].inicializar(carta);
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            reservas[i] = mazo.obtenerUnaCarta();
        }
    }

    // Buscar una reserva vacía
    public int buscarReservaVacia() {
        for (int i = 0; i < 8; i++) {
            if (reservas[i] == null) {
                return i;
            }
        }

        return -1;
    }

    // Obtener una columna
    public TableauDeck obtenerColumna(int posicion) {
        if (posicion < 0 || posicion >= 8) {
            return null;
        }

        return columnas[posicion];
    }

    // Mover una carta entre columnas
    public boolean moverEntreColumnas(int origen, int destino) {
        if (origen < 0 || origen >= 8 ||
                destino < 0 || destino >= 8 ||
                origen == destino || historial.llena()) {
            return false;
        }

        CartaInglesa carta = columnas[origen].verUltimaCarta();

        if (carta == null) {
            return false;
        }

        if (!columnas[destino].sePuedeAgregarCarta(carta)) {
            return false;
        }

        if (columnas[destino].agregarCarta(carta)) {
            columnas[origen].removerUltimaCarta();

            historial.Push(new Movimientos(
                    origen, destino, carta, "columna", "columna"));

            return true;
        }

        return false;
    }

    // Mover una carta de la reserva a una columna
    public boolean moverDeReserva(int reserva, int columna) {
        if (reserva < 0 || reserva >= 8 ||
                columna < 0 || columna >= 8 ||
                historial.llena()) {
            return false;
        }

        CartaInglesa carta = reservas[reserva];

        if (carta == null) {
            return false;
        }

        if (columnas[columna].agregarCarta(carta)) {
            reservas[reserva] = null;

            historial.Push(new Movimientos(
                    reserva, columna, carta, "reserva", "columna"));

            return true;
        }

        return false;
    }

    // Mover una carta de una columna a una reserva
    public boolean moverAReserva(int columna) {
        if (columna < 0 || columna >= 8 || historial.llena()) {
            return false;
        }

        int reserva = buscarReservaVacia();

        if (reserva == -1) {
            return false;
        }

        CartaInglesa carta = columnas[columna].verUltimaCarta();

        if (carta == null) {
            return false;
        }

        columnas[columna].removerUltimaCarta();
        reservas[reserva] = carta;

        historial.Push(new Movimientos(
                columna, reserva, carta, "columna", "reserva"));

        return true;
    }

    // Mover una carta de una columna a su fundación
    public boolean moverAFundacion(int columna) {
        if (columna < 0 || columna >= 8 || historial.llena()) {
            return false;
        }

        CartaInglesa carta = columnas[columna].verUltimaCarta();

        if (carta == null) {
            return false;
        }

        int posicion = carta.getPalo().ordinal();

        if (fundaciones[posicion].agregarCarta(carta)) {
            columnas[columna].removerUltimaCarta();

            historial.Push(new Movimientos(
                    columna, posicion, carta, "columna", "fundacion"));

            return true;
        }

        return false;
    }

    // Mover una carta de la reserva a su fundación
    public boolean moverReservaAFundacion(int reserva) {
        if (reserva < 0 || reserva >= 8 || historial.llena()) {
            return false;
        }

        CartaInglesa carta = reservas[reserva];

        if (carta == null) {
            return false;
        }

        int posicion = carta.getPalo().ordinal();

        if (fundaciones[posicion].agregarCarta(carta)) {
            reservas[reserva] = null;

            historial.Push(new Movimientos(
                    reserva, posicion, carta, "reserva", "fundacion"));

            return true;
        }

        return false;
    }

    // Deshacer el último movimiento
    public boolean deshacerMovimiento() {
        if (historial.vacia()) {
            return false;
        }

        Movimientos movimiento = historial.Pop();

        int origen = movimiento.getOrigen();
        int destino = movimiento.getDestino();
        CartaInglesa carta = movimiento.getCarta();

        String tipoOrigen = movimiento.getTipoOrigen();
        String tipoDestino = movimiento.getTipoDestino();

        if (tipoDestino.equals("columna")) {
            columnas[destino].removerUltimaCarta();
        } else if (tipoDestino.equals("reserva")) {
            reservas[destino] = null;
        } else if (tipoDestino.equals("fundacion")) {
            fundaciones[destino].removerUltimaCarta();
        }

        if (tipoOrigen.equals("columna")) {
            columnas[origen].inicializar(carta);
        } else if (tipoOrigen.equals("reserva")) {
            reservas[origen] = carta;
        }

        return true;
    }

    // Obtener una carta de la reserva
    public CartaInglesa obtenerReserva(int posicion) {
        if (posicion < 0 || posicion >= 8) {
            return null;
        }

        return reservas[posicion];
    }

    // Obtener una fundación
    public FoundationDeck obtenerFundacion(int posicion) {
        if (posicion < 0 || posicion >= 4) {
            return null;
        }

        return fundaciones[posicion];
    }

    // Mostrar una pista
    public String darPista() {
        CartaInglesa carta;
        CartaInglesa ultima;
        int posicion;

        // Revisar las columnas
        for (int i = 0; i < 8; i++) {
            carta = columnas[i].verUltimaCarta();

            if (carta != null) {
                posicion = carta.getPalo().ordinal();
                ultima = fundaciones[posicion].getUltimaCarta();

                if (ultima == null && carta.getValorBajo() == 1) {
                    return "Mueve la carta de la columna "
                            + (i + 1) + " a la fundacion.";
                }

                if (ultima != null &&
                        ultima.getValorBajo() + 1 == carta.getValorBajo()) {
                    return "Mueve la carta de la columna "
                            + (i + 1) + " a la fundacion.";
                }
            }
        }

        // Revisar las reservas
        for (int i = 0; i < 8; i++) {
            carta = reservas[i];

            if (carta != null) {
                posicion = carta.getPalo().ordinal();
                ultima = fundaciones[posicion].getUltimaCarta();

                if (ultima == null && carta.getValorBajo() == 1) {
                    return "Mueve la reserva "
                            + (i + 1) + " a la fundacion.";
                }

                if (ultima != null &&
                        ultima.getValorBajo() + 1 == carta.getValorBajo()) {
                    return "Mueve la reserva "
                            + (i + 1) + " a la fundacion.";
                }
            }
        }

        // Revisar movimientos entre columnas
        for (int i = 0; i < 8; i++) {
            carta = columnas[i].verUltimaCarta();

            if (carta != null) {
                for (int j = 0; j < 8; j++) {
                    if (i != j &&
                            columnas[j].sePuedeAgregarCarta(carta)) {
                        return "Mueve la columna "
                                + (i + 1) + " a la columna " + (j + 1) + ".";
                    }
                }
            }
        }

        // Revisar movimientos de las reservas
        for (int i = 0; i < 8; i++) {
            if (reservas[i] != null) {
                for (int j = 0; j < 8; j++) {
                    if (columnas[j].sePuedeAgregarCarta(reservas[i])) {
                        return "Mueve la reserva " + (i + 1)
                                + " a la columna " + (j + 1) + ".";
                    }
                }
            }
        }

        return "No se encontraron movimientos disponibles.";
    }

    // Verificar si el jugador ganó
    public boolean verificarVictoria() {
        int total = 0;

        for (int i = 0; i < 4; i++) {
            total = total + fundaciones[i].getCantidad();
        }

        return total == 52;
    }
}

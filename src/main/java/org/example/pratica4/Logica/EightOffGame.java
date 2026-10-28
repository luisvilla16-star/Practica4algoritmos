
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

    // Iniciar una partida nueva
    public void iniciarJuego() {

        mazo = new Mazo();

        for (int i = 0; i < 8; i++) {
            columnas[i].limpiar();
        }

        for (int i = 0; i < 4; i++) {
            fundaciones[i].limpiar();
        }

        for (int i = 0; i < 8; i++) {
            reservas[i] = null;
        }

        historial = new Pila<>(100);

        // Repartir seis cartas a cada columna
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {

                CartaInglesa carta = mazo.obtenerUnaCarta();

                if (carta != null) {
                    columnas[i].inicializar(carta);
                }
            }
        }

        // Repartir cuatro cartas a las reservas
        for (int i = 0; i < 4; i++) {

            CartaInglesa carta = mazo.obtenerUnaCarta();

            if (carta != null) {
                carta.makeFaceUp();
                reservas[i] = carta;
            }
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

    // Mover una carta de una columna a otra
    public boolean moverEntreColumnas(int origen, int destino) {

        if (historial.llena()) {
            return false;
        }

        if (origen < 0 || origen >= 8
                || destino < 0 || destino >= 8
                || origen == destino) {
            return false;
        }

        CartaInglesa carta = columnas[origen].verUltimaCarta();

        if (carta == null
                || !columnas[destino].sePuedeAgregarCarta(carta)) {
            return false;
        }

        CartaInglesa movida = columnas[origen].removerUltimaCarta();

        if (movida == null) {
            return false;
        }

        if (!columnas[destino].agregarCarta(movida)) {
            columnas[origen].restaurarCarta(movida);
            return false;
        }

        historial.Push(new Movimientos(
                origen, destino, movida, "columna", "columna"
        ));

        return true;
    }

    // Mover una carta de una reserva a una columna
    public boolean moverDeReserva(int reserva, int columna) {

        if (historial.llena()) {
            return false;
        }

        if (reserva < 0 || reserva >= 8
                || columna < 0 || columna >= 8) {
            return false;
        }

        CartaInglesa carta = reservas[reserva];

        if (carta == null
                || !columnas[columna].sePuedeAgregarCarta(carta)) {
            return false;
        }

        if (!columnas[columna].agregarCarta(carta)) {
            return false;
        }

        reservas[reserva] = null;

        historial.Push(new Movimientos(
                reserva, columna, carta, "reserva", "columna"
        ));

        return true;
    }

    // Mover una carta de una columna a una reserva
    public boolean moverAReserva(int columna) {

        if (historial.llena()) {
            return false;
        }

        if (columna < 0 || columna >= 8) {
            return false;
        }

        int reserva = buscarReservaVacia();

        if (reserva == -1) {
            return false;
        }

        CartaInglesa carta = columnas[columna].removerUltimaCarta();

        if (carta == null) {
            return false;
        }

        reservas[reserva] = carta;

        historial.Push(new Movimientos(
                columna, reserva, carta, "columna", "reserva"
        ));

        return true;
    }

    // Mover una carta de una columna a su fundación
    public boolean moverAFundacion(int columna) {

        if (historial.llena()) {
            return false;
        }

        if (columna < 0 || columna >= 8) {
            return false;
        }

        CartaInglesa carta = columnas[columna].verUltimaCarta();

        if (carta == null) {
            return false;
        }

        int posicion = carta.getPalo().ordinal();

        if (!fundaciones[posicion].agregarCarta(carta)) {
            return false;
        }

        columnas[columna].removerUltimaCarta();

        historial.Push(new Movimientos(
                columna, posicion, carta, "columna", "fundacion"
        ));

        return true;
    }

    // Mover una carta de una reserva a su fundación
    public boolean moverReservaAFundacion(int reserva) {

        if (historial.llena()) {
            return false;
        }

        if (reserva < 0 || reserva >= 8) {
            return false;
        }

        CartaInglesa carta = reservas[reserva];

        if (carta == null) {
            return false;
        }

        int posicion = carta.getPalo().ordinal();

        if (!fundaciones[posicion].agregarCarta(carta)) {
            return false;
        }

        reservas[reserva] = null;

        historial.Push(new Movimientos(
                reserva, posicion, carta, "reserva", "fundacion"
        ));

        return true;
    }

    // DESHACER EL ÚLTIMO MOVIMIENTO
    // Regresa la carta sin comprobar las reglas del juego
    public boolean deshacerMovimiento() {

        if (historial.vacia()) {
            return false;
        }

        Movimientos movimiento = historial.Pop();

        CartaInglesa carta = movimiento.getCarta();

        int origen = movimiento.getOrigen();
        int destino = movimiento.getDestino();

        String tipoOrigen = movimiento.getTipoOrigen();
        String tipoDestino = movimiento.getTipoDestino();

        // Columna a columna
        if (tipoOrigen.equals("columna")
                && tipoDestino.equals("columna")) {

            columnas[destino].removerUltimaCarta();
            columnas[origen].restaurarCarta(carta);
        }

        // Reserva a columna
        else if (tipoOrigen.equals("reserva")
                && tipoDestino.equals("columna")) {

            columnas[destino].removerUltimaCarta();
            reservas[origen] = carta;
        }

        // Columna a reserva
        else if (tipoOrigen.equals("columna")
                && tipoDestino.equals("reserva")) {

            reservas[destino] = null;
            columnas[origen].restaurarCarta(carta);
        }

        // Columna a fundación
        else if (tipoOrigen.equals("columna")
                && tipoDestino.equals("fundacion")) {

            fundaciones[destino].removerUltimaCarta();
            columnas[origen].restaurarCarta(carta);
        }

        // Reserva a fundación
        else if (tipoOrigen.equals("reserva")
                && tipoDestino.equals("fundacion")) {

            fundaciones[destino].removerUltimaCarta();
            reservas[origen] = carta;
        }

        else {
            historial.Push(movimiento);
            return false;
        }

        return true;
    }

    // Obtener una carta de una reserva
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

    // Dar una pista
    public String darPista() {

        // Buscar cartas que puedan ir a las fundaciones
        for (int i = 0; i < 8; i++) {

            CartaInglesa carta = columnas[i].verUltimaCarta();

            if (carta != null) {

                FoundationDeck fundacion =
                        fundaciones[carta.getPalo().ordinal()];

                CartaInglesa ultima = fundacion.getUltimaCarta();

                if ((ultima == null && carta.getValorBajo() == 1)
                        || (ultima != null
                        && ultima.getValorBajo() + 1
                        == carta.getValorBajo())) {

                    return "Mueve " + carta + " de la columna "
                            + (i + 1) + " a su fundación.";
                }
            }
        }

        // Buscar cartas de las reservas que puedan ir a las fundaciones
        for (int i = 0; i < 8; i++) {

            CartaInglesa carta = reservas[i];

            if (carta != null) {

                FoundationDeck fundacion =
                        fundaciones[carta.getPalo().ordinal()];

                CartaInglesa ultima = fundacion.getUltimaCarta();

                if ((ultima == null && carta.getValorBajo() == 1)
                        || (ultima != null
                        && ultima.getValorBajo() + 1
                        == carta.getValorBajo())) {

                    return "Mueve " + carta + " de la reserva "
                            + (i + 1) + " a su fundación.";
                }
            }
        }

        // Buscar movimientos de una reserva a una columna
        for (int i = 0; i < 8; i++) {

            CartaInglesa carta = reservas[i];

            if (carta != null) {

                for (int j = 0; j < 8; j++) {

                    if (columnas[j].sePuedeAgregarCarta(carta)) {
                        return "Mueve " + carta + " de la reserva "
                                + (i + 1) + " a la columna " + (j + 1) + ".";
                    }
                }
            }
        }

        // Buscar movimientos entre columnas
        for (int i = 0; i < 8; i++) {

            CartaInglesa carta = columnas[i].verUltimaCarta();

            if (carta != null) {

                for (int j = 0; j < 8; j++) {

                    if (i != j
                            && columnas[j].sePuedeAgregarCarta(carta)) {

                        return "Mueve " + carta + " de la columna "
                                + (i + 1) + " a la columna " + (j + 1) + ".";
                    }
                }
            }
        }

        // Buscar una carta que se pueda guardar en una reserva
        if (buscarReservaVacia() != -1) {

            for (int i = 0; i < 8; i++) {

                CartaInglesa carta = columnas[i].verUltimaCarta();

                if (carta != null) {
                    return "Puedes guardar " + carta + " de la columna "
                            + (i + 1) + " en una reserva vacía.";
                }
            }
        }

        return "No encontré movimientos disponibles. "
                + "Intenta deshacer un movimiento o iniciar otra partida.";
    }

    // Comprobar si se ganó el juego
    public boolean verificarVictoria() {

        int total = 0;

        for (int i = 0; i < 4; i++) {
            total = total + fundaciones[i].getCantidad();
        }

        return total == 52;
    }
}

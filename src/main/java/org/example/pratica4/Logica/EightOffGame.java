package org.example.pratica4.Logica;

import java.util.ArrayList;

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
        historial = new Pila<>(500);

        for (int i = 0; i < 8; i++) {
            columnas[i] = new TableauDeck();
        }

        for (int i = 0; i < 4; i++) {
            fundaciones[i] = new FoundationDeck(Palo.values()[i]);
        }
    }

    public void iniciarJuego() {

        historial = new Pila<>(100);
        mazo = new Mazo();

        for (int i = 0; i < 8; i++) {
            columnas[i].limpiar();
            reservas[i] = null;
        }

        for (int i = 0; i < 4; i++) {
            fundaciones[i].limpiar();
        }

        repartirColumnas();
        repartirReservas();
    }

    private void repartirColumnas() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {

                CartaInglesa carta = mazo.obtenerUnaCarta();

                if (carta != null) {
                    columnas[i].inicializar(carta);
                }
            }
        }
    }

    private void repartirReservas() {

        for (int i = 0; i < 4; i++) {

            CartaInglesa carta = mazo.obtenerUnaCarta();

            if (carta != null) {
                carta.makeFaceUp();
                reservas[i] = carta;
            }
        }
    }

    public int buscarReservaVacia() {

        for (int i = 0; i < 8; i++) {
            if (reservas[i] == null) {
                return i;
            }
        }

        return -1;
    }

    public TableauDeck obtenerColumna(int posicion) {

        if (posicion < 0 || posicion >= 8) {
            return null;
        }

        return columnas[posicion];
    }

    public boolean moverEntreColumnas(int origen, int destino) {

        if (origen < 0 || origen >= 8 || destino < 0 || destino >= 8) {
            return false;
        }

        return moverSecuenciaEntreColumnas(
                origen, destino, columnas[origen].getCantidad() - 1
        );
    }

    public boolean moverSecuenciaEntreColumnas(int origen, int destino, int inicio) {

        if (origen < 0 || origen >= 8 || destino < 0 || destino >= 8
                || origen == destino || historial.llena()) {
            return false;
        }

        TableauDeck columnaOrigen = columnas[origen];
        TableauDeck columnaDestino = columnas[destino];

        if (inicio < 0 || inicio >= columnaOrigen.getCantidad()) {
            return false;
        }

        if (!columnaOrigen.secuenciaValida(inicio)) {
            return false;
        }

        CartaInglesa primera = columnaOrigen.obtenerCarta(inicio);

        if (!columnaDestino.sePuedeAgregarCarta(primera)) {
            return false;
        }

        ArrayList<CartaInglesa> secuencia =
                columnaOrigen.obtenerSecuencia(inicio);

        Movimientos movimiento = new Movimientos(
                origen, destino, secuencia, "columna", "columna"
        );

        columnaOrigen.removerSecuencia(inicio);

        for (int i = 0; i < secuencia.size(); i++) {
            columnaDestino.restaurarCarta(secuencia.get(i));
        }

        historial.Push(movimiento);

        return true;
    }

    public boolean moverDeReserva(int reserva, int columna) {

        if (reserva < 0 || reserva >= 8 || columna < 0 || columna >= 8
                || historial.llena()) {
            return false;
        }

        CartaInglesa carta = reservas[reserva];

        if (carta == null) {
            return false;
        }

        if (!columnas[columna].sePuedeAgregarCarta(carta)) {
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

    public boolean moverDeReservaAColumna(int reserva, int columna) {
        return moverDeReserva(reserva, columna);
    }

    public boolean moverAReserva(int columna) {

        if (columna < 0 || columna >= 8 || historial.llena()) {
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

    public boolean moverAFundacion(int columna) {

        if (columna < 0 || columna >= 8 || historial.llena()) {
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

    public boolean moverReservaAFundacion(int reserva) {

        if (reserva < 0 || reserva >= 8 || historial.llena()) {
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

    public boolean deshacerMovimiento() {

        if (historial.vacia()) {
            return false;
        }

        Movimientos movimiento = historial.Pop();

        int origen = movimiento.getOrigen();
        int destino = movimiento.getDestino();

        String tipoOrigen = movimiento.getTipoOrigen();
        String tipoDestino = movimiento.getTipoDestino();

        if (tipoOrigen.equals("columna")
                && tipoDestino.equals("columna")
                && movimiento.getCartasMovidas().size() > 1) {

            ArrayList<CartaInglesa> secuencia =
                    movimiento.getCartasMovidas();

            for (int i = 0; i < secuencia.size(); i++) {
                columnas[destino].removerUltimaCarta();
            }

            for (int i = 0; i < secuencia.size(); i++) {
                columnas[origen].restaurarCarta(secuencia.get(i));
            }

            return true;
        }

        CartaInglesa carta = movimiento.getCarta();

        if (tipoDestino.equals("columna")) {
            columnas[destino].removerUltimaCarta();
        } else if (tipoDestino.equals("reserva")) {
            reservas[destino] = null;
        } else if (tipoDestino.equals("fundacion")) {
            fundaciones[destino].removerUltimaCarta();
        }

        if (tipoOrigen.equals("columna")) {
            columnas[origen].restaurarCarta(carta);
        } else if (tipoOrigen.equals("reserva")) {
            reservas[origen] = carta;
        }

        return true;
    }

    public CartaInglesa obtenerReserva(int posicion) {

        if (posicion < 0 || posicion >= 8) {
            return null;
        }

        return reservas[posicion];
    }

    public FoundationDeck obtenerFundacion(int posicion) {

        if (posicion < 0 || posicion >= 4) {
            return null;
        }

        return fundaciones[posicion];
    }

    public boolean verificarVictoria() {

        int total = 0;

        for (int i = 0; i < 4; i++) {
            total += fundaciones[i].getCantidad();
        }

        return total == 52;
    }

    public String darPista() {

        for (int i = 0; i < 8; i++) {

            CartaInglesa carta = columnas[i].verUltimaCarta();

            if (carta != null) {

                FoundationDeck fundacion =
                        fundaciones[carta.getPalo().ordinal()];

                CartaInglesa ultima = fundacion.getUltimaCarta();

                if ((ultima == null && carta.getValorBajo() == 1)
                        || (ultima != null
                        && ultima.getValorBajo() + 1 == carta.getValorBajo())) {

                    return "Mueve " + carta + " de la columna "
                            + (i + 1) + " a su fundación.";
                }
            }
        }

        for (int i = 0; i < 8; i++) {

            CartaInglesa carta = reservas[i];

            if (carta != null) {

                FoundationDeck fundacion =
                        fundaciones[carta.getPalo().ordinal()];

                CartaInglesa ultima = fundacion.getUltimaCarta();

                if ((ultima == null && carta.getValorBajo() == 1)
                        || (ultima != null
                        && ultima.getValorBajo() + 1 == carta.getValorBajo())) {

                    return "Mueve " + carta + " de la reserva "
                            + (i + 1) + " a su fundación.";
                }
            }
        }

        for (int i = 0; i < 8; i++) {

            CartaInglesa carta = columnas[i].verUltimaCarta();

            if (carta != null) {

                for (int j = 0; j < 8; j++) {

                    if (i != j && columnas[j].sePuedeAgregarCarta(carta)) {
                        return "Mueve " + carta + " de la columna "
                                + (i + 1) + " a la columna " + (j + 1) + ".";
                    }
                }
            }
        }

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

        return "No hay movimientos disponibles.";
    }
}
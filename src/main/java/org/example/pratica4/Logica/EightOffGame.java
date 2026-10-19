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

        for (int i = 0; i < columnas.length; i++) {
            columnas[i] = new TableauDeck();
        }

        for (int i = 0; i < fundaciones.length; i++) {
            fundaciones[i] = new FoundationDeck(Palo.values()[i]);
        }
    }

    // Iniciar o reiniciar el juego
    public void iniciarJuego() {
        historial = new Pila<>(100);

        for (int i = 0; i < columnas.length; i++) {
            columnas[i].limpiar();
        }

        for (int i = 0; i < fundaciones.length; i++) {
            fundaciones[i].limpiar();
        }

        for (int i = 0; i < reservas.length; i++) {
            reservas[i] = null;
        }

        mazo = new Mazo();

        // Repartir 6 cartas en cada columna
        for (int i = 0; i < columnas.length; i++) {
            for (int j = 0; j < 6; j++) {
                CartaInglesa carta = mazo.obtenerUnaCarta();

                if (carta != null) {
                    columnas[i].inicializar(carta);
                }
            }
        }

        // Repartir una carta en las primeras 4 reservas
        for (int i = 0; i < 4; i++) {
            reservas[i] = mazo.obtenerUnaCarta();
        }
    }

    // Buscar una reserva vacía
    public int buscarReservaVacia() {
        for (int i = 0; i < reservas.length; i++) {
            if (reservas[i] == null) {
                return i;
            }
        }

        return -1;
    }

    // Obtener una columna
    public TableauDeck obtenerColumna(int posicion) {
        if (posicion < 0 || posicion >= columnas.length) {
            return null;
        }

        return columnas[posicion];
    }

    // Mover una carta entre columnas
    public boolean moverEntreColumnas(int origen, int destino) {
        if (origen < 0 || origen >= columnas.length ||
                destino < 0 || destino >= columnas.length ||
                origen == destino || historial.llena()) {
            return false;
        }

        CartaInglesa carta = columnas[origen].verUltimaCarta();

        if (carta == null ||
                !columnas[destino].sePuedeAgregarCarta(carta)) {
            return false;
        }

        Movimientos movimiento = new Movimientos(
                origen, destino, carta, "columna", "columna"
        );

        columnas[origen].removerUltimaCarta();

        if (columnas[destino].agregarCarta(carta)) {
            historial.Push(movimiento);
            return true;
        }

        columnas[origen].inicializar(carta);
        return false;
    }

    // Mover una carta de la reserva a una columna
    public boolean moverDeReserva(int reserva, int columna) {
        if (reserva < 0 || reserva >= reservas.length ||
                columna < 0 || columna >= columnas.length ||
                historial.llena()) {
            return false;
        }

        CartaInglesa carta = reservas[reserva];

        if (carta == null ||
                !columnas[columna].sePuedeAgregarCarta(carta)) {
            return false;
        }

        Movimientos movimiento = new Movimientos(
                reserva, columna, carta, "reserva", "columna"
        );

        if (columnas[columna].agregarCarta(carta)) {
            reservas[reserva] = null;
            historial.Push(movimiento);
            return true;
        }

        return false;
    }

    // Mover una carta de una columna a una reserva
    public boolean moverAReserva(int columna) {
        if (columna < 0 || columna >= columnas.length ||
                historial.llena()) {
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

        Movimientos movimiento = new Movimientos(
                columna, reserva, carta, "columna", "reserva"
        );

        columnas[columna].removerUltimaCarta();
        reservas[reserva] = carta;
        historial.Push(movimiento);

        return true;
    }

    // Mover una carta de una columna a su fundación
    public boolean moverAFundacion(int columna) {
        if (columna < 0 || columna >= columnas.length ||
                historial.llena()) {
            return false;
        }

        CartaInglesa carta = columnas[columna].verUltimaCarta();

        if (carta == null) {
            return false;
        }

        int posicion = carta.getPalo().ordinal();

        Movimientos movimiento = new Movimientos(
                columna, posicion, carta, "columna", "fundacion"
        );

        if (fundaciones[posicion].agregarCarta(carta)) {
            columnas[columna].removerUltimaCarta();
            historial.Push(movimiento);
            return true;
        }

        return false;
    }

    // Mover una carta de una reserva a su fundación
    public boolean moverReservaAFundacion(int reserva) {
        if (reserva < 0 || reserva >= reservas.length ||
                historial.llena()) {
            return false;
        }

        CartaInglesa carta = reservas[reserva];

        if (carta == null) {
            return false;
        }

        int posicion = carta.getPalo().ordinal();

        Movimientos movimiento = new Movimientos(
                reserva, posicion, carta, "reserva", "fundacion"
        );

        if (fundaciones[posicion].agregarCarta(carta)) {
            reservas[reserva] = null;
            historial.Push(movimiento);
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

        // Quitar la carta de donde se movió
        if (tipoDestino.equals("columna")) {
            columnas[destino].removerUltimaCarta();
        } else if (tipoDestino.equals("reserva")) {
            reservas[destino] = null;
        } else if (tipoDestino.equals("fundacion")) {
            fundaciones[destino].removerUltimaCarta();
        }

        // Regresar la carta a su lugar anterior
        if (tipoOrigen.equals("columna")) {
            columnas[origen].inicializar(carta);
        } else if (tipoOrigen.equals("reserva")) {
            reservas[origen] = carta;
        }

        return true;
    }

    // Obtener una carta de la reserva
    public CartaInglesa obtenerReserva(int posicion) {
        if (posicion < 0 || posicion >= reservas.length) {
            return null;
        }

        return reservas[posicion];
    }

    // Obtener una fundación
    public FoundationDeck obtenerFundacion(int posicion) {
        if (posicion < 0 || posicion >= fundaciones.length) {
            return null;
        }

        return fundaciones[posicion];
    }

    // Verificar si el jugador ganó
    public boolean verificarVictoria() {
        int total = 0;

        for (int i = 0; i < fundaciones.length; i++) {
            total += fundaciones[i].getCantidad();
        }

        return total == 52;
    }
}
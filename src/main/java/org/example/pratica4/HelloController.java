package org.example.pratica4;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;

import org.example.pratica4.Logica.EightOffGame;
import org.example.pratica4.Logica.TableauDeck;
import org.example.pratica4.Logica.FoundationDeck;
import org.example.pratica4.DeckOfCards.CartaInglesa;

public class HelloController {

    private EightOffGame juego = new EightOffGame();

    private Button[] reservas = new Button[8];
    private Button[] fundaciones = new Button[4];
    private Button[] botonesColumna = new Button[8];

    private VBox[] cajasColumnas = new VBox[8];

    private Label mensaje = new Label("Bienvenido a Eight Off");

    private int seleccion = -1;
    private String tipoSeleccion = "";
    private int indiceCartaSeleccionada = -1;

    public Parent crearInterfaz() {

        BorderPane principal = new BorderPane();
        principal.setPadding(new Insets(15));
        principal.setStyle("-fx-background-color: #176b45;");

        Label titulo = new Label("EIGHT OFF");
        titulo.setFont(Font.font("Arial", 28));
        titulo.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button nuevo = new Button("Nuevo juego");
        Button deshacer = new Button("Deshacer");
        Button pista = new Button("Pista");

        nuevo.setOnAction(e -> iniciarJuego());

        deshacer.setOnAction(e -> {
            if (juego.deshacerMovimiento()) {
                limpiarSeleccion();
                actualizarTablero();
                mensaje.setText("Movimiento deshecho.");
            } else {
                mensaje.setText("No hay movimientos para deshacer.");
            }
        });

        pista.setOnAction(e -> mensaje.setText(juego.darPista()));

        HBox controles = new HBox(10, nuevo, deshacer, pista);
        controles.setAlignment(Pos.CENTER);

        Label tituloReservas = crearTitulo("RESERVAS");
        Label tituloFundaciones = crearTitulo("FUNDACIONES");

        HBox filaReservas = new HBox(8);
        filaReservas.setAlignment(Pos.CENTER);

        for (int i = 0; i < 8; i++) {
            final int posicion = i;

            reservas[i] = crearBotonCarta("Vacía");
            reservas[i].setOnAction(e -> seleccionarReserva(posicion));

            filaReservas.getChildren().add(reservas[i]);
        }

        HBox filaFundaciones = new HBox(8);
        filaFundaciones.setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) {
            final int posicion = i;

            fundaciones[i] = crearBotonCarta("Vacía");
            fundaciones[i].setOnAction(e -> moverAFundacion(posicion));

            filaFundaciones.getChildren().add(fundaciones[i]);
        }

        VBox zonaReservas = new VBox(8, tituloReservas, filaReservas);
        zonaReservas.setAlignment(Pos.CENTER);

        VBox zonaFundaciones = new VBox(8, tituloFundaciones, filaFundaciones);
        zonaFundaciones.setAlignment(Pos.CENTER);

        HBox zonasSuperiores = new HBox(30, zonaReservas, zonaFundaciones);
        zonasSuperiores.setAlignment(Pos.CENTER);
        zonasSuperiores.setPadding(new Insets(15));

        VBox superior = new VBox(12, titulo, controles, zonasSuperiores);
        superior.setAlignment(Pos.CENTER);

        principal.setTop(superior);

        GridPane tablero = new GridPane();
        tablero.setAlignment(Pos.TOP_CENTER);
        tablero.setHgap(8);
        tablero.setVgap(8);
        tablero.setPadding(new Insets(10));

        for (int i = 0; i < 8; i++) {
            final int posicion = i;

            botonesColumna[i] = new Button("Columna " + (i + 1));
            botonesColumna[i].setMinWidth(80);
            botonesColumna[i].setMaxWidth(Double.MAX_VALUE);

            botonesColumna[i].setOnAction(e -> seleccionarColumna(posicion));

            cajasColumnas[i] = new VBox(5);
            cajasColumnas[i].setAlignment(Pos.TOP_CENTER);
            cajasColumnas[i].setMinWidth(80);

            cajasColumnas[i].getChildren().add(botonesColumna[i]);

            tablero.add(cajasColumnas[i], i, 0);
        }

        tablero.setMinWidth(850);
        tablero.setMinHeight(600);

        ScrollPane desplazamiento = new ScrollPane();
        desplazamiento.setContent(tablero);
        desplazamiento.setFitToWidth(false);
        desplazamiento.setFitToHeight(false);
        desplazamiento.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        desplazamiento.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        desplazamiento.setPannable(true);
        desplazamiento.setStyle(
                "-fx-background: #176b45; -fx-background-color: #176b45;"
        );

        principal.setCenter(desplazamiento);

        mensaje.setFont(Font.font("Arial", 14));
        mensaje.setStyle("-fx-text-fill: white;");
        mensaje.setWrapText(true);
        mensaje.setAlignment(Pos.CENTER);
        mensaje.setPadding(new Insets(12));

        principal.setBottom(mensaje);

        iniciarJuego();

        return principal;
    }

    private Label crearTitulo(String texto) {

        Label titulo = new Label(texto);
        titulo.setFont(Font.font("Arial", 14));
        titulo.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        return titulo;
    }

    private Button crearBotonCarta(String texto) {

        Button boton = new Button(texto);

        boton.setMinSize(65, 85);
        boton.setPrefSize(75, 90);
        boton.setMaxSize(85, 95);

        boton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d5d5d5;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-background-radius: 7;" +
                        "-fx-border-radius: 7;" +
                        "-fx-font-family: Arial;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222222;"
        );

        return boton;
    }

    private Button crearCarta(CartaInglesa carta) {

        String texto = carta.toString();
        Button boton = crearBotonCarta(texto);

        String color = "#222222";

        if (texto.contains("♥") || texto.contains("❤")
                || texto.contains("♦")) {
            color = "#d71920";
        }

        boton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d5d5d5;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-background-radius: 7;" +
                        "-fx-border-radius: 7;" +
                        "-fx-font-family: Arial;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + color + ";"
        );

        return boton;
    }

    private void mostrarImagenCarta(Button boton, CartaInglesa carta) {

        if (carta == null) {
            boton.setGraphic(null);
            boton.setText("Vacía");
            return;
        }

        String palo = carta.getPalo().toString().toUpperCase();

        if (palo.contains("CORAZ")) {
            palo = "CORAZON";
        } else if (palo.contains("DIAM")) {
            palo = "DIAMANTE";
        } else if (palo.contains("TREB")) {
            palo = "TREBOL";
        } else if (palo.contains("PICA") || palo.contains("ESPADA")) {
            palo = "PICAS";
        }

        String ruta = "/cartas/CARTA "
                + carta.getValorBajo() + " "
                + palo + ".jpg";

        URL archivo = getClass().getResource(ruta);

        if (archivo == null) {
            boton.setGraphic(null);
            boton.setText(carta.toString());
            return;
        }

        Image imagen = new Image(archivo.toExternalForm());

        ImageView vista = new ImageView(imagen);
        vista.setFitWidth(65);
        vista.setFitHeight(85);
        vista.setPreserveRatio(true);

        boton.setText("");
        boton.setGraphic(vista);
    }

    private void iniciarJuego() {

        juego.iniciarJuego();

        limpiarSeleccion();
        actualizarTablero();

        mensaje.setText("Partida nueva. Selecciona una carta.");
    }

    private void seleccionarColumna(int posicion) {

        if (tipoSeleccion.equals("columna")) {

            if (seleccion == posicion) {
                limpiarSeleccion();
                actualizarTablero();
                mensaje.setText("Selección cancelada.");
                return;
            }

            if (juego.moverSecuenciaEntreColumnas(
                    seleccion, posicion, indiceCartaSeleccionada)) {
                terminarMovimiento("Secuencia movida correctamente.");
            } else {
                mensaje.setText(
                        "Movimiento inválido. Revisa el orden de las cartas."
                );
            }

            return;
        }

        if (tipoSeleccion.equals("reserva")) {

            if (juego.moverDeReserva(seleccion, posicion)) {
                terminarMovimiento("Carta movida desde la reserva.");
            } else {
                mensaje.setText("No puedes colocar esa carta aquí.");
            }

            return;
        }

        TableauDeck columna = juego.obtenerColumna(posicion);

        if (columna == null || columna.verUltimaCarta() == null) {
            mensaje.setText("Columna vacía seleccionada.");
            seleccion = posicion;
            tipoSeleccion = "columna";
            indiceCartaSeleccionada = -1;
            actualizarTablero();
            return;
        }

        seleccion = posicion;
        tipoSeleccion = "columna";
        indiceCartaSeleccionada = columna.getCantidad() - 1;

        actualizarTablero();

        mensaje.setText(
                "Columna " + (posicion + 1)
                        + " seleccionada. Elige dónde mover la carta."
        );
    }

    private void seleccionarCartaColumna(int posicion, int indiceCarta) {

        if (tipoSeleccion.equals("columna") && seleccion != posicion) {

            if (juego.moverSecuenciaEntreColumnas(
                    seleccion, posicion, indiceCartaSeleccionada)) {
                terminarMovimiento("Secuencia movida correctamente.");
            } else {
                mensaje.setText("Movimiento inválido. Revisa las reglas.");
            }

            return;
        }

        if (tipoSeleccion.equals("reserva")) {

            if (juego.moverDeReserva(seleccion, posicion)) {
                terminarMovimiento("Carta movida desde la reserva.");
            } else {
                mensaje.setText("No puedes colocar esa carta aquí.");
            }

            return;
        }

        seleccion = posicion;
        tipoSeleccion = "columna";
        indiceCartaSeleccionada = indiceCarta;

        actualizarTablero();

        mensaje.setText(
                "Carta seleccionada. También se moverán las cartas de encima."
        );
    }

    private void seleccionarReserva(int posicion) {

        if (tipoSeleccion.equals("columna")) {

            if (juego.buscarReservaVacia() >= 0) {

                if (juego.moverAReserva(seleccion)) {
                    terminarMovimiento("Carta guardada en una reserva.");
                } else {
                    mensaje.setText("No se pudo guardar la carta.");
                }

            } else {
                mensaje.setText("No quedan reservas vacías.");
            }

            return;
        }

        if (tipoSeleccion.equals("reserva") && seleccion == posicion) {
            limpiarSeleccion();
            actualizarTablero();
            mensaje.setText("Selección cancelada.");
            return;
        }

        CartaInglesa carta = juego.obtenerReserva(posicion);

        if (carta == null) {
            mensaje.setText("Esta reserva está vacía.");
            return;
        }

        seleccion = posicion;
        tipoSeleccion = "reserva";
        indiceCartaSeleccionada = -1;

        actualizarTablero();

        mensaje.setText(
                "Reserva seleccionada. Elige una columna o fundación."
        );
    }

    private void moverAFundacion(int posicion) {

        boolean movido = false;

        if (tipoSeleccion.equals("columna")) {

            TableauDeck columna = juego.obtenerColumna(seleccion);

            if (columna != null
                    && indiceCartaSeleccionada == columna.getCantidad() - 1) {
                movido = juego.moverAFundacion(seleccion);
            }

        } else if (tipoSeleccion.equals("reserva")) {
            movido = juego.moverReservaAFundacion(seleccion);
        } else {
            mensaje.setText("Primero selecciona una carta.");
            return;
        }

        if (movido) {
            terminarMovimiento("Carta movida a la fundación.");
        } else {
            mensaje.setText("Movimiento inválido para la fundación.");
        }
    }

    private void terminarMovimiento(String texto) {

        limpiarSeleccion();
        actualizarTablero();

        if (juego.verificarVictoria()) {
            mensaje.setText("¡Ganaste! Completaste las 52 cartas.");
        } else {
            mensaje.setText(texto);
        }
    }

    private void limpiarSeleccion() {

        seleccion = -1;
        tipoSeleccion = "";
        indiceCartaSeleccionada = -1;
    }

    private void actualizarTablero() {

        for (int i = 0; i < 8; i++) {

            final int posicion = i;
            TableauDeck columna = juego.obtenerColumna(i);

            cajasColumnas[i].getChildren().clear();
            botonesColumna[i].setText("Columna " + (i + 1));

            if (tipoSeleccion.equals("columna") && seleccion == i) {
                botonesColumna[i].setStyle(
                        "-fx-background-color: #ffe082; -fx-font-weight: bold;"
                );
            } else {
                botonesColumna[i].setStyle("");
            }

            cajasColumnas[i].getChildren().add(botonesColumna[i]);

            if (columna == null) {
                continue;
            }

            for (int j = 0; j < columna.getCantidad(); j++) {

                final int indiceCarta = j;
                CartaInglesa carta = columna.obtenerCarta(j);

                if (carta == null) {
                    continue;
                }

                Button botonCarta = crearCarta(carta);
                mostrarImagenCarta(botonCarta, carta);

                if (tipoSeleccion.equals("columna")
                        && seleccion == posicion
                        && indiceCartaSeleccionada == indiceCarta) {
                    botonCarta.setStyle(
                            "-fx-background-color: #ffe082;" +
                                    "-fx-border-color: #ff9800;" +
                                    "-fx-border-width: 3;"
                    );
                }

                botonCarta.setOnAction(e ->
                        seleccionarCartaColumna(posicion, indiceCarta)
                );

                cajasColumnas[i].getChildren().add(botonCarta);
            }
        }

        for (int i = 0; i < 8; i++) {

            CartaInglesa carta = juego.obtenerReserva(i);

            if (carta == null) {
                reservas[i].setGraphic(null);
                reservas[i].setText("Vacía");
            } else {
                mostrarImagenCarta(reservas[i], carta);
            }

            if (tipoSeleccion.equals("reserva") && seleccion == i) {
                reservas[i].setStyle(
                        "-fx-background-color: #ffe082;" +
                                "-fx-border-color: black;"
                );
            } else {
                reservas[i].setStyle(
                        "-fx-background-color: white;" +
                                "-fx-border-color: #d5d5d5;" +
                                "-fx-background-radius: 7;" +
                                "-fx-border-radius: 7;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;"
                );
            }
        }

        for (int i = 0; i < 4; i++) {

            FoundationDeck fundacion = juego.obtenerFundacion(i);
            CartaInglesa carta = null;

            if (fundacion != null) {
                carta = fundacion.getUltimaCarta();
            }

            mostrarImagenCarta(fundaciones[i], carta);
        }
    }
}
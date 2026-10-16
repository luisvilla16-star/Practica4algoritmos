
package org.example.pratica4.DeckOfCards;

import org.example.pratica4.Logica.ListaSimple;
import java.util.Random;

public class Mazo {

    private ListaSimple<CartaInglesa> cartas =
            new ListaSimple<>();

    private int cantidad = 0;

    public Mazo() {
        llenar();
        mezclar();
    }

    public ListaSimple<CartaInglesa> getCartas() {
        return cartas;
    }

    public CartaInglesa obtenerUnaCarta() {

        if (cantidad > 0) {
            cantidad--;
            return cartas.eliminarInicio();
        }

        return null;
    }

    private void mezclar() {

        Random random = new Random();

        CartaInglesa[] arreglo =
                new CartaInglesa[cantidad];

        for (int i = 0; i < cantidad; i++) {
            arreglo[i] = cartas.eliminarInicio();
        }

        for (int i = cantidad - 1; i > 0; i--) {

            int j = random.nextInt(i + 1);

            CartaInglesa aux = arreglo[i];
            arreglo[i] = arreglo[j];
            arreglo[j] = aux;
        }

        for (int i = 0; i < cantidad; i++) {
            cartas.insetarFin(arreglo[i]);
        }
    }

    private void llenar() {

        for (int i = 2; i <= 14; i++) {

            for (Palo palo : Palo.values()) {

                CartaInglesa c =
                        new CartaInglesa(
                                i, palo, palo.getColor());

                cartas.insetarFin(c);
                cantidad++;
            }
        }
    }

    public void ordenar() {

        CartaInglesa[] arreglo =
                new CartaInglesa[cantidad];

        for (int i = 0; i < cantidad; i++) {
            arreglo[i] = cartas.eliminarInicio();
        }

        for (int i = 0; i < cantidad; i++) {

            for (int j = i + 1; j < cantidad; j++) {

                if (arreglo[i].compareTo(arreglo[j]) > 0) {

                    CartaInglesa aux = arreglo[i];
                    arreglo[i] = arreglo[j];
                    arreglo[j] = aux;
                }
            }
        }

        for (int i = 0; i < cantidad; i++) {
            cartas.insetarFin(arreglo[i]);
        }
    }

    @Override
    public String toString() {

        String resultado = "";

        CartaInglesa[] arreglo =
                new CartaInglesa[cantidad];

        for (int i = 0; i < cantidad; i++) {

            arreglo[i] = cartas.eliminarInicio();
            resultado += arreglo[i] + " ";
        }

        for (int i = 0; i < cantidad; i++) {
            cartas.insetarFin(arreglo[i]);
        }

        return resultado;
    }
}

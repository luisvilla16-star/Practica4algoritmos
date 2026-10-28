
package org.example.pratica4.Logica;

public class ListaSimple<T> {

    private Nodo<T> inicio;

    public void insertarInicio(T dato) {
        Nodo<T> n = new Nodo<>(dato);
        n.setNext(inicio);
        inicio = n;
    }

    public void insetarFin(T dato) {

        Nodo<T> n = new Nodo<>(dato);

        if (inicio == null) {
            inicio = n;
        } else {
            Nodo<T> r = inicio;

            while (r.getNext() != null) {
                r = r.getNext();
            }

            r.setNext(n);
        }
    }

    public T eliminarInicio() {

        if (inicio == null) {
            return null;
        }

        T dato = inicio.getDato();
        inicio = inicio.getNext();

        return dato;
    }

    public T eliminarFin() {

        if (inicio == null) {
            return null;
        }

        if (inicio.getNext() == null) {
            T dato = inicio.getDato();
            inicio = null;
            return dato;
        }

        Nodo<T> r = inicio;
        Nodo<T> a = inicio;

        while (r.getNext() != null) {
            a = r;
            r = r.getNext();
        }

        T dato = r.getDato();
        a.setNext(null);

        return dato;
    }

    public T verUltimo() {

        if (inicio == null) {
            return null;
        }

        Nodo<T> r = inicio;

        while (r.getNext() != null) {
            r = r.getNext();
        }

        return r.getDato();
    }

    // Contar los elementos de la lista
    public int getCantidad() {

        int cantidad = 0;
        Nodo<T> r = inicio;

        while (r != null) {
            cantidad++;
            r = r.getNext();
        }

        return cantidad;
    }

    // Obtener un elemento por su posición
    public T obtener(int posicion) {

        if (posicion < 0) {
            return null;
        }

        Nodo<T> r = inicio;
        int contador = 0;

        while (r != null) {

            if (contador == posicion) {
                return r.getDato();
            }

            r = r.getNext();
            contador++;
        }

        return null;
    }
}

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
            n.setNext(inicio);
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
            System.out.println("Lista vacia");
            return null;
        } else {
            T dato = inicio.getDato();
            inicio = inicio.getNext();
            return dato;

        }
    }

    public T eliminarFin() {
        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        } else if (inicio.getNext() == null) {
            T dato = inicio.getDato();
            inicio = null;
            return dato;
        }else{
            Nodo<T> r = inicio;
            Nodo<T> a = inicio;
            while (r.getNext() != null) {
                a=r;
                r = r.getNext();

            }
            T dato = r.getDato();
            a.setNext(null);

            return dato;
        }
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


}
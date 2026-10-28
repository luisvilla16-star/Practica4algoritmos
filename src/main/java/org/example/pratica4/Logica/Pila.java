package org.example.pratica4.Logica;

public class Pila<T> {
    private T[] pila;
    private int tope;


    public Pila() {
        pila = (T[]) new Object[100];
        tope = -1;
    }

    public Pila(int capacidad) {
        pila = (T[]) new Object[capacidad];
        tope = -1;
    }

    public boolean Push(T dato) {
        if (llena()) {
            return false;
        }

        tope++;
        pila[tope] = dato;
        return true;
    }

    public T Pop() {
        if (vacia()) {
            return null;
        }

        T dato = pila[tope];
        pila[tope] = null;
        tope--;
        return dato;
    }

    public boolean llena() {
        return tope >= pila.length - 1;
    }

    public boolean vacia() {
        return tope == -1;
    }
}

package org.example.pratica4.Logica;

public class Pila<T> {

    private T[] pila;
    private int tope;

    public Pila() {
        pila = (T[]) new Object[10];
        this.tope=-1;
    }

    public Pila(int capacidad) {
        pila = (T[]) new Object[capacidad];
        this.tope = -1;
    }

    public boolean Push(T dato) {
        if (llena()) {
            return false;
        } else{
            tope++;
            pila[tope]=dato;
            return true;
        }
    }

    public T Pop() {
        if (vacia()) {
            return null;
        } else {
            T dato= pila[tope];
            tope--;
            return dato;
        }
    }


    public boolean llena(){

        return tope>= pila.length-1;
    }

    public boolean vacia(){

        return tope==-1;
    }

}
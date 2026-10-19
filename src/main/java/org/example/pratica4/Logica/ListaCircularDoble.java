package org.example.pratica4.Logica;

public class ListaCircularDoble<T> {
    private NodoDoble<T> inicio;
    private NodoDoble<T> fin;

    public void insertarInicio(T dato) {
        NodoDoble<T> n = new NodoDoble<>(dato);
        if (inicio == null) {
            inicio = fin = n;
            inicio.setAnd(fin);
            fin.setSig(inicio);
        } else {
           n.setSig(inicio);
           inicio=n;
           fin.setSig(inicio);
           inicio.setAnd(fin);
        }
    }

    public void insertarFin(T dato) {
        NodoDoble<T> n = new NodoDoble<>(dato);
        n.setInfo(dato);

        if (inicio == null) {
            inicio = fin = n;
            fin.setSig(inicio);
            inicio.setAnd(fin);
        } else if (inicio==fin) {
            inicio.setSig(n);
            n.setAnd(inicio);
            fin = n;
            fin.setSig(inicio);
            inicio.setAnd(fin);
        }else{
           NodoDoble<T> r= inicio;
           while(r.getSig()!=inicio){
               r=r.getSig();
           }
           r.setSig(n);
           n.setAnd(r);
           fin=n;
           n.setSig(inicio);
           inicio.setAnd(fin);
        }
    }

    public T eliminarInicio() {
        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        } else if (inicio == fin) {
            T dato = inicio.getInfo();
            inicio = fin = null;
            return dato;
        } else {
            T dato = inicio.getInfo();
            fin.setSig(inicio.getSig());
            inicio = inicio.getSig();
            inicio.setAnd(fin);
            return dato;
        }
    }

    public T eliminarFinal() {
        if (inicio == null) {
            System.out.println("Lista Vacia");
            return null;
        } else if (inicio == fin) {
            T dato = inicio.getInfo();
            inicio = fin = null;
            return dato;
        } else {
           NodoDoble<T> r=inicio;
           NodoDoble<T> a=inicio;
           while(r.getSig()!=inicio){
               a=r;
               r=r.getSig();
           }
           T dato=r.getInfo();
           a.setSig(inicio);
           r.setAnd(null);
           fin=a;
           inicio.setAnd(fin);
            return dato;
        }
    }

}

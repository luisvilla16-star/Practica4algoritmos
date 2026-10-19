package org.example.pratica4.Logica;

public class NodoDoble<T> {
    private T info;
   private  NodoDoble<T>  sig;
   private NodoDoble<T>  and;

   public NodoDoble(T info, NodoDoble<T> sig, NodoDoble<T> and){
       this.info= info;
       this.sig=sig;
       this.and=and;

   }


    public NodoDoble(T info) {
        this.info = info;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public NodoDoble<T> getSig() {
        return sig;
    }

    public void setSig(NodoDoble<T> sig) {
        this.sig = sig;
    }

    public NodoDoble<T> getAnd() {
        return and;
    }

    public void setAnd(NodoDoble<T> and) {
        this.and = and;
    }

}

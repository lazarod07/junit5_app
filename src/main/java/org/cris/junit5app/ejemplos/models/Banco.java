package org.cris.junit5app.ejemplos.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {

    private String nombre;

    private List<Cuenta> cuentas;

    public Banco() {
        this.cuentas = new ArrayList<>();
    }

    public void tranferir(Cuenta origen, Cuenta destino, BigDecimal monto){
        origen.debito(monto);
        destino.credito(monto);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void addCuenta(Cuenta cuenta){
        this.cuentas.add(cuenta);
        cuenta.setBanco(this);
    }
}

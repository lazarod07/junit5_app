package org.cris.junit5app.ejemplos.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Cristian", new BigDecimal("1000.12345"));
        //cuenta.setPersona("Cristian");

        String esperado = "Cristian";
        String real = cuenta.getPersona();

        assertEquals(esperado, real);
        assertTrue(real.equals("Cristian"));
    }
}
package org.cris.junit5app.ejemplos.models;

import org.cris.junit5app.ejemplos.exceptions.DineroInsuficienteException;
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

        assertNotNull(cuenta.getSaldo(), () -> "La cuenta no puede ser nula");
        assertEquals(esperado, real, () -> "El nombre de la cuenta no es el que se esperaba");
        assertTrue(real.equals("Cristian"), () -> "Nombre cuenta esperada debe ser igual a la real");
    }

    @Test
    void testSaldoCuenta(){
        Cuenta cuenta = new Cuenta("Cristian", new BigDecimal("1000.12345"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));

        //assertNotEquals(cuenta, cuenta2);
        assertEquals(cuenta, cuenta2);
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Cristian", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal("100"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Cristian", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal("100"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException() {
        Cuenta cuenta = new Cuenta("Cristian", new BigDecimal("1000.12345"));

        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });

        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";

        assertEquals(esperado, actual);
    }

    @Test
    void testTranferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("Cristian", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Frency", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Davivienda");
        banco.tranferir(cuenta2, cuenta1, new BigDecimal(500));

        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta("Cristian", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Frency", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);
        banco.setNombre("Davivienda");
        banco.tranferir(cuenta2, cuenta1, new BigDecimal(500));

        assertAll(() -> {assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(), () -> "El valor del saldo de la cuenta2 no es el esperado");},()
                -> {assertEquals("3000", cuenta1.getSaldo().toPlainString(), () -> "El valor del saldo de la cuenta1 no es el esperado");},()
                -> {assertEquals(2, banco.getCuentas().size());},()
                -> {assertEquals("Davivienda", cuenta1.getBanco().getNombre());}, ()
                -> {assertEquals("Cristian", banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Cristian"))
                .findFirst()
                .get().getPersona());},()
                -> {assertTrue(banco.getCuentas().stream()
                .anyMatch(c -> c.getPersona().equals("Frency")));});
    }
}
package org.cris.junit5app.ejemplos.models;

import org.cris.junit5app.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest(){
        this.cuenta = new Cuenta("Cristian", new BigDecimal("1000.12345"));
        System.out.println("Iniciando el metodo de prueba");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    @Test
    @DisplayName("Probando nombre de la cuenta")
    void testNombreCuenta() {

        //cuenta.setPersona("Cristian");

        String esperado = "Cristian";
        String real = cuenta.getPersona();

        assertNotNull(cuenta.getSaldo(), () -> "La cuenta no puede ser nula");
        assertEquals(esperado, real, () -> "El nombre de la cuenta no es el que se esperaba");
        assertTrue(real.equals("Cristian"), () -> "Nombre cuenta esperada debe ser igual a la real");
    }

    @Test
    @DisplayName("Probando el saldo del la cuenta corriente, que no sea null, mayor que cero, valor esperado")
    void testSaldoCuenta(){
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Testiando referencias que sean iguales con el metodo equals")
    void testReferenciaCuenta() {
        cuenta = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));

        //assertNotEquals(cuenta, cuenta2);
        assertEquals(cuenta, cuenta2);
    }

    @Test
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal("100"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal("100"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException() {

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
    //@Disabled
    @DisplayName("Probando relaciones entre las cuentas y el banco con assertAll")
    void testRelacionBancoCuentas() {
        //fail();
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

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows() {
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    void testSoloLinuxMac() {
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testNoWindows() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void soloJdk8() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_11)
    void soloJdk11() {
    }

    @Test
    @DisabledOnJre(JRE.JAVA_11)
    void testNoJdk11() {
    }

    @Test
    void imprimirSysemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k,v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = ".*11.*")
    void testJavaVersion() {
    }

    @Test
    @DisabledIfSystemProperty(named = "os.arch", matches = ".*32:*")
    void testSolo64() {
    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*32:*")
    void testNo64() {
    }

    @Test
    @EnabledIfSystemProperty(named = "user.name", matches = "Crist")
    void testUserName() {
    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void testDev() {
    }

    @Test
    void imprimirVariablesAmbiente() {
        Map<String, String> getEnv = System.getenv();

        getEnv.forEach((k,v) -> System.out.println(k + "=" + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-11.0.2:*")
    void testJavaHome() {
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "4")
    void testProcesadores() {
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
    void testEnv(){

    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
    void testEnvNoProd(){

    }
}
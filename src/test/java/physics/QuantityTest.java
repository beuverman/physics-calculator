package physics;

import org.junit.jupiter.api.Test;
import physics.exceptions.IncompatibleUnitsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuantityTest {
    private static final Quantity a = new Quantity("300.465");
    private static final Quantity b = new Quantity("10.913");
    private static final Quantity c = new Quantity("2134.945m");
    private static final Quantity d = new Quantity("-92N");
    private static final Quantity e = new Quantity("0");
    private static final Quantity f = new Quantity("225");

    @Test
    void negate() {
        assertEquals(e, e.negate());
        assertEquals(new Quantity("-2134.945m"), c.negate());
    }

    @Test
    void add() {
        assertEquals((new Quantity("311.378")), a.add(b));
        assertThrows(IncompatibleUnitsException.class, () -> c.add(d));
        assertEquals(b, e.add(b));
    }

    @Test
    void subtract() {
        assertEquals((new Quantity("-289.552")), b.subtract(a));
        assertThrows(IncompatibleUnitsException.class, () -> c.subtract(d));
        assertEquals(b.negate(), e.subtract(b));
    }

    @Test
    void multiply() {
        assertEquals(new Quantity("-196414.94J"), c.multiply(d));
        assertEquals(e, a.multiply(e));
        assertEquals(new Quantity("23298.654785m"), b.multiply(c));
    }

    @Test
    void divide() {
        assertEquals(new Quantity("1.3354"), a.divide(f));
        assertEquals(e, e.divide(f));
        assertThrows(ArithmeticException.class, () -> d.divide(e));
    }

    @Test
    void pow() {
    }

    @Test
    void sin() {
    }

    @Test
    void cos() {
    }

    @Test
    void tan() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void isDimensionless() {
    }

    @Test
    void toLatexString() {
        assertEquals("0", (new Quantity("0.00")).toLatexString());
        assertEquals("0", (new Quantity("0.0E-105")).toLatexString());
        assertEquals("123456", (new Quantity("123456")).toLatexString());
        assertEquals("1.23457*10^{6}", (new Quantity("1234567")).toLatexString());
        assertEquals("1*10^{-9}", (new Quantity("0.000000001")).toLatexString());
        assertEquals("632.023", (new Quantity("632.023")).toLatexString());
        assertEquals("10000", (new Quantity("10000")).toLatexString());
        assertEquals("10mm", (new Quantity("10mm").toLatexString()));
        assertEquals("1000kg", (new Quantity("1000kg").toLatexString()));
        assertEquals("1000g", (new Quantity("1000g").toLatexString()));
        assertEquals("1238au", (new Quantity("1238au").toLatexString()));
    }

    @Test
    void testToString() {
        assertEquals("0", (new Quantity("0.00")).toString());
        assertEquals("0", (new Quantity("0.0E-105")).toString());
        assertEquals("123456", (new Quantity("123456")).toString());
        assertEquals("1.23457E+6", (new Quantity("1234567")).toString());
        assertEquals("1E-9", (new Quantity("0.000000001")).toString());
        assertEquals("632.023", (new Quantity("632.023")).toString());
        assertEquals("10000", (new Quantity("10000")).toString());
        assertEquals("0.01m", (new Quantity("10mm").toString()));
        assertEquals("1000kg", (new Quantity("1000kg").toString()));
        assertEquals("1kg", (new Quantity("1000g").toString()));
        assertEquals("1.85202E+14m", (new Quantity("1238au").toString()));
    }
}
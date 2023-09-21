package physics;

public class Units extends Quantity {
    private static final Units[] UNITS = new Units[]{
        new Units("1.602176634e-19J", "eV"),
        new Units("60s", "min"),
        new Units("101325Pa", "atm"),
        new Units("4184J", "cal")
    };

    private String alias;

    public Units(String quantity, String alias) {
        super(quantity);
        this.alias = alias;
    }

    public static Quantity identify(String str) {
        if (UNITS == null)
            return null;

        for (Units unit : UNITS) {
            if (unit.alias.equals(str))
                return unit;
        }

        return null;
    }
}
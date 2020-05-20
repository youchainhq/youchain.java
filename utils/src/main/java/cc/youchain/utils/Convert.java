package cc.youchain.utils;

import java.math.BigDecimal;

/**
 * YOUChain unit conversion functions.
 */
public final class Convert {
    private Convert() {
    }

    public static BigDecimal fromLu(String number, Unit unit) {
        return fromLu(new BigDecimal(number), unit);
    }

    public static BigDecimal fromLu(BigDecimal number, Unit unit) {
        return number.divide(unit.getLuFactor());
    }

    public static BigDecimal toLu(String number, Unit unit) {
        return toLu(new BigDecimal(number), unit);
    }

    public static BigDecimal toLu(BigDecimal number, Unit unit) {
        return number.multiply(unit.getLuFactor());
    }

    public enum Unit {
        LU("lu", 0),
        KLU("klu", 3),
        MLU("mlu", 6),
        GLU("glu", 9),
        MICRO("micro",12),
        MILLI("milli",15),
        YOU("you", 18),
        KYOU("kyou", 21),
        MYOU("myou", 24),
        GYOU("gyou", 27),
        TYOU("tyou", 30),
        ;

        private String name;
        private BigDecimal luFactor;

        Unit(String name, int factor) {
            this.name = name;
            this.luFactor = BigDecimal.TEN.pow(factor);
        }

        public BigDecimal getLuFactor() {
            return luFactor;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Unit fromString(String name) {
            if (name != null) {
                for (Unit unit : Unit.values()) {
                    if (name.equalsIgnoreCase(unit.name)) {
                        return unit;
                    }
                }
            }
            return Unit.valueOf(name);
        }
    }
}

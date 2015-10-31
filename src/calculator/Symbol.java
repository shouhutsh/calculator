package calculator;

import calculator.digital.Digital;
import calculator.digital.DoubleDigital;

/**
 * Created by Qi on 2015/10/28.
 */
public class Symbol extends Node implements Comparable<Symbol> {

    private final Type type;

    private final int priority;

    public Symbol(Formula formula) throws Exception {
        type = Type.getType(formula);
        priority = formula.getBuffPriority() + type.property;
    }

    public static boolean isSymbol(String text) {
        for (Type t : Type.values()) {
            if (text.startsWith(t.name)) return true;
        }
        return false;
    }

    public Digital calculate() {
        return type.doCalculate(this);
    }

    @Override
    public int compareTo(Symbol o) {
        return o.priority - priority;
    }

    private enum Type {
        ADD(1, "+") {
            @Override
            Digital doCalculate(Symbol s) {
                if (s.getLeft() instanceof Digital) {
                    Digital left = (Digital) s.getLeft();
                    Digital right = (Digital) s.getRight();
                    Digital result = new DoubleDigital(left.getDigital() + right.getDigital());
                    doRelate(left.getLeft(), result, right.getRight());
                    return result;
                } else {
                    Digital right = (Digital) s.getRight();
                    Digital result = new DoubleDigital(right.getDigital());
                    doRelate(s.getLeft(), result, right.getRight());
                    return result;
                }
            }
        },
        SUB(2, "-") {
            @Override
            Digital doCalculate(Symbol s) {
                if (s.getLeft() instanceof Digital) {
                    Digital left = (Digital) s.getLeft();
                    Digital right = (Digital) s.getRight();
                    Digital result = new DoubleDigital(left.getDigital() - right.getDigital());
                    doRelate(left.getLeft(), result, right.getRight());
                    return result;
                } else {
                    Digital right = (Digital) s.getRight();
                    Digital result = new DoubleDigital(-right.getDigital());
                    doRelate(s.getLeft(), result, right.getRight());
                    return result;
                }
            }
        },
        MUL(3, "*") {
            @Override
            Digital doCalculate(Symbol s) {
                Digital left = (Digital) s.getLeft();
                Digital right = (Digital) s.getRight();
                Digital result = new DoubleDigital(left.getDigital() * right.getDigital());
                doRelate(left.getLeft(), result, right.getRight());
                return result;
            }
        },
        DIV(3, "/") {
            @Override
            Digital doCalculate(Symbol s) {
                Digital left = (Digital) s.getLeft();
                Digital right = (Digital) s.getRight();
                Digital result = new DoubleDigital(left.getDigital() / right.getDigital());
                doRelate(left.getLeft(), result, right.getRight());
                return result;
            }
        },
        SIN(4, "SIN") {
            @Override
            Digital doCalculate(Symbol s) {
                Digital right = (Digital) s.getRight();
                Digital result = new DoubleDigital(Math.sin(right.getDigital()));
                doRelate(s.getLeft(), result, right.getRight());
                return result;
            }
        },
        COS(4, "COS") {
            @Override
            Digital doCalculate(Symbol s) {
                Digital right = (Digital) s.getRight();
                Digital result = new DoubleDigital(Math.cos(right.getDigital()));
                doRelate(s.getLeft(), result, right.getRight());
                return result;
            }
        };

        public final int property;
        public final String name;

        Type(int property, String name) {
            this.property = property;
            this.name = name;
        }

        public static Type getType(Formula formula) throws Exception {
            for (Type t : values()) {
                if (formula.getLeavingText().startsWith(t.name)) {
                    formula.doExcursion(t.name.length());
                    return t;
                }
            }
            throw new Exception("无法匹配运算符号");
        }

        abstract Digital doCalculate(Symbol s);
    }
}

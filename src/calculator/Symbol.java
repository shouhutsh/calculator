package calculator;

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

    public Digital calculate() throws Exception {
        return type.run(this);
    }

    @Override
    public int compareTo(Symbol o) {
        return o.priority - priority;
    }

    private enum Type {
        ADD(1, "+") {
            @Override
            double doCalculate(Digital var) {
                return var.getDigital();
            }

            @Override
            double doCalculate(Digital left, Digital right) {
                return left.getDigital() + right.getDigital();
            }
        },
        SUB(1, "-") {
            @Override
            double doCalculate(Digital var) {
                return -var.getDigital();
            }

            @Override
            double doCalculate(Digital left, Digital right) {
                return left.getDigital() - right.getDigital();
            }
        },
        MUL(2, "*") {
            @Override
            double doCalculate(Digital var) throws Exception {
                throw new Exception("必须有两个参数");
            }

            @Override
            double doCalculate(Digital left, Digital right) {
                return left.getDigital() * right.getDigital();
            }
        },
        DIV(2, "/") {
            @Override
            double doCalculate(Digital var) throws Exception {
                throw new Exception("必须有两个参数");
            }

            @Override
            double doCalculate(Digital left, Digital right) {
                return left.getDigital() / right.getDigital();
            }
        },
        SIN(3, "SIN") {
            @Override
            double doCalculate(Digital var) {
                return Math.sin(var.getDigital());
            }

            @Override
            double doCalculate(Digital left, Digital right) throws Exception {
                throw new Exception("必须只有一个参数");
            }
        },
        COS(3, "COS") {
            @Override
            double doCalculate(Digital var) {
                return Math.cos(var.getDigital());
            }

            @Override
            double doCalculate(Digital left, Digital right) throws Exception {
                throw new Exception("必须只有一个参数");
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

        public Digital run(Symbol s) throws Exception {
            if (s.getLeft() instanceof Digital) {
                if (s.getRight() instanceof Digital) {
                    return Operate.DOUBLE.doOperate(s, this);
                } else {
                    return Operate.LEFT.doOperate(s, this);
                }
            } else {
                if (s.getRight() instanceof Digital) {
                    return Operate.RIGHT.doOperate(s, this);
                } else {
                    throw new Exception("符号解析错误或优先级设置错误");
                }
            }
        }

        abstract double doCalculate(Digital var) throws Exception;

        abstract double doCalculate(Digital left, Digital right) throws Exception;
    }

    private enum Operate {
        DOUBLE {
            @Override
            Digital doOperate(Symbol s, Type t) throws Exception {
                Digital left = (Digital) s.getLeft();
                Digital right = (Digital) s.getRight();
                Digital result = new Digital(t.doCalculate(left, right));
                doRelate(left.getLeft(), result, right.getRight());
                return result;
            }
        },
        LEFT {
            @Override
            Digital doOperate(Symbol s, Type t) throws Exception {
                Digital left = (Digital) s.getLeft();
                Digital result = new Digital(t.doCalculate(left));
                doRelate(left.getLeft(), result, s.getRight());
                return result;
            }
        },
        RIGHT {
            @Override
            Digital doOperate(Symbol s, Type t) throws Exception {
                Digital right = (Digital) s.getRight();
                Digital result = new Digital(t.doCalculate(right));
                doRelate(s.getLeft(), result, right.getRight());
                return result;
            }
        };

        abstract Digital doOperate(Symbol s, Type t) throws Exception;
    }
}

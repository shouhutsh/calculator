package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 公式类
 * 处理传入的算数式
 */
public class Formula {

    public static void main(String[] args) throws Exception {
        Formula f = new Formula("-1+2-3*4/2+SIN5+COS(1-3)");
        System.out.println(f.terms(f.parse()).getDigital());
    }

    private static final int PRIIORITY_WINDOW = 10;

    // 指向算式字符串的偏移量
    private int offset = 0;
    // 基础优先级，主要用于 '(' 和 ')'
    private int basePriority = 0;
    // 次要优先级，主要用于连续几个符号的情况，例如：1+-1
    private int secondPriority = 0;
    // 算数式
    private final String text;

    // 为了解决算数式只有数字的BUG，虽然比较丑，但比较简洁
    private Digital temp;

    public Formula(String text) throws Exception {
        if (text == null || text.isEmpty()) throw new Exception("算数式不能为空！");
        this.text = text;
    }

    /**
     * 规约 将算数式依据优先级计算
     * 例如：     1 + 2 * 3 + 4
     * 数据结构：  [1]<->[+]<->[2]<->[*]<->[3]<->[+]<->[4]
     * |
     * 一次规约：              [2]<->[*]<->[3]
     * |
     * |
     * [1]<->[+]<->[6]<->[+]<->[4]
     * |
     * 二次规约：       [1]<->[+]<->[6]
     * |
     * |
     * [7]<->[+]<->[4]
     * |
     * 三次规约：             [11]
     *
     * @param symbols
     * @return
     */
    public Digital terms(List<Symbol> symbols) throws Exception {
        Collections.sort(symbols);

        for (Symbol s : symbols) {
            temp = doTerms(s);
        }
        return temp;
    }

    // 解析 将算数式分为 Digital 和 Symbol 两种 Node, 并链接形成双向链表
    public List<Symbol> parse() throws Exception {
        Node prev = null;
        List<Symbol> symbols = new ArrayList<>();
        while (offset < text.length()) {
            Node cur = getNextNode();
            if (cur == null) continue;
            if (cur instanceof Digital) temp = (Digital) cur;
            if (cur instanceof Symbol) symbols.add((Symbol) cur);

            Node.doRelate(prev, cur);
            prev = cur;
        }
        if (getBuffPriority() != 0) throw new Exception("公式解析错误");
        return symbols;
    }

    // 处理 Digital，若遇到数字将 次要优先级 重置为 0
    private Digital doDigital() {
        secondPriority = 0;
        return new Digital(this);
    }

    // 处理 Symbol，每遇到一次 Symbol 增加次要优先级，增加的数量也不算是随便弄的吧，反正目前这样就可以
    private Symbol doSymbol() throws Exception {
        secondPriority += (PRIIORITY_WINDOW / 2);
        return new Symbol(this);
    }

    // 获取算数式的下一个 Node （Symbol 或 Digital），遇到 '(' 和 ')' 时需要调整 优先级
    private Node getNextNode() throws Exception {
        if ("()".contains(readahead() + "")) {
            basePriority += ('(' == getchar()) ? PRIIORITY_WINDOW : -PRIIORITY_WINDOW;
            return null;
        }
        if (Digital.isDigital(readahead())) return doDigital();
        if (Symbol.isSymbol(getLeavingText())) return doSymbol();
        throw new Exception("不识别符号：" + readahead());
    }

    // 将指针滑动 off 个字符
    public void doExcursion(int off) {
        this.offset += off;
    }

    // 获取当前指针之后的 公式字符串（未接析的公式）
    public String getLeavingText() {
        return text.substring(offset);
    }

    // 判断是否解析完成
    public boolean hasNext() {
        return offset < text.length();
    }

    // 获取下一个字符并移动指针
    public char getchar() {
        return text.charAt(offset++);
    }

    // 预读一个字符
    public char readahead() {
        return text.charAt(offset);
    }

    // 做一次规约
    private static Digital doTerms(Symbol symbol) throws Exception {
        return symbol.calculate();
    }

    // 获取 Symbol 附加的优先级
    public int getBuffPriority() {
        return basePriority + secondPriority;
    }
}

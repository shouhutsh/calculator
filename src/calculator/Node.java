package calculator;

/**
 * Created by Qi on 2015/10/28.
 */
public abstract class Node {

    private Node left;

    private Node right;

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public static void doRelate(Node t1, Node t2) {
        if (t1 != null) t1.setRight(t2);
        if (t2 != null) t2.setLeft(t1);
    }

    public static void doRelate(Node left, Node cur, Node right){
        doRelate(left, cur);
        doRelate(cur, right);
    }
}

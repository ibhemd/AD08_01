import java.util.ArrayList;

public class BalancedSearchTree <T extends Comparable<T>>{

    Node<T> root;
    Node<T> NIL;
    float alpha;

    public BalancedSearchTree(float alpha) {
        this.root = NIL;
        this.alpha = alpha;

    }

    public BalancedSearchTree(Node<T> x, float alpha) {
        this.root = x;
        this.alpha = alpha;
    }

    public class Node<T extends Comparable<T>> {
        T key;
        BalancedSearchTree<T>.Node<T> parent;
        BalancedSearchTree<T>.Node<T> left;
        BalancedSearchTree<T>.Node<T> right;

        public Node(T key) {
            this.key = key;
            this.parent = this.left = this.right = (BalancedSearchTree<T>.Node<T>) NIL;
        }

    }

    public void insert(Node<T> x) {
        Node<T> y = this.root;
        int k = 1;
        while (y != NIL) {
            x.parent = y;
            if (x.key.compareTo(y.key) < 0) {
                y = y.left;
            } else {
                y = y.right;
            }
            k += 1;
        }
        if (x.parent == NIL) {
            this.root = x;
        } else {
            if (x.key.compareTo(x.parent.key) < 0) {
                x.parent.left = x;
            } else {
                x.parent.right = x;
            }
        }
        Rebalance(x);
    }

    public void delete(Node<T> z) {
        Node<T> x;
        Node<T> y;
        if (z.left == NIL || z.right == NIL) {
            y = z;
        } else {
            y = Successor(z);
        }
        if (y.left != NIL) {
            x = y.left;
        } else {
            x = y.right;
        }
        if (x != NIL) {
            x.parent = y.parent;
        }
        if (y.parent == NIL) {
            this.root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }
        if (y != z) {
            z.key = y.key;
        }
        Rebalance(z);
    }

    public void LeftRotate(Node<T> x){
        Node<T> y = x.right;
        x.right = y.left;
        if (y.left != NIL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    public void DoubleLeftRotate(Node<T> x){
        Node<T> y = x.right;
        Node<T> z = y.left;
        if (z.left != NIL) {
            z.left.parent = x;
            x.right = z.left;
        }
        if (z.right != NIL) {
            z.right.parent = y;
            y.left = z.right;
        }
        if (x.parent == NIL) {
            this.root = z;
        } else if (x == x.parent.left) {
            x.parent.left = z;
        } else {
            x.parent.right = z;
        }
        x.parent = y.parent = z;
        z.left = x;
        z.right = y;
    }

    public void RightRotate(Node<T> x){
        Node<T> y = x.left;
        x.left = y.right;
        if (y.right != NIL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    public void DoubleRightRotate(Node<T> x){
        Node<T> y = x.left;
        Node<T> z = y.right;
        if (z.right != NIL) {
            z.right.parent = x;
            x.left = z.right;
        }
        if (z.left != NIL) {
            z.left.parent = y;
            y.right = z.left;
        }
        if (x.parent == NIL) {
            this.root = z;
        } else if (x == x.parent.right) {
            x.parent.right = z;
        } else {
            x.parent.left = z;
        }
        x.parent = y.parent = z;
        z.right = x;
        z.left = y;
    }

    public void Rebalance(Node<T> x){
        while (x != this.root) {
            if (pT(x) < alpha) {
                if (pT(x.right) <= (1/(2-alpha))) {
                    LeftRotate(x);
                } else {
                    DoubleLeftRotate(x);
                }
            }
            if (pT(x) > (1-alpha)) {
                if (pT(x.left) >= (1-(1/(2-alpha)))) {
                    RightRotate(x);
                } else {
                    DoubleRightRotate(x);
                }
            }
            x = x.parent;
        }
    }

    public int absOfT(Node<T> x) {
        return toSortedArrayList(x).size();
    }

    public float pT(Node<T> x) {
        return ((absOfT(x.left)+1)/(absOfT(x)+1));
    }

    public Node<T> search(T key){
        return search(this.root, key);
    }

    public Node<T> search(Node<T> x,T key){
        if (x == NIL || x.key.compareTo(key) == 0) {
            return x;
        }
        if (key.compareTo(x.key) < 0) {
            return search(x.left, key);
        } else {
            return search(x.right,key);
        }
    }

    public Node<T> min(){
        return min(this.root);
    }

    public Node<T> min(Node<T> x){
        while (x != NIL) {
            x = x.left;
        }
        return x;
    }

    public Node<T> max(){
        return max(this.root);
    }

    public Node<T> max(Node<T> x){
        while (x != NIL) {
            x = x.right;
        }
        return x;
    }

    public Node<T> Successor(Node<T> x){
        if (x.right != NIL) {
            return min(x.right);
        }
        Node<T> y = x.parent;
        while (y != NIL && x == y.right) {
            x = y;
            y = x.parent;
        }
        return y;
    }

    public ArrayList<T> toSortedArrayList(Node<T> x){
        ArrayList<T> L = new ArrayList();
        Node<T> n = min(x);
        Node<T> m = max(x);
        L.add(n.key);
        while (n != m) {
            n = Successor(x);
            L.add(n.key);
        }
        return L;
    }

}

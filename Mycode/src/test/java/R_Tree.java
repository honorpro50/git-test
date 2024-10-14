
import java.util.ArrayList;
import java.util.List;

public class R_Tree {
    private Node root;
    private int maxChildren;

    public R_Tree(int maxChildren) {
        this.root = new Node(true);
        this.maxChildren = maxChildren;
    }

    public void insert(Point point) {
        Node leaf = chooseLeaf(root, point);
        leaf.getEntries().add(new Entry(point));
        if (leaf.getEntries().size() > maxChildren) {
            splitNode(leaf);
        }
    }

    private Node chooseLeaf(Node node, Point point) {
        if (node.isLeaf()) {
            return node;
        } else {
            Node child = node.getChildren().get(0);
            double minEnlargement = Double.MAX_VALUE;
            for (Node c : node.getChildren()) {
                double enlargement = c.getRectangle().enlargement(point);
                if (enlargement < minEnlargement) {
                    minEnlargement = enlargement;
                    child = c;
                }
            }
            return chooseLeaf(child, point);
        }
    }

    private void splitNode(Node node) {
        if (node.isRoot()) {
            Node newRoot = new Node(false);
            newRoot.getChildren().add(node);
            node.setParent(newRoot);
            root = newRoot;
        }

        Node[] splitNodes = node.split(maxChildren);
        Node parent = node.getParent();
        for (Node n : splitNodes) {
            parent.getChildren().add(n);
            n.setParent(parent);
        }
        parent.getChildren().remove(node);
    }

    public static void main(String[] args) {
        R_Tree rTree = new R_Tree(4);
        rTree.insert(new Point(1, 1));
        rTree.insert(new Point(2, 2));
        // Insert more points as needed
    }
}

class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

class Rectangle {
    private Point lowerLeft;
    private Point upperRight;

    public Rectangle(Point lowerLeft, Point upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    public double enlargement(Point point) {
        // Calculate the enlargement of the rectangle if point is added
        return 0; // Placeholder
    }
}

class Entry {
    private Point point;

    public Entry(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }
}

class Node {
    private List<Entry> entries;
    private List<Node> children;
    private Node parent;
    private Rectangle rectangle;

    public Node(boolean isLeaf) {
        this.entries = new ArrayList<>();
        this.children = new ArrayList<>();
        this.parent = null;
        this.rectangle = null;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public Node[] split(int maxChildren) {
        // Split the node into two nodes
        return new Node[2]; // Placeholder
    }
}
package main;

import utils.io.InputReader;
import utils.io.OutputWriter;

import java.util.ArrayList;
import java.util.List;

class Node {
    int key;
    Node left, right;
    List<Integer> orderList = new ArrayList<>();

    public List<Integer> getOrderList() {
        return orderList;
    }

    public Node() {
    }

    public Node(int item) {
        key = item;
        left = right = null;
    }

    void postOrderTraverse(Node node) {
        if (node == null)
            return;

        postOrderTraverse(node.left);
        postOrderTraverse(node.right);
        orderList.add(node.key);
    }

    void preOrderTraverse(Node node) {
        if (node == null)
            return;
        orderList.add(node.key);

        preOrderTraverse(node.left);
        preOrderTraverse(node.right);
    }
}

public class EthanTraversesATree {

    public void solve(int testNumber, InputReader reader, OutputWriter writer) {
        int n = reader.readInteger();
        int k = reader.readInteger();
        Node[] nodes = new Node[n + 1];

        for (int i = 1; i <= n; i++) nodes[i] = new Node(i);

        for (int i = 1; i <= n; i++) {
            int left = reader.readInteger();
            int right = reader.readInteger();

            if (left > 0) nodes[i].left = nodes[left];
            if (right > 0) nodes[i].right = nodes[right];
        }

        Node node = new Node();
        node.preOrderTraverse(nodes[1]);
        List<Integer> preOrder = node.getOrderList();

        node = new Node();
        node.postOrderTraverse(nodes[1]);
        List<Integer> postOrder = node.getOrderList();

//        System.out.println(preOrder);
//        System.out.println(postOrder);
        int map[] = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int x = preOrder.get(i - 1);
            int y = postOrder.get(i - 1);
            map[x] = y;
        }

        boolean[] flag = new boolean[n + 1];

        int label = 0;
        int L[] = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int x = i;
            if (!flag[x] && label + 1 <= k) ++label;
            while (!flag[x]) {
                L[x] = label;
                flag[x] = true;
                x = map[x];
            }
        }

        if (label == k) {
            writer.print("Case #" + testNumber + ":");
            for (int i = 1; i <= n; i++) writer.print(" " + L[i]);
            writer.print("\n");
        } else writer.print("Case #" + testNumber + ": Impossible\n");
    }
}
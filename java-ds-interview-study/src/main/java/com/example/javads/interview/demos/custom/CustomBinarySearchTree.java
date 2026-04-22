package com.example.javads.interview.demos.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary search tree of ints (unbalanced; order depends on insertion sequence).
 *
 * <p><b>Interview:</b> degenerates to linked list O(n) height; balancing → AVL/red-black (JDK {@link java.util.TreeMap}).
 */
public final class CustomBinarySearchTree {

    private Node root;

    public void insert(int v) {
        root = insert(root, v);
    }

    public boolean contains(int v) {
        Node cur = root;
        while (cur != null) {
            if (cur.v == v) {
                return true;
            }
            cur = (v < cur.v) ? cur.left : cur.right;
        }
        return false;
    }

    /** Sorted order for verification (inorder traversal). */
    public List<Integer> inorder() {
        List<Integer> out = new ArrayList<>();
        inorder(root, out);
        return out;
    }

    private Node insert(Node n, int v) {
        if (n == null) {
            return new Node(v);
        }
        if (v < n.v) {
            n.left = insert(n.left, v);
        } else if (v > n.v) {
            n.right = insert(n.right, v);
        }
        // equal: ignore duplicate for this teaching BST
        return n;
    }

    private void inorder(Node n, List<Integer> out) {
        if (n == null) {
            return;
        }
        inorder(n.left, out);
        out.add(n.v);
        inorder(n.right, out);
    }

    private static final class Node {
        final int v;
        Node left;
        Node right;

        Node(int v) {
            this.v = v;
        }
    }
}

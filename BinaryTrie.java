import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {

    // much of my solution borrows inspiration from the princeton algs4 Huffman
    // https://algs4.cs.princeton.edu/55compression/Huffman.java.html

    private Node root;
    private Map<Character, BitSequence> table;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        this.table = new HashMap<>();
        MinPQ<Node> nodes = new MinPQ<>();
        // create and add nodes with freq to minPQ
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            nodes.insert(new Node(entry.getKey(), entry.getValue(), null, null));
        }
        // similar to princeton's huffman buildTrie method
        while (nodes.size() > 1) {
            // get the two smallest nodes and take them out of minPQ
            Node left = nodes.delMin();
            Node right = nodes.delMin();
            // merge into super node
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            // put super node into minPQ
            nodes.insert(parent);
        }
        this.root = nodes.delMin(); // last node in minPQ is largest node (root)
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node p = root;
        // similar to the princeton's huffman expand method
        // iterate through sequence
        for (int i = 0; i < querySequence.length(); i++) {
            // leaf means end of symbol, return the match
            if (p.isLeaf()) {
                return new Match(querySequence.firstNBits(i), p.ch);
            }
            // traverse right branch for bit 1 and left for bit 0
            if (querySequence.bitAt(i) == 1) {
                p = p.right;
            } else if (querySequence.bitAt(i) == 0) {
                p = p.left;
            }
        }
        return new Match(querySequence, p.ch);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        // recursively traverse decoding trie from root
        // and add sequence to table when reaching a leaf
        lookUpTableHelper(table, root, "");
        return table;
    }

    private void lookUpTableHelper(Map<Character, BitSequence> tabley, Node p, String seq) {
        // leaf indicates symbol end; add (char, running seq) to table
        if (p.isLeaf()) {
            tabley.put(p.ch, new BitSequence(seq));
        } else {
            // traverse left branch and add a '0' to running seq
            lookUpTableHelper(tabley, p.left, seq + '0');
            // traverse right branch and add '1' to running seq
            lookUpTableHelper(tabley, p.right, seq + '1');
        }
    }

    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            return (left == null) && (right == null);
        }

        public int compareTo(Node other) {
            return this.freq - other.freq;
        }
    }
}

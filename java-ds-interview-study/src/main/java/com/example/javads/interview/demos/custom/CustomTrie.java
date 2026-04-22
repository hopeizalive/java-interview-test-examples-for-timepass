package com.example.javads.interview.demos.custom;

/**
 * Lowercase-letter trie for prefix and exact word queries (26 children per node).
 *
 * <p><b>Interview:</b> insert O(L), prefix lookup O(L); space shares prefixes across words.
 */
public final class CustomTrie {

    private final TrieNode root = new TrieNode();

    public void insert(String s) {
        TrieNode cur = root;
        for (int i = 0; i < s.length(); i++) {
            int idx = s.charAt(i) - 'a';
            if (idx < 0 || idx >= 26) {
                throw new IllegalArgumentException("only a-z supported in demo trie");
            }
            if (cur.next[idx] == null) {
                cur.next[idx] = new TrieNode();
            }
            cur = cur.next[idx];
        }
        cur.end = true;
    }

    /** True if full word was inserted. */
    public boolean contains(String s) {
        TrieNode node = walk(s);
        return node != null && node.end;
    }

    /** True if any inserted word has this prefix. */
    public boolean hasPrefix(String s) {
        return walk(s) != null;
    }

    private TrieNode walk(String s) {
        TrieNode cur = root;
        for (int i = 0; i < s.length(); i++) {
            int idx = s.charAt(i) - 'a';
            if (idx < 0 || idx >= 26 || cur.next[idx] == null) {
                return null;
            }
            cur = cur.next[idx];
        }
        return cur;
    }

    private static final class TrieNode {
        private final TrieNode[] next = new TrieNode[26];
        boolean end;
    }
}

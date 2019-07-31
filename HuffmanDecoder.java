public class HuffmanDecoder {
    public static void main(String[] args) {
        String fileName = args[0];
        ObjectReader or = new ObjectReader(fileName);
        // reading decoding trie and # of symbols
        BinaryTrie decodingTrie = (BinaryTrie) or.readObject();
        int numberOfSymbols = (int) or.readObject();
        // read massive bit sequence
        BitSequence encoded = (BitSequence) or.readObject();
        // keep going through encoded seqeunce until no more symbols left
        char[] decoded = new char[numberOfSymbols];
        for (int i = 0; i < numberOfSymbols; i++) {
            // find longest prefix match for symbol
            Match pair = decodingTrie.longestPrefixMatch(encoded);
            // record symbol in array
            decoded[i] = pair.getSymbol();
            // bits left over to stil decode
            encoded = encoded.allButFirstNBits(pair.getSequence().length());
        }
        // write decoded sequence
        FileUtils.writeCharArray(args[1], decoded);
    }
}

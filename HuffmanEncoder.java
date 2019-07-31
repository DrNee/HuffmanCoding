import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : inputSymbols) {
            if (frequencyTable.containsKey(c)) {
                frequencyTable.put(c, frequencyTable.get(c) + 1);
            } else {
                frequencyTable.put(c, 1);
            }
        }
        return frequencyTable;
    }

    public static void main(String[] args) {
        // read file as 8 bit symbols and build table
        String fileName = args[0];
        char[] symbols = FileUtils.readFile(fileName);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(FileUtils.readFile(fileName));
        // construct binary decoding trie
        BinaryTrie decodingTrie = new BinaryTrie(frequencyTable);
        // write binary trie and number of symbols to .huf file
        ObjectWriter ow = new ObjectWriter(fileName + ".huf");
        ow.writeObject(decodingTrie);
        ow.writeObject(symbols.length);
        // use trie to create lookup table
        Map<Character, BitSequence> lookUpTable = decodingTrie.buildLookupTable();
        // create list, iterate through 8-bit symbols
        // add corresponding bit seq for each symbol to list
        List<BitSequence> encodedSeqs = new ArrayList<>();
        for (char c : symbols) {
            encodedSeqs.add(lookUpTable.get(c));
        }
        // assemble all bit seqs into one bit seq
        BitSequence encoded = BitSequence.assemble(encodedSeqs);
        // write bit seq to .huf file
        ow.writeObject(encoded);
    }
}

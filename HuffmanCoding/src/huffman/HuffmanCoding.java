package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);

        sortedCharFreqList = new ArrayList<CharFreq>();
        ArrayList<Character> input = new ArrayList<Character>();
        char[] asciioccur = new char[128];
        int size = 0;

        while (StdIn.hasNextChar()){
            char cha = StdIn.readChar();
            size++;
            asciioccur[(int)cha] = cha;
            input.add(cha);
        }

        for(int i = 127; i >= 0; i--){
            if (asciioccur[i] != 0){
                CharFreq charfreq = new CharFreq();
                charfreq.setCharacter(asciioccur[i]);

                double freq = 0.00;
                for (int j = 0; j <input.size(); j++){
                    if(input.get(j) == asciioccur[i]){
                        freq++;
                    }
                }
                charfreq.setProbOcc(freq/size);
                
                sortedCharFreqList.add(charfreq);
            }
        }

        if(sortedCharFreqList.size() == 1){
            if((int)(sortedCharFreqList.get(0).getCharacter()) == 127){
                CharFreq nullchar = new CharFreq();
                nullchar.setCharacter((char)0);
                nullchar.setProbOcc(0.00);
                sortedCharFreqList.add(0,nullchar);
            }
            else{
                CharFreq nullchar = new CharFreq();
                nullchar.setCharacter((char)((int)(sortedCharFreqList.get(0).getCharacter())+1));
                nullchar.setProbOcc(0.00);
                sortedCharFreqList.add(0,nullchar);
            }
        }

        Collections.sort(sortedCharFreqList);
	/* Your code goes here */
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() { 
        Queue<TreeNode> source = new Queue<TreeNode>();
        Queue<TreeNode> target = new Queue<TreeNode>();

        huffmanRoot = new TreeNode();
        TreeNode num1 = new TreeNode();
        TreeNode num2 = new TreeNode();

        for(int i = 0; i < sortedCharFreqList.size(); i++){
            TreeNode tempo = new TreeNode();
            tempo.setData(sortedCharFreqList.get(i));
            source.enqueue(tempo);
        }
        
        while(!source.isEmpty()){
            if(target.isEmpty()){
                num1 = source.dequeue();
                num2 = source.dequeue();
            }
            else {
                //1 time
                if(source.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()){
                    num1 = source.dequeue();
                }
                else{
                    num1 = target.dequeue();
                }

                // 2 times
                if(target.isEmpty()){
                    num2 = source.dequeue();
                }
                else if (source.isEmpty()){
                    num2 = target.dequeue();
                }
                else{
                    if(source.peek().getData().getProbOcc() <= target.peek().getData().getProbOcc()){
                        num2 = source.dequeue();
                    }
                    else{
                        num2 = target.dequeue();
                    }
                }
            }
            CharFreq tempdata = new CharFreq();
            tempdata.setCharacter(null);
            tempdata.setProbOcc(num1.getData().getProbOcc() + num2.getData().getProbOcc());

            TreeNode targetnode = new TreeNode();
            targetnode.setData(tempdata);
            targetnode.setLeft(num1);
            targetnode.setRight(num2);

            target.enqueue(targetnode);
            
        }
        if(target.size() == 1){
            huffmanRoot = target.peek();
            return;
        }
        else{
            while(target.size() != 1){
                num1 = target.dequeue();
                num2 = target.dequeue();

                CharFreq tempdata = new CharFreq();
                tempdata.setCharacter(null);
                tempdata.setProbOcc(num1.getData().getProbOcc() + num2.getData().getProbOcc());

                TreeNode targetnode = new TreeNode();
                targetnode.setData(tempdata);
                targetnode.setLeft(num1);
                targetnode.setRight(num2);

                target.enqueue(targetnode);
            }
            huffmanRoot = target.peek();
            return;
        }

        

	/* Your code goes here */
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        encodings = new String[128];
        TreeNode ptr = huffmanRoot;

        Queue<TreeNode> tree = new Queue<>();
        Queue<String> code = new Queue<>();

        tree.enqueue(ptr);
        code.enqueue("");

        while(!tree.isEmpty()){

            TreeNode root = tree.dequeue();
            String temp = code.dequeue();
    
            if(root.getLeft() !=null){
                String left =  temp + "0";
                tree.enqueue(root.getLeft());
                code.enqueue(left);
            }
    
            if(root.getRight() !=null){
                String right =  temp + "1";
                tree.enqueue(root.getRight());
                code.enqueue(right);
            }

            if(root.getLeft() == null && root.getRight() == null){
                encodings[(int)(root.getData().getCharacter())] = temp;
            }
        }
	/* Your code goes here */
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
	/* Your code goes here */
    String code = "";

        while(StdIn.hasNextChar()){
            char tempchar = StdIn.readChar();
            String tempstring = encodings[(int)(tempchar)];
            code = code + tempstring;
        }
        writeBitString(encodedFile, code);
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

	/* Your code goes here */
        String code = readBitString(encodedFile);
        String text = "";
        TreeNode ptr = huffmanRoot;

        for(int i = 0; i < code.length(); i++){
            if(ptr.getLeft() == null && ptr.getRight() == null){
                text = text + ptr.getData().getCharacter();
                ptr = huffmanRoot;
            }
            if(code.charAt(i) == '0'){
                ptr = ptr.getLeft();
            }
            if(code.charAt(i) == '1'){
                ptr = ptr.getRight();
            }
        }
        if(ptr.getLeft() == null && ptr.getRight() == null){
            text = text + ptr.getData().getCharacter();
        }
        StdOut.print(text);

    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndexedMTree {
    private static MessageDigest md;
    private static String NULL = "0";
    private int hight;
    private Node root;
    private ArrayList<Node> leaves;
    static {
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public IndexedMTree(int el){
        ArrayList<Node> empt = Zerogen(el);
        this.leaves = Zerogen(el);
        this.root = TreeBuild(empt);
        if(empt.size() > 1) this.hight = (int)(Math.log(empt.size()) / Math.log(2)) + 1;
        else this.hight = 1;
    }

    private IndexedMTree(ArrayList<Node> leave, int hight)
    {
        this.root = TreeBuild(leave);
        this.hight = hight;
        this.leaves = leave;
    }

    private Node TreeBuild(ArrayList<Node> leave){
        ArrayList<Node> leafbuild = new ArrayList<>(leave);
        while(leafbuild.size() > 1)
        {
            ArrayList<Node> newlevel = new ArrayList<>();
            for(int i = 0; i < leafbuild.size(); i+=2){
                newlevel.add(new Node(leafbuild.get(i),leafbuild.get(i+1)));
            }
            leafbuild.clear();
            leafbuild.addAll(newlevel);
        }
        return leafbuild.get(0);
    }

    public Node getRoot(){
        return root;
    }

   private ArrayList<Node> sortNodes(ArrayList<Node> A)
    {
        ArrayList<String> value = new ArrayList<>();
        for (int i = 0; i < A.size(); i++){
            value.add(A.get(i).value);
        }
        ArrayList<String> sortvalue = new ArrayList<>();
        sortvalue.addAll(value);
        Collections.sort(sortvalue);
        for (int i = 0; i < A.size(); i++){
            if(sortvalue.indexOf(A.get(i).value) != (int) Math.pow(2, hight - 1) - 1) {
                A.get(i).ind = value.indexOf(sortvalue.get(sortvalue.indexOf(A.get(i).value) + 1));
               // System.out.println(sortvalue.indexOf(A.get(i).value));
               // System.out.println((int) Math.pow(2, hight - 1) - 1);
            }
            else A.get(i).ind = value.indexOf(sortvalue.get(0));
        }
        return A;
    }

    public static class Node{
        private String value;
        private Node left;
        private Node right;
        private int ind;

        public Node(Node left, Node right)
        {
            this.left=left;
            this.right=right;
            if(right.value == NULL) this.value = SHA_512(left.value);
            else if(left.value == NULL) this.value = SHA_512(right.value);
            else this.value = SHA_512(left.value+right.value);
        }

        public Node(String value)
        {
            this.value = SHA_512(value);
        }

    }

    public IndexedMTree Addnode(Node x, int index){
        leaves.set(index, x);
        sortNodes(leaves);
        return new IndexedMTree(leaves, hight);
    }

    public void EP(Node x){
        Node temp = leaves.get(0);
        while (temp.ind != 0 && x.value.compareTo(temp.value) > -1){
            if(x.value.compareTo(temp.value) > 0) temp = leaves.get(temp.ind);
            else{
                System.out.println("Consist of member");
                return;
            }
        }
        if(x.value.compareTo(temp.value) == 0) System.out.println("Consist of member");
        else System.out.println("X -- IS NOT A MEMBER");

    }

    public void MP(Node x){
        Node temp = leaves.get(0);
        while (temp.ind != 0 && x.value.compareTo(temp.value) > -1){
            if(x.value.compareTo(temp.value) == 0) {
                System.out.println("Consist of member");
                return;
            }
            else {
                temp = leaves.get(temp.ind);
            }
        }
        if(x.value.compareTo(temp.value) == 0) System.out.println("Consist of member");
        else System.out.println("X -- IS NOT A MEMBER");
    }

    public static String SHA_512(String input) {
        if (input == NULL) return NULL;
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        return no.toString(16);
    }

    public static ArrayList<Node> Zerogen(int el)
    {
        ArrayList<Node> key = new ArrayList<>();
        for(int i = 0; i < el; i++)
        {
            key.add(new Node(NULL));
        }
        return key;
    }
}

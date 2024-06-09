import javax.lang.model.type.NullType;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MTree {
    private static MessageDigest md;
    private static String NULL = "";
    private Node root;
    private int hight;

    static {
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public MTree(ArrayList<String> values) {
        this.root = TreeBuild(values);
        if(values.size() > 1) this.hight = (int)(Math.log(values.size()) / Math.log(2)) + 1;
        else this.hight = 1;
    }

    public Node getRoot()
    {
        return root;
    }

    public static String SHA_512(String input) {
        if (input == NULL) return NULL;
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        return no.toString(16);
    }

    public Node TreeBuild(ArrayList<String> values){
        ArrayList<Node> level = new ArrayList<>();
        for(String leaf : values){
            level.add(new Node(leaf));
        }
        while(level.size() > 1)
        {
            ArrayList<Node> newlevel = new ArrayList<>();
            for(int i = 0; i < level.size(); i+=2){
                newlevel.add(new Node(level.get(i),level.get(i+1)));
            }
            level.clear();
            level.addAll(newlevel);
        }
        return level.get(0);
    }

    public static class Node{
        public String value;
        public Node left;
        public Node right;

        public Node(String value){
            this.value = SHA_512(value);
        }

        public Node(Node left, Node right)
        {
            this.left = left;
            this.right = right;
            if(right.value == NULL) this.value = SHA_512(left.value);
            else if(left.value == NULL) this.value = SHA_512(right.value);
            else this.value = SHA_512(left.value+right.value);
        }
    }



    public void MP(Node x, int index)
    {
        ArrayList<Node> ProofBase = new ArrayList<>();
        int[] binarytrip = new int[hight-1];
        int allel = (int) Math.pow(2, hight - 1);
        Node temp = root;
        for(int i = 0; i < hight - 2; i++)
        {
            if(index > allel/2) {
                ProofBase.add(0, temp.left);
                temp = temp.right;
                binarytrip[i] = 1;
                index= index - allel/2;
            }
            else {
                ProofBase.add(0, temp.right);
                temp = temp.left;
                binarytrip[i] = 0;
            }

            allel/=2;
        }
        if(index%2 == 0) {
            ProofBase.add(0, temp.left);
            binarytrip[hight-2] = 1;
        }
        else {
            ProofBase.add(0, temp.right);
            binarytrip[hight-2] = 0;
        }

        String Xroot = x.value;
        for(int i = 0; i < hight-1; i++)
        {
            if(binarytrip[hight-2-i] == 0) Xroot = SHA_512(Xroot + ProofBase.get(i).value);
            else Xroot = SHA_512(ProofBase.get(i).value+Xroot);
        }
        System.out.println("Xroot = " + Xroot);
        if(Xroot.equals(root.value)) System.out.println("X -- IS A MEMBER");
        else System.out.println("OOPs...");
    }


    public static ArrayList<String> keyGen(int el)
    {
        BigInteger a = new BigInteger("0", 2);
        ArrayList<String> key = new ArrayList<>();
        for(int i = 0; i < el; i++)
        {
            key.add(a.toString(2));
            a = a.add(BigInteger.ONE);
        }
        return key;
    }



}


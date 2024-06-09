import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<String> a = MTree.keyGen(65536);
        MTree test = new MTree(a);
        System.out.println(test.getRoot().value);
        test.MP(new MTree.Node("1111111111111111"), 65536);
        System.out.println("//////////////////////////////////////");
        SparseMTree test1 = new SparseMTree(65536);
        for (int i = 0; i < a.size(); i+=500)
        {
            test1 = test1.AddNode(new SparseMTree.Node(a.get(i)), i+1);
        }
        test1.MP(new SparseMTree.Node("0"),1);
        test1.MP(new SparseMTree.Node("111110100"),501);
        test1.MP(new SparseMTree.Node("111110101"),502);
        test1.EP(2);
        System.out.println("///////////////////////////////////////////");
        IndexedMTree test3 = new IndexedMTree(65536);
        for (int i = 1; i < 65536; i++)
        {
            test3 = test3.Addnode(new IndexedMTree.Node(a.get(i)), i);
        }
        test3.EP(new IndexedMTree.Node("10"));
        test3.EP(new IndexedMTree.Node("100"));
        test3.MP(new IndexedMTree.Node("1000"));

    }
}

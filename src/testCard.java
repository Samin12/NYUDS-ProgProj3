import java.util.*;

/**
 * Created by Samin on 7/26/17.
 */
public class testCard {
    public static void main(String[] args) {
        String inputs = "DBACGHJK";


        BinarySearchTree<Visitor> mytree = new BinarySearchTree<Visitor>();
        for (char c : inputs.toCharArray()) {
            Visitor v = new Visitor(c + "");
            mytree.add(v);
        }

        System.out.println("\n\n");
        System.out.println("Tree for input: " + inputs);
        mytree.printTree();


    }

}

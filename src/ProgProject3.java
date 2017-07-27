import java.io.IOException;

public class ProgProject3 {
	public static void main(String[] args)throws IOException {


		String[] inputs = {
				"DBACGHJK",
				"DACBEFMLGHJK",
				"JABCDEFISRQPON"
		};
		for (int k=0; k<inputs.length;k++) {
		BinarySearchTree<Visitor> mytree = new BinarySearchTree<Visitor>();
		for (int i =0 ; i< inputs[k].length(); i++) {
			Visitor v = new Visitor(inputs[k].substring(i, i+1));
			v.vname = inputs[k].substring(i, i+1);
			mytree.add(v);

		}
		System.out.println("\n\n");
		System.out.println("Tree for input: " + inputs[k]);
		//mytree.printTree();
			mytree.printTreeByLevel();


        }


	}





}

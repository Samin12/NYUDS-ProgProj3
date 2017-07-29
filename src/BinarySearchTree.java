//---------------------------------------------------------------------------
// BinarySearchTree.java          by Dale/Joyce/Weems               Chapter 7
//
// Defines all constructs for a reference-based BST.
// Supports three traversal orders Preorder, Postorder & Inorder ("natural")
//---------------------------------------------------------------------------

 

import java.util.*;   // Iterator, Comparator

import static java.util.Collections.singletonList;


public class BinarySearchTree<T> {
    protected BSTNode<T> root;      // reference to the root of this BST
    protected Comparator<T> comp;   // used for all comparisons

    protected boolean found;   // used by remove

    public BinarySearchTree()
    // Precondition: T implements Comparable
    // Creates an empty BST object - uses the natural order of elements.
    {
        root = null;
        comp = new Comparator<T>() {
            public int compare(T element1, T element2) {
                return ((Comparable) element1).compareTo(element2);
            }
        };
    }

    public BinarySearchTree(Comparator<T> comp)
    // Creates an empty BST object - uses Comparator comp for order
    // of elements.
    {
        root = null;
        this.comp = comp;
    }

    public boolean isFull()
    // Returns false; this link-based BST is never full.
    {
        return false;
    }

    public boolean isEmpty()
    // Returns true if this BST is empty; otherwise, returns false.
    {
        return (root == null);
    }

    public T min()
    // If this BST is empty, returns null;
    // otherwise returns the smallest element of the tree.
    {
        if (isEmpty())
            return null;
        else {
            BSTNode<T> node = root;
            while (node.getLeft() != null)
                node = node.getLeft();
            return node.getInfo();
        }
    }

    public T max()
    // If this BST is empty, returns null;
    // otherwise returns the largest element of the tree.
    {
        if (isEmpty())
            return null;
        else {
            BSTNode<T> node = root;
            while (node.getRight() != null)
                node = node.getRight();
            return node.getInfo();
        }
    }

    public int recSize(BSTNode<T> node)
    // Returns the number of elements in subtree rooted at node.
    {
        if (node == null)
            return 0;
        else
            return 1 + recSize(node.getLeft()) + recSize(node.getRight());
    }

    public int size()
    // Returns the number of elements in this BST.
    {
        return recSize(root);
    }

    public int size2() throws Exception
    // Returns the number of elements in this BST.
    {
        int count = 0;
        if (root != null) {
            LinkedStack<BSTNode<T>> nodeStack = new LinkedStack<BSTNode<T>>();
            BSTNode<T> currNode;
            nodeStack.push(root);
            while (!nodeStack.isEmpty()) {
                currNode = nodeStack.top();
                nodeStack.pop();
                count++;
                if (currNode.getLeft() != null)
                    nodeStack.push(currNode.getLeft());
                if (currNode.getRight() != null)
                    nodeStack.push(currNode.getRight());
            }
        }
        return count;
    }

    public boolean recContains(T target, BSTNode<T> node)
    // Returns true if the subtree rooted at node contains info i such that
    // comp.compare(target, i) == 0; otherwise, returns false.
    {
        if (node == null)
            return false;       // target is not found
        else if (comp.compare(target, node.getInfo()) < 0)
            return recContains(target, node.getLeft());   // Search left subtree
        else if (comp.compare(target, node.getInfo()) > 0)
            return recContains(target, node.getRight());  // Search right subtree
        else
            return true;        // target is found
    }

    public boolean contains(T target)
    // Returns true if this BST contains a node with info i such that
    // comp.compare(target, i) == 0; otherwise, returns false.
    {
        return recContains(target, root);
    }


    public T recGet(T target, BSTNode<T> node)
    // Returns info i from the subtree rooted at node such that
    // comp.compare(target, i) == 0; if no such info exists, returns null.
    {
        if (node == null)
            return null;             // target is not found
        else if (comp.compare(target, node.getInfo()) < 0)
            return recGet(target, node.getLeft());         // get from left subtree
        else if (comp.compare(target, node.getInfo()) > 0)
            return recGet(target, node.getRight());        // get from right subtree
        else
            return node.getInfo();  // target is found
    }

    public T get(T target)
    // Returns info i from node of this BST where comp.compare(target, i) == 0;
    // if no such node exists, returns null.
    {
        return recGet(target, root);
    }

    public BSTNode<T> recAdd(T element, BSTNode<T> node)
    // Adds element to tree rooted at node; tree retains its BST property.
    {
        if (node == null)
            // Addition place found
            node = new BSTNode<T>(element);
        else if (comp.compare(element, node.getInfo()) <= 0)
            node.setLeft(recAdd(element, node.getLeft()));    // Add in left subtree
        else
            node.setRight(recAdd(element, node.getRight()));   // Add in right subtree
        return node;
    }

    public boolean add(T element)
    // Adds element to this BST. The tree retains its BST property.
    {
        root = recAdd(element, root);
        return true;
    }


    public T getPredecessor(BSTNode<T> subtree)
    // Returns the information held in the rightmost node of subtree
    {
        BSTNode<T> temp = subtree;
        while (temp.getRight() != null)
            temp = temp.getRight();
        return temp.getInfo();
    }

    public BSTNode<T> removeNode(BSTNode<T> node)
    // Removes the information at node from the tree.
    {
        T data;
        if (node.getLeft() == null)
            return node.getRight();
        else if (node.getRight() == null)
            return node.getLeft();
        else {
            data = getPredecessor(node.getLeft());
            node.setInfo(data);
            node.setLeft(recRemove(data, node.getLeft()));
            return node;
        }
    }

    public BSTNode<T> recRemove(T target, BSTNode<T> node)
    // Removes element with info i from tree rooted at node such that
    // comp.compare(target, i) == 0 and returns true;
    // if no such node exists, returns false.
    {
        if (node == null)
            found = false;
        else if (comp.compare(target, node.getInfo()) < 0)
            node.setLeft(recRemove(target, node.getLeft()));
        else if (comp.compare(target, node.getInfo()) > 0)
            node.setRight(recRemove(target, node.getRight()));
        else {
            node = removeNode(node);
            found = true;
        }
        return node;
    }

    public boolean remove(T target)
    // Removes a node with info i from tree such that comp.compare(target,i) == 0
    // and returns true; if no such node exists, returns false.
    {
        root = recRemove(target, root);
        return found;
    }


    public void preOrder(BSTNode<T> node, LinkedQueue<T> q)
    // Enqueues the elements from the subtree rooted at node into q in preOrder.
    {
        if (node != null) {
            q.enqueue(node.getInfo());
            preOrder(node.getLeft(), q);
            preOrder(node.getRight(), q);
        }
    }

    public void inOrder(BSTNode<T> node, LinkedQueue<T> q)
    // Enqueues the elements from the subtree rooted at node into q in inOrder.
    {
        if (node != null) {
            inOrder(node.getLeft(), q);
            q.enqueue(node.getInfo());
            inOrder(node.getRight(), q);
        }
    }

    public void postOrder(BSTNode<T> node, LinkedQueue<T> q)
    // Enqueues the elements from the subtree rooted at node into q in postOrder.
    {
        if (node != null) {
            postOrder(node.getLeft(), q);
            postOrder(node.getRight(), q);
            q.enqueue(node.getInfo());
        }
    }


    public void BFS(BSTNode<T> root) {
        Queue<BSTNode> queue = new LinkedList<BSTNode>();
        Queue<String> stringQueue= new LinkedList<String>();

        if (root == null) {
            return;
        }
        queue.clear();
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode node = queue.remove();
            System.out.println(node.getInfo().toString() + "");

            stringQueue.add(node.getInfo().toString());

                if (node.getLeft() != null) {
                    queue.add(node.getLeft());
                }else {
                    stringQueue.add(" lef");
                }

                if (node.getRight() != null) {
                    queue.add(node.getRight());

                }else {
                    stringQueue.add(" rig");
                }


        }

        System.out.println(stringQueue);

    }





    public ArrayList<String> arrayRepTree(BSTNode root) {
        if (root == null) {
            return null;
        }

        /*array of arrays
        */
        ArrayList<String> shortList = new ArrayList<>();


        Queue<BSTNode> q1 = new LinkedList<BSTNode>();
        Queue<BSTNode> q2 = new LinkedList<BSTNode>();


        q1.add(root);
        while (!q1.isEmpty() || !q2.isEmpty()) {
            while (!q1.isEmpty()) {
                root = q1.poll();
                //System.out.println(root.getInfo().toString());
                shortList.add(root.getInfo().toString());

                if (root.getLeft() != null) {
                    q2.add(root.getLeft());
                    //shortList2.add(root.getLeft().getInfo().toString());
                } else {
                    //  System.out.println("no left child");
                    shortList.add("no left");
                    //shortList2.add(" ");

                }
                if (root.getRight() != null) {
                    q2.add(root.getRight());
                    //shortList2.add(root.getRight().getInfo().toString());
                } else {
                    //System.out.println("no right child");
                    shortList.add("no right");
                }
            }

            shortList.add("next level");


            while (!q2.isEmpty()) {
                root = q2.poll();
                //  System.out.println(root.getInfo().toString());
                shortList.add(root.getInfo().toString());


                if (root.getLeft() != null) {
                    q1.add(root.getLeft());
                } else {
                    //    System.out.println("no left child");
                    shortList.add("no left");

                }
                if (root.getRight() != null) {
                    q1.add(root.getRight());
                } else {
                    //  System.out.println("no right child");
                    shortList.add("no right");
                }
            }
            //System.out.println();
            shortList.add("next level");


        }


        return shortList;

    }
    public void printTree() {


        System.out.println("PRINT TREE");
        String[][] treeArray = new String[100][100];
        readyArray(treeArray);



        //treeArray[0][50]="D";
        //printTrunks(treeArray,0,50,5,1);
        System.out.println( arrayRepTree(root));

        //if node + 1 ! = no left then more on and node = something else
        int level=0;
        for (int i = 0; i < arrayRepTree(root).size()-1; i++) {
            int childNo =0;
            int r=-0;
            int c=0;

            //the node gets assigned a place in the array and the number of children


            //node is placed in the array
            for (int row = 0; row < treeArray.length; row++) {
                for (int col = 0; col < treeArray[row].length; col++) {

                }
            }
        }

        System.out.println(findChildren(12));
       // finalizeArrray(treeArray);
        printArray(treeArray);


    }
    public int findChildren(int index){
        int children =0;

        if (arrayRepTree(root).get(index).length()<=2){

            if (arrayRepTree(root).get(index+1).contains("no left") && arrayRepTree(root).get(index+2).contains("no right") ){
                children=0;
                return 0;
            }
            else if (arrayRepTree(root).get(index+1).contains("no left")){
                children=1;
                return 1;
            }
            else if (arrayRepTree(root).get(index+1).contains("no right")){
                children=-1;
                return -1;
            }
            else if (arrayRepTree(root).get(index+1).contains("next level")){
                children=2;
                return 2;
            }else {
                children=2;
                return 2;
            }
        }else {
            return 3;
        }

    }

    public void printTrunks(String[][] treeArray,int rowNode,int colNode,int childNo){
        for (int row = 0; row < treeArray.length; row++) {
            for (int col = 0; col < treeArray[row].length; col++) {
                if (row == rowNode && col == colNode) {
                    //-1 only right, 1 only left, 0 no children, 2 both children
                    if (childNo==-1){
                        treeArray[row+1][col+1]="\\";
                    }else if (childNo==1){
                        treeArray[row+1][col-1]="/";
                    }else if (childNo==2){
                        treeArray[row+1][col-1]="/";
                        treeArray[row+1][col+1]="\\";
                    }
                }
            }
        }
    }
    public int getTreeLevels(){
        int totalLevels = 0;
        String[][] treeArray = new String[100][100];
        readyArray(treeArray);
        byte currentLevelNumber = 0;
        LinkedList currentLevel = new LinkedList();
        LinkedList nextLevel = new LinkedList();

        currentLevel.add(this.root);

        for(treeArray[currentLevelNumber][50] = this.root.getInfo().toString(); !currentLevel.isEmpty(); nextLevel = new LinkedList()) {
            BSTNode currentNode;
            for(Iterator it = currentLevel.iterator(); it.hasNext();) {
                currentNode = (BSTNode)it.next();
                if(currentNode.getLeft() != null) {
                    nextLevel.add(currentNode.getLeft());
                }

                if(currentNode.getRight() != null) {
                    nextLevel.add(currentNode.getRight());
                }
            }
            ++totalLevels;
            currentLevel = nextLevel;
        }

        //System.out.println(totalLevels);
        printArray(treeArray);
        return totalLevels;
    }

    public void printArray(String[][] treeArray) {
        for(int i = 0; i < treeArray.length; ++i) {
            for(int j = 0; j < treeArray[i].length; ++j) {
                System.out.print(treeArray[i][j]);
            }

            System.out.println();
        }

    }

    public void readyArray(String[][] treeArray) {
        for(int i = 0; i < treeArray.length; ++i) {
            for(int j = 0; j < treeArray[i].length; ++j) {
                treeArray[i][j] = " ";
            }
        }

    }

    public void finalizeArrray(String[][] treeArray) {
        for(int row = 0; row < treeArray.length; ++row) {
            int count = 0;

            int i;
            for(i = 0; i < treeArray.length; ++i) {
                if(treeArray[i][row].equals(" ")) {
                    ++count;
                }
            }

            if(count == 100) {
                for(i = 0; i < treeArray.length; ++i) {
                    treeArray[row][i] = "";
                }
            }
        }

    }


















    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    public <T> void printNode(BSTNode<T> root) {
        int maxLevel = getTreeLevels();
        System.out.println(maxLevel);
        printNodeInternal(singletonList(root), 1, maxLevel);
    }

    private <T> void printNodeInternal(List<BSTNode<T>> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        printWhitespaces(firstSpaces);

        List<BSTNode<T>> newNodes = new ArrayList<BSTNode<T>>();
        for (BSTNode<T> node : nodes) {
            if (node != null) {
                System.out.print(node.getInfo());
                newNodes.add(node.getLeft());
                newNodes.add(node.getRight());
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }


            printWhitespaces(betweenSpaces);
        }


        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
               printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).getLeft() != null)
                    System.out.print("/");
                else
                    printWhitespaces(1);

                printWhitespaces(i + i - 1);

                if (nodes.get(j).getRight() != null)
                    System.out.print("\\");
                else
                    printWhitespaces(1);

                printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println("");
        }

        printNodeInternal(newNodes, level + 1, maxLevel);

    }
    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }









}




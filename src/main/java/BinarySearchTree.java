import com.opencsv.CSVWriter;

import java.io.*;
import java.math.BigDecimal;

/**
 * Lewe poddrzewo każdego węzła zawiera wyłącznie elementy o kluczach mniejszych niż klucz węzła
 * a prawe poddrzewo zawiera wyłącznie elementy o kluczach nie mniejszych niż klucz węzła
 */
public class BinarySearchTree<T extends Comparable<T>>{

    private static int comparisons;

    public static int getComparisons() {
        return comparisons;
    }
    public static void setZero() {
        comparisons = 0;
    }

    static class Node<T extends Comparable<T>> {
        T key;
        Node<T> left, right;

        // constructor
        Node(T key) {
            this.key = key;
            left = null;
            right = null;
        }

        private void printTree(OutputStreamWriter out) throws IOException {
            if (right != null) {
                right.printTree(out, true, "");
            }
            printNodeValue(out);
            if (left != null) {
                left.printTree(out, false, "");
            }
        }

        private void printNodeValue(OutputStreamWriter out) throws IOException {
            if (key == null) {
                out.write("<null>");
            } else {
                out.write(key.toString());
            }
            out.write('\n');
        }

        // use string and not string buffer on purpose as we need to change the indent at each recursion
        private void printTree(OutputStreamWriter out, boolean isRight, String indent) throws IOException {
            if (right != null) {
                right.printTree(out, true, indent + (isRight ? "        " : " |      "));
            }
            out.write(indent);
            if (isRight) {
                out.write(" /");
            } else {
                out.write(" \\");
            }
            out.write("----- ");
            printNodeValue(out);
            if (left != null) {
                left.printTree(out, false, indent + (isRight ? " |      " : "        "));
            }
        }
    }

    /* A recursive function to insert a new key in BST */
    public Node<T> insertNode(Node<T> root, T key) {

        /* If the tree is empty, return a new node */
        comparisons++;
        if (root == null) {
            root = new Node<>(key);
            return root;
        }

        /* Otherwise, recur down the tree */
        comparisons += 2;
        if (key.compareTo(root.key) < 0)
            root.left = insertNode(root.left, key);
        else if (key.compareTo(root.key) > 0)
            root.right = insertNode(root.right, key);

        return root;
    }

    public Node<T> deleteNode(Node<T> root, T key) {
        /* Base Case: If the tree is empty */
        comparisons++;
        if (root == null)
            return root;

        /* Otherwise, recur down the tree */
        comparisons += 3;
        if (key.compareTo(root.key) < 0)
            root.left = deleteNode(root.left, key);
        else if (key.compareTo(root.key) > 0)
            root.right = deleteNode(root.right, key);

        // if key is same as root's key, then this is the node to be deleted
        else {
            // node with only one child or no children
            comparisons += 2;
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            // node with two children: Get the inorder
            // successor (smallest in the right subtree)
            root.key = minValue(root.right);

            // Delete the inorder successor
            root.right = deleteNode(root.right, root.key);
        }

        return root;
    }

    T minValue(Node<T> root) {
        T minValue = root.key;
        while (root.left != null) {
            comparisons++;
            minValue = root.left.key;
            root = root.left;
        }
        return minValue;
    }

    int height(Node<T> node) {
        if (node == null)
            return -1;
        else {
            /* compute the depth of each subtree */
            int leftHeight = height(node.left);
            int rightHeight = height(node.right);

            /* use the larger one */
            if (leftHeight > rightHeight)
                return (leftHeight + 1);
            else
                return (rightHeight + 1);
        }
    }

    public static void main(String[] args) throws IOException {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        BinarySearchTree<String> binarySearchTree2 = new BinarySearchTree<>();

        OutputStream outputStream = new FileOutputStream("Test.txt");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        int[] ascendingArray = InputGenerator.ascendingGenerator(50);
        int[] randomArray = InputGenerator.randomGenerator(50);
        Node<String> root = new Node<>("Ala");
        binarySearchTree2.insertNode(root, "Bolek");
        binarySearchTree2.insertNode(root, "Hania");
        binarySearchTree2.insertNode(root, "Adam");
        root.printTree(outputStreamWriter);
        outputStreamWriter.close();

        generateOutput(binarySearchTree, ascendingArray, randomArray, "generated_tree.txt", 0);

        generateOutput(binarySearchTree, randomArray, randomArray, "generated_tree_case_2.txt", 1);

        convertToCSV("Results.csv");

    }

    private static void generateOutput(BinarySearchTree<Integer> binarySearchTree, int[] ascendingArray, int[] randomArray, String s, int i) throws IOException {
        OutputStream outputStream = new FileOutputStream(s);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        Node<Integer> root = new Node<>(i);

        for (int value : ascendingArray) {
            outputStreamWriter.write("Insert " + value);
            outputStreamWriter.write("\n");
            binarySearchTree.insertNode(root, value);
            root.printTree(outputStreamWriter);
            outputStreamWriter.write("Height: " + binarySearchTree.height(root) + "\n");
        }
        for (int value : randomArray) {
            outputStreamWriter.write("Delete " + value);
            outputStreamWriter.write("\n");
            binarySearchTree.deleteNode(root, value);
            root.printTree(outputStreamWriter);
            outputStreamWriter.write("Height: " + binarySearchTree.height(root) + "\n");
        }
        outputStreamWriter.close();
    }

    private static void convertBinarySearchTreeOutputToCSV(CSVWriter writer, long sumOfComparisons) {
        String[] data1;
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        for (int j = 1000; j <= 10000; j += 5000) {
            Node<Integer> root = new Node<>(1);
            for(int i = 0; i <= 100; i++) {
                // add data to csv
                int[] randomArray = InputGenerator.randomGenerator(j);
                int[] ascendingArray = InputGenerator.ascendingGenerator(j);
                for(int k: ascendingArray) {
                    binarySearchTree.insertNode(root, k);
                }
                for(int k: randomArray) {
                    binarySearchTree.deleteNode(root, k);
                }
                long comparisons = getComparisons();
                sumOfComparisons += comparisons;
                setZero();
            }
            double averageNumberOfComparisons = (double) sumOfComparisons /100;

            data1 = new String[]{"Binary Search Tree", new BigDecimal(j).toPlainString(), String.format("%.3f", averageNumberOfComparisons)};
            writer.writeNext(data1);

            sumOfComparisons = 0;
        }
    }
    public static void convertToCSV(String fileName) {
        File file = new File(fileName);

        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);
            String[] header = { "Tree","Size of generated array","Average Compares" };

            writer.writeNext(header);
            int sumOfComparisons = 0;
            convertBinarySearchTreeOutputToCSV(writer, sumOfComparisons);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
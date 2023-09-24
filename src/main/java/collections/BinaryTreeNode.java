/*
BinaryTreeNode.java

Brandon Euverman
T00741729
COMP 2231 Assignment 4

Represents a node in a binary tree
Found in chapter 19.7 of Java Foundations
 */

package collections;

public class BinaryTreeNode<T>
{
    protected T element;
    protected BinaryTreeNode<T> left, right;

    //Creates a new tree node with the specified data
    public BinaryTreeNode(T obj) {
        element = obj;
        left = null;
        right = null;
    }

    //Creates a new tree node with the specified data and subtrees
    public BinaryTreeNode(T obj, LinkedBinaryTree<T> left, LinkedBinaryTree<T> right) {
        element = obj;
        if (left == null)
            this.left = null;
        else
            this.left = left.getRootNode();

        if (right == null)
            this.right = null;
        else
            this.right = right.getRootNode();
    }

    //Returns the number of non-null children of the node
    public int numChildren() {
        int children = 0;

        if (left != null)
            children = 1 + left.numChildren();

        if (right != null)
            children = children + 1 + right.numChildren();

        return children;
    }

    //Return the element at this node
    public T getElement() {
        return element;
    }

    //Return the right child of the node
    public BinaryTreeNode<T> getRight() {
        return right;
    }

    //Set the right child of the node
    public void setRight(BinaryTreeNode<T> node) {
        right = node;
    }

    //Return the left child of the node
    public BinaryTreeNode<T> getLeft() {
        return left;
    }

    //Set the left child of the node
    public void setLeft(BinaryTreeNode<T> node) {
        left = node;
    }

    public String toString() {
        return element.toString();
    }
}
/*
LinkedBinaryTree.java

Brandon Euverman
T00741729
COMP 2231 Assignment 4

Implements a binary tree via links
Adapted from a partial implementation found in chapter 19.7 of Java Foundations
 */

package collections;

import collections.exceptions.*;
import java.util.*;

public class LinkedBinaryTree<T> implements BinaryTreeADT<T>, Iterable<T>
{
    protected BinaryTreeNode<T> root;
    protected int modCount;

    //Creates an empty binary tree
    public LinkedBinaryTree() {
        root = null;
    }

    //Creates a binary tree with the specified element as its root
    public LinkedBinaryTree(T element) {
        root = new BinaryTreeNode<>(element);
    }

    //Creates a binary tree with the specified element as its root and the
    //given trees as its left child and right child
    public LinkedBinaryTree(T element, LinkedBinaryTree<T> left, LinkedBinaryTree<T> right) {
        root = new BinaryTreeNode<>(element);
        root.setLeft(left.root);
        root.setRight(right.root);
    }

    //Returns a reference to the element at the root
    public T getRootElement() throws EmptyCollectionException {
        if (root == null)
            throw new EmptyCollectionException("binary tree");

        return root.element;
    }

    //Returns a reference to the node at the root
    protected BinaryTreeNode<T> getRootNode() throws EmptyCollectionException {
        if (root == null)
            throw new EmptyCollectionException("binary tree");

        return root;
    }

    //Returns the left subtree of the root of the tree
    public LinkedBinaryTree<T> getLeft() {
        LinkedBinaryTree<T> subtree = new LinkedBinaryTree<>();
        subtree.root = root.getLeft();

        return subtree;
    }

    //Returns the right subtree of the root of the tree
    public LinkedBinaryTree<T> getRight() {
        LinkedBinaryTree<T> subtree = new LinkedBinaryTree<>();
        subtree.root = root.getRight();

        return subtree;
    }

    //Returns true if the binary tree is empty and false otherwise
    public boolean isEmpty() {
        return (root == null);
    }

    //Returns the integer size of the tree
    public int size() {
        if (root == null)
            return 0;

        return 1 + getLeft().size() + getRight().size();
    }

    //Returns the height of the tree
    public int getHeight() {
        if (root == null)
            return -1;

        return Math.max(getLeft().getHeight(), getRight().getHeight()) + 1;
    }

    //Returns the height of the specified node
    private int height(BinaryTreeNode<T> node) {
        LinkedBinaryTree<T> subtree = new LinkedBinaryTree<>();
        subtree.root = node;

        return subtree.getHeight();
    }

    //Returns true if this tree contains an element that matches the
    //specified target element and false otherwise
    public boolean contains(T targetElement) {
        try {
            find(targetElement);
        }
        catch (ElementNotFoundException e) {
            return false;
        }

        return true;
    }

    //Returns a reference to the specified target element if it is
    //found in the binary tree. Throws a ElementNotFoundException if
    //the specified target element is not found in the binary tree
    public T find(T targetElement) throws ElementNotFoundException {
        BinaryTreeNode<T> current = findNode(targetElement, root);

        if (current == null)
            throw new ElementNotFoundException("LinkedBinaryTree");

        return (current.getElement());
    }

    //Returns a reference to the specified target element if it is found in this binary tree
    private BinaryTreeNode<T> findNode(T targetElement, BinaryTreeNode<T> next) {
        if (next == null)
            return null;

        if (next.getElement().equals(targetElement))
            return next;

        BinaryTreeNode<T> temp = findNode(targetElement, next.getLeft());

        if (temp == null)
            temp = findNode(targetElement, next.getRight());

        return temp;
    }

    //Returns a string representation of the binary tree showing the nodes in an inorder fashion.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        TreeIterator iterator = (TreeIterator) iteratorInOrder();

        while (iterator.hasNext()) {
            sb.append(iterator.next().toString()).append(" ");
        }

        return sb.toString();
    }

    //Returns an iterator over the elements in the tree using the iteratorInOrder method
    public Iterator<T> iterator() {
        return iteratorInOrder();
    }

    //Performs an inorder traversal on the binary tree by calling an
    //overloaded, recursive inorder method that starts with the root
    public Iterator<T> iteratorInOrder() {
        ArrayList<T> tempList = new ArrayList<>();
        inOrder(root, tempList);

        return new TreeIterator(tempList.iterator());
    }

    //Performs a recursive inorder traversal.
    protected void inOrder(BinaryTreeNode<T> node, ArrayList<T> tempList) {
        if (node != null) {
            inOrder(node.getLeft(), tempList);
            tempList.add(node.getElement());
            inOrder(node.getRight(), tempList);
        }
    }

    //Performs a preorder traversal on the binary tree by calling
    //an overloaded, recursive preorder method that starts with the root
    public Iterator<T> iteratorPreOrder() {
        ArrayList<T> tempList = new ArrayList<>();
        preOrder(root, tempList);

        return new TreeIterator(tempList.iterator());
    }

    //Performs a recursive preorder traversal
    protected void preOrder(BinaryTreeNode<T> node, ArrayList<T> tempList) {
        if (node != null) {
            tempList.add(node.getElement());
            preOrder(node.getLeft(), tempList);
            preOrder(node.getRight(), tempList);
        }
    }

    //Performs a postorder traversal on the binary tree by calling
    //an overloaded, recursive postorder method that starts with the root
    public Iterator<T> iteratorPostOrder() {
        ArrayList<T> tempList = new ArrayList<>();
        postOrder(root, tempList);

        return new TreeIterator(tempList.iterator());
    }

    //Performs a recursive postorder traversal
    protected void postOrder(BinaryTreeNode<T> node, ArrayList<T> tempList) {
        if (node != null) {
            preOrder(node.getLeft(), tempList);
            preOrder(node.getRight(), tempList);
            tempList.add(node.getElement());
        }
    }

    //Performs a levelorder traversal on the binary tree, using a templist
    public Iterator<T> iteratorLevelOrder() {
        ArrayList<BinaryTreeNode<T>> nodes = new ArrayList<>();
        ArrayList<T> tempList = new ArrayList<>();
        BinaryTreeNode<T> current;

        nodes.add(root);

        while (!nodes.isEmpty()) {
            current = nodes.remove(0);

            if (current != null) {
                tempList.add(current.getElement());
                if (current.getLeft() != null)
                    nodes.add(current.getLeft());
                if (current.getRight() != null)
                    nodes.add(current.getRight());
            }
            else
                tempList.add(null);
        }

        return new TreeIterator(tempList.iterator());
    }

    //Inner class to represent an iterator over the elements of the tree
    private class TreeIterator implements Iterator<T> {
        private int expectedModCount;
        private Iterator<T> iter;

        //Sets up this iterator using the specified iterator
        public TreeIterator(Iterator<T> iter) {
            this.iter = iter;
            expectedModCount = modCount;
        }

        //Returns true if this iterator has at least one more element to deliver in the iteration
        public boolean hasNext() throws ConcurrentModificationException {
            if (!(modCount == expectedModCount))
                throw new ConcurrentModificationException();

            return (iter.hasNext());
        }

        //Returns the next element in the iteration. If there are no
        //more elements in this iteration, a NoSuchElementException is thrown
        public T next() throws NoSuchElementException {
            if (hasNext())
                return (iter.next());
            else
                throw new NoSuchElementException();
        }

        //The remove operation is not supported.
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

/*
BinaryTreeADT.java

Brandon Euverman
T00741729
COMP 2231 Assignment 4

Defines the interface to a binary tree
Found in chapter 19.4 of Java Foundations
 */

package collections;

import java.util.Iterator;

public interface BinaryTreeADT<T>
{
    //Returns a reference to the root element
    T getRootElement();

    //Returns true if the binary tree is empty and false otherwise
    boolean isEmpty();

    //Returns the number of elements in this binary tree
    int size();

    //Returns true if the binary tree contains an element that matches the specified element and false otherwise.
    boolean contains(T targetElement);

    //Returns a reference to the specified element if it is found in the binary tree
    //Throws an exception if the specified element is not found
    T find(T targetElement);

    //Returns the string representation of the binary tree
    String toString();

    //Returns an iterator over the elements of the tree
    Iterator<T> iterator();

    //Returns an iterator that represents an inorder traversal on the binary tree.
    Iterator<T> iteratorInOrder();

    //Returns an iterator that represents a preorder traversal on the binary tree
    Iterator<T> iteratorPreOrder();

    //Returns an iterator that represents a postorder traversal on the binary tree
    Iterator<T> iteratorPostOrder();

    //Returns an iterator that represents a levelorder traversal on the binary tree
    Iterator<T> iteratorLevelOrder();
}

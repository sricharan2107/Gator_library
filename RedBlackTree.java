import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RedBlackTree {

	private MinHeap root;
	private MinHeap TNULL;
	public List<Integer> inOrderTraversal;

    // constructor to initialize the Red Black Tree values
	public RedBlackTree() {
		TNULL = new MinHeap();
		TNULL.color = 0;
		TNULL.left = null;
		TNULL.right = null;
		inOrderTraversal = new ArrayList<>();
		root = TNULL;
	}

	public void inOrderTraversalofTree(MinHeap root, HashMap<Integer, Integer> colorTree){
		if(root==null)return;
		inOrderTraversalofTree(root.left, colorTree);
		colorTree.put(root.bookId, root.color);
		inOrderTraversalofTree(root.right, colorTree);
		return;
	}
    // inserts book into the red black tree
	public MinHeap insert(int bookId, String bookName, String authorName, String availability) {
		MinHeap node = new MinHeap(bookId, bookName, authorName, availability);
		node.parent = null;
		node.left = TNULL;
		node.right = TNULL;
		node.color = 1;

		MinHeap y = null;
		MinHeap x = this.root;

		this.inOrderTraversal.add(node.bookId);
		Collections.sort(this.inOrderTraversal);
		while (x != TNULL) {
			y = x;
			if (node.bookId < x.bookId) {
				x = x.left;
			} else {
				x = x.right;
			}
		}

		node.parent = y;
		if (y == null) {
			root = node;
		} else if (node.bookId < y.bookId) {
			y.left = node;
		} else {
			y.right = node;
		}

		if (node.parent == null) {
			node.color = 0;
			return node;
		}

		if (node.parent.parent == null) {
			return node;
		}
		fixInsert(node);
		return node;
	}

	// Balance the node after insertion
	private void fixInsert(MinHeap k) {
		MinHeap u;
		while (k.parent.color == 1) {
			if (k.parent == k.parent.parent.right) {
				u = k.parent.parent.left;
				if (u.color == 1) {
					u.color = 0;
					k.parent.color = 0;
					k.parent.parent.color = 1;
					k = k.parent.parent;
				} else {
					if (k == k.parent.left) {
						k = k.parent;
						rightRotate(k);
					}
					k.parent.color = 0;
					k.parent.parent.color = 1;
					leftRotate(k.parent.parent);
				}
			} else {
				u = k.parent.parent.right;

				if (u.color == 1) {
					u.color = 0;
					k.parent.color = 0;
					k.parent.parent.color = 1;
					k = k.parent.parent;
				} else {
					if (k == k.parent.right) {
						k = k.parent;
						leftRotate(k);
					}
					k.parent.color = 0;
					k.parent.parent.color = 1;
					rightRotate(k.parent.parent);
				}
			}
			if (k == root) {
				break;
			}
		}
		root.color = 0;
	}

	// finds the book with given bookId and prints it
	public void findNode(MinHeap curr, int bookId){
		if (curr == null)
			return;
		// prints the books found in the given book number range
		if (bookId == curr.bookId ) {
            printNode(curr);
		}
		// first we recurse the left subtree if it has values greater than bookId
		if (bookId < curr.bookId) {
			findNode(curr.left, bookId);
		}
		// recurse the right subtree
		else findNode(curr.right, bookId);
	}

	private void findNodesInRangeHelper(MinHeap curr, int bookId1, int bookId2, ArrayList<MinHeap> listOfNodes){
		if (curr == null)
			return;
		// first we recurse the left subtree if it has values greater than bookNumber1
		if (curr.bookId > bookId1) {
			findNodesInRangeHelper(curr.left, bookId1, bookId2,listOfNodes);
		}
		// prints the books found in the given book number range
		if (bookId1 <= curr.bookId && bookId2 >= curr.bookId) {
			listOfNodes.add(curr);
		}
		// recurse the right subtree
		findNodesInRangeHelper(curr.right, bookId1, bookId2,listOfNodes);
	}
	// finds the books in range of the given book numbers
	public ArrayList<MinHeap> findNodesInRange(MinHeap curr, int bookId1, int bookId2){
		ArrayList<MinHeap> listOfNodes = new ArrayList<>();
		findNodesInRangeHelper(curr, bookId1, bookId2, listOfNodes);
		return listOfNodes;
	}
	
	// Search the tree for a given bookNumber
	private MinHeap searchHelper(MinHeap node, int bookId) {
		// return null if the search reached a null node
		if (node == TNULL)
			return null;

		// return the current node if it has the same book number
		if (bookId == node.bookId) {
			return node;
		}
		// recursively search the left subtree if the curr node book number is greater
		// than the passed value
		if (bookId < node.bookId) {
			return searchHelper(node.left, bookId);
		}
		// recursively search the right subtree
		return searchHelper(node.right, bookId);
	}

	// Balance the RBT after deletion of a book, using left and right rotations
	private void deleteFixup(MinHeap x) {
		MinHeap s;
		
		// Continue fixing the tree until the properties of the Red-Black Tree are restored
		while (x != root && x.color == 0) {
			// If the current node is the left child of its parent
			if (x == x.parent.left) {
				s = x.parent.right;
				
				// Case 1: If the sibling (s) is red
				if (s.color == 1) {
					s.color = 0;
					x.parent.color = 1;
					leftRotate(x.parent);
					s = x.parent.right;
				}
				// Case 2: If both children of s are black
				if (s.left.color == 0 && s.right.color == 0) {
					s.color = 1;
					x = x.parent;
				} else {
					// Case 3: If the right child of s is black
					if (s.right.color == 0) {
						s.left.color = 0;
						s.color = 1;
						rightRotate(s);
						s = x.parent.right;
					}
					
					// Case 4: Restructuring the tree
					s.color = x.parent.color;
					x.parent.color = 0;
					s.right.color = 0;
					leftRotate(x.parent);
					x = root;
				}
			} else {
				// If the current node is the right child of its parent (symmetric cases)
				s = x.parent.left;
				
				// Case 1: If the sibling (s) is red
				if (s.color == 1) {
					s.color = 0;
					x.parent.color = 1;
					rightRotate(x.parent);
					s = x.parent.left;
				}
				// Case 2: If both children of s are black
				if (s.left.color == 0 && s.right.color == 0) {
					s.color = 1;
					x = x.parent;
				} else {
					// Case 3: If the left child of s is black
					if (s.left.color == 0) {
						s.right.color = 0;
						s.color = 1;
						leftRotate(s);
						s = x.parent.left;
					}
					
					// Case 4: Restructuring the tree
					s.color = x.parent.color;
					x.parent.color = 0;
					s.left.color = 0;
					rightRotate(x.parent);
					x = root;
				}
			}
		}
		
		// Set the color of the final node to black to maintain the Red-Black Tree properties
		x.color = 0;
	}
	
	// delete the book with a given bookNumber from the tree
	private void deleteHelper(MinHeap node, int bookId) {
		MinHeap z = TNULL;
		MinHeap x, y;
		// search the tree for the book number that has to be deleted
		while (node != TNULL) {
			if (node.bookId == bookId) {
				z = node;
			}

			// Move to the left or right based on the comparison with bookId
			if (node.bookId <= bookId) {
				node = node.right;
			} else {
				node = node.left;
			}
		}

		// if search results in null, then node doesn't exist in the tree to delete
		if (z == TNULL) {
			System.out.println("Couldn't find key in the tree");
			return;
		}

		// logic to delete the node through transformations
		y = z;
		int yOriginalColor = y.color;
		if (z.left == TNULL) {
			x = z.right;
			rbTransform(z, z.right);
		} else if (z.right == TNULL) {
			x = z.left;
			rbTransform(z, z.left);
		} else {
			// If the node to be deleted has both left and right children
			y = minimum(z.right);
			yOriginalColor = y.color;
			x = y.right;
			if (y.parent == z) {
				x.parent = y;
			} else {
				rbTransform(y, y.right);
				y.right = z.right;
				y.right.parent = y;
			}

			rbTransform(z, y);
			y.left = z.left;
			y.left.parent = y;
			y.color = z.color;
		}
		// perform rotations to fix the tree if the node color is black
		if (yOriginalColor == 0) {
			deleteFixup(x);
		}
	}


	// transform the red black tree to satisfy constraints
	private void rbTransform(MinHeap node1, MinHeap node2) {
		if (node1.parent == null) {
			root = node2;
		} else if (node1 == node1.parent.left) {
			node1.parent.left = node2;
		} else {
			node1.parent.right = node2;
		}
		node2.parent = node1.parent;
	}

	// return the TreeNode found upon searching or null if no node is found for the
	// passed booknumber
	public MinHeap search(int bookId) {
		return searchHelper(this.root, bookId);
	}
	public void printNode(MinHeap curr){
			ArrayList<String> heap = new ArrayList<>();
			for(UserNode node: curr.reservationHeap){
				heap.add(node.userId);
			}
			String output = "BookID = " + curr.bookId + "\n" + "Title = " + curr.bookName+ "\n"+ "Author = "+ curr.authorName
                            + "\n"+ "Availability = " + curr.availability + "\n" + "BorrowedBy = "+curr.borrowedBy+"\n"+"Reservations = "+ heap.toString()+"\n";
            System.out.println(output);
	}
	// return the minimum node linked to a given node
	public MinHeap minimum(MinHeap node) {
		while (node.left != TNULL) {
			node = node.left;
		}
		return node;
	}

	// performs a right rotation to balance the red black tree
	public void rightRotate(MinHeap x) {
		MinHeap y = x.left;
		x.left = y.right;
		if (y.right != TNULL) {
			y.right.parent = x;
		}
		y.parent = x.parent;
		if (x.parent == null) {
			this.root = y;
		} else if (x == x.parent.right) {
			x.parent.right = y;
		} else {
			x.parent.left = y;
		}
		y.right = x;
		x.parent = y;
	}

	// performs a left rotation to balance the red black tree
	public void leftRotate(MinHeap x) {
		MinHeap y = x.right;
		x.right = y.left;
		if (y.left != TNULL) {
			y.left.parent = x;
		}
		y.parent = x.parent;
		if (x.parent == null) {
			this.root = y;
		} else if (x == x.parent.left) {
			x.parent.left = y;
		} else {
			x.parent.right = y;
		}
		y.left = x;
		x.parent = y;
	}


	// returns the root to the driver function
	public MinHeap getRoot() {
		return this.root;
	}

	// prints the books closest to book with bookId
	public ArrayList<Integer> printClosest(int bookId){
		int minBook = Integer.MAX_VALUE;
		ArrayList<Integer> minindices = new ArrayList<>();
		for(int val: inOrderTraversal){
			if(minBook>Math.abs(val-bookId)){
				minBook = Math.abs(val-bookId);
				minindices = new ArrayList<>();
				minindices.add(val);
			}
			else{
				if(minBook == Math.abs(val-bookId)){
					minindices.add(val);
				}
			}
		}
		
		return minindices;
	}

	// call the deleteHelper to perform delete
	public void deleteNode(int bookId) {
		deleteHelper(this.root, bookId);
		int delIndex = inOrderTraversal.indexOf(bookId);
		inOrderTraversal.remove(delIndex);
		Collections.sort(inOrderTraversal);
	}
}
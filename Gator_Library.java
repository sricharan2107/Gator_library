import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Main class to run the program
public class Gator_Library {
	public static void main(String[] args) throws IOException {

		PrintStream out = new PrintStream(new FileOutputStream("output.txt", true));
		System.setOut(out);
		

		// setting a flag 'terminate' to terminate code when duplicate ride is found
		boolean terminate = false;
		int ColorFlipCount = 0;
		List<String> inputList = new ArrayList<>();
		String input;
		RedBlackTree tree = new RedBlackTree();
		// setting the path of the output file
		try {
			// File file = new File("input2.txt");
			File file = new File(args[0]);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null)
				inputList.add(st);
			br.close();
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		try {
			for (String s : inputList) {
			input = s;
			String[] arr = input.split("\\(");
			input = arr[0].trim();
			switch (input) {
				case "PrintBook":
					// Splitting the input to extract book ID(s)
					String[] bookIds = arr[1].split("\\)");
					
					// Searching the tree for the book with the specified ID
					MinHeap book = tree.search(Integer.parseInt(bookIds[0].trim()));

					// If no such book is found, print an error message
					if (book == null) {
						System.out.println("Book " + bookIds[0].trim() + " not found in the Library\n");
					} else {
						// Printing the details of the found book
						tree.printNode(book);
					}

					// Exiting the switch statement
					break;

				case "PrintBooks":
					// Extracting book ID(s) and splitting based on ","
					bookIds = arr[1].split("\\)")[0].split(",");

					// Searching for book nodes within the specified range
					// and storing the results in the ArrayList foundNodes
					ArrayList<MinHeap> foundNodes = tree.findNodesInRange(tree.getRoot(),
							Integer.parseInt(bookIds[0]),
							Integer.parseInt(bookIds[1].trim()));

					// If no books are found in the given range, print an error message
					if (foundNodes.size() == 0) {
						System.out.println("No books are found in the given range\n");
					} else {
						// Printing the details of the found books
						for (MinHeap node : foundNodes) {
							tree.printNode(node);
						}
					}

					// Exiting the switch statement
					break;

				case "InsertBook":
					// Creating a HashMap to store the color information before the insertion
					HashMap<Integer, Integer> oldColorMap = new HashMap<>();
					tree.inOrderTraversalofTree(tree.getRoot(), oldColorMap);

					// Extracting book details and splitting based on ","
					String[] bookDetails = arr[1].split("\\)")[0].split(",");
					// Searching for the book with the specified ID
					book = tree.search(Integer.parseInt(bookDetails[0].trim()));

					// Checking if a book with the given ID already exists
					if (book != null) {
						// If a duplicate book is found, print an error message
						System.out.println("Duplicate Book\n");
						break;
					} else {
						// Inserting the new book into the Red-Black Tree
						tree.insert(Integer.parseInt(bookDetails[0].trim()),
									bookDetails[1],
									bookDetails[2],
									bookDetails[3]);
					}

					// Creating a new HashMap to store the color information after the insertion
					HashMap<Integer, Integer> newColorMap = new HashMap<>();
					tree.inOrderTraversalofTree(tree.getRoot(), newColorMap);

					// Comparing the color information before and after insertion
					for (Map.Entry<Integer, Integer> entry : newColorMap.entrySet()) {
						if (oldColorMap.containsKey(entry.getKey())) {
							// If there's a change in color, increment the ColorFlipCount
							if (oldColorMap.get(entry.getKey()) != entry.getValue()) {
								ColorFlipCount++;
							}
						}
					}

					// Exiting the switch statement
					break;

				case "BorrowBook":
					bookDetails = arr[1].split("\\)")[0].split(",");
					book = tree.search(Integer.parseInt(bookDetails[1].trim()));
					//book doesnt exist
					if (book == null) {
						System.out.println("Book does not exist\n");
						break;
					}
					//if book availabile
					if(book.availability.toString().trim().equals(new String("\"Yes\""))){
						book.availability = "No";
						book.borrowedBy = bookDetails[0];
						System.out.println("Book "+ bookDetails[1].trim()+ " Borrowed by Patron "+ bookDetails[0].trim()+"\n");
					}else{
						UserNode newPatron = new UserNode(bookDetails[0].trim(),
											 Integer.parseInt(bookDetails[2].trim()), 
											new Timestamp(System.currentTimeMillis()));
						book.reservationHeap.add(newPatron);
						System.out.println("Book "+ book.bookId +" Reserved by Patron " + bookDetails[0]+"\n");
					}

					break;

				case "ReturnBook":
					// Extracting book details and splitting based on ","
					bookDetails = arr[1].split("\\)")[0].split(",");
					// Searching for the book with the specified ID
					book = tree.search(Integer.parseInt(bookDetails[1].trim()));

					// Checking if the book exists
					if (book == null) {
						// If the book does not exist, print an error message
						System.out.println("Book does not exist\n");
						break;
					}

					// Printing the book return details
					System.out.println("Book " + bookDetails[1].trim() + " Returned by patron " + bookDetails[0] + "\n");

					// Checking if the book is available
					if (book.reservationHeap.isEmpty()) {
						book.availability = "\"Yes\"";
						book.borrowedBy = "None";
					} else {
						// Allocating the book to the next patron in the reservation heap
						UserNode newPatron = book.reservationHeap.poll();
						book.borrowedBy = newPatron.userId;
						System.out.println("Book " + book.bookId + " Allocated to Patron " + newPatron.userId + "\n");
					}

					// Exiting the switch statement
					break;


				case "DeleteBook":
					// Creating a HashMap to store the color information before the deletion
					oldColorMap = new HashMap<>();
					tree.inOrderTraversalofTree(tree.getRoot(), oldColorMap);

					// Extracting book ID and searching for the book with the specified ID
					String bookId = arr[1].split("\\)")[0];
					book = tree.search(Integer.parseInt(bookId.trim()));

					// Checking if the book exists
					if (book == null) {
						// If the book does not exist, print an error message
						System.out.println("Book does not exist\n");
						break;
					}

					// Constructing the display string for book availability and reservation cancellation details
					String displayString = "Book " + bookId + " is no longer available.";
					if (book.reservationHeap.size() >= 1) {
						if (book.reservationHeap.size() > 1) {
							displayString += " Reservations made by Patrons ";
							for (UserNode node : book.reservationHeap) {
								displayString += node.userId + " ";
							}
							displayString += "have been cancelled!";
						} else {
							displayString += " Reservation made by Patron " + book.reservationHeap.poll().userId + " has been cancelled!";
						}
					}
					displayString += "\n";
					System.out.println(displayString);

					// Deleting the book node from the Red-Black Tree
					tree.deleteNode(Integer.parseInt(bookId.trim()));

					// Creating a new HashMap to store the color information after the deletion
					newColorMap = new HashMap<>();
					tree.inOrderTraversalofTree(tree.getRoot(), newColorMap);

					// Comparing the color information before and after deletion
					for (Map.Entry<Integer, Integer> entry : newColorMap.entrySet()) {
						if (oldColorMap.containsKey(entry.getKey())) {
							// If there's a change in color, increment the ColorFlipCount
							if (oldColorMap.get(entry.getKey()) != entry.getValue()) {
								ColorFlipCount++;
							}
						}
					}

					// Exiting the switch statement
					break;


				case "FindClosestBook":
					// Extracting book ID and converting it to an integer
					bookId = arr[1].split("\\)")[0];
					ArrayList<Integer> arrayList = tree.printClosest(Integer.parseInt(bookId.trim()));

					// Printing details of the books closest to the specified book ID
					for (int book_id : arrayList) {
						// Searching for the book node with the current book ID
						MinHeap fNode = tree.search(book_id);
						// Printing details of the found book node
						tree.printNode(fNode);
					}

					// Exiting the switch statement
					break;

				case "ColorFlipCount":
					System.out.println("Color Flip Count: "+ ColorFlipCount+"\n");
					break;

				case "Quit":
					System.out.println("Program Terminated!!\n");
					// Exiting the program
					System.exit(0);
					// Exiting the switch statement
					break;

			}
			// terminating the program in case of inserting a duplicate ride
			if (terminate)
				break;
		}
		out.close();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Invalid Input\n");
			System.exit(0);
		}
		// processing the input
		
	}
}
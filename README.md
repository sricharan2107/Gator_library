# GatorLibrary
Library Management System using Red Black tree for management of books, patron and their borrowing options
Gator Library
This project implements a Library Management System called GatorLibrary, from where the patrons/users can borrow or reserve the books they want. The system is implemented by using Red Black trees and Min Heap.

Red Black trees
these are used to manage the books and store the details of the book in the library in an efficient way, where every book is a node of the red black tree.
Binary Min Heap
This is used to manage the reservation made by patrons for a book according to their priority. When a book is unavailable to be borrowed, the patron can reserve it. Every book in the library has a separate min heap to manage the reservation of the patrons for the book.

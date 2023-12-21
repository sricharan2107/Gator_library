JCFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JCFLAGS) $<

# Use a wildcard to find all Java source files in the current directory
SRCS = gator_Library.java RedBlackTree.java TreeNode.java UserNode.java

# Convert Java source files to class files
CLASSES = $(SRCS:.java=.class)

default: clean classes

classes: $(CLASSES)

clean:
	$(RM) *.class


import java.util.*;

public class LibraryManagement {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        Library library = new Library();
        Scanner sc = new Scanner(System.in);

        // Preloaded books
        library.addBook("Atomic Habits", "James Clear");
        library.addBook("1984", "George Orwell");
        library.addBook("Clean Code", "Robert C. Martin");
        library.addBook("The Alchemist", "Paulo Coelho");

        int choice;
        do {
            System.out.println("\n📖--- Library Management System ---");
            System.out.println("1. Register New User");
            System.out.println("2. List Available Books");
            System.out.println("3. Issue a Book");
            System.out.println("4. Return a Book");
            System.out.println("5. Show Issued Books");
            System.out.println("6. Show User Report");
            System.out.println("7. Admin Login");
            System.out.println("8. Exit");
            System.out.print("👉 Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("👤 Enter New User ID: ");
                    String newUserId = sc.next();
                    System.out.print("🧑 Enter User Name: ");
                    String newName = sc.next();
                    library.registerUser(newUserId, newName);
                    break;
                case 2:
                    library.listAvailableBooks();
                    break;
                case 3:
                    System.out.print("📘 Enter Book ID: ");
                    int bid = sc.nextInt();
                    System.out.print("👤 Enter User ID: ");
                    String uid = sc.next();
                    library.issueBook(bid, uid);
                    break;
                case 4:
                    System.out.print("📘 Enter Book ID: ");
                    int rbid = sc.nextInt();
                    System.out.print("👤 Enter User ID: ");
                    String ruid = sc.next();
                    library.returnBook(rbid, ruid);
                    break;
                case 5:
                    library.listIssuedBooks();
                    break;
                case 6:
                    System.out.print("👤 Enter User ID: ");
                    String uidRep = sc.next();
                    library.userReport(uidRep);
                    break;
                case 7:
                    sc.nextLine(); // consume newline
                    System.out.print("🔑 Enter admin username: ");
                    String username = sc.nextLine();
                    System.out.print("🔒 Enter admin password: ");
                    String password = sc.nextLine();
                    if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                        int adminChoice;
                        do {
                            System.out.println("\n👨‍💼 Admin Panel");
                            System.out.println("1. Add Book");
                            System.out.println("2. Remove Book");
                            System.out.println("3. Back to Main Menu");
                            System.out.print("🔧 Choose: ");
                            adminChoice = sc.nextInt();
                            switch (adminChoice) {
                                case 1:
                                    sc.nextLine(); // flush
                                    System.out.print("📗 Enter Book Title: ");
                                    String title = sc.nextLine();
                                    System.out.print("✍️ Enter Author: ");
                                    String author = sc.nextLine();
                                    library.addBook(title, author);
                                    break;
                                case 2:
                                    System.out.print("🗑️ Enter Book ID to Remove: ");
                                    int removeId = sc.nextInt();
                                    library.removeBook(removeId);
                                    break;
                                case 3:
                                    System.out.println("↩️ Returning to main menu...");
                                    break;
                                default:
                                    System.out.println("⚠️ Invalid choice.");
                            }
                        } while (adminChoice != 3);
                    } else {
                        System.out.println("🚫 Invalid admin credentials.");
                    }
                    break;
                case 8:
                    System.out.println("👋 Exiting Library System. Goodbye!");
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Try again.");
            }

        } while (choice != 8);
    }
}




// Book class
class Book {
    int id;
    String title;
    String author;
    boolean isIssued;
    String issuedTo;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = false;
        this.issuedTo = null;
    }

    public void display() {
        System.out.println((isIssued ? "❌ " : "✅ ") + "Book ID: " + id + ", Title: " + title + ", Author: " + author +
                (isIssued ? " [Issued to: " + issuedTo + "]" : ""));
    }
}

// User class
class User {
    String userId;
    String name;
    List<Integer> borrowedBooks;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public void displayBorrowedBooks() {
        System.out.println("📘 Books borrowed by " + name + ": " + borrowedBooks);
    }
}

// Library class
class Library {
    Map<Integer, Book> allBooks = new HashMap<>();
    Map<String, User> users = new HashMap<>();
    private int nextBookId = 5;

    public void addBook(String title, String author) {
        Book book = new Book(nextBookId++, title, author);
        allBooks.put(book.id, book);
        System.out.println("✅ Book added successfully with ID: " + book.id);
    }

    public void removeBook(int bookId) {
        if (allBooks.containsKey(bookId)) {
            if (allBooks.get(bookId).isIssued) {
                System.out.println("⚠️ Cannot remove book. It is currently issued.");
            } else {
                allBooks.remove(bookId);
                System.out.println("✅ Book removed successfully.");
            }
        } else {
            System.out.println("⚠️ Book ID not found.");
        }
    }

    public void registerUser(String userId, String name) {
        if (users.containsKey(userId)) {
            System.out.println("⚠️ User ID already exists.");
        } else {
            users.put(userId, new User(userId, name));
            System.out.println("✅ User registered successfully!");
        }
    }

    public void issueBook(int bookId, String userId) {
        if (!allBooks.containsKey(bookId)) {
            System.out.println("⚠️ Book not found.");
            return;
        }
        if (!users.containsKey(userId)) {
            System.out.println("⚠️ User not found.");
            return;
        }

        Book book = allBooks.get(bookId);
        if (book.isIssued) {
            System.out.println("🚫 Book is already issued.");
        } else {
            book.isIssued = true;
            book.issuedTo = userId;
            users.get(userId).borrowedBooks.add(bookId);
            System.out.println("✅ Book issued successfully to " + userId);
        }
    }

    public void returnBook(int bookId, String userId) {
        if (!allBooks.containsKey(bookId) || !users.containsKey(userId)) {
            System.out.println("⚠️ Invalid book or user ID.");
            return;
        }

        Book book = allBooks.get(bookId);
        User user = users.get(userId);

        if (!book.isIssued || !book.issuedTo.equals(userId)) {
            System.out.println("🚫 Book not issued to this user.");
        } else {
            book.isIssued = false;
            book.issuedTo = null;
            user.borrowedBooks.remove((Integer) bookId);
            System.out.println("✅ Book returned successfully by " + userId);
        }
    }

    public void listAvailableBooks() {
        System.out.println("\n📚 Available Books:");
        boolean found = false;
        for (Book book : allBooks.values()) {
            if (!book.isIssued) {
                book.display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No available books at the moment.");
        }
    }

    public void listIssuedBooks() {
        System.out.println("\n📕 Issued Books:");
        boolean found = false;
        for (Book book : allBooks.values()) {
            if (book.isIssued) {
                book.display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books are currently issued.");
        }
    }

    public void userReport(String userId) {
        if (users.containsKey(userId)) {
            users.get(userId).displayBorrowedBooks();
        } else {
            System.out.println("⚠️ User not found.");
        }
    }
}

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private List<Book> books;
    private FileHandler fileHandler;

    public LibraryManager() {
        books = new ArrayList<>();
        fileHandler = new FileHandler();
        loadBooks();
    }

    public void addBook(String bookId, String title, String author, String genre) {
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {
                throw new IllegalArgumentException("Book ID already exists!");
            }
        }
        Book book = new Book(bookId, title, author, genre);
        books.add(book);
        saveBooks();
    }

    public void updateBook(String bookId, String title, String author, String genre) {
        Book book = findBook(bookId);
        if (book != null) {
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            saveBooks();
        } else {
            throw new IllegalArgumentException("Book not found!");
        }
    }

    public void deleteBook(String bookId) {
        Book book = findBook(bookId);
        if (book != null) {
            books.remove(book);
            saveBooks();
        } else {
            throw new IllegalArgumentException("Book not found!");
        }
    }

    public void issueBook(String bookId) {
        Book book = findBook(bookId);
        if (book != null && "Available".equals(book.getStatus())) {
            book.setStatus("Issued");
            saveBooks();
        } else {
            throw new IllegalArgumentException("Book not available or not found!");
        }
    }

    public void returnBook(String bookId) {
        Book book = findBook(bookId);
        if (book != null && "Issued".equals(book.getStatus())) {
            book.setStatus("Available");
            saveBooks();
        } else {
            throw new IllegalArgumentException("Book not issued or not found!");
        }
    }

    public Book findBook(String bookId) {
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> searchBooks(String searchTerm) {
        List<Book> results = new ArrayList<>();
        searchTerm = searchTerm.toLowerCase();
        for (Book book : books) {
            if (book.getBookId().toLowerCase().contains(searchTerm) ||
                book.getTitle().toLowerCase().contains(searchTerm) ||
                book.getAuthor().toLowerCase().contains(searchTerm)) {
                results.add(book);
            }
        }
        return results;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    private void loadBooks() {
        books = fileHandler.loadBooks();
    }

    private void saveBooks() {
        fileHandler.saveBooks(books);
    }
}

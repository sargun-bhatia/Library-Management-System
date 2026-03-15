import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LibraryGUI extends JFrame {
    private LibraryManager libraryManager;
    
    // Form fields
    private JTextField bookIdField, titleField, authorField, searchField;
    private JComboBox<String> genreCombo;
    
    // Buttons
    private JButton addBtn, updateBtn, deleteBtn, clearBtn;
    private JButton issueBtn, returnBtn, searchBtn, refreshBtn;
    
    // Table
    private JTable booksTable;
    private DefaultTableModel tableModel;

    public LibraryGUI() {
        libraryManager = new LibraryManager();
        initializeGUI();
        loadBooksTable();
        setupListeners();
        setVisible(true);
    }

    private void initializeGUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Split main content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createLeftPanel());
        splitPane.setRightComponent(createRightPanel());
        splitPane.setDividerLocation(400);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        add(mainPanel);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Book Management"));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form fields
        formPanel.add(new JLabel("Book ID:"));
        bookIdField = new JTextField();
        formPanel.add(bookIdField);

        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        formPanel.add(authorField);

        formPanel.add(new JLabel("Genre:"));
        genreCombo = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Mystery", "Science", "Biography", "Other"});
        formPanel.add(genreCombo);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        addBtn = new JButton("Add Book");
        updateBtn = new JButton("Update Book");
        deleteBtn = new JButton("Delete Book");
        clearBtn = new JButton("Clear Fields");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);

        leftPanel.add(formPanel, BorderLayout.CENTER);
        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        // Books table
        String[] columns = {"Book ID", "Title", "Author", "Genre", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Books"));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout());
        refreshBtn = new JButton("Refresh");
        issueBtn = new JButton("Issue Book");
        returnBtn = new JButton("Return Book");
        
        actionPanel.add(refreshBtn);
        actionPanel.add(issueBtn);
        actionPanel.add(returnBtn);
        rightPanel.add(actionPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Search"));

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField(20);
        searchBtn = new JButton("Search");
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        bottomPanel.add(searchPanel, BorderLayout.CENTER);

        return bottomPanel;
    }

    private void setupListeners() {
        addBtn.addActionListener(e -> addBook());
        updateBtn.addActionListener(e -> updateBook());
        deleteBtn.addActionListener(e -> deleteBook());
        clearBtn.addActionListener(e -> clearFields());
        
        refreshBtn.addActionListener(e -> loadBooksTable());
        issueBtn.addActionListener(e -> issueBook());
        returnBtn.addActionListener(e -> returnBook());
        searchBtn.addActionListener(e -> searchBooks());
    }

    private void addBook() {
        try {
            String bookId = bookIdField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = (String) genreCombo.getSelectedItem();

            if (bookId.isEmpty() || title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            libraryManager.addBook(bookId, title, author, genre);
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBooksTable();
            clearFields();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        try {
            String bookId = bookIdField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = (String) genreCombo.getSelectedItem();

            if (bookId.isEmpty() || title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            libraryManager.updateBook(bookId, title, author, genre);
            JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBooksTable();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        String bookId = bookIdField.getText().trim();
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete book " + bookId + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                libraryManager.deleteBook(bookId);
                JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBooksTable();
                clearFields();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void issueBook() {
        String bookId = getSelectedBookId();
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a book from the table!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            libraryManager.issueBook(bookId);
            JOptionPane.showMessageDialog(this, "Book issued successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBooksTable();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {
        String bookId = getSelectedBookId();
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a book from the table!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            libraryManager.returnBook(bookId);
            JOptionPane.showMessageDialog(this, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBooksTable();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBooks() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadBooksTable();
            return;
        }
        List<Book> results = libraryManager.searchBooks(searchTerm);
        updateTable(results);
    }

    private void clearFields() {
        bookIdField.setText("");
        titleField.setText("");
        authorField.setText("");
        genreCombo.setSelectedIndex(0);
    }

    private String getSelectedBookId() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getValueAt(selectedRow, 0).toString();
        }
        return "";
    }

    private void loadBooksTable() {
        updateTable(libraryManager.getAllBooks());
    }

    private void updateTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            Object[] row = {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getStatus()
            };
            tableModel.addRow(row);
        }
    }
}

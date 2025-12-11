package kz.edu.java46.library.ui;

import kz.edu.java46.library.model.Loan;
import kz.edu.java46.library.model.Fine;
import kz.edu.java46.library.service.LibraryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class MainFrame extends JFrame {

    private final LibraryService libraryService;

    private final BookTableModel bookModel;
    private final ReaderTableModel readerModel;
    private final LoanTableModel loanModel;
    private final EnhancedFineTableModel fineModel;

    private final JTable bookTable;
    private final JTable readerTable;
    private final JTable loanTable;
    private final JTable fineTable;

    private final JTextField txtTitle = new JTextField(15);
    private final JTextField txtAuthor = new JTextField(15);
    private final JTextField txtIsbn = new JTextField(15);

    private final JTextField txtReaderName = new JTextField(15);
    private final JTextField txtReaderContact = new JTextField(15);

    private final JTextField txtLoanBookId = new JTextField(5);
    private final JTextField txtLoanReaderId = new JTextField(5);
    private final JTextField txtLoanDateIssued = new JTextField("2023-01-01", 10);
    private final JTextField txtLoanDateDue = new JTextField("2023-01-15", 10);

    private final JButton btnAddBook = new JButton("Add Book");
    private final JButton btnAddReader = new JButton("Add Reader");
    private final JButton btnIssueLoan = new JButton("Issue Loan");
    private final JButton btnReturnLoan = new JButton("Return Loan");
    private final JButton btnCalcFines = new JButton("Calculate Fines");
    private final JButton btnSaveAll = new JButton("Save All");

    private JTabbedPane tabbedPane;

    public MainFrame(LibraryService libraryService) throws Exception {
        this.libraryService = libraryService;


        this.bookModel = new BookTableModel();
        this.readerModel = new ReaderTableModel();
        this.loanModel = new LoanTableModel();

        this.fineModel = new EnhancedFineTableModel(libraryService);

        this.bookModel.setItems(libraryService.listBooks());
        this.readerModel.setItems(libraryService.listReaders());
        this.loanModel.setItems(libraryService.listLoans());
        this.fineModel.setItems(libraryService.listFines());

        this.bookTable = new JTable(this.bookModel);
        this.readerTable = new JTable(this.readerModel);
        this.loanTable = new JTable(this.loanModel);
        this.fineTable = new JTable(this.fineModel);

        initUI();
        initHandlers();
    }

    private void initUI() {
        setTitle("Библиотека");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        tabbedPane = createTabbedPanel();
        add(tabbedPane, BorderLayout.CENTER);

        this.pack();
        this.setMinimumSize(new Dimension(850, 650));
        this.setLocationRelativeTo(null);
    }

    private JPanel createControlPanel() {
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JPanel bookInputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        bookInputPanel.setBorder(BorderFactory.createTitledBorder("Добавить Книгу"));
        bookInputPanel.add(new JLabel("Title:")); bookInputPanel.add(txtTitle);
        bookInputPanel.add(new JLabel("Author:")); bookInputPanel.add(txtAuthor);
        bookInputPanel.add(new JLabel("ISBN:")); bookInputPanel.add(txtIsbn);
        bookInputPanel.add(new JPanel()); bookInputPanel.add(btnAddBook);
        mainPanel.add(bookInputPanel);

        JPanel readerInputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        readerInputPanel.setBorder(BorderFactory.createTitledBorder("Добавить Читателя"));
        readerInputPanel.add(new JLabel("Name:")); readerInputPanel.add(txtReaderName);
        readerInputPanel.add(new JLabel("Contact:")); readerInputPanel.add(txtReaderContact);
        readerInputPanel.add(new JPanel());
        readerInputPanel.add(new JPanel());
        readerInputPanel.add(new JPanel());
        readerInputPanel.add(btnAddReader);
        mainPanel.add(readerInputPanel);

        JPanel loanActionPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        loanActionPanel.setBorder(BorderFactory.createTitledBorder("Управление Займами"));
        loanActionPanel.add(new JLabel("BookId:")); loanActionPanel.add(txtLoanBookId);
        loanActionPanel.add(new JLabel("ReaderId:")); loanActionPanel.add(txtLoanReaderId);
        loanActionPanel.add(new JLabel("Issued:")); loanActionPanel.add(txtLoanDateIssued);
        loanActionPanel.add(new JLabel("Due:")); loanActionPanel.add(txtLoanDateDue);
        loanActionPanel.add(btnIssueLoan);
        loanActionPanel.add(btnReturnLoan);
        loanActionPanel.add(btnCalcFines);
        loanActionPanel.add(btnSaveAll);
        mainPanel.add(loanActionPanel);

        return mainPanel;
    }

    private JTabbedPane createTabbedPanel() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Книги", new JScrollPane(bookTable));
        tabs.addTab("Читатели", new JScrollPane(readerTable));
        tabs.addTab("Займы", new JScrollPane(loanTable));
        tabs.addTab("Штрафы", new JScrollPane(fineTable));
        return tabs;
    }

    private void initHandlers() {
        btnAddBook.addActionListener(e -> {
            try {
                libraryService.addBook(txtTitle.getText(), txtAuthor.getText(), txtIsbn.getText());
                JOptionPane.showMessageDialog(MainFrame.this, "Book added");
                refreshData();
            } catch (Exception ex) { showError("Error adding book", ex); }
        });

        btnAddReader.addActionListener(e -> {
            try {
                libraryService.addReader(txtReaderName.getText(), txtReaderContact.getText());
                JOptionPane.showMessageDialog(MainFrame.this, "Reader added");
                refreshData();
            } catch (Exception ex) { showError("Error adding reader", ex); }
        });

        btnIssueLoan.addActionListener(e -> {
            try {
                libraryService.issueLoan(
                        Long.parseLong(txtLoanBookId.getText()),
                        Long.parseLong(txtLoanReaderId.getText()),
                        LocalDate.parse(txtLoanDateIssued.getText()),
                        LocalDate.parse(txtLoanDateDue.getText())
                );
                JOptionPane.showMessageDialog(MainFrame.this, "Loan issued");
                refreshData();
            } catch (Exception ex) { showError("Error issuing loan", ex); }
        });

        btnReturnLoan.addActionListener(e -> {
            String loanId = JOptionPane.showInputDialog(MainFrame.this, "Enter Loan ID:");
            String date = JOptionPane.showInputDialog(MainFrame.this, "Return Date (YYYY-MM-DD):");
            if (loanId != null && date != null) {
                try {
                    libraryService.returnLoan(Long.parseLong(loanId), LocalDate.parse(date));
                    JOptionPane.showMessageDialog(MainFrame.this, "Loan returned");
                    refreshData();
                } catch (Exception ex) { showError("Error returning loan", ex); }
            }
        });

        btnCalcFines.addActionListener(e -> {
            try {
                for (Loan loan : libraryService.findAllOverdueLoans()) {
                    Fine f = libraryService.calculateFine(loan);
                    if (f.getAmount() > 0) libraryService.saveFine(f);
                }
                JOptionPane.showMessageDialog(MainFrame.this, "Fines calculated");
                refreshData();
            } catch (Exception ex) { showError("Error calculating fines", ex); }
        });

        btnSaveAll.addActionListener(e -> {
            try {
                libraryService.persistAll();
                JOptionPane.showMessageDialog(MainFrame.this, "Saved to file");
            } catch (Exception ex) { showError("Error saving", ex); }
        });
    }

    private void showError(String msg, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, msg + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void refreshData() {
        try {
            bookModel.setItems(libraryService.listBooks());
            readerModel.setItems(libraryService.listReaders());
            loanModel.setItems(libraryService.listLoans());
            fineModel.setItems(libraryService.listFines());

            bookTable.repaint();
            readerTable.repaint();
            loanTable.repaint();
            fineTable.repaint();
        } catch (Exception e) {
            showError("Error refreshing data", e);
        }
    }
}
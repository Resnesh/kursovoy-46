package kz.edu.java46.library;

import kz.edu.java46.library.service.LibraryService;
import kz.edu.java46.library.model.Book;
import kz.edu.java46.library.model.Reader;
import kz.edu.java46.library.model.Loan;
import kz.edu.java46.library.model.Fine;
import kz.edu.java46.library.storage.DataStorage;

import java.time.LocalDate;
import java.util.List;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        DataStorage storage = new DataStorage();
        LibraryService svc = new LibraryService(storage);

        if (svc.listBooks().isEmpty()) {
            Book b1 = svc.addBook("Clean Code", "Robert C. Martin", "978-0132350884");
            Book b2 = svc.addBook("Effective Java", "Joshua Bloch", "978-0134685991");
            System.out.println("Added books: " + b1 + ", " + b2);
        }

        if (svc.listReaders().isEmpty()) {
            Reader r = svc.addReader("Иван Иванов", "ivan@example.com");
            System.out.println("Added reader: " + r);
        }

        List<Reader> readers = svc.listReaders();
        List<Book> books = svc.listBooks();

        if (!readers.isEmpty() && !books.isEmpty()) {
            Loan loan = svc.issueLoan(
                    books.get(0).getId(),
                    readers.get(0).getId(),
                    LocalDate.now().minusDays(30),
                    LocalDate.now().minusDays(16)
            );
            System.out.println("Issued loan: " + loan);
        }

        List<Loan> overdue = svc.findAllOverdueLoans();
        for (Loan l : overdue) {
            Fine f = svc.calculateFine(l);
            if (f.getAmount() > 0) svc.saveFine(f);
            System.out.println("Loan " + l.getId() + " fine: " + f.getAmount());
        }

        System.out.println("Fines in DB: " + svc.listFines());
        System.out.println("App finished. H2 DB file is ./data/librarydb.mv.db");

        SwingUtilities.invokeLater(() -> {
            try {
                kz.edu.java46.library.ui.MainFrame frame = new kz.edu.java46.library.ui.MainFrame(svc);
                frame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Ошибка при запуске GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}

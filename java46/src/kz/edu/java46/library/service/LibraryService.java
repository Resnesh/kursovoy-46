package kz.edu.java46.library.service;

import kz.edu.java46.library.model.Book;
import kz.edu.java46.library.model.Fine;
import kz.edu.java46.library.model.Loan;
import kz.edu.java46.library.model.Reader;
import kz.edu.java46.library.repository.BookDao;
import kz.edu.java46.library.repository.FineDao;
import kz.edu.java46.library.repository.LoanDao;
import kz.edu.java46.library.repository.ReaderDao;
import kz.edu.java46.library.storage.DataStorage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * LibraryService — бизнес-логика для работы с книгами, читателями, выдачами и штрафами.
 * Исправления:
 * - добавлены перегруженные методы addBook(String,String,String) и addReader(String,String)
 *   чтобы UI мог вызывать эти методы напрямую (как в MainFrame).
 * - добавлен дополнительный конструктор, принимающий DAO напрямую (удобно для тестов).
 * - добавлены простые проверки null.
 */
public class LibraryService {

    private final BookDao bookDao;
    private final ReaderDao readerDao;
    private final LoanDao loanDao;
    private final FineDao fineDao;
    private final DataStorage dataStorage; // может быть null, если использован второй конструктор

    // Конструктор для инициализации DAOs и DataStorage
    public LibraryService(DataStorage dataStorage,
                          BookDao bookDao,
                          ReaderDao readerDao,
                          LoanDao loanDao,
                          FineDao fineDao) {
        this.dataStorage = dataStorage;
        this.bookDao = bookDao;
        this.readerDao = readerDao;
        this.loanDao = loanDao;
        this.fineDao = fineDao;

        if (this.dataStorage != null) {
            // Загружаем данные при создании сервиса (если DataStorage предоставлен)
            this.dataStorage.loadAllData();
        }
    }

    // Удобный конструктор — если вы хотите, чтобы сервис сам создавал DAO'шки (как было раньше)
    public LibraryService(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        if (this.dataStorage != null) this.dataStorage.loadAllData();
        this.bookDao = new BookDao();
        this.readerDao = new ReaderDao();
        this.loanDao = new LoanDao();
        this.fineDao = new FineDao();
    }


    public List<Book> listBooks() throws Exception {
        return bookDao.findAll();
    }

    public Book addBook(Book book) throws Exception {
        if (book == null) throw new IllegalArgumentException("book == null");
        return bookDao.save(book);
    }

    public Book addBook(String title, String author, String isbn) throws Exception {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return addBook(book);
    }

    public List<Reader> listReaders() throws Exception {
        return readerDao.findAll();
    }

    public Reader addReader(Reader reader) throws Exception {
        if (reader == null) throw new IllegalArgumentException("reader == null");
        return readerDao.save(reader);
    }

    public Reader addReader(String fullName, String contact) throws Exception {
        Reader reader = new Reader();
        reader.setFullName(fullName);
        reader.setContact(contact);
        return addReader(reader);
    }

    public Loan issueLoan(Long bookId, Long readerId, LocalDate dateIssued, LocalDate dateDue) throws Exception {
        if (bookId == null) throw new IllegalArgumentException("bookId == null");
        if (readerId == null) throw new IllegalArgumentException("readerId == null");
        Book book = bookDao.findById(bookId).orElseThrow(() -> new Exception("Book not found: " + bookId));
        Reader reader = readerDao.findById(readerId).orElseThrow(() -> new Exception("Reader not found: " + readerId));

        Loan loan = new Loan();
        loan.setBookId(book.getId());
        loan.setReaderId(reader.getId());
        loan.setDateIssued(dateIssued);
        loan.setDateDue(dateDue);
        loan.setDateReturned(null);

        return loanDao.save(loan);
    }

    public List<Loan> listLoans() throws Exception {
        return loanDao.findAll();
    }

    public Optional<Loan> findLoanById(Long loanId) throws Exception {
        return loanDao.findById(loanId);
    }

    public void returnLoan(Long loanId, LocalDate returned) throws Exception {
        if (loanId == null) throw new IllegalArgumentException("loanId == null");
        Optional<Loan> opt = loanDao.findById(loanId);
        if (opt.isPresent()) {
            Loan l = opt.get();
            l.setDateReturned(returned);
            loanDao.save(l);
        } else {
            throw new Exception("Loan not found: " + loanId);
        }
    }

    public List<Loan> findAllOverdueLoans() throws Exception {
        return loanDao.findAllOverdueLoans();
    }

    public Fine calculateFine(Loan loan) {
        if (loan == null) throw new IllegalArgumentException("loan == null");
        LocalDate due = loan.getDateDue();
        if (due == null) {
            Fine empty = new Fine();
            empty.setLoanId(loan.getId());
            empty.setAmount(0.0);
            empty.setPaid(false);
            empty.setDateCalculated(LocalDate.now());
            return empty;
        }

        LocalDate returned = loan.getDateReturned() != null ? loan.getDateReturned() : LocalDate.now();
        long days = ChronoUnit.DAYS.between(due, returned);
        double amount = days > 0 ? days * 10.0 : 0.0;

        Fine f = new Fine();
        f.setId(null);
        f.setLoanId(loan.getId());
        f.setAmount(amount);
        f.setPaid(false);
        f.setDateCalculated(LocalDate.now());
        return f;
    }

    public Fine saveFine(Fine f) throws Exception {
        if (f == null) throw new IllegalArgumentException("fine == null");
        return fineDao.save(f);
    }

    public List<Fine> listFines() throws Exception {
        return fineDao.findAll();
    }

    public void markFinePaid(Long fineId) throws Exception {
        if (fineId == null) throw new IllegalArgumentException("fineId == null");
        Optional<Fine> opt = fineDao.findById(fineId);
        if (opt.isPresent()) {
            Fine f = opt.get();
            f.setPaid(true);
            fineDao.save(f);
        } else {
            throw new Exception("Fine not found: " + fineId);
        }
    }

    public void persistAll() {
        if (dataStorage != null) dataStorage.saveAllData();
    }
}
package kz.edu.java46.library.ui;

import kz.edu.java46.library.model.Book;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;


public class BookTableModel extends AbstractTableModel {

    private List<Book> books = new ArrayList<>();
    private final String[] columnNames = {"ID", "Title", "Author", "ISBN", "Status"};

    public void setItems(List<Book> newBooks) {
        this.books = newBooks;
        fireTableDataChanged();
    }

    public Book getItem(int rowIndex) {
        return books.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);
        switch (columnIndex) {
            case 0: return book.getId();
            case 1: return book.getTitle();
            case 2: return book.getAuthor();
            case 3: return book.getIsbn();
            case 4: return book.getStatus();
            default: return null;
        }
    }
}
package kz.edu.java46.library;


public class Main {
    public static void main(String[] args) {
        try {
            App.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fatal error: " + e.getMessage());
            System.exit(1);
        }
    }
}
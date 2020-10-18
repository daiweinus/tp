package moneytracker;

import moneytracker.command.Command;
import moneytracker.exception.MoneyTrackerException;
import moneytracker.parser.Parser;
import moneytracker.storage.Storage;
import moneytracker.transaction.*;
import moneytracker.ui.Ui;

import java.time.LocalDate;

/**
 * Implements an application that allows users to manage monetary transactions.
 * It contains methods that allow the creation, modification and storage of monetary transactions.
 */
public class MoneyTracker {
    private final Storage storage;
    private final Ui ui;
    private TransactionList transactions;
    private CategoryList categories;

    /**
     * Initializes a <code>MoneyTracker</code> object.
     *
     * @param transactionsFilePath Path of the text file used for storing app data.
     */
    public MoneyTracker(String transactionsFilePath, String categoriesFilePath) {
        assert !transactionsFilePath.isBlank() : "filePath should not be blank";
        ui = new Ui();
        storage = new Storage(transactionsFilePath, categoriesFilePath);
        try {
            storage.createDirectory();
        } catch (MoneyTrackerException e) {
            ui.printError(e.getMessage());
        }
        try {
            categories = new CategoryList(storage.loadCategories(categoriesFilePath));
        } catch (MoneyTrackerException e) {
            ui.printError(e.getMessage());
            categories = new CategoryList();
        }
        try {
            transactions = new TransactionList(storage.loadTransactions(transactionsFilePath));
        } catch (MoneyTrackerException e) {
            ui.printError(e.getMessage());
            transactions = new TransactionList();
        }
    }

    public void run() {
        ui.printWelcome();
        double exp = calExpSummary();
        double inc = calIncSummary();
        ui.printSummary(exp, inc);

            boolean isExit = false;
            while (!isExit) {
                try {
                    String fullCommand = ui.readUserCommand();
                    Command c = Parser.parse(fullCommand);
                    c.execute(transactions, ui, storage, categories);
                    isExit = c.isExit();
                } catch (MoneyTrackerException e) {
                    ui.printError(e.getMessage());
                }
            }
        }

    private double calIncSummary() {
        double inc = 0;
        for (Transaction tran : transactions.getTransactions()) {
            if (LocalDate.now().getMonth() == tran.getLocalDate().getMonth()) {
                if (tran instanceof Income) {
                    inc += tran.getAmountNumber();
                }
            }
        }
        return inc;
    }



    private double calExpSummary() {
        double exp = 0;
        for (Transaction tran : transactions.getTransactions()) {
            if (LocalDate.now().getMonth() == tran.getLocalDate().getMonth()) {
                if (tran instanceof Expense) {
                    exp += tran.getAmountNumber();
                }
            }
        }
        return exp;
    }




    /**
     *  Main method of Money Tracker. This is the starting point of the app.
     *  @param args Command line arguments. Not used.
     */

    public static void main(String[] args) {
        new MoneyTracker("data/transactions.txt",
                "data/categories.txt").run();
    }
}
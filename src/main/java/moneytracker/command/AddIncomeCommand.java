package moneytracker.command;

import moneytracker.exception.MoneyTrackerException;
import moneytracker.parser.Parser;
import moneytracker.storage.Storage;
import moneytracker.transaction.ExpenseCategoryList;
import moneytracker.transaction.IncomeCategoryList;
import moneytracker.transaction.TransactionList;
import moneytracker.ui.Ui;

/**
 * Contains the methods for user to add an income.
 */
public class AddIncomeCommand extends Command {
    private final String fullCommand;

    /**
     * Initializes a <code>AddIncomeCommand</code> object.
     *
     * @param fullCommand User's full input string.
     */
    public AddIncomeCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the add income command.
     *
     * @param transactions List of <code>Transaction</code> objects.
     * @param ui <code>Ui</code> object for displaying user interactions.
     * @param storage <code>Storage</code> object for loading and saving user data.
     * @param incomeCategories List of income categories.
     * @param expenseCategories List of expense categories.
     */
    @Override
    public void execute(TransactionList transactions, Ui ui, Storage storage,
                        IncomeCategoryList incomeCategories,
                        ExpenseCategoryList expenseCategories) throws MoneyTrackerException {
        transactions.addTransaction(Parser.createIncome(fullCommand));
        storage.saveTransactions(transactions);
        ui.printAddedTransaction(transactions);
    }
}

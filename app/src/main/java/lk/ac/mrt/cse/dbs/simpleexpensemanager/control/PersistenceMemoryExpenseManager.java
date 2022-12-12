package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistenceMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistenceMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHelper;

public class PersistenceMemoryExpenseManager extends ExpenseManager{
    private Context context;

    public PersistenceMemoryExpenseManager(Context context) {
        super();
        this.context = context;
        setup();
    }
    @Override
    public void setup()  {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        TransactionDAO persistenceMemoryTransactionDAO = new PersistenceMemoryTransactionDAO(context,databaseHelper);
        setTransactionsDAO(persistenceMemoryTransactionDAO);

        AccountDAO persistenceMemoryAccountDAO = new PersistenceMemoryAccountDAO(context,databaseHelper);
        setAccountsDAO(persistenceMemoryAccountDAO);
        databaseHelper.close();

    }
}

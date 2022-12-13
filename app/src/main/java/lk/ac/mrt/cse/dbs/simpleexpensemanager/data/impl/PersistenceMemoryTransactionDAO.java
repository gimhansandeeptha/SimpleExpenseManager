package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.TransactionTable;

public class PersistenceMemoryTransactionDAO implements TransactionDAO {
    private ArrayList<Transaction> transactions;
    private DatabaseHelper databaseHelper;
    private Context context;
    private TransactionTable transactionTable;

    public PersistenceMemoryTransactionDAO(Context context, DatabaseHelper databaseHelper) {
        transactions = new ArrayList<Transaction>();
        this.databaseHelper = databaseHelper;
        this.context=context;
        transactionTable = new TransactionTable();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        PersistenceMemoryAccountDAO persistenceMemoryAccountDAO = new PersistenceMemoryAccountDAO(context,databaseHelper);

        ContentValues contentValues = new ContentValues();
        contentValues.put(transactionTable.getDate(),date.toString());
        contentValues.put(transactionTable.getAccountNo(),accountNo);
        contentValues.put(transactionTable.getExpenseType(),expenseType.toString());
        contentValues.put(transactionTable.getAmount(),amount);

        try {
            if (expenseType==ExpenseType.INCOME ||
                    (expenseType == ExpenseType.EXPENSE && persistenceMemoryAccountDAO.getAccount(accountNo).getBalance()-amount>0) ) {
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                long result = sqLiteDatabase.insert(transactionTable.getTableName(), null, contentValues);
                if (result == -1) {
                    Toast.makeText(context, "Failed to Add the Transaction", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Transaction Added Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(context, "Account balance is insufficient!", Toast.LENGTH_SHORT).show();
            }
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        Cursor cursor = databaseHelper.readAllData();
        Transaction transaction;
        if(!cursor.moveToFirst()){
            System.out.println("Error");
        }else{
            while (cursor.moveToNext()){
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(cursor.getString(1));
                String accountNo = cursor.getString(2);
                ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(3));
                Double amount = cursor.getDouble(4);

                transaction = new Transaction(date ,
                        accountNo,
                        expenseType,
                        amount);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> kq=getAllTransactionLogs();
        int size = kq.size();
        if (size <= limit) {
            return kq;
        }
        // return the last <code>limit</code> number of transaction logs
        List<Transaction> li1 =kq.subList(size - limit, size);
        return li1;
    }
}


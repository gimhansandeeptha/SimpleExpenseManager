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
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHelper;

public class PersistenceMemoryTransactionDAO implements TransactionDAO {
    ArrayList<Transaction> transactions;
    DatabaseHelper databaseHelper;
    private Context context;

    public PersistenceMemoryTransactionDAO(Context context, DatabaseHelper databaseHelper) {
        transactions = new ArrayList<Transaction>();
        this.databaseHelper = databaseHelper;
        this.context=context;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("date",date.toString());
        contentValues.put("account_no",accountNo);
        contentValues.put("type",expenseType.toString());
        contentValues.put("amount",amount);

        long result = sqLiteDatabase.insert("Transactions",null,contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
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
            Log.d("Gimhan","Size="+size);
            return kq;
        }
        // return the last <code>limit</code> number of transaction logs
        List<Transaction> li1 =kq.subList(size - limit, size);
        Log.d("Gimhan","Size="+li1.size());
        return li1;
    }
}

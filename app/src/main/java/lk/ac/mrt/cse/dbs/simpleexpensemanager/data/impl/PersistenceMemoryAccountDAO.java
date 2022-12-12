package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DatabaseHelper;

public class PersistenceMemoryAccountDAO implements AccountDAO {
    ArrayList<String> accountNumbersList = new ArrayList<>();
    ArrayList<Account> accountList = new ArrayList<>();
    private Context context;
    DatabaseHelper databaseHelper;

    public PersistenceMemoryAccountDAO(Context context, DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        this.context = context;
    }

    @Override

    public List<String> getAccountNumbersList() {
        accountNumbersList=new ArrayList<String>();
        try {
            SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query("Accounts", new String[]{"accountNO"},
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                accountNumbersList.add(cursor.getString(cursor.getColumnIndex("accountNO")));
            }
            cursor.close();
            sqLiteDatabase.close();
        }
        catch (SQLiteException ex){
            Toast toast = Toast.makeText(null,"Database is not available",Toast.LENGTH_SHORT); // null context problem
            toast.show();
        }

        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        accountList = new ArrayList<Account>();
        try {
            SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query("Accounts", new String[]{"accountNO","bankName","accountHolderName","balance"},
                    null, null, null, null, null);
            while (cursor.moveToNext()){
                String accountNO = cursor.getString(cursor.getColumnIndex("accountNO"));
                String bankName = cursor.getString(cursor.getColumnIndex("bankName"));
                String accountHolderName = cursor.getString(cursor.getColumnIndex("accountHolderName"));
                Double balance = cursor.getDouble(cursor.getColumnIndex("balance"));

                Account account = new Account(accountNO,bankName,accountHolderName,balance);
                accountList.add(account);

            }

            cursor.close();
            sqLiteDatabase.close();
        }
        catch (SQLiteException ex){
            Toast toast = Toast.makeText(context,"Database is not available",Toast.LENGTH_SHORT); // null context problem
            toast.show();
        }

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = null;  // check this
        try {
            SQLiteDatabase sqLiteDatabase1 = databaseHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase1.query("Accounts", new String[]{"accountNO","bankName","accountHolderName","balance"},
                    "accountNO = ?", new String[] {accountNo}, null, null, null);
            if (cursor.moveToNext()){
                String accountNO = cursor.getString(cursor.getColumnIndex("accountNO"));
                String bankName = cursor.getString(cursor.getColumnIndex("bankName"));
                String accountHolderName = cursor.getString(cursor.getColumnIndex("accountHolderName"));
                Double balance = cursor.getDouble(cursor.getColumnIndex("balance"));

                account = new Account(accountNO,bankName,accountHolderName,balance);

            }

            cursor.close();
            sqLiteDatabase1.close();
        }
        catch (SQLiteException ex){
            Toast toast = Toast.makeText(context,"Database is not available",Toast.LENGTH_SHORT); // null context problem
            toast.show();
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        databaseHelper.addAccount(account.getAccountNo(),account.getBankName(),
                account.getAccountHolderName(),account.getBalance());
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        databaseHelper.deleteRow("Accounts","accountNO",accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        ContentValues updateValues = new ContentValues();
        Account account = getAccount(accountNo);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        if ( expenseType == ExpenseType.EXPENSE) {
            updateValues.put("balance", account.getBalance() - amount);
        }
        else if ( expenseType == ExpenseType.INCOME){
            updateValues.put("balance", account.getBalance()+amount);
        }
        long p=sqLiteDatabase.update("Accounts",updateValues,"accountNO = ?",new String[] {accountNo});
        Log.d("Gimahn",String.valueOf(p));
        sqLiteDatabase.close();
    }
}

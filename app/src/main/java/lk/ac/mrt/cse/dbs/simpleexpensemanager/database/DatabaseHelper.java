package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DatabaseHelper extends SQLiteOpenHelper{
    private  static DatabaseHelper databaseHelper;
    private Context context;
    private static final String DATABASE_NAME = "200574N";
    private static final int DATABASE_VERSION = 7;


    public DatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        TransactionTable transactionTable = new TransactionTable();
        AccountTable accountTable = new AccountTable();

        String query1 = "CREATE TABLE " + transactionTable.getTableName() + "("+
                transactionTable.getTransactionId() + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                transactionTable.getDate() + " NUMERIC ," +
                transactionTable.getAccountNo() + " TEXT ," +
                transactionTable.getExpenseType() + " TEXT ," +
                transactionTable.getAmount() + " INTEGER );" ;
        sqLiteDatabase.execSQL(query1);

        String query2 = "CREATE TABLE " + accountTable.getTableName() + "("+
                accountTable.getAccountNo() + " TEXT PRIMARY KEY ," +
                accountTable.getBankName() + " TEXT ," +
                accountTable.getAccountHolderName() + " TEXT ," +
                accountTable.getBalance() + " INTEGER );" ;
        sqLiteDatabase.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        TransactionTable transactionTable = new TransactionTable();
        AccountTable accountTable = new AccountTable();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + transactionTable.getTableName()+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + accountTable.getTableName()+";");
        onCreate(sqLiteDatabase);

    }
    public void addTransaction (Date date, String account_no , ExpenseType type, double amount){
        TransactionTable transactionTable = new TransactionTable();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(transactionTable.getDate(), String.valueOf(date));
        contentValues.put(transactionTable.getAccountNo(),String.valueOf(account_no));
        contentValues.put(transactionTable.getExpenseType(), String.valueOf(type));
        contentValues.put(transactionTable.getAmount(), amount);

        long result = sqLiteDatabase.insert(transactionTable.getTableName(),null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    public  void addAccount(String accountNO,String bankName, String accountHolderName, double balance){
        AccountTable accountTable = new AccountTable();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(accountTable.getAccountNo(),accountNO);
        contentValues.put(accountTable.getBankName(),bankName);
        contentValues.put(accountTable.getAccountHolderName(),accountHolderName);
        contentValues.put(accountTable.getBalance(),balance);


        long result = sqLiteDatabase.insert(accountTable.getTableName(),null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }


    public Cursor readAllData(){
        TransactionTable transactionTable = new TransactionTable();
        String query = "SELECT * FROM " + transactionTable.getTableName() + " ;";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor= null;
        if(sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readColumn(String column){
        TransactionTable transactionTable = new TransactionTable();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(transactionTable.getTableName(),new String[] {column},null,null,null,null,null);
        return cursor;
    }

    public void deleteAllData(){
        TransactionTable transactionTable = new TransactionTable();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + transactionTable.getTableName() + " ;");
    }
    public void deleteRow(String tableName, String row, String value){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(tableName,row+" = ?", new String[] {value});

    }
}

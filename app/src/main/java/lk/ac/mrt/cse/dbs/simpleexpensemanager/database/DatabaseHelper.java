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
    private static final String DATABASE_NAME = "ExpenseDB";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_NAME = "Transactions";
    private static final String DATE = "date";
    private static final String ACCOUNT_NO = "account_no";
    private static final String TYPE = "type";
    private static final String AMOUNT = "amount";

    public DatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + "("+
                "_id " + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DATE + " NUMERIC ," +
                ACCOUNT_NO + " TEXT ," +
                TYPE + " TEXT ," +
                AMOUNT + " INTEGER );" ;
        sqLiteDatabase.execSQL(query);

        String query2 = "CREATE TABLE " + " Accounts" + "("+
                "_id " + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "bankName " + " TEXT ," +
                "accountNO " + " TEXT ," +
                "accountHolderName " + " TEXT ," +
                "balance " + " INTEGER );" ;
        sqLiteDatabase.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "Accounts;");
        onCreate(sqLiteDatabase);

    }
    public void addTransaction (Date date, String account_no , ExpenseType type, double amount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DATE, String.valueOf(date));
        contentValues.put(ACCOUNT_NO,String.valueOf(account_no));
        contentValues.put(TYPE, String.valueOf(type));
        contentValues.put(AMOUNT, amount);

        long result = sqLiteDatabase.insert(TABLE_NAME,null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    public  void addAccount(String accountNO,String bankName, String accountHolderName, double balance){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("accountNO",accountNO);
        contentValues.put("bankName",bankName);
        contentValues.put("accountHolderName",accountHolderName);
        contentValues.put("balance",balance);


        long result = sqLiteDatabase.insert("Accounts",null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }


    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME + " ;";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor= null;
        if(sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readColumn(String column){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,new String[] {column},null,null,null,null,null);
        return cursor;
    }

    public void deleteAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME + " ;");
    }
    public void deleteRow(String tableName, String row, String value){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(tableName,row+" = ?", new String[] {value});

    }
}

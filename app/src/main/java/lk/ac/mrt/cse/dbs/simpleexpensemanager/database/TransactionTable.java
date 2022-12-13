package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

public class TransactionTable implements  Table{
    private String tableName;
    private String transactionId;
    private  String date;
    private String accountNo;
    private String expenseType;
    private  String amount;

    public TransactionTable() {
        tableName="TRANSACTIONS";
        transactionId = "TRANSACTION_ID";
        date = "DATE";
        accountNo = "ACCOUNT_NUMBER";
        expenseType = "EXPENSE_TYPE";
        amount = "AMOUNT";
    }

    public String getTableName() {
        return tableName;
    }
    public String getTransactionId() {
        return transactionId;
    }

    public String getDate() {
        return date;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public String getAmount() {
        return amount;
    }


}
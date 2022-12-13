package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

public class AccountTable implements  Table{

    private String tableName;
    private  String accountNo;
    private String bankName;
    private  String accountHolderName;
    private String balance;

    public AccountTable() {
        tableName = "ACCOUNTS";
        accountNo = "ACCOUNT_NUMBER";
        bankName = "BANK_NAME";
        accountHolderName = "ACCOUNT_HOLDER";
        balance = "BALANCE";
    }

    public String getTableName() {
        return tableName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getBalance() {
        return balance;
    }

}
package dsme.myfinance.models;

public class Expense{

    String expenseName;
    int isRepeatingExpense;
    String expenseImage;
    float expenseAmount;
    String category;
    int isSaved = 1;
    String note;
    long timestamp;
    String userName;

    public Expense(String name, int isRepeating, long timestamp, String expenseImage, float amount, String category, String note){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.timestamp = timestamp;
        this.note = note;

        if(expenseImage != null) {
            this.expenseImage = expenseImage;
        }

        this.expenseAmount = amount;
        this.category = category;
    }


    public String getExpenseName() {
        return expenseName;
    }

    public long getTimestamp(){ return timestamp;}

    public void setTimestamp(long timestamp){ this.timestamp = timestamp; }

    public int isRepeatingExpenseBool() {
        return isRepeatingExpense;
    }

    public int getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(int isSaved) {
        this.isSaved = isSaved;
    }

    public String getExpenseImage() {
        return expenseImage;
    }

    public void setExpenseImage(String expenseImage) {
        this.expenseImage = expenseImage;
    }

    public double getExpenseAmount(){
        return this.expenseAmount;
    }

    public void setExpenseAmount(float amount){
        this.expenseAmount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserName(){
        return this.userName;
    }

    public int getIsRepeatingExpense(){ return isRepeatingExpense; }

    public void setIsRepeatingExpense(int isRepeatingExpense){this.isRepeatingExpense = isRepeatingExpense; }

    public String getNote(){ return note; }

    public void setNote(String note){ this.note = note; }

}
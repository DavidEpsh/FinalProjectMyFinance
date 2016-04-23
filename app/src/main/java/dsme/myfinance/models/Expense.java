package dsme.myfinance.models;

import java.sql.Date;

public class Expense{

    String expenseName;
    int isRepeatingExpense;
    String expenseImage;
    float expenseAmount;
    String category;
    int isSaved = 1;
    String note;
    long date;
    String userName;

    public Expense(String name, int isRepeating, long date, String expenseImage, float amount, String category, String userName){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.date = date;

        if(expenseImage != null) {
            this.expenseImage = expenseImage;
        }

        this.expenseAmount = amount;
        this.category = category;
    }


    public String getExpenseName() {
        return expenseName;
    }

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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUserName(){
        return this.userName;
    }

}
package dsme.myfinance.models;

public class Expense{

    String expenseName;
    int isRepeatingExpense;
    String expenseImage;
    float expenseAmount;
    String category;
    int isSaved = 1;
    String note;
    String mongoId;
    long date;
    String userName;

    public Expense(String mongoId, String name, long date, int isRepeating,  String expenseImage, float amount, String category, String note){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.mongoId = mongoId;
        this.date = date;
        this.note = note;

        if(expenseImage != null) {
            this.expenseImage = expenseImage;
        }

        this.expenseAmount = amount;
        this.category = category;
    }

    public Expense(String name, long date, int isRepeating,  String expenseImage, float amount, String category, String note){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.date = date;
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

    public String getMongoId(){ return mongoId;}

    public void setMongoId(String mongoId){ this.mongoId = mongoId; }

    public long getDate(){ return date;}

    public void setDate(long date){ this.date = date; }

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

    public float getExpenseAmount(){
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
package dsme.myfinance;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import dsme.myfinance.models.Expense;
import dsme.myfinance.models.Model;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class TransactionEditActivity extends AppCompatActivity {

    private Button saveButton;
    private Button cancelButton;
    private Button dateButton;
    private Button timeButton;
    private Button imageButton;
    private Spinner categoryButton;
    private EditText expenseAmount;
    private CheckBox isRepeating;
    private ImageView imageView;
    private TextView noteTextView;
    private EditText descriptionEditText;
    private Expense mExpense;
    String imagePath;
    GregorianCalendar cal;
    long id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);

        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        dateButton = (Button) findViewById(R.id.dateButton);
        timeButton = (Button) findViewById(R.id.timeButton);
        imageButton = (Button) findViewById(R.id.addImageButton);
        imageView = (ImageView) findViewById(R.id.addImageView);
        isRepeating = (CheckBox) findViewById(R.id.repeatingCheckBox);
        expenseAmount = (EditText) findViewById(R.id.amountEditText);
        categoryButton = (Spinner) findViewById(R.id.categorySpinner);
        noteTextView = (EditText) findViewById(R.id.noteAutoCompleteTextView);

        initializeSpinner();

        if(getIntent() != null) { // This handles the case were the user opened an existing expense
            long expenseId = (getIntent().getLongExtra(MainActivity.EXPENSE_ID, 0));

            if (expenseId != 0) {
                mExpense = Model.instance().getExpense(expenseId);
                id = mExpense.getTimestamp();
                expenseAmount.setText(Float.toString(mExpense.getExpenseAmount()));
                descriptionEditText.setText(mExpense.getExpenseName());
                noteTextView.setText(mExpense.getNote());

                cal = new GregorianCalendar();
                cal.setTimeInMillis(mExpense.getDate());

                if (mExpense.getIsRepeatingExpense() == 1) {
                    isRepeating.setChecked(true);
                }

                List<String> categories = (ArrayList) Model.instance().getAllCategories();
                categoryButton.setSelection(categories.indexOf(mExpense.getCategory()));

                if (mExpense.getExpenseImage() != null) {
                    setImage(mExpense.getExpenseImage());
                }

                setCalender(false);

            } else {
                setCalender(true);
            }
        }else{
            setCalender(true);
        }


        EasyImage.configuration(this).setImagesFolderName("My Finance");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(TransactionEditActivity.this, "Please Choose image source", 1);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setCalender(boolean setNewCalendar){

        if(setNewCalendar) {
            cal = new GregorianCalendar();
        }

        dateButton.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR));
        int minute = cal.get(Calendar.MINUTE);

        if(minute >= 0 && minute <= 10){
            timeButton.setText(cal.get(Calendar.HOUR_OF_DAY)+ ":" + "0" + minute);
        }else {
            timeButton.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + minute);
        }

        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                dateButton.setText(dayOfMonth + "/" + (monthOfYear+ 1) + "/" + year);
                cal.set(year, monthOfYear,dayOfMonth);
            }
        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });


        final TimePickerDialog timePickerDialog =new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeButton.setText(hourOfDay + ":" + minute);
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
            }
        },cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

    }


    public void saveExpense(){
        Expense mExpense;
        int repeating;
        long timestamp;

        if (id > 0) {
            timestamp = id;
        }else{
            timestamp = GregorianCalendar.getInstance().getTimeInMillis();
        }

        long date = cal.getTimeInMillis();

        if(isRepeating.isChecked()){
            repeating = 1;
        }else { repeating = 0;};

        mExpense = new Expense(timestamp,
                descriptionEditText.getText().toString(),
                date,
                repeating,
                imagePath,
                Float.valueOf(expenseAmount.getText().toString()),
                categoryButton.getSelectedItem().toString(),
                noteTextView.getText().toString());

        Model.instance().addExpense(mExpense);
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("result", MainActivity.RESULT_ADD_EXPENSE);
//        setResult(this.RESULT_OK, returnIntent);
        finish();
    }

    public void initializeSpinner(){
        final List<String> categories = Model.instance().getAllCategories();
        categories.add("Add Category");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categoryButton.setAdapter(spinnerAdapter);

        categoryButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == categories.size()-1){
                    buildAlertDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void buildAlertDialog(){
        final EditText txtUserName = new EditText(this);
        txtUserName.setHint("Enter Category Name");

        new AlertDialog.Builder(this)
                .setTitle("Add a new category")
                .setView(txtUserName)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Model.instance().addCategory(txtUserName.getText().toString());
                        ((ArrayAdapter) categoryButton.getAdapter()).clear();
                        final List<String> categories = Model.instance().getAllCategories();
                        categories.add("Add Category");
                        ((ArrayAdapter) categoryButton.getAdapter()).addAll(categories);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                imagePath = imageFile.getPath();
                setImage(imagePath);

            }
        });
    }

    public void setImage(String imagePath){
        Bitmap imageBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 15;
        imageBitmap = BitmapFactory.decodeFile(imagePath, options);
        imageView.setImageBitmap(imageBitmap);
        imageView.setScaleX(3);
        imageView.setScaleY(3);
        imageView.setRotation(90);
    }
}
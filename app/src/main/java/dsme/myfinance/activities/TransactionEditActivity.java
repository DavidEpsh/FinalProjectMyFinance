package dsme.myfinance.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import dsme.myfinance.R;
import dsme.myfinance.models.Expense;
import dsme.myfinance.models.Model;
import dsme.myfinance.models.ModelCloudDB;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class TransactionEditActivity extends AppCompatActivity {

    private Button saveButton;
    private Button cancelButton;
    private Button dateButton;
    private Button timeButton;
    private ImageButton deleteExpense;
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
    boolean updateExistingExpense = false;
    String mongoId;


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
        deleteExpense = (ImageButton) findViewById(R.id.delete_expense_button);

        initializeSpinner();

        if(getIntent() != null) { // This handles the case were the user opened an existing expense
            String expenseId = (getIntent().getStringExtra(MainActivity.EXPENSE_ID));

            if (expenseId != null) {
                mExpense = Model.instance().getExpense(expenseId);
                mongoId = mExpense.getMongoId();
                expenseAmount.setText(Float.toString(mExpense.getExpenseAmount()));
                descriptionEditText.setText(mExpense.getExpenseName());
                noteTextView.setText(mExpense.getNote());
//                deleteExpense.setEnabled(true);
                deleteExpense.setVisibility(View.VISIBLE);

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
                updateExistingExpense = true;
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePath != null) {
                    showImageDialog();
                }
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

        if (checkFields()) {
            long date = cal.getTimeInMillis();

            if (isRepeating.isChecked()) {
                repeating = 1;
            } else {
                repeating = 0;
            }

            if (mongoId != null){
                mExpense = new Expense(mongoId,
                        descriptionEditText.getText().toString(),
                        date,
                        repeating,
                        imagePath,
                        Float.valueOf(expenseAmount.getText().toString()),
                        categoryButton.getSelectedItem().toString(),
                        noteTextView.getText().toString());
            }else {
                mExpense = new Expense(descriptionEditText.getText().toString(),
                        date,
                        repeating,
                        imagePath,
                        Float.valueOf(expenseAmount.getText().toString()),
                        categoryButton.getSelectedItem().toString(),
                        noteTextView.getText().toString());
            }

//            if (updateExistingExpense){
//            Model.instance().updateOrAddExpense(mExpense, false);
//            }else {
//                Model.instance().addExpense(mExpense);
//            }
//
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("result", MainActivity.RESULT_ADD_EXPENSE);
//        setResult(this.RESULT_OK, returnIntent);
//            new ModelCloudDB().new AddNewExpenseToCloud().execute(mExpense);

            new ModelCloudDB().new
                    AddNewExpenseToCloud(){
                        @Override
                        protected void onPostExecute(String result){
                            if (!result.equals("Did not work!")){
                                Toast.makeText(getApplicationContext() ,"Successfully added", Toast.LENGTH_LONG);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext() ,"Couldn't add expense, please try again", Toast.LENGTH_LONG);
                            }
                        }
                    }.execute(mExpense);
        }
    }

    public boolean checkFields(){
    if(expenseAmount.getText().toString().length() == 0) {
        Toast.makeText(this, "Please add expense amount", Toast.LENGTH_LONG).show();
        return false;
    }else if(descriptionEditText.getText().toString().length() == 0) {
        Toast.makeText(this, "Please add a description", Toast.LENGTH_LONG).show();
        return false;
    }else
        return true;
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
        this.imagePath = imagePath;
        Bitmap imageBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 15;
        imageBitmap = BitmapFactory.decodeFile(imagePath, options);
        imageView.setImageBitmap(imageBitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleX(3);
        imageView.setScaleY(3);
    }

    public void showImageDialog() {
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();

        while (bitmapHeight > (screenHeight - 250) || bitmapWidth > (screenWidth - 250)) {
            bitmapHeight = bitmapHeight / 2;
            bitmapWidth = bitmapWidth / 2;
        }
        BitmapDrawable resizedBitmap = new BitmapDrawable(this.getResources(), Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, false));

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_popup);

        ImageView image = (ImageView) dialog.findViewById(R.id.imageview);
        image.setBackground(resizedBitmap);
        dialog.getWindow().setBackgroundDrawable(null);

        dialog.show();
    }
}
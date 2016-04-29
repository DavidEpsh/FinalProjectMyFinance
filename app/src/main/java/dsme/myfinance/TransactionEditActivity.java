package dsme.myfinance;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    String imagePath;
    GregorianCalendar cal;


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

        setCalender();
        initializeSpinner();

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

    private void setCalender(){
        cal = new GregorianCalendar();
        dateButton.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR));
        timeButton.setText(cal.get(Calendar.HOUR_OF_DAY)+ ":" + cal.get(Calendar.MINUTE));

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

        long timestamp = GregorianCalendar.getInstance().getTimeInMillis();
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
                "test", //categoryButton.toString(),
                noteTextView.getText().toString());

        Model.instance().addExpense(mExpense);
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("result", MainActivity.RESULT_ADD_EXPENSE);
//        setResult(this.RESULT_OK, returnIntent);
        finish();
    }

    public void initializeSpinner(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Model.instance().getAllCategories());
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categoryButton.setAdapter(spinnerAdapter);
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
                Bitmap imageBitmap = null;
                imagePath = imageFile.getPath();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 15;
                imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                imageView.setImageBitmap(imageBitmap);
                imageView.setScaleX(3);
                imageView.setScaleY(3);
                imageView.setRotation(90);
            }
        });
    }
}
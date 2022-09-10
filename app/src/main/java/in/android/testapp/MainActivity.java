package in.android.testapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView placeSpinner, btnPdfDownload, btnAddData, btnExcelDownload;
    Button btnNext, btnValidation, btnBarChart, btnPieChart, btnRadarChart;
    EditText edtName, edtAge, edtJob;
    PdfDocument pdfDocument;
    File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
    ArrayList<Data> arrayList;
    ArrayList<String> cityList;
    Dialog dialog;
    public static ArrayList<BarEntry> visitors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();

        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);
        edtJob = findViewById(R.id.edtJob);


        btnPdfDownload = findViewById(R.id.btnPdfDownload);
        btnAddData = findViewById(R.id.btnAddExcel);
        btnExcelDownload = findViewById(R.id.btnExcelDownload);
        btnNext = findViewById(R.id.next);
        btnValidation = findViewById(R.id.validate);


        btnBarChart = findViewById(R.id.btnBarChart);
        btnPieChart = findViewById(R.id.btnPieChart);
        btnRadarChart = findViewById(R.id.btnRadarChart);


        placeSpinner = findViewById(R.id.placeSpinner);
        placeSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchableSpinner();
            }
        });

        cityListInit();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        btnPdfDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdf();
            }
        });

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String age = edtAge.getText().toString();
                String place = placeSpinner.getText().toString();
                String job = edtJob.getText().toString();

                arrayList.add(new Data(name, age, place, job));
                Toast.makeText(MainActivity.this, "Data added", Toast.LENGTH_SHORT).show();
            }
        });

        btnExcelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createExcel();
            }
        });

        btnNext.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PdfDownloadActivity.class);
            startActivity(intent);
        });

        btnValidation.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        btnBarChart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BarChartActivity.class);
            startActivity(intent);
        });

        btnPieChart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PieChartActivity.class);
            startActivity(intent);
        });

        btnRadarChart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RadarChartActivity.class);
            startActivity(intent);
        });

    }

    private void searchableSpinner() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(650,1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();

        EditText edtCity = dialog.findViewById(R.id.edtCity);
        ListView listView = dialog.findViewById(R.id.list_view);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, cityList);
        listView.setAdapter(arrayAdapter);

        edtCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                placeSpinner.setText(arrayAdapter.getItem(i));
                dialog.dismiss();
            }
        });
    }

    public static void init(){
        visitors = new ArrayList<>();
        visitors.add(new BarEntry(2015,435));
        visitors.add(new BarEntry(2016,445));
        visitors.add(new BarEntry(2017,455));
        visitors.add(new BarEntry(2018,475));
        visitors.add(new BarEntry(2019,535));
        visitors.add(new BarEntry(2020,485));
        visitors.add(new BarEntry(2021,515));
    }


    private void cityListInit() {
        cityList = new ArrayList<>();
        cityList.add("Ahmedabad");
        cityList.add("Hyderabad");
        cityList.add("Bangalore");
        cityList.add("Delhi");
        cityList.add("Chennai");
        cityList.add("Mumbai");
        cityList.add("Kolkata");
        cityList.add("Surat");
        cityList.add("Pune");
        cityList.add("Indore");
        cityList.add("Bhopal");
        cityList.add("Jabalpur");
    }

    public void createPdf() {
        pdfDocument = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(720, 1080, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paintText = new Paint();
        paintText.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL));
        paintText.setTextSize(25);
        paintText.setColor(ContextCompat.getColor(this, R.color.black));
        paintText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("DATA", 396, 50, paintText);

        paintText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        paintText.setColor(ContextCompat.getColor(this, R.color.black));
        paintText.setTextSize(16);
        paintText.setTextAlign(Paint.Align.LEFT);

        canvas.drawText(((EditText)findViewById(R.id.edtName)).getText().toString(), 50, 100, paintText);
        canvas.drawText(((EditText)findViewById(R.id.edtAge)).getText().toString(), 50, 120, paintText);
        canvas.drawText(((TextView)findViewById(R.id.placeSpinner)).getText().toString(), 50, 140, paintText);
        canvas.drawText(((EditText)findViewById(R.id.edtJob)).getText().toString(), 50, 160, paintText);

        pdfDocument.finishPage(page);
        createPdfFile();
    }

    private void createPdfFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "data.pdf");
        startActivityForResult(intent, 1);
    }

    public void createExcel(){
        Log.d("heloo", "this worked");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Custom Sheet");

        HSSFRow row = sheet.createRow(0);

        HSSFCell cellName = row.createCell(0);
        cellName.setCellValue("Name");

        HSSFCell cellAge = row.createCell(1);
        cellAge.setCellValue("Age");

        HSSFCell cellPlace = row.createCell(2);
        cellPlace.setCellValue("Place");

        HSSFCell cellJob = row.createCell(3);
        cellJob.setCellValue("Job");

        for (int i = 0; i < arrayList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);

            cellName = (HSSFCell) row1.createCell(0);
            cellName.setCellValue(arrayList.get(i).getName());

            cellAge = (HSSFCell) row1.createCell(1);
            cellAge.setCellValue(arrayList.get(i).getAge());

            cellPlace = (HSSFCell) row1.createCell(2);
            cellPlace.setCellValue(arrayList.get(i).getPlace());

            cellJob = (HSSFCell) row1.createCell(3);
            cellJob.setCellValue(arrayList.get(i).getJob());


            sheet.setColumnWidth(0, (20 * 200));
            sheet.setColumnWidth(1, (30 * 200));
            sheet.setColumnWidth(2, (30 * 200));

        }

        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            Toast.makeText(this, "Excel Created in "+ filePath, Toast.LENGTH_SHORT).show();

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri uri = null;
            if(data != null){
                uri = data.getData();

                if(pdfDocument != null){
                    ParcelFileDescriptor pfd = null;
                    try{
                        pfd = getContentResolver().openFileDescriptor(uri,"w");
                        FileOutputStream outputStream = new FileOutputStream(pfd.getFileDescriptor());
                        pdfDocument.writeTo(outputStream);
                        pdfDocument.close();
                        Toast.makeText(this,"Pdf Saved Successfully", Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                        try {
                            DocumentsContract.deleteDocument(getContentResolver(),uri);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
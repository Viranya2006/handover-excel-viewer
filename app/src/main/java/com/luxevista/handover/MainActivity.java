package com.luxevista.handover;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Uri excelFileUri;
    private List<List<String>> excelData = new ArrayList<>();
    private List<String> headers = new ArrayList<>();
    private ResultAdapter adapter;
    private EditText searchInput;
    private Spinner searchTypeSpinner;

    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    excelFileUri = result.getData().getData();
                    readExcelFile(excelFileUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        Button btnUpload = findViewById(R.id.btnUpload);
        Button btnSearch = findViewById(R.id.btnSearch);
        searchInput = findViewById(R.id.searchInput);
        searchTypeSpinner = findViewById(R.id.searchTypeSpinner);
        RecyclerView resultList = findViewById(R.id.resultList);

        adapter = new ResultAdapter(new ArrayList<>());
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(adapter);

        btnUpload.setOnClickListener(v -> pickExcelFile());

        btnSearch.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            String selectedField = searchTypeSpinner.getSelectedItem().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(this, "Enter " + selectedField, Toast.LENGTH_SHORT).show();
                return;
            }
            searchByField(selectedField, query);
        });
    }

    private void pickExcelFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        filePickerLauncher.launch(intent);
    }

    private void readExcelFile(Uri fileUri) {
        excelData.clear();
        headers.clear();
        try (InputStream inputStream = getContentResolver().openInputStream(fileUri);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                int totalColumns = isFirstRow ? row.getLastCellNum() : headers.size();

                for (int i = 0; i < totalColumns; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value;
                    if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                        value = dateFormat.format(cell.getDateCellValue());
                    } else {
                        cell.setCellType(CellType.STRING);
                        value = cell.getStringCellValue();
                    }
                    rowData.add(value.trim());
                }

                if (isFirstRow) {
                    headers.addAll(rowData);
                    isFirstRow = false;
                } else {
                    excelData.add(rowData);
                }
            }

            Toast.makeText(this, "Excel loaded successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to read Excel file", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchByField(String columnName, String value) {
        List<List<String>> results = new ArrayList<>();
        int index = -1;

        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).trim().equalsIgnoreCase(columnName)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            Toast.makeText(this, columnName + " column not found", Toast.LENGTH_SHORT).show();
            return;
        }

        for (List<String> row : excelData) {
            if (index < row.size() && row.get(index).equalsIgnoreCase(value)) {
                results.add(row);
            }
        }

        if (results.isEmpty()) {
            Toast.makeText(this, "No match found for " + columnName, Toast.LENGTH_SHORT).show();
        }

        adapter.setHeaders(headers);
        adapter.updateData(results);
    }

    private String getFileName(Uri uri) {
        String result = "Excel File";
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        return result;
    }
}
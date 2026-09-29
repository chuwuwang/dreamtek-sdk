package view;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.activity.R;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Utils.Constants;
import base.MyApplication;
import butterknife.BindView;
import butterknife.ButterKnife;
import entity.cases.BaseCase;
import moudles.newModules.BaseServiceModule;
import moudles.newModules.ServiceModule;
import testtools.MessageCenter;
import testtools.TestCaseListener;
import testtools.Testcase;
import testtools.ToastUtil;


public class NewTestActivity extends AppCompatActivity {
    private static final int REQUEST_CHOOSEFILE = 100;
    private static final String TAG = "NewTestActivity";
    private static final String className = "verfone.com.vfcp2pusbipservice.P2PUsbIpService";
    private static final String packageName = "verfone.com.vfcp2pusbipservice";

    @BindView(R.id.service_bt)
    Button serviceBt;
    @BindView(R.id.magcard_bt)
    Button magcardBt;
    @BindView(R.id.touchic_bt)
    Button touchicBt;
    @BindView(R.id.notouchIc_bt)
    Button notouchIcBt;
    @BindView(R.id.beer_bt)
    Button beerBt;
    @BindView(R.id.led_bt)
    Button ledBt;
    @BindView(R.id.scan_bt)
    Button scanBt;
    @BindView(R.id.pint_bt)
    Button pintBt;
    @BindView(R.id.port_bt)
    Button portBt;
    @BindView(R.id.pinpad_bt)
    Button pinpadBt;
    @BindView(R.id.pboc_bt)
    Button pbocBt;
    @BindView(R.id.serviceinfo_bt)
    Button serviceInfoBt;
    @BindView(R.id.usbport_bt)
    Button usbPortBt;
    @BindView(R.id.ex_port_bt)
    Button externalSrialPost;
    @BindView(R.id.emv_bt)
    Button emvBt;
    @BindView(R.id.dukpt_bt)
    Button dukptBt;
    //    @Bind(R.id.smartEx_bt)
//    Button smartEx;
    @BindView(R.id.system)
    Button systemBt;
    @BindView((R.id.combination_bt))
    Button combinationBt;
    @BindView(R.id.kld_bt)
    Button kldBt;
    @BindView(R.id.rsa_bt)
    Button rsaBt;
    @BindView(R.id.felica_bt)
    Button felicaBt;
    @BindView(R.id.code_card_bt)
    Button codeCardBt;
    @BindView(R.id.sde_bt)
    Button sdeBt;

    @BindView((R.id.tv_prompt))
    TextView tvPrompt;
    @BindView((R.id.saveExcel))
    Button saveExcelBt;
    @BindView((R.id.sendMail))
    Button sendMailBt;
    @BindView((R.id.runAll))
    Button runAllCaseBt;
    @BindView((R.id.autoTest_bt))
    Button autoTestBt;
    @BindView((R.id.edTextEmail))
    TextView tvEmail;
    //    @Bind((R.id.cbPrinter))
//    CheckBox cbPrinter;
    @BindView((R.id.copyOTG))
    Button copy2OTG;



    /**
     * Whether parameters were imported successfully
     */
    private boolean importSuc = false;
    private ArrayList<BaseCase> cases;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_test);
        ButterKnife.bind(this);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);  Disable screen capture, for testing

        initListener();
//        bindService();


        SharedPreferences read = getSharedPreferences("lock", MODE_PRIVATE);
        String value = read.getString("paramFilePath", "");
//        value = "/sdcard/testCase_v1.61.xlsx";
        if (value.length() > 0) {
            tvPrompt.setText(String.format(getString(R.string.prompt_param_file), value));
            paramFileFullPath = value;
            (findViewById(R.id.iv_reload)).setVisibility(View.VISIBLE);
        } else {
            tvPrompt.setText(String.format(getString(R.string.prompt_import_param), new String(Character.toChars(128513))));
            (findViewById(R.id.iv_reload)).setVisibility(View.GONE);
        }

        value = read.getString("email", "");
        if (value.length() > 0) {
            tvEmail.setText(value);
        }

        value = read.getString("printing", "1");   // 1 print anytime, 0 no print, 2 print after test
        mPrintMode = Integer.valueOf(value);
        switch (mPrintMode) {
            case 0:
                ((RadioButton) findViewById(R.id.radioBtnPntDisabled)).setChecked(true);
                ((RadioButton) findViewById(R.id.radioBtnPntAny)).setChecked(false);
                ((RadioButton) findViewById(R.id.radioBtnPntOnFinish)).setChecked(false);
                break;
            case 1:
                ((RadioButton) findViewById(R.id.radioBtnPntDisabled)).setChecked(false);
                ((RadioButton) findViewById(R.id.radioBtnPntAny)).setChecked(true);
                ((RadioButton) findViewById(R.id.radioBtnPntOnFinish)).setChecked(false);
                break;
            default:
                ((RadioButton) findViewById(R.id.radioBtnPntDisabled)).setChecked(false);
                ((RadioButton) findViewById(R.id.radioBtnPntAny)).setChecked(false);
                ((RadioButton) findViewById(R.id.radioBtnPntOnFinish)).setChecked(true);

                break;
        }

        MessageCenter.getInstance().setTestCaseListener(new TestCaseListener() {
            @Override
            public void onReceived(List<Testcase> testcases) {
                if (testcases != null && testcases.size() > 0) {
                    BaseCase.Items[] items = new BaseCase.Items[10];
                    items[0] = BaseCase.Items.Api;
                    items[1] = BaseCase.Items.CaseID;
                    items[2] = BaseCase.Items.Status;
                    items[3] = BaseCase.Items.Describe;
                    items[4] = BaseCase.Items.Parameter;
                    items[5] = BaseCase.Items.ModuleID;
                    items[6] = BaseCase.Items.ResultType;
                    items[7] = BaseCase.Items.ExpectResult;
                    items[8] = BaseCase.Items.AidlNotes;
                    items[9] = BaseCase.Items.ApiNotes;


                    cases = new ArrayList();
                    for (Testcase testcase : testcases) {
                        BaseCase singleCase = new BaseCase();
                        singleCase.setValue(items[0], testcase.getAPI());
                        singleCase.setValue(items[1], testcase.getCaseID());
                        singleCase.setValue(items[2], testcase.getStatus());
                        singleCase.setValue(items[3], testcase.getDescribe());
                        singleCase.setValue(items[4], testcase.getParameter());    // Save the corresponding case id in parameters
                        singleCase.setValue(items[5], testcase.getModuleId());
                        singleCase.setValue(items[6], testcase.getResultType());
                        singleCase.setValue(items[7], testcase.getExpectResult());
                        Log.d(TAG, singleCase.toString());
                        cases.add(singleCase);
                    }
                    if (cases.size() > 0) {
                        importSuc = true;
                        ToastUtil.showToastLong("Received " + cases.size() + " test cases! Please check RUN ALL CASES");
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_import_param, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_import_param:
                startFileExplorer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startFileExplorer() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Open the system file manager to browse the specified directory
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.parse(path), "*/*");
//                intent.setType("*/*");// No type restriction
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CHOOSEFILE);
    }

    // Set up listener for entering the third-level interface
    private void initListener() {
        usbPortBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.usbPortBt);
            }
        });

        serviceBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.serviceBt);
            }
        });
        magcardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.magcardBt);
            }
        });
        touchicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.touchicBt);
            }
        });
        notouchIcBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.notouchIcBt);
            }
        });
        beerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.beerBt);
            }
        });
        ledBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.ledBt);
            }
        });
        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.scanBt);
            }
        });
        pintBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.pintBt);
            }
        });
        portBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.serialPortBt);
            }
        });
        pinpadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.pinpadBt);
            }
        });
        kldBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.kld);
            }
        });
        rsaBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.rsa);
            }
        });
        felicaBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.felicaBt);
            }
        });
        codeCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.codeCardBt);
            }
        });
        pbocBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.pbocBt);
            }
        });
        serviceInfoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.serviceInfoBt);
            }
        });
        externalSrialPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.externalSerialPortBt);
            }
        });
        emvBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.emvBt);
            }
        });
        sdeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterThirdActivity(Constants.sdeBt);
            }
        });
        dukptBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.dukptBt);
            }
        });
        systemBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.systemBt);
            }
        });

        combinationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterThirdActivity(Constants.combinationBt);
            }
        });

        saveExcelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                ServiceModule newServiceModule = MyApplication.newServiceModule;
                newServiceModule.saveResults();
            }
        });

        sendMailBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                ServiceModule newServiceModule = MyApplication.newServiceModule;
                String mailList = "";
                if (((CheckBox) findViewById(R.id.cbChao)).isChecked()) {
                    mailList += "kaiyi.z@verifone.cn;";
                }
                if (((CheckBox) findViewById(R.id.cbKai)).isChecked()) {
                    mailList += "kai.l@verifone.cn;";
                }
                if (((CheckBox) findViewById(R.id.cbLiJun)).isChecked()) {
                    mailList += "xuna.l@verifone.cn;";
                }
                String mailAddr = tvEmail.getText().toString();
                if (mailAddr.length() > 0) {
                    mailList += mailAddr;
                }
                // Save file name: create a SharedPreferences.Editor interface object, lock is the XML file name to write, MODE_WORLD_WRITEABLE for write operations
                SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_PRIVATE).edit();

                editor.putString("email", mailAddr);
                editor.commit();


                if (mailList.length() == 0) {
                    mailList += "chao.l@verifone.cn;";
                }

                newServiceModule.sendMail(paramFileFullPath, mailList);
            }
        });

        copy2OTG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        runAllCaseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterThirdActivity("ALL");
            }
        });
        autoTestBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterThirdActivity(Constants.autoTestBt);
            }
        });
//        cbPrinter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_PRIVATE).edit();
//
//                if( b ){
//                    editor.putString("printing", "1");
//                } else {
//                    editor.putString("printing", "0");
//                }
//                editor.commit();
//
//            }
//        });
    }

    int mPrintMode = 0;

    public void setPrintingMode(View view) {
        RadioButton radioButton = (RadioButton) view;
        if (radioButton == findViewById(R.id.radioBtnPntDisabled)) {
            // 0
            mPrintMode = 0;
        } else if (radioButton == findViewById(R.id.radioBtnPntAny)) {
            // 1
            mPrintMode = 1;
        } else {
            // 2
            mPrintMode = 2;
        }
        SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_PRIVATE).edit();
        editor.putString("printing", String.valueOf(mPrintMode));
        editor.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 100) {
            Uri uri = data.getData();
            if (uri != null) {
                String path = getPath(this, uri);
                if (path != null) {
                    File file = new File(path);
                    if (file.exists()) {
                        String paramFilePath = file.toString();
                        String fileName = file.getName();

                        startLoadingParams(fileName, paramFilePath);
                    } else {
                        Log.w(TAG, "file not exist: " + path);
                    }
                }
            }
        }
    }

    private static String getCellValueByCell(Cell cell, FormulaEvaluator evaluator) {
        // Check if null or empty string
        if (cell == null || null == cell.toString() || cell.toString().trim().equals("")) {
            return "";
        }
        String cellString = "";
        CellValue cellValue;
        int cellType = cell.getCellType();
        if (cellType == HSSFCell.CELL_TYPE_FORMULA) {  // Formula type
//            cellValue = evaluator.evaluate(cell);
//             cellType=cellValue.getCellType();
            cellType = evaluator.evaluateFormulaCell(cell);
        } else {
        }

        switch (cellType) {
            case HSSFCell.CELL_TYPE_STRING:  // String type
                cellString = cell.getStringCellValue();//.trim();
                // cellValue= cellValue.replace("\r\n", "|");    // Restore multi-line to | separator
                cellString = cellString.replace("\n", "|");    // Restore multi-line to | separator
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:   // Boolean type
                cellString = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:  // Numeric type
//                if  (HSSFDateUtil.isCellDateFormatted(cell)) {   // Check if date type
//                    cellValue =    DateUtil.formatDateByFormat(cell.getDateCellValue(),  "yyyy-MM-dd" );
//                }  else  {   // No
//                    cellValue =  new  DecimalFormat( "#.######" ).format(cell.getNumericCellValue());
//                }
                cellString = String.valueOf((int) cell.getNumericCellValue());
                break;
            default:  // Other types, use empty string
                cellString = "";
                break;
        }
        return cellString;
    }

    private void loadSheets(HSSFWorkbook workbook, FormulaEvaluator evaluator, int sheetIndex) {

        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        loadSheets(null, sheet, evaluator);
    }

    private void loadSheets(XSSFWorkbook workbook, FormulaEvaluator evaluator, String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        loadSheets(sheet, null, evaluator);
    }

    private void loadSheets(XSSFWorkbook workbook, FormulaEvaluator evaluator, int sheetIndex) {

        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        loadSheets(sheet, null, evaluator);
    }

    private void loadSheets(XSSFSheet sheet, HSSFSheet sheet2, FormulaEvaluator evaluator) {

//        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

        String sheetName;
        if (null == sheet) {
            sheetName = sheet2.getSheetName();
        } else {
            sheetName = sheet.getSheetName();
        }

        if (sheetName.compareToIgnoreCase("Sheet0") == 0) {
            sheetName = "";
        } else {
            sheetName = "[" + sheetName + "]";
        }

        int rowCount;
        if (null == sheet) {
            rowCount = sheet2.getLastRowNum();
        } else {
            rowCount = sheet.getLastRowNum();
        }
        if (rowCount < 4) {
//            Toast.makeText(this, "No test data,\nplease select again", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "Row count:" + rowCount);
        int columnCount = 0;
        for (BaseCase.Items item : BaseCase.Items.values()) {
            if (item.isRead) {
                ++columnCount;
            }
        }
        Log.d(TAG, "Column count reset to " + columnCount);

        BaseCase.Items[] columnKeys = new BaseCase.Items[columnCount];
        int[] columns = new int[columnCount];

        Row row;
        if (null == sheet) {
            row = sheet2.getRow(0);
        } else {
            row = sheet.getRow(0);
        }
        Cell cell = row.getCell(0);
        int rowIndex = 3;
        if ("\u6848\u4f8b\u6807\u9898\u884c".compareToIgnoreCase(getCellValueByCell(cell, evaluator)) == 0) {
            cell = row.getCell(1);
            rowIndex = Integer.valueOf(getCellValueByCell(cell, evaluator));
            if (rowIndex == 0) {
                rowIndex = 3;
            } else {
                --rowIndex;
                Log.d(TAG, "set row index to: " + rowIndex);
            }
        }

        for (int i = 1; i < rowIndex; i++) {
            if (null == sheet) {
                row = sheet2.getRow(i);
            } else {
                row = sheet.getRow(i);
            }
            if (null == row) {
                Log.w(TAG, "Got a null ROW at " + i);
                continue;
            }
            cell = row.getCell(0);
            String autoCases = "";
            if ("\u7ec4\u5408\u6848\u4f8b".compareToIgnoreCase(getCellValueByCell(cell, evaluator)) == 0) {
                Log.d(TAG, "auto case at row: " + i);
                String api = "";
                String cellStr = "";
                int colMax = row.getLastCellNum();
                int loopCnt = 0;   // 0, default, >0: loop
                for (int j = 1; j < colMax; j++) {
                    cell = row.getCell(j);
                    cellStr = getCellValueByCell(cell, evaluator);
                    if (cellStr.length() < 2) {
                        continue;
                    }
                    if (j == 1) {
                        api = sheetName + cellStr;
                        if (cellStr.toLowerCase().startsWith("loop-")) {
                            loopCnt = 1;
                        }
                    } else {
                        if (loopCnt == 1 && j == 2) {
                            loopCnt = Integer.valueOf(cellStr);
                        } else {
                            autoCases = autoCases.concat(sheetName + cellStr);
                            autoCases = autoCases.concat("^"); // Cannot use pipe here, it would conflict with combination cases
                        }
                    }
                }
                if (autoCases.length() > 4) {
                    ++i;
                    if (null == sheet) {
                        row = sheet2.getRow(i);
                    } else {
                        row = sheet.getRow(i);
                    }

                    cell = row.getCell(1);

                    cellStr = getCellValueByCell(cell, evaluator);
//                    if( cellStr.length() > 1){
//                        autoCases = autoCases.concat( cellStr );
//                    }
                    Log.d(TAG, "got auto case: " + autoCases);
                    BaseCase singleCase = new BaseCase();
                    singleCase.setValue(BaseCase.Items.Api, api);
                    singleCase.setValue(BaseCase.Items.CaseID, api);
                    singleCase.setValue(BaseCase.Items.Status, String.valueOf(loopCnt));
                    if (cellStr.length() > 1) {
                        singleCase.setValue(BaseCase.Items.Describe, cellStr);
                    }
                    singleCase.setValue(BaseCase.Items.Parameter, autoCases);    // Save the corresponding case id in parameters
                    singleCase.setValue(BaseCase.Items.ModuleID, Constants.autoTestBt);
                    Log.d(TAG, singleCase.toString());
                    // if( singleCase.getCaseStatus() != 0 ) {
                    cases.add(singleCase);
                }
            }
        }


        if (null == sheet) {
            row = sheet2.getRow(rowIndex);
        } else {
            row = sheet.getRow(rowIndex);
        }

        Cell[] cells = new Cell[8];
        String[] cellValues = new String[columnCount];
        String cellValue;

        ;
        // Traverse the header row, record columns that need to be converted to JSON and their corresponding keys
        for (int i = 0, offset = 0; i < columnCount; offset++) {
            cell = row.getCell(offset);
            cellValue = getCellValueByCell(cell, evaluator);
            if( offset > row.getLastCellNum() ) {
                Log.w(TAG, "No more column");
                break;
            }
            if (cellValue.length() == 0) {
                // Empty data
                Log.w(TAG, "Got empty value on Title ROW!" + offset);
                continue;
            } else {
                Log.v(TAG, "Got value on Title ROW:" + cellValue);
            }
            for (BaseCase.Items item : BaseCase.Items.values()) {
                if (item.title.equalsIgnoreCase(cellValue)
                        || item.jsonKey.equalsIgnoreCase(cellValue)) {
                    if (item.isRead) {
                        // Record
                        columns[i] = offset;
                        columnKeys[i] = item;
                        Log.d(TAG, String.format("Found title [%s] at col [%d], save to [%d]", cellValue, offset, i));
                        ++i;
                        break;
                    }
                }
            }
        }
        for (int i = (rowIndex + 1); i <= rowCount; i++) {
            if (null == sheet) {
                row = sheet2.getRow(i);
            } else {
                row = sheet.getRow(i);
            }


            if (null == row) {
                Log.w(TAG, "Got a null ROM, skip");
                continue;
            }

            BaseCase singleCase = new BaseCase();

            int j = 0;
            int len = 0;
            for (int col : columns) {
                cell = row.getCell(col);
                cellValues[j] = getCellValueByCell(cell, evaluator);
                if( cellValues[j].length() > 0 ) {
                    Log.v(TAG, "Got cell value:" + cellValues[j]);
                    len += cellValues[j].length();
                }
                if (null != columnKeys[j]) {
                    // If a column is missing from the imported table, columnkey is null
                    singleCase.setValue(columnKeys[j], cellValues[j]);
                }

                ++j;    // Note: increment first, then evaluate the condition
            }
            if( len > 0 ) {
                singleCase.setValue(BaseCase.Items.Describe,  singleCase.getCaseDescribe()); // sheetName + " " +
                singleCase.setValue(BaseCase.Items.CaseID, sheetName + singleCase.getCaseId());
                Log.d(TAG, singleCase.toString());

                cases.add(singleCase);
            }
        }
        return;
    }

    private void loadCaseSuccess(final String paramFilePath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvPrompt.setText(getString(R.string.prompt_param_file, paramFilePath));
                importSuc = true;
                paramFileFullPath = paramFilePath;
                Toast.makeText(getApplicationContext(), String.format(getString(R.string.toast_case_count), cases.size()), Toast.LENGTH_SHORT).show();

                // Save file name: create a SharedPreferences.Editor interface object, lock is the XML file name to write, MODE_WORLD_WRITEABLE for write operations
                SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_PRIVATE).edit();

                editor.putString("paramFilePath", paramFilePath);
                editor.commit();
                (findViewById(R.id.iv_reload)).setVisibility(View.VISIBLE);

            }
        });
    }

    String paramFileFullPath;

    private void startLoadingParams(final String fileName, final String paramFilePath) {

        if (fileName == null) {
            Toast.makeText(this, getString(R.string.toast_file_format_invalid), Toast.LENGTH_SHORT).show();
            return;
        }
        if (fileName.toLowerCase().endsWith("xls") || fileName.toLowerCase().endsWith("xlsx")) {
        } else {
            Toast.makeText(this, getString(R.string.toast_file_format_invalid), Toast.LENGTH_SHORT).show();
            return;
        }
        showConfirm(getString(R.string.dialog_loading_params), paramFilePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLoadingParamsXLS(fileName, paramFilePath);
            }
        }).start();
    }

    private void startLoadingParamsXLS(String fileName, final String paramFilePath) {
        if (fileName.endsWith("xls")) {
            //
            try {
                cases = new ArrayList<>();

                final HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(paramFilePath));
                final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                final int sheetCnt = workbook.getNumberOfSheets();

                if (sheetCnt > 1) {
                    // More than 1 sheet in the test case, select which sheet to load
                    final Context context = this;
                    final ArrayList<String> sheetNames = new ArrayList<String>();

                    for (int i = 0; i < sheetCnt; i++) {
                        String sheetName = workbook.getSheetName(i);
                        if (sheetName.contains("\u8bf4\u660e")
                                || sheetName.contains("\u6d4b\u8bd5\u6570\u636e")) {
                            continue;
                        }
                        sheetNames.add(workbook.getSheetName(i));
                        Log.d(TAG, "got sheet name: " + sheetName);
                    }
                    final boolean[] sheetSelected = new boolean[sheetNames.size()];
                    final String[] sheetNames2 = new String[sheetNames.size()];
                    int i = 0;
                    for (String name : sheetNames) {
                        sheetNames2[i] = name;
                        ++i;
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Select Sheets");
                    builder.setMultiChoiceItems(sheetNames2, sheetSelected,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    Log.d(TAG, "select: " + sheetNames.get(which));
                                    sheetSelected[which] = isChecked;
                                }
                            });
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "on confirm");
                            for (int i = 0; i < sheetCnt; i++) {
                                if (sheetSelected[i]) {

                                    loadSheets(null, workbook.getSheet(sheetNames2[i]), evaluator);
                                }
                            }

                            if (cases.size() > 0) {
                                loadCaseSuccess(paramFilePath);

                            } else {
                                Log.w(TAG, "No valid cases found");
                                importSuc = false;
                                showConfirm(getString(R.string.dialog_load_file), getString(R.string.dialog_no_valid_cases));
                                return;
                            }
                            closeConfirm();
                            return;

                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    loadSheets(workbook, evaluator, 0);
                    if (cases.size() > 0) {
                        loadCaseSuccess(paramFilePath);
                    } else {
                        importSuc = false;
                        Log.w(TAG, "No valid cases found");
                        showConfirm(getString(R.string.dialog_load_file), getString(R.string.dialog_no_valid_cases));
                        return;
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            closeConfirm();
            return;
        } else if (fileName.endsWith(".xlsx")) {
            //
            try {
                cases = new ArrayList<>();

//                OPCPackage pkg = OPCPackage.open( paramFilePath );
                final XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(paramFilePath)); //  );
                final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                final int sheetCnt = workbook.getNumberOfSheets();

                if (sheetCnt > 1) {
                    // More than 1 sheet in the test case, select which sheet to load
                    final Context context = this;
                    final ArrayList<String> sheetNames = new ArrayList<String>();

                    for (int i = 0; i < sheetCnt; i++) {
                        String sheetName = workbook.getSheetName(i);
                        if (sheetName.contains("\u8bf4\u660e")
                                || sheetName.contains("\u6d4b\u8bd5\u6570\u636e")) {
                            continue;
                        }
                        sheetNames.add(workbook.getSheetName(i));
                        Log.d(TAG, "got sheet name: " + sheetName);
                    }
                    final boolean[] sheetSelected = new boolean[sheetNames.size()];
                    final String[] sheetNames2 = new String[sheetNames.size()];
                    int i = 0;
                    for (String name : sheetNames) {
                        sheetNames2[i] = name;
                        ++i;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Select Sheets");
                            builder.setMultiChoiceItems(sheetNames2, sheetSelected,
                                    new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                            Log.d(TAG, "select: " + sheetNames2[which]);
                                            sheetSelected[which] = isChecked;
                                        }
                                    });
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "on confirm");
                                    for (int i = 0; i < sheetSelected.length; i++) {
                                        if (sheetSelected[i]) {
                                            loadSheets(workbook, evaluator, sheetNames2[i]);
                                        }
                                    }

                                    if (cases.size() > 0) {
                                        loadCaseSuccess(paramFilePath);

                                    } else {
                                        Log.w(TAG, "No valid cases found");
                                        importSuc = false;
                                        showConfirm(getString(R.string.dialog_load_file), getString(R.string.dialog_no_valid_cases));
                                        return;
                                    }
                                    closeConfirm();
                                    return;

                                }
                            });
                            builder.setNegativeButton("Cancel", null);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                    });

                } else {
                    loadSheets(workbook, evaluator, 0);
                    if (cases.size() > 0) {
                        loadCaseSuccess(paramFilePath);
                    } else {
                        importSuc = false;
                        Log.w(TAG, "No valid cases found");
                        showConfirm(getString(R.string.dialog_load_file), getString(R.string.dialog_no_valid_cases));
                        return;
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();

            }
            closeConfirm();
            return;
        }
        // No longer support JSON format cases
//        if (!fileName.endsWith("json") ) {
//            Toast.makeText(this, getString(R.string.toast_file_format_invalid), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        JsonParser<BaseCase> jsonParser = new JsonParser<>();
//        jsonParser.setListener(new JsonParser.ParserListener() {
//            @Override
//            public void onFinish() {
//
//            }
//        });
//        ArrayList<BaseCase> allCases = jsonParser.parse(BaseCase.class, paramFilePath);
//        cases = new ArrayList<>();
//        for ( BaseCase basecase : allCases ) {
////            if( basecase.getCaseStatus() != 0 ) {
//                cases.add( basecase );
////            }
//        }
//        importSuc = true;
//        this.paramFileFullPath = paramFilePath;
    }

    public void enterThirdActivity(String name) {
        if (!importSuc) {
            Toast.makeText(this, getString(R.string.toast_import_params_first), Toast.LENGTH_SHORT).show();
            return;
        }
        MyApplication.newServiceModule.enablePrinter(mPrintMode);

        Intent intent = new Intent(NewTestActivity.this, NewThirdActivity.class);
        intent.putExtra("name", name);
        intent.putParcelableArrayListExtra("cases", cases);
        Log.d(TAG, "Intent set name: " + name);
        /** Pass parameters **/
        switch (name) {
            case "ALL":
                MyApplication.newServiceModule.setCases(cases);
                MyApplication.newServiceModule.getModule("AutoTest").setCases(cases);   // All contains auto test cases, need to load corresponding cases
                break;
            default:
                MyApplication.newServiceModule.getModule(name).setCases(cases);
                break;
        }

        startActivity(intent);
//        importSuc = false;
//        tvPrompt.setText(getString(R.string.prompt_import_param, ""));
    }

    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                // Below is a print example:
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), ContentUris.parseId(uri));

                    return getDataColumn(context, contentUri, null, null);
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"file***"+uri.toString());
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public void reloadParafile(View view) {
        startLoadingParams(paramFileFullPath, paramFileFullPath);
    }

    AlertDialog alertDialog;

    public void showConfirm(String title, String message) {
        showConfirm(title, message, 0);
    }

    public void closeConfirm() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        if (null != alertDialogCloser) {
            alertDialogCloser.cancel();
            alertDialogCloser = null;
        }
    }

    Timer alertDialogCloser;

    public void showConfirm(String title, String message, int timeout) {
        Log.d(TAG, "show confirm dialog, timeout:" + timeout);
//        if (!isFront){
//            Log.d(TAG, "not in front, isFront:" + isFront);
//            return;
//        }
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(title);
        build.setMessage(message);
        if (timeout > 0) {
            //
            build.setPositiveButton("Dismiss after " + timeout / 1000 + "s", null);
        } else {
            build.setPositiveButton("Dismiss", null);
        }

        if (null != alertDialog) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        alertDialog = build.create();
        alertDialog.show();

        if (timeout <= 0) {
            if (null != alertDialogCloser) {
                alertDialogCloser.cancel();
                alertDialogCloser = null;
            }
        }
        if (timeout > 0) {
            // set a timer to close the dialog
            Timer t = new Timer();
            alertDialogCloser = t;
            t.schedule(new TimerTask() {
                public void run() {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    if (null != alertDialogCloser) {
                        alertDialogCloser.cancel();
                        alertDialogCloser = null;
                    }
                }
            }, timeout);
        }
    }

//    private void bindService() {
//        Log.d(TAG, "bind service");
//        Intent intent = new Intent();
//        intent.setPackage(packageName);
//        intent.setClassName(packageName, className);
//        boolean a = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
//        Log.d(TAG, "bindService:" + a);
//    }
//
//    private final ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            BaseServiceModule.baseService = BaseServiceConnection.Stub.asInterface(service);
//            Log.d(TAG, "service:" + BaseServiceModule.baseService);
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            BaseServiceModule.baseService = null;
//        }
//
//    };

}

package moudles.newModules;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dreamtek.smartpos.deviceservice.aidl.IDeviceService;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sun.mail.util.MailSSLSocketFactory;
import com.dreamtek.smartpos.system_service.aidl.ISystemManager;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utils.Constants;
import Utils.LogUtils;
import base.MyApplication;
import dalvik.system.DexFile;
import entity.cases.BaseCase;
import moudles.newModules.data.PanBundleStore;

public class ServiceModule implements Module {
    private static final String TAG = "ServiceModule";
    //MyApplication
    Context context;
    //那么就是当全局变量引用
    public IDeviceService deviceService;
    public boolean isConnect = false;
     public LogUtils logUtils;

    ArrayList<BaseCase> resultList = new ArrayList<>();

    public ServiceModule(Context context) {
        this.context = context;
         logUtils = MyApplication.logUtils;
        printMode = 1;  // 1 随时打印，0 不打印，2 测完打印
    }

    int printMode;

    public void enablePrinter( int printMode ){
        Log.d(TAG, "Printer mode: " + printMode );
        this.printMode = printMode;
    }

    List<String> testModuleNames = new ArrayList<>();   // 保存测试模块的类型，用于创建对象
    List<Object> testModules = new ArrayList<>();   // 测试模块的实例

    // 根据传入的模块名称，通过反射方式创建对应的对象。
    // 注意，有些类的名称和内部定义的名称有差别，需要重新设置类的名称
    public synchronized TestModule getModule( String module ) {

        if( testModuleNames.size() == 0 ){
            // 初始化类名列表
            try {
                String packageName = "moudles.newModules";
                Log.d(TAG, "Searching all modules ..." + packageName );
                DexFile df = new DexFile(context.getPackageCodePath()); //通过DexFile查找当前的APK中可执行文件
                Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式

                while(enumeration.hasMoreElements()){
                    String  className = enumeration.nextElement();
                    if (className.contains(packageName)) {
                        if( className.endsWith("Module")) {
                            Log.d(TAG,"find init class path :" + className);
                            testModuleNames.add( className );
                        }
                    }
                }
                Log.d(TAG, "Searching all modules ... finish");
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 名称不一致时，重新设置类名
        switch (module){
            case Constants.serviceBt:
                module = "deviceinfo"; // 类名与定义名不一致时，需要添加
                break;
            case Constants.systemBt:
                module = "systemservice";   // 类名与定义名不一致时，需要添加
                break;
            case Constants.serialPortBt:
                module = "Serial";
                break;
            case "ISettingsManager":
                module = "SysSettings";
                break;
        }

        String moduleName = "moudles.newModules." + module + "Module";
        for ( Object object: testModules ) {
            if( ((TestModule)object).getClassName().equalsIgnoreCase( moduleName ) ) {
                // 找到实例，返回
                ((TestModule)object).setContext(context);
                ((TestModule)object).enablePrinter(printMode);
                return (TestModule) object;
            }
        }

        for (String moduleClassName: testModuleNames ) {
            if( moduleClassName.equalsIgnoreCase( moduleName )){
                // 查找精确的类名、创建对象
                try {
                    Class<?> aClass = Class.forName( moduleClassName );
                    Object object = aClass.newInstance();
                    ((TestModule)object).setContext(context);
                    ((TestModule)object).enablePrinter(printMode);
                    testModules.add( object );
                    return  (TestModule) object;

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.e(TAG, "Invalid module name ["+ module +"]is given!");
        return null;


    }

    public void updateService( IDeviceService iDeviceService ){
        TestModule.updateService( iDeviceService );
//        try {
//            iDeviceInfo = iDeviceService.getDeviceInfo();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    public void updateService( ISystemManager iSystemManager){
        TestModule.updateService( iSystemManager );
    }

    public void runTheMethod(BaseCase caseInfo) {

//        caseInfo.setResult("Not RUN");
        Log.d(TAG, "start case: " + caseInfo.getCaseId() );
        if (caseInfo.getCaseId().contains("keyboardFromParam")){
            String name = caseInfo.getCaseDescribe();
            Log.d(TAG,"keyboardFromParam案例描述："+name);
            Pattern panPattern = Pattern.compile("PAN-(\\d+)");
            Pattern pinPattern = Pattern.compile("PIN-(\\d+)");

            Matcher panMatcher = panPattern.matcher(name);
            Matcher pinMatcher = pinPattern.matcher(name);

            if (panMatcher.find() && pinMatcher.find()) {
                String pan = panMatcher.group(1);
                String pin = pinMatcher.group(1);

                Log.i(TAG, "keyboardFromParam：PAN: " + pan);
                Log.i(TAG, "keyboardFromParam：PIN: " + pin);
                Bundle bundle = new Bundle();
                bundle.putString("PAN",pan);
                bundle.putString("PIN",pin);
                PanBundleStore.setBundle(bundle);
            } else {
                Log.e(TAG, "PAN或PIN未找到");
            }

        }
        caseInfo.resetResult();
        TestModule testModule = getModule( caseInfo.getModuleId() );
        if( null == testModule ){
            TestModule.logUtils.caseFinished(1);
            return;
        }
        switch ( caseInfo.getModuleId().toLowerCase() ) {
            case Constants.combinationBt:
                ( (CombinationModule)testModule ).runTheMethod(this, caseInfo);
                break;
            case Constants.autoTestBt:
                ( (AutoTestModule)testModule ).runTheMethod(this, caseInfo);
                break;
            default:
                testModule.runTheMethod( caseInfo );
                break;
        }

        Log.d(TAG, "finished case: " + caseInfo.getCaseId() );
        TestModule.logUtils.showCaseLog();
        // add to result
        try {
            Thread.sleep(500);    // 停留 500 毫秒，给UI处理 logUtil发出的更新内容的handler message
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if( caseInfo.getModuleId() != Constants.autoTestBt ){
            caseInfo.setCaseLog( logUtils.getCaseLog());
            resultList.add( new BaseCase(caseInfo) );
            ++test_total;
        }

        {
            String value = caseInfo.getValue( BaseCase.Items.Result );
            if ( value.contains("Failure")
                    || value.contains( "Error")
                    || value.contains("Not ")
                    || value.contains("Exception")
                    || value.contains("Invalid ")
                    || value.contains( "null returns" )
                    || value.contains( "Wrong" )
            ) {
                ++test_failure;
            }
        }
        TestModule.logUtils.caseFinished(1);
    }

    public void runAllMethod(final String module ) {
        test_total = 0;
        test_failure = 0;

        switch ( module ){
            case "ALL":
                for (int i = 0; i < 1; i++) {
                    Log.d(TAG, "starting index: " + i);
                    for (BaseCase baseCase : allCases) {
                        if( baseCase.getCaseStatus() <= 0 ){
                            continue;
                        }
                        TestModule.logUtils.printCaseInfo(baseCase.getApi() + "\n" + baseCase.getMethodParams() );
                        runTheMethod(baseCase);
                        TestModule.logUtils.showCaseLog();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            default:
                getModule( module ).runAllMethod(this);
                break;

        }
        TestModule.logUtils.addCaseLog("\n所有案例执行完毕\n总数：" + test_total + "\n失败：" + test_failure);
        TestModule.logUtils.printCaseLog( "\n所有案例执行完毕\n总数：" + test_total + "\n失败：" + test_failure );
        TestModule.logUtils.printCaseLog( "\n\n\n" );
        TestModule.logUtils.caseFinished(2);
    }

    public void editCaseParam(final BaseCase caseInfo, final Context context ){
//        this.context = context;
        Log.d(TAG, "input index to clear");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("修改参数(保存后，下次运行使用新的参数。但是不修改excel案例)");
        final EditText et = new EditText(context);
        et.setHint("");
        et.setText( caseInfo.getMethodParams() );
        et.setSingleLine(false);
        et.setHorizontallyScrolling( false );
        et.setLines(5);
        et.setInputType(InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_FLAG_MULTI_LINE );
        builder.setView(et);
        builder.setNeutralButton("取消",null);
        builder.setNegativeButton("保存并运行", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newParam = et.getText().toString();
                        caseInfo.setValue( BaseCase.Items.Parameter, newParam );
                        Log.d(TAG, "new param: " + newParam);
                        runTheMethod( caseInfo );

                    }
                } );
                builder.setPositiveButton("仅运行", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newParam = et.getText().toString();
                        Log.d(TAG, "new param: " + newParam);
                        BaseCase baseCase = new BaseCase(caseInfo);
                        baseCase.setValue( BaseCase.Items.Parameter, newParam );
                        runTheMethod( baseCase );
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if( i == KeyEvent.KEYCODE_ENTER ) {
                    // confrim
                    String newParam = et.getText().toString();
                    caseInfo.setValue( BaseCase.Items.Parameter, newParam );
                    Log.d(TAG, "new param: " + newParam);
                    runTheMethod( caseInfo );
                    alertDialog.cancel();
                }
                return false;
            }
        });

        // show input after 200 ms.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    et.setFocusable(true);
                    et.setFocusableInTouchMode( true );
                    et.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(et, 0);

                } catch ( Exception e ){

                }
            }
        }, 200);

    }


    public void editMethodParam(BaseCase caseInfo, Context context) {

        TestModule testModule = getModule( caseInfo.getModuleId() );
        if( null == testModule ){
            return;
        }

        editCaseParam(caseInfo, context);
    }





    //展示case信息
    public void showTheCaseInfo(String module, int groupPosition, int childPosition) {
        TestModule.logUtils.clearLog();
        switch (module) {
            case "ALL":
                showSelfCaseInfo(groupPosition, childPosition);
                break;
            default:
                getModule( module ).showTheCaseInfo(groupPosition, childPosition);
                break;
        }
    }

    public void showSelfCaseInfo(int groupPosition, int childPosition) {
        String name = allCaseNames.get(groupPosition).get(childPosition).getCaseDescribe()
                + "\n" +  allCaseNames.get(groupPosition).get(childPosition).getApi()
                + "\n" +  allCaseNames.get(groupPosition).get(childPosition).getMethodParams();
        TestModule.logUtils.printCaseInfo(name);
    }

    public ArrayList<String> getApiList() {
        ArrayList<String> a = new ArrayList<String>();
        a.add( "All Cases");
        return a;
    }

    ArrayList<BaseCase> allCases;

    @Override
    public Object setCases(ArrayList cases) {
        //
        allCases = cases;
        return null;
    }


    int test_total = 0;
    int test_failure = 0;
    public void saveResults(){
        ArrayList<BaseCase> results = resultList;

        test_total = 0;
        test_failure = 0;


        Workbook wb = new HSSFWorkbook();
        Sheet sh = wb.createSheet();

        int cellnum = 0;
        for ( BaseCase.Items item: BaseCase.Items.values()
        ) {
            sh.setColumnWidth(cellnum, BaseCase.getWidth(item)*256);
            cellnum++;
        }

        CellStyle styleWrap = wb.createCellStyle();
        styleWrap.setWrapText( true );
        styleWrap.setVerticalAlignment( CellStyle.VERTICAL_CENTER );// 上下居中

        // 加大字号
        CellStyle cellStyleBold = wb.createCellStyle();
        Font cellFontBold = wb.createFont();//创建字体
        // cellFontBold.setColor(IndexedColors.RED.getIndex());//设置字体颜色
//        twoheadFontRed.setFontName("宋体"); //设置字体
        cellFontBold.setFontHeightInPoints((short) 16);//设置字体大小
        cellStyleBold.setFont(cellFontBold);//给样式设置字体
        cellStyleBold.setAlignment( CellStyle.ALIGN_CENTER );// 左右居中
        cellStyleBold.setVerticalAlignment( CellStyle.VERTICAL_CENTER );// 上下居中
        cellStyleBold.setWrapText( true );


        CellStyle cellStyleRed = wb.createCellStyle();
        Font twoheadFontRed = wb.createFont();//创建字体
        twoheadFontRed.setColor(IndexedColors.RED.getIndex());//设置字体颜色
//        twoheadFontRed.setFontName("宋体"); //设置字体
        twoheadFontRed.setFontHeightInPoints((short) 16);//设置字体大小
        cellStyleRed.setFont(twoheadFontRed);//给样式设置字体
        cellStyleRed.setAlignment( CellStyle.ALIGN_CENTER );// 左右居中
        cellStyleRed.setVerticalAlignment( CellStyle.VERTICAL_CENTER );// 上下居中
        cellStyleRed.setWrapText( true );


        // 原文链接：https://blog.csdn.net/qq_38256982/article/details/88398897


        // the title row
        int rownum = 0;

        org.apache.poi.ss.usermodel.Row row = sh.createRow( rownum++ );

        cellnum = 0;
        {
//            try {
//            Cell cell = row.createCell(cellnum++);
//            cell.setCellValue( "VF-Service");
//            cell = row.createCell(cellnum++);
//            cell.setCellValue( iDeviceInfo.getServiceVersion() );
//
//            cell = row.createCell(cellnum++);
//            cell.setCellValue( "SP");
//            cell = row.createCell(cellnum++);
//            cell.setCellValue( iDeviceInfo.getK21Version() );
//
//            cell = row.createCell(cellnum++);
//            cell.setCellValue( "ROM");
//            cell = row.createCell(cellnum++);
//            cell.setCellValue( iDeviceInfo.getROMVersion() );
//
//            cellnum++;
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        rownum++;   // skip a row for the result

        rownum++;   // 再隔一个空行、功能保留，当报告作为案例再次被运行时，是从第4行开始解析的，所以此处要增加一个空行

        // the title row
        row = sh.createRow( rownum++ );

        cellnum = 0;
        for ( BaseCase.Items item: BaseCase.Items.values()
        ) {
            Cell cell = row.createCell(cellnum);
            cell.setCellValue( BaseCase.getTitle(item));
            cellnum++;
        }


        if( results.size() > 0 ){
            //
            for ( BaseCase result: results ) {
                Log.d(TAG, result.toString() );
                ++test_total;

                row = sh.createRow( rownum++ );

                cellnum = 0;
                for ( BaseCase.Items item: BaseCase.Items.values()
                ) {
                    Cell cell = row.createCell(cellnum);
                    String value = result.getValue(item);
                    if( null == value ) {
                        Log.w(TAG, "set item:" + item + ", null to space at " + rownum);
                        value = "";
                    } else if( item.equals(BaseCase.Items.Status) ){
                        value = "1";
                    }
                    if( value.contains("|")) {
                        value = value.replace('|', '\n');
                    }
                    cell.setCellValue( value );
                    if( item.equals( BaseCase.Items.Result )){
                        if( value.length() == 0 ) {
                            value = "Wrong Case";
                            cell.setCellValue( value );
                        }
                        if ( value.contains("Failure")
                                || value.contains( "Error")
                                || value.contains("Not ")
                                || value.contains("Exception")
                                || value.contains("Invalid ")
                                || value.contains( "null returns" )
                                || value.contains( "Wrong" )
                        ){
                            cell.setCellStyle(cellStyleRed);
                            ++test_failure;
                        } else if (value.contains( "Skip")) {
                            cell.setCellStyle( cellStyleBold );
                        } else {
                            cell.setCellStyle( styleWrap );
                        }
                    }
                    else {
                        cell.setCellStyle( styleWrap );
                    }
                    cellnum++;
                }

            }
            row = sh.createRow( 1 );

            cellnum = 1;
            {
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue( "总案例");
                    cell = row.createCell(cellnum++);
                    cell.setCellValue( test_total );

                    cell = row.createCell(cellnum++);
                    cell.setCellValue( "失败");
                    cell = row.createCell(cellnum++);
                    cell.setCellValue( test_failure );
                    if( test_failure > 0 ){
                        cell.setCellStyle(cellStyleRed );
                    }
            }
        } else {
            //
            Log.w( TAG, "no test result found" );
        }

        try {
            FileOutputStream fos = // openFileOutput(filename, Context.MODE_PRIVATE);
                    new FileOutputStream( new File(filenameExcel));
            wb.write(fos);
            fos.close();
            Toast.makeText(context.getApplicationContext(), "导出成功", Toast.LENGTH_SHORT).show();

            resultList.clear();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    String[] str = new String[]{"one","two","three","four","five","six","seven","eight","nine","ten"};
    String filenameExcel = "/sdcard/testReport.xls";

    public void exportExcelFile() {
        // https://blog.csdn.net/ouchsowhat/article/details/53896909
        int size = 10;
        Workbook wb = new HSSFWorkbook();
        Sheet sh = wb.createSheet();

        for (int rownum = 0; rownum < size; rownum++) {
            org.apache.poi.ss.usermodel.Row row = sh.createRow(rownum);
            for (int cellnum = 0; cellnum < 10; cellnum++) {
                Cell cell = row.createCell(cellnum);
                cell.setCellValue(str[cellnum]);
            }
        }

        try {
            FileOutputStream fos = // openFileOutput(filename, Context.MODE_PRIVATE);
                    new FileOutputStream( new File(filenameExcel));
            wb.write(fos);
            fos.close();
            Toast.makeText(context.getApplicationContext(), "导出成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String ver_vf_service = "";
    String ver_sp = "";
    String ver_ROM = "";
    String serialNumber = "";

    public void sendMail(final String paramFilePath, final String mailList){
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//
//                    try {
//                        ver_ROM = iDeviceInfo.getROMVersion();
//                        ver_sp = iDeviceInfo.getK21Version();
//                        ver_vf_service = iDeviceInfo.getServiceVersion();
//                        serialNumber = iDeviceInfo.getSerialNo();
//
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                    sendMail(mailList, // "chao.l@verifone.cn",  // ;kai.l@verifone.cn;yi1.l@verifone.cn
//                            paramFilePath,
//                            "Hi All, \r\nTestClient 测试结果\r\n" +
//                                    "总案例:" + test_total+ ", 失败: " + test_failure + "\r\n" +
//                                    "SP " + ver_sp + "\r\nROM " + ver_ROM + "\r\n" +
//                                    "SN " + serialNumber +
//                                    "\r\n"
//                            ,
//                            "RE: TestClient, VF-Service " + ver_vf_service  );
//                } catch (GeneralSecurityException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };
//        thread.start();
    }

    MimeBodyPart setAttachment( String filename, String shortFilename ){
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        // attached file
        try {
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        String shortName = filename;
        if( shortFilename.length() > 0 ){
            shortName = shortFilename;
        } else {
            if( filename.lastIndexOf('/') > 0 ){
                shortName = filename.substring( filename.lastIndexOf('/') + 1 );
                Log.d( TAG, "Shortname:" + shortName );
            }
        }
            messageBodyPart.setFileName(shortName);

        //messageBodyPart.setFileName(filename);
        // encoding
        messageBodyPart.setFileName(MimeUtility.encodeText(shortName));
        Log.d(TAG, "add file:" + shortName );
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  messageBodyPart;
    }

    void sendMail( String toList, String filename,String body, String title ) throws GeneralSecurityException {

        //
        String comment = "\r\n\r\n\r\n";


        // the account to send mail
        //String from = "Simonryu@qq.com";
        //String fromPSW = "egmdhzqyjbubcahc";

        // smtp setting smtp.qq.com
        //String host = "smtp.qq.com"; //

        final String from = "redmine@verifone.cn";
        final String fromPSW = "LiuCHao1324";

        String host = "smtp.mxhichina.com"; //

        // the system property
        Properties properties = new  Properties();

        // setup the smtp
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // session
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {  // authentication for qq
                return new PasswordAuthentication(from, fromPSW); //
            }
        });

        try{
            // MimeMessage
            MimeMessage message = new MimeMessage(session);

            // Set From
            message.setFrom(new InternetAddress(from));

            // Set To
            String toAddrList[] = toList.split(";");
            for ( String to: toAddrList
            ) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }

            // Set Subject:
            message.setSubject( title );

            // the message body
            BodyPart messageBodyPart = new MimeBodyPart();

            //
            messageBodyPart.setText( body + comment );

            // multi part of message
            Multipart multipart = new MimeMultipart();

            // body part
            multipart.addBodyPart(messageBodyPart);

            if( filename.length() > 2 ) {
                // attachments
                messageBodyPart = setAttachment(filename, "");

                multipart.addBodyPart(messageBodyPart);
            }

            if( filenameExcel.length() > 2 ) {
                // attachments
                SimpleDateFormat sdf = new SimpleDateFormat("MMdd_HHmm"); // "yyyy-MM-dd_HHmmss"

                String logFileTime = sdf.format(new Date()) ;

                String shortname = filenameExcel.substring( filenameExcel.lastIndexOf('/')+1, filenameExcel.lastIndexOf('.') );
                String ext = filenameExcel.substring( filenameExcel.lastIndexOf('.') );

                shortname = shortname + "_" + logFileTime + ext;
                messageBodyPart = setAttachment(filenameExcel, shortname);

                multipart.addBodyPart(messageBodyPart);

            }

            // string log file
            final String logfile = saveLog();
            if( logfile.length() > 2 ) {
                // attachments
                SimpleDateFormat sdf = new SimpleDateFormat("MMdd_HHmmss"); // "yyyy-MM-dd_HHmmss"

                String logFileTime = sdf.format(new Date()) ;
                String logFileShortname = "testClient_VF(" + ver_vf_service + ")_" + serialNumber + "_" + logFileTime + ".log.gz";


                messageBodyPart = setAttachment(logfile, logFileShortname );

                multipart.addBodyPart(messageBodyPart);

            }
            // set message content
            message.setContent(multipart );

            Log.d( TAG, "Try send to:" + toList + "\nComment:" + body + "\nFile:" + filename );

            // SEND
            Transport.send(message);
            Log.d( TAG, "Sent message successfully....");


        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    ArrayList<ArrayList<BaseCase>> allCaseNames;
    public ArrayList<ArrayList<BaseCase>> getAllCaseNames() {
        allCaseNames = new ArrayList<ArrayList<BaseCase>>();
        allCaseNames.add( allCases );
        return  allCaseNames;
    }

    String saveLog(){

        SimpleDateFormat sdf = new SimpleDateFormat("MMdd_HHmmss"); // "yyyy-MM-dd_HHmmss"

        String logFileTime = sdf.format(new Date()) ;
        String logFilename = "/sdcard/testClient_VF(" + ver_vf_service + ")_" + serialNumber + "_" + ".log";

        File file = new File( logFilename );
        if( file.exists()){
            file.delete();
            Log.d(TAG, "Delete the last log file.");
        }

        try {
            MyApplication.systemServiceModule.iSystemManager.initLogcat(6, 0, null);
            logFilename = MyApplication.systemServiceModule.iSystemManager.getLogcat(logFilename, 1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "start logcat save to file:" + logFilename);
        File logFile = new File( logFilename );

        long lastModified;
        do {
            lastModified = logFile.lastModified();
            Log.d(TAG, logFile + " last modified time " + lastModified);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (logFile.lastModified() > lastModified);

        Log.d(TAG, "logcat saved to file:" + logFilename);

        return logFilename;
    }

    private void execCommand(String cmd, boolean isNeedReadProcess) {
        try {
            Log.d(TAG, "execCommand: " + cmd);
            Process process = Runtime.getRuntime().exec(cmd);
            if (isNeedReadProcess) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                StringBuilder log = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    log.append(line);
                }
                Log.d(TAG, log.toString() );
            }
        } catch (IOException e) {
        }
    }
}
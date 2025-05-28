package moudles.newModules;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import Utils.Constants;
import entity.cases.BaseCase;

/**
 * Created by Simon on 2021/7/31
 */
class AutoTestModule extends TestModule {
    private static final String TAG = "AutoTestModule";

    public AutoTestModule() {
        super();
        module = Constants.autoTestBt;
    }

    @Override
    protected void addAllapi(ArrayList<BaseCase> cases) {
        if (cases == null || cases.size() == 0) {
            logUtils.addCaseLog("No Cases, please import cases");
            Log.w(TAG, "No Cases, please import cases" );
            return;
        }
        allCases.clear();
        apiList.clear();
        caseNames.clear();

        try {
            // 遍历所有案例，保存所有autotest模块的案例
            Iterator<BaseCase> caseIterator = cases.iterator();
            while (caseIterator.hasNext()) {
                BaseCase nextCase = caseIterator.next();
                String module = nextCase.getModuleId();
                Log.v(TAG, "find module:" + module );
                if ( module.compareToIgnoreCase( this.module) == 0 ) {
                    String apiName = nextCase.getApi();
                    if (!apiList.contains(apiName)) {
                        apiList.add(apiName);
                        caseNames.add(new ArrayList<BaseCase>());
                    }
                    caseNames.get(apiList.indexOf( apiName )).add(nextCase); // 添加自动案例
//                    String[] caseList = nextCase.getMethodParams().split("\\|");
                    String[] caseList = nextCase.getMethodParams().split("\\^");
                    for ( String caseID: caseList  ) {
                        //
                        Log.d(TAG, "search case: " + caseID );
                        for ( BaseCase baseCase: cases ) {
                            if( caseID.compareToIgnoreCase(baseCase.getCaseId()) == 0 ){
                                caseNames.get(apiList.indexOf( apiName )).add(baseCase); // 添加自动案例对应的案例
                                allCases.add( baseCase );
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runTheMethod(ServiceModule serviceModule, BaseCase caseInfo) {
        ArrayList<BaseCase> autoCases;
        autoCases = caseNames.get(apiList.indexOf( caseInfo.getApi() )); // 找到 autocase 对应的case
        Log.d(TAG, "has " + autoCases.size() + " cases");
        boolean skipThe1st = true;
        if( caseInfo.getApi().toLowerCase().indexOf("auto-") > 0 ) {
            // 顺序执行
            for ( BaseCase baseCase: autoCases ) {
                if( skipThe1st ){
                    skipThe1st = false;
                    continue;
                }
                serviceModule.runTheMethod( baseCase );
            }
        } else if( caseInfo.getApi().toLowerCase().indexOf("random-") > 0 ){
            // 随机案例
            int maximum = (autoCases.size()-1)*2;
            int random = (int)Math.random()*maximum;
            while ( maximum > 0){
                random = (int)Math.random()*autoCases.size();
                BaseCase baseCase = autoCases.get( random+1 );
                serviceModule.runTheMethod( baseCase );

                --maximum;
            }
        } else if( caseInfo.getApi().toLowerCase().indexOf("loop-") > 0 ){
            // 循环执行
            int loopCnt = caseInfo.getCaseStatus();
            Log.d(TAG, "Got loop count: " + loopCnt );
            for (int i = 0; i < loopCnt; i++) {
                Log.d(TAG, "Loop index: " + i);
                skipThe1st = true;
                for ( BaseCase baseCase: autoCases ) {
                    if( skipThe1st ){
                        skipThe1st = false;
                        continue;
                    }
                    serviceModule.runTheMethod( baseCase );
                }
            }

        } else if ( caseInfo.getApi().toLowerCase().startsWith("each-")){
            // 遍历执行
            int maximum = autoCases.size()-1;
            maximum = 4;
            int[] order = new int[maximum];
            int[] exchange = new int [maximum];
            for (int i = 0; i < maximum; i++) {
                order[i] = i;
                exchange[i] = 0;
            }

            do{

                for (int i = 0; i < maximum; i++) {
                    String out = "";
                    for (int j = 0; j < maximum ; j++) {
                        out += order[j];
                        out += ",";
                    }
                    Log.d(TAG, out);
                    for (int j = 0; j < maximum-1; j++) {
                        if( exchange[j] < (1<<i) ){
                            int a = order[j];
                            order[j] = order[j+1];
                            order[j+1] = a;
                            ++exchange[j];
                            if( j > 0 ){
                                exchange[j-1] = 0;
                            }
                            break;
                        }else {
//                            int a = order[j];
//                            order[j] = order[j+1];
//                            order[j+1] = a;
//                            ++exchange[j];
//                            exchange[j-1] = 0;
//                            break;
                        }

                    }
                }
            } while ( true);


            
        }
    }

}

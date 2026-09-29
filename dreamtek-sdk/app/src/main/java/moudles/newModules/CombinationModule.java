package moudles.newModules;

import android.text.TextUtils;
import android.util.Log;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import entity.cases.BaseCase;

/**
 * Created by RuihaoS on 2021/5/15.
 */


public class CombinationModule extends TestModule {

    private static final String TAG = "CombinationModule";

    ArrayList<BaseCase> combineList = new ArrayList<>(); // Use the parent version.


    /**
     * Add cases based on API name and JSON configuration file.
     *
     * @param cases All cases configured in the JSON file.
     */
    protected void addAllapi(ArrayList<BaseCase> cases) {
        /** Clear all APIs and re-add. **/
        apiList.clear();
        apiList.add("Combination");
        /** clear all caseNames **/
        caseNames.clear();
        allCases.clear();
        if (cases == null || cases.size() == 0) {
            logUtils.addCaseLog("No Cases, please import cases");
            return;
        }
        combineList.clear();
        /** Start reading cases and match them with APIs. **/
        Iterator<BaseCase> caseIterator = cases.iterator();
        while (caseIterator.hasNext()) {
            BaseCase nextCase = caseIterator.next();
            /** Skip case if API NAME is not configured. **/
            if (nextCase == null
                    || TextUtils.isEmpty(nextCase.getApi())
                    || nextCase.getModuleId() == null
                    || !"combination".equalsIgnoreCase(nextCase.getModuleId())) {
                continue;
            }
            combineList.add(nextCase);
            allCases.add(nextCase);
        }
        caseNames.add(combineList);

    }



    public void runTheMethod(ServiceModule serviceModule, BaseCase caseInfo) {
        printMsgTool( caseInfo.getCaseId() + "\n" + caseInfo.getCaseDescribe() , Log.DEBUG );
        String[] apiList = caseInfo.getApi().split("\\|");
        String[] paramsList = caseInfo.getMethodParams().split("\\|");
        String[] resultType = caseInfo.getExpectResultType().split("\\|");
        String[] expectResult = caseInfo.getExpectResult().split("\\|");
        if (apiList.length != paramsList.length) {
            return;
        }
        logUtils.clearLog();
        String caseId = caseInfo.getCaseId();
        String caseResult = "";
        for (int i = 0; i < apiList.length; i++) {
            String singleApi = apiList[i];
            String[] api = singleApi.split("\\_");
            if (api.length != 2) {
                return;
            }
            String moduleName = api[0];
            String apiName = api[1];
            Log.d(TAG, "moduleName->" + moduleName);
            Log.d(TAG, "apiName->" + apiName);


            String methodParam = paramsList[i].trim();
            Object[] methods = new Object[]{};
            Class<?>[] params = new Class[]{};
            String[] parameters;
            if (!TextUtils.isEmpty(methodParam)) {
                parameters = methodParam.split(",");
                for (int k = 0; k < parameters.length ; k++) {
                    parameters[k] = parameters[k].trim();
                }
                methods = parameters;
            }
            if (methods.length > 0) {
                params = new Class[methods.length];
                for (int j = 0; j < methods.length; j++) {
                    params[j] = String.class;
                }
            }
            logUtils.addCaseLog(apiName + " params:" + methodParam);
            try {
                Class<?> aClass = Class.forName("moudles.newModules." + moduleName + "Module");
                Log.d(TAG, "Got" + aClass.toString() + ", from:" + moduleName );
                Method method;
                if (methods.length > 0 && methodParam.length() > 0 ) {
                    method = aClass.getDeclaredMethod( "T_" + apiName, params);
                } else {
                    method = aClass.getDeclaredMethod( "T_" + apiName );
                }

                method.setAccessible(true);
                Object result;
                if (methods.length > 0 && methodParam.length() > 0) {
                    result = method.invoke(getInvokeModule(moduleName, serviceModule), methods);
                } else {
                    result = method.invoke(getInvokeModule(moduleName, serviceModule));
                }
                if( expectResult.length > i
                    && resultType.length > i ) {
                    // check the result

                    Log.d(TAG,"Combination resultType before filtering: "+resultType);

                    if (resultType[i].contains("public")) {
                        resultType[i] = resultType[i].replace("public", "").trim();
                    }
                    Log.d(TAG,"Combination resultType after filtering: "+resultType);

                    int ret = caseInfo.setResult( resultType[i], expectResult[i], result );

                    if( ret >= 0 ){
                        logUtils.addCaseLog(apiName + ", execute succeeded: " + ret );
                        this.printMsgTool( apiName + ", succeeded: " + ret + ":\n" + caseInfo.getValue( BaseCase.Items.ActualValue ), Log.INFO );

                    } else {
                        logUtils.addCaseLog(apiName + ", execute failed: " + ret );
                        Log.e(TAG, apiName + ", execute failed: " + ret );
                        this.printMsgTool( apiName + ", failed: " + ret + ":\n" + caseInfo.getValue( BaseCase.Items.ActualValue ), Log.ERROR );
                    }
                } else {
                    logUtils.addCaseLog(apiName + ", execute completed: " );
                }


            } catch (Exception e) {
                e.printStackTrace();
                logUtils.addCaseLog(apiName + "exception found during execution");
            }
        }

        logUtils.addCaseLog(caseId + ", execute completed");
    }

    private Object getInvokeModule(String moduleName, ServiceModule serviceModule) {
        return serviceModule.getModule(moduleName);
    }




}

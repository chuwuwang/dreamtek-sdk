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

    ArrayList<BaseCase> combineList = new ArrayList<>(); // 使用父类的


    /**
     * 根据API Name 与json配置文件 加入 cases
     *
     * @param cases JSON 文件里配置额所有Cases
     */
    protected void addAllapi(ArrayList<BaseCase> cases) {
        /** 清空所有的API重新添加**/
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
        /** 开始读取cases 并配对API **/
        Iterator<BaseCase> caseIterator = cases.iterator();
        while (caseIterator.hasNext()) {
            BaseCase nextCase = caseIterator.next();
            /** 未配置API NAME 则跳过该cases **/
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
                /** 获取本方法所有参数类型 **/
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
                    //过滤掉public
                    Log.d(TAG,"Combination过滤前的resultType"+resultType);

                    if (resultType[i].contains("public")) {
                        resultType[i] = resultType[i].replace("public", "").trim();
                    }
                    Log.d(TAG,"Combination过滤后的resultType"+resultType);

                    int ret = caseInfo.setResult( resultType[i], expectResult[i], result );

                    if( ret >= 0 ){
                        logUtils.addCaseLog(apiName + ", execute 成功:" + ret );
                        this.printMsgTool( apiName + ", 成功:" + ret + ":\n" + caseInfo.getValue( BaseCase.Items.ActualValue ), Log.INFO );

                    } else {
                        logUtils.addCaseLog(apiName + ", execute 失败：" + ret );
                        Log.e(TAG, apiName + ", execute 失败：" + ret );
                        this.printMsgTool( apiName + ", 失败：" + ret + ":\n" + caseInfo.getValue( BaseCase.Items.ActualValue ), Log.ERROR );
                    }
                } else {
                    logUtils.addCaseLog(apiName + ", execute 完毕:" );
                }


            } catch (Exception e) {
                e.printStackTrace();
                logUtils.addCaseLog(apiName + "exception found during execution");
            }
        }

        logUtils.addCaseLog(caseId + ", execute 完毕");
    }

    private Object getInvokeModule(String moduleName, ServiceModule serviceModule) {
        return serviceModule.getModule(moduleName);
    }




}

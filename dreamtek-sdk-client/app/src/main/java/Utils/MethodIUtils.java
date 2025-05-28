package Utils;

import java.util.ArrayList;

/**
 * Created by WenpengL1 on 2016/12/29.
 */

public class MethodIUtils
{
    ArrayList<ArrayList<String>> serviceBt = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> magcardBt = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> touchicBt = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> notouchIcBt = new ArrayList<ArrayList<String>>();

    ArrayList<ArrayList<String>> beerBt = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> ledBt = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> scanBt = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> pintBt = new ArrayList<ArrayList<String>>();

    ArrayList<ArrayList<String>> portBt = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> pinpadBt = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> pbocBt = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> deviceBt = new ArrayList<ArrayList<String>>();

    public MethodIUtils()
    {

    }

    /**
     * 根据模块名字，利用反射，拿到对应的二级字符串
     *
     * @param value
     * @return
     */
    public static ArrayList<ArrayList<String>> getChildCaseNames(String value)
    {
        return null;
    }

    /**
     * 利用反射，拿到api的方法数组
     *
     * @param value
     * @return
     */
    public static ArrayList<String> getAPIs(String value)
    {
        return null;
    }

    public void runTheMethod(String moudleName, int index)
    {
        // String method = ledMethod.get(index);
       /* try
        {
            Class aClass = Class.forName("moudls." + moudleName);
           *//* TouchicBtMoudle moudle = (TouchicBtMoudle) aClass.newInstance();
            // aClass.getMethod(method, aClass);
            Method method1 = aClass.getMethod("");
            method1.invoke(moudle);*//*
        } catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        } catch(InstantiationException e)
        {
            e.printStackTrace();
        } catch(IllegalAccessException e)
        {
            e.printStackTrace();
        } catch(InvocationTargetException e)
        {
            e.printStackTrace();
        }*/
    }


}
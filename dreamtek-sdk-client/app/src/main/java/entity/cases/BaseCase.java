package entity.cases;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.verifone.smartpos.utils.StringUtil;


/**
 * Created by RuihaoS on 2021/5/9.
 */
public class BaseCase<row> implements Parcelable {
  private static final String TAG = "BaseCase";

  // values 保存测试案例的属性
  private String[] values = new String[Index+1];
  private static int Index = 0;


  public void resetResult() {
    setValue( Items.ActualValue, "" );
    setValue( Items.Result, "" );
  }

  public void setResult(String result) {
    setValue( Items.Result, result );
  }

  public static String getErrorMessage( int errorCode ){
    errorCode = errorCode*(-1);
    if( errorCode >= 0 && errorCode < errorMessage.length ){
      return errorMessage[errorCode];
    } else {
      return " - ";
    }
  }

  public static String[] errorMessage = {
          "", // 0
          "返回值不匹配", // 1 for -1
          "预期结果未设置", // 2 for -2
          "预期结果未设置", // 3 for -3
          "数据类型不支持", // 4 for -4
          "返回 null", // 5 for -5
          "请填写结果", // 6 for -6
  };
  public int setResult(String resultType, String expectResult, Object apiResult ) {
    if( resultType == null
            || expectResult == null
             ){
      setValue( Items.Result, "Not set result" );
      return -2;
    }
    String actualResult = getValue( Items.ActualValue );
    String result = getValue( Items.Result );

    if( apiResult == null ) {
      if( expectResult.compareTo("null") == 0
      || expectResult.compareTo("void") == 0) {
        if( result.length() > 1 ){
          result += "|";
        }
        result = "Pass";
        if( actualResult.length() > 1 ){
          actualResult += "|";
        }
        actualResult += "null";

        setValue( Items.Result , result );
        setValue( Items.ActualValue, actualResult );
        return 0;
      } else if( expectResult.compareToIgnoreCase("input") == 0 ){
        if( actualResult.length() > 1 ){
          actualResult += "|";
        }
        actualResult += "INPUT";

        setValue( Items.Result , result );
        setValue( Items.ActualValue, actualResult );
        return -6;
      }
      else {
        if( actualResult.length() > 1 ){
          actualResult += "|";
        }
        actualResult += "null";
      }
      if( result.length() > 1 ){
        result += "|";
      }
      result += "null returns";

      setValue( Items.Result , result );
      setValue( Items.ActualValue, actualResult );
      return -5;
    }
    if( resultType.length() == 0
            || ( resultType.compareToIgnoreCase("Skip") != 0 && expectResult.length() == 0 ) ) {
      if( result.length() > 1 ){
        result += "|";
      }
      result += "Not set result";

      setValue( Items.Result , result );
      setValue( Items.ActualValue, actualResult );
      return -3;
    }

    int ret = 0;
    // check the result
    if( actualResult.length() > 1 ){
      actualResult += "|";
    }
    switch (resultType.toLowerCase() ){
      case "boolean" :
        if( expectResult.compareToIgnoreCase("Skip") == 0){
          actualResult += "Skip:" + ((Boolean) apiResult).toString();
          ret = 1;
        } else
        if( ((Boolean) apiResult).toString().compareTo( expectResult ) == 0 ){
          ret = 0;
          actualResult += (Boolean) apiResult;
        } else {
          actualResult += (Boolean) apiResult;
          ret = -1;
        }
        break;
      case "string":
        if( expectResult.compareToIgnoreCase("Skip") == 0){
          actualResult += "Skip:";
          ret = 1;
        } else
        if( ((String)apiResult).compareTo( expectResult) == 0  ){
          ret = 0;
        } else {
          ret = -1;
        }
        actualResult += (String)apiResult;
        break;
      case "byte":
        if( expectResult.compareToIgnoreCase("Skip") == 0){
          actualResult += "Skip:";
          ret = 1;
        } else
        if(  StringUtil.byte2HexStr( (Byte)apiResult).toUpperCase().compareTo( expectResult) == 0  ){
          ret = 0;
        } else {
          ret = -1;
        }
        actualResult += StringUtil.byte2HexStr( (Byte)apiResult);
        break;
      case "byte[]":
      {
          byte[] bytes = (byte[]) apiResult;

        if( expectResult.compareToIgnoreCase("Skip") == 0){
          actualResult += "Skip:";
          ret = 1;
        } else
        if(  StringUtil.byte2HexStr( bytes ).toUpperCase().compareTo( expectResult) == 0  ){
            ret = 0;
          } else {
            Log.e(TAG, "API returns:" + StringUtil.byte2HexStr( bytes ).toUpperCase() );
            ret = -1;
          }
        actualResult += StringUtil.byte2HexStr( bytes );
      }
      break;
      case "skip":
      {
        ret = 1;
        actualResult += "Skip";
      }
        break;
      case "void":
      {
        ret = 1;
        actualResult += "void";
      }
        break;
    case "int":
      if( expectResult.compareToIgnoreCase("Skip") == 0){
        actualResult += "Skip:";
        ret = 1;
      } else
      if(  ((Integer)apiResult).intValue() == Integer.valueOf( expectResult) ){
        ret = 0;
      } else {
        Log.e( TAG, "Compare: " + ((Integer)apiResult).intValue() +" with "+ Integer.valueOf( expectResult)) ;
        ret = -1;
      }
      actualResult += ((Integer)apiResult).toString();
      break;

      default:
        ret = -4;
        actualResult += "Invalid Type";
        break;
    }
    if( result.length() > 1 ){
      result += "|";
    }
    if( ret == 0 ){
        result += "Pass";
    } else if( ret == -1 ) {
      result += "Failure";
    } else if( ret == 1 ) {
      result += "Skip";
    } else {
      result += "Invalid Type";
    }

    setValue( Items.Result , result );
    setValue( Items.ActualValue, actualResult );
    return ret;
  }

  public void setCaseLog(String caseLog) {
    setValue( Items.Log, caseLog );
  }

  @Override
  public String toString() {
    return "BaseCase{" +
            getValue( Items.CaseID) + '\'' +
            getValue( Items.Describe ) +
            '}';
  }

  public enum Items {
    ModuleID  ( 12, "Module ID", "moduleId"), //
    Api       ( 16, "API", "api"),
    CaseID    ( 20, "Case ID", "caseId"),
    Describe  ( 20, "Describe", "caseDescribe"),
    Result    ( 12, "Result"),
    Parameter ( 40, "Parameter", "methodParams"),
    ExpectResult( 30, "ExpectResult", "expectResult"),
    ActualValue( 30, "实际结果"),
    Status(8, "Status", "caseStatus"),
    Log(80, "Log"),
    ResultType( 8,"Result Type", "expectResultType"),
    ApiNotes (80, "TestClient API参数说明", "apiNotes"),
    AidlNotes (80, "AIDL参数说明", "AidlNotes"),
    ;

    public String title;  // excel 的每列的标题
    public String jsonKey;  // excel 每列对应的json key
    protected int type;  // 类型，
    public int width; // excel 每列的宽度
    public boolean isRead;
    public boolean isWrite;
    protected static final int TypeRead = 1; // 从 excel读出数值
    protected static final int TypeWrite= 2; // 写回到 excel 作为报告
    protected int index;  // 对应 BaseCase的Values数组的索引

    Items( int width, String title){
      this( width, title,"", TypeWrite);
    }
    Items( int width, String title, String jsonKey){
      this( width, title,jsonKey, TypeRead + TypeWrite);
    }

    Items( int width, String title, String jsonKey, int type){
      this.width = width;
      this.title = title;
      this.jsonKey = jsonKey;
      this.type = type;
      if( (type & TypeWrite) == TypeWrite ) {
        isWrite = true;
      } else {
        isWrite = false;
      }
      if( (type & TypeRead) == TypeRead ) {
        isRead = true;
      } else {
        isRead = false;
      }
      this.index = Index;
      Index++;
    }

  };

  public static String getTitle( Items items){
    return items.title;
  }
  public static int getWidth( Items items){
    return items.width;
  }
  public String getValue( Items items){
    return values[ items.index ];
  }
  public void setValue( Items items, String value ){
    values[ items.index ] = value;
  }

  public String getMethodParams() {
    return getValue( Items.Parameter);
  }

  public String getModuleId() {
    return getValue( Items.ModuleID );
  }


  public String getApi() {
    return getValue( Items.Api );
  }

  public void setApi(String api) {
    setValue( Items.Api, api );
  }

  public String getCaseId() {
    // 设置的代码
    // singleCase.setValue(BaseCase.Items.CaseID, sheetName + singleCase.getCaseId());
    return getValue( Items.CaseID );
  }

  public String getCaseDescribe() {
    return getValue( Items.Describe );
  }


  public String getExpectResultType() {
    return getValue( Items.ResultType );
  }

  public String getExpectResult() {
    return getValue( Items.ExpectResult );
  }

  public int getCaseStatus() {
    try {
      int status = Integer.valueOf(getValue(Items.Status));
      return status;
    } catch ( java.lang.NumberFormatException e){
      return 0;
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    for ( BaseCase.Items item: BaseCase.Items.values()  ) {
      dest.writeString( getValue(item) );
    }
  }

  public void readFromParcel(Parcel source) {
    for ( BaseCase.Items item: BaseCase.Items.values()  ) {
      setValue( item, source.readString());
    }
  }

  public BaseCase() {
    setValue(Items.Result , "");
    setValue(Items.ActualValue, "");
  }
  public BaseCase(BaseCase baseCase) {
    for ( BaseCase.Items item: BaseCase.Items.values()  ) {
      setValue( item, baseCase.getValue(item) );
    }
  }

  protected BaseCase(Parcel in) {
    for ( BaseCase.Items item: BaseCase.Items.values()  ) {
      setValue( item, in.readString());
    }
  }

  public static final Creator<BaseCase> CREATOR = new Creator<BaseCase>() {
    @Override
    public BaseCase createFromParcel(Parcel source) {
      return new BaseCase(source);
    }

    @Override
    public BaseCase[] newArray(int size) {
      return new BaseCase[size];
    }
  };
}

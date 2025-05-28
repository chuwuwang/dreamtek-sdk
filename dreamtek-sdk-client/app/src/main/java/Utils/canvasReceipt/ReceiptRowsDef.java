package Utils.canvasReceipt;

/**
 * Created by Simon on 2019/2/27.
 */


import android.graphics.Color;

/**
 * this is for ALL items defined
 *
 * Fixed list
 * 2020/7/31, add FONT_16 for test
 */
public enum ReceiptRowsDef {

    // parameters: type, description (file name for Logo), string value, integer value, print style of description, print style of value
    // print style of description, print style of value, default font size 16, alignment left
//    LOGO        (PrinterItemType.IMAGE_ASSETS, new PrinterElement("verifone_logo.jpg"), new PrinterElement() ),
    LOGO        (ReceiptRowType.IMAGE_RES, new ReceiptItem(0), new ReceiptItem(0) ),

    LOGO_CACHE  (ReceiptRowType.IMAGE_BCD, new ReceiptItem(0), new ReceiptItem(0) ),

    HEADER1     (ReceiptRowType.STRING, new ReceiptItem(), new ReceiptItem()),
    HEADER2     (ReceiptRowType.STRING, new ReceiptItem(), new ReceiptItem()),
    HEADER3     (ReceiptRowType.STRING, new ReceiptItem(), new ReceiptItem()),
    HEADER4     (ReceiptRowType.STRING, new ReceiptItem(), new ReceiptItem()),
    HEADER5     (ReceiptRowType.STRING, new ReceiptItem(), new ReceiptItem()),
    HEADER6     (ReceiptRowType.STRING, new ReceiptItem(), new ReceiptItem()),

    TEXT2DISPLAY(ReceiptRowType.STRING, ReceiptItemDefine.PStyle_only_show, new ReceiptItem( "This is only for displaying", 20, ReceiptItemDefine.PStyle_align_center, Color.GREEN), new ReceiptItem() ),
    TEXT2PRINT  (ReceiptRowType.STRING, ReceiptItemDefine.PStyle_only_print, new ReceiptItem( "This is only for printing", ReceiptItemDefine.PStyle_align_center), new ReceiptItem( )),

    DUPLICATE   (ReceiptRowType.STRING, new ReceiptItem("DUPLICATE", 30, ReceiptItemDefine.PStyle_align_center + ReceiptItemDefine.PStyle_revert), new ReceiptItem()),

    TITLE       (ReceiptRowType.STRING, new ReceiptItem("Verifone X990", 32 , ReceiptItemDefine.PStyle_align_center), new ReceiptItem()),
    SUBTITLE    (ReceiptRowType.STRING, new ReceiptItem("", 20, ReceiptItemDefine.PStyle_align_right ), new ReceiptItem()),
    COPY_NOTE   (ReceiptRowType.STRING, new ReceiptItem("", 20, ReceiptItemDefine.PStyle_align_left ), new ReceiptItem("merchant copy", 16)),
    MERCHANT_NAME(ReceiptRowType.STRING, new ReceiptItem("Merchant Name",24, ReceiptItemDefine.PStyle_align_left),  new ReceiptItem("Verifone", + 32, ReceiptItemDefine.PStyle_align_center , ReceiptItemDefine.Font_Bold)), // +32 + PStyle_force_multi_lines
    MERCHANT_ID (ReceiptRowType.STRING, new ReceiptItem("Merchant ID"),  new ReceiptItem("", 32)),
    MERCHANT_NAME_ID (ReceiptRowType.STRING, new ReceiptItem("Merchant ID"),  new ReceiptItem()),
    TERMINAL_ID (ReceiptRowType.STRING, new ReceiptItem("Terminal ID"),  new ReceiptItem( )),
    OPERATOR_ID (ReceiptRowType.STRING, new ReceiptItem("Operator ID",20),  new ReceiptItem( )),
    HOST        (ReceiptRowType.STRING, new ReceiptItem("HOST", 20),  new ReceiptItem()),
    TRANS_TYPE  (ReceiptRowType.STRING, new ReceiptItem(),
                                         new ReceiptItem("", 28, ReceiptItemDefine.PStyle_align_center + ReceiptItemDefine.PStyle_revert_row, ReceiptItemDefine.Font_Bold )),
    PRINT_TYPE  (ReceiptRowType.STRING, new ReceiptItem(),
            new ReceiptItem("", 28, ReceiptItemDefine.PStyle_align_center + ReceiptItemDefine.PStyle_revert_row, ReceiptItemDefine.Font_Bold )),
    CARD_ISSUE  (ReceiptRowType.STRING, new ReceiptItem("Card Issue", 20),  new ReceiptItem() ),
    CARD_NO     (ReceiptRowType.STRING, new ReceiptItem("",  20),
                                         new ReceiptItem("",20, ReceiptItemDefine.PStyle_align_left , 0, ReceiptItemDefine.Font_Bold, Color.YELLOW ) , false ), //+ PStyle_align_center + PStyle_force_multi_lines),
    CARD_TYPE   (ReceiptRowType.STRING, new ReceiptItem("Card Type"),  new ReceiptItem() ),
    CARD_HOLDER (ReceiptRowType.STRING, new ReceiptItem("Card Holder", 20),  new ReceiptItem() ),
    CARD_VALID  (ReceiptRowType.STRING, new ReceiptItem("Card Valid", 20),  new ReceiptItem() ),
    AID         (ReceiptRowType.STRING, new ReceiptItem("AID"),  new ReceiptItem() ),
    APP         (ReceiptRowType.STRING, new ReceiptItem("APP"),  new ReceiptItem() ),
    BATCH_NO    (ReceiptRowType.STRING, new ReceiptItem("BATCH #"),  new ReceiptItem() ),
    DATE_TIME   (ReceiptRowType.STRING, new ReceiptItem("DATE/TIME", 14),  new ReceiptItem(24) ),
    REFER_NO    (ReceiptRowType.STRING, new ReceiptItem("REFER", 20),  new ReceiptItem() ),
    TRACK_NO    (ReceiptRowType.STRING, new ReceiptItem("TRACE #", 20),  new ReceiptItem() ),
    AUTH_NO     (ReceiptRowType.STRING, new ReceiptItem("APR CODE : ", 20),  new ReceiptItem() ),
    AMOUNT      (ReceiptRowType.STRING, new ReceiptItem("AMT  PHP", 32, ReceiptItemDefine.Font_Bold),  new ReceiptItem("12.34", 32, ReceiptItemDefine.PStyle_revert, ReceiptItemDefine.Font_Bold) ),
    AMOUNT_BASE (ReceiptRowType.STRING, new ReceiptItem("BASE PHP", 32, ReceiptItemDefine.Font_Bold),  new ReceiptItem("", 32, ReceiptItemDefine.Font_Bold) ),
    AMOUNT_TIP  (ReceiptRowType.STRING, new ReceiptItem("TIP  PHP", 32, ReceiptItemDefine.Font_Bold),  new ReceiptItem("", 32, ReceiptItemDefine.Font_Bold) ),
    AMOUNT_TOTAL(ReceiptRowType.STRING, new ReceiptItem("TOTAL", 32, ReceiptItemDefine.PStyle_revert, ReceiptItemDefine.Font_Bold ),  new ReceiptItem("", 32, ReceiptItemDefine.Font_Bold) ),
    BALANCE     (ReceiptRowType.STRING, new ReceiptItem("BALANCE", 20),  new ReceiptItem() ),
    TIP         (ReceiptRowType.STRING, new ReceiptItem("TIP"),  new ReceiptItem() ),
    TOTAL       (ReceiptRowType.STRING, new ReceiptItem("TOTAL"),  new ReceiptItem() ),
    REFERENCE   (ReceiptRowType.STRING, new ReceiptItem("REFERENCE"),  new ReceiptItem() ),
    PIN         (ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem("", ReceiptItemDefine.PStyle_font_size_small, ReceiptItemDefine.PStyle_align_center ) ),
    E_SIGN      (ReceiptRowType.IMAGE_BCD, new ReceiptItem("SIGN",20),  new ReceiptItem() ),    // title will be printed if is not blank
    SIGN        (ReceiptRowType.STRING, new ReceiptItem("SIGN",0),  new ReceiptItem("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _", 0) ),
    RE_PRINT_NOTE(ReceiptRowType.STRING, new ReceiptItem("RE-PRINT",20 , ReceiptItemDefine.PStyle_align_center),  new ReceiptItem() ),
    TC          (ReceiptRowType.STRING, new ReceiptItem("TC",20),  new ReceiptItem() ),
    PTS         (ReceiptRowType.STRING, new ReceiptItem("TOTAL AVAILPTS:"),  new ReceiptItem() ),

    COMMENT_1   (ReceiptRowType.STRING, ReceiptItemDefine.PStyle_only_print, new ReceiptItem("", 12, ReceiptItemDefine.PStyle_align_center, ReceiptItemDefine.Font_Bold),  new ReceiptItem() ),
    COMMENT_2   (ReceiptRowType.STRING, ReceiptItemDefine.PStyle_only_print, new ReceiptItem("", 12, ReceiptItemDefine.PStyle_align_center, ReceiptItemDefine.Font_Bold),  new ReceiptItem() ),
    COMMENT_3   (ReceiptRowType.STRING, ReceiptItemDefine.PStyle_only_print, new ReceiptItem("", 12, ReceiptItemDefine.PStyle_align_center, ReceiptItemDefine.Font_Bold),  new ReceiptItem() ),
    COMMENT_4   (ReceiptRowType.STRING, ReceiptItemDefine.PStyle_only_print, new ReceiptItem("", 12, ReceiptItemDefine.PStyle_align_center, ReceiptItemDefine.Font_Bold),  new ReceiptItem() ),

    COPY_TYPE_1 (ReceiptRowType.STRING, ReceiptItemDefine.PStyle_receipt_1, new ReceiptItem("", 16, ReceiptItemDefine.PStyle_align_center, ReceiptItemDefine.Font_Bold),  new ReceiptItem() ),
    COPY_TYPE_2 (ReceiptRowType.STRING, ReceiptItemDefine.PStyle_receipt_2, new ReceiptItem("", 16, ReceiptItemDefine.PStyle_align_center, ReceiptItemDefine.Font_Bold),  new ReceiptItem() ),
    COPY_TYPE_3 (ReceiptRowType.STRING, ReceiptItemDefine.PStyle_receipt_3, new ReceiptItem("", 16, ReceiptItemDefine.PStyle_align_center, ReceiptItemDefine.Font_Bold),  new ReceiptItem() ),

    FOOTER_1  (ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    FOOTER_2  (ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    FOOTER_3  (ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    FOOTER_4  (ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    FOOTER_5  (ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    FOOTER_6  (ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),

    BLANK_LINE  (ReceiptRowType.STRING, new ReceiptItem(" "),  new ReceiptItem() ),

//    GUIDE1        (PrinterItemType.IMAGE_ASSETS, new PrinterElement("guide/2-main.png"), new PrinterElement() ),
//    GUIDE2        (PrinterItemType.IMAGE_ASSETS, new PrinterElement("guide/2-main.png"), new PrinterElement() ),
//    GUIDE3        (PrinterItemType.IMAGE_ASSETS, new PrinterElement("guide/2-main.png"), new PrinterElement() ),
//    GUIDE4        (PrinterItemType.IMAGE_ASSETS, new PrinterElement("guide/2-main.png"), new PrinterElement() ),

    BARCODE_1     (ReceiptRowType.BARCODE, new ReceiptItem("Barcode for refund", 12, ReceiptItemDefine.PStyle_align_center), new ReceiptItem("123456789", 24, ReceiptItemDefine.PStyle_align_center ) ),
    BARCODE_2     (ReceiptRowType.BARCODE, new ReceiptItem("Barcode for refund", 18, ReceiptItemDefine.PStyle_align_center), new ReceiptItem("123456789", 48, ReceiptItemDefine.PStyle_align_center ) ),

    QRCODE_1     (ReceiptRowType.QRCODE, new ReceiptItem("123456789", 160, ReceiptItemDefine.PStyle_align_center), new ReceiptItem("123456789", 128, ReceiptItemDefine.PStyle_align_center) ),
    QRCODE_2     (ReceiptRowType.QRCODE, new ReceiptItem("123456789", 128, ReceiptItemDefine.PStyle_align_center), new ReceiptItem("123456789", 160, ReceiptItemDefine.PStyle_align_center) ),

    CUT         (ReceiptRowType.STRING, 1, new ReceiptItem("----------x----------x----------", 24 ),  new ReceiptItem() ),
    LINE        (ReceiptRowType.LINE, new ReceiptItem("",2),  new ReceiptItem() ),
    LINE_DOT    (ReceiptRowType.LINE, new ReceiptItem("",0x11),  new ReceiptItem() ),
    LINE_DOT_2X (ReceiptRowType.LINE, new ReceiptItem("",0x22),  new ReceiptItem() ),
    LINE_DOT_4X (ReceiptRowType.LINE, new ReceiptItem("",0x42),  new ReceiptItem() ),
    LINE_DOT_8X (ReceiptRowType.LINE, new ReceiptItem("",0x82),  new ReceiptItem() ),
    LINE_DOT_16X4(ReceiptRowType.LINE, new ReceiptItem("",0x104),  new ReceiptItem() ), // 长度16，粗细4

    FONT_16  (ReceiptRowType.STRING, new ReceiptItem("", 16, ReceiptItemDefine.Font_default),  new ReceiptItem("", 16, ReceiptItemDefine.Font_default) ),
    FONT_18  (ReceiptRowType.STRING, new ReceiptItem("", 18, ReceiptItemDefine.Font_default),  new ReceiptItem("", 18, ReceiptItemDefine.Font_default) ),
    FONT_20  (ReceiptRowType.STRING, new ReceiptItem(20),  new ReceiptItem(20) ),
    FONT_24  (ReceiptRowType.STRING, new ReceiptItem(24),  new ReceiptItem(24) ),

    FONT_INFO  (ReceiptRowType.STRING, new ReceiptItem("", 20, ReceiptItemDefine.Font_default),  new ReceiptItem("", 20, ReceiptItemDefine.Font_Heavy) ),
    FONT_WARN  (ReceiptRowType.STRING, new ReceiptItem( "", 22, ReceiptItemDefine.Font_default),  new ReceiptItem("", 22, ReceiptItemDefine.Font_Heavy) ),
    FONT_ERROR (ReceiptRowType.STRING, new ReceiptItem("", 24, ReceiptItemDefine.Font_default),  new ReceiptItem("", 24 + ReceiptItemDefine.PStyle_revert, ReceiptItemDefine.Font_Heavy) ),
    FONT_DEBUG (ReceiptRowType.STRING, new ReceiptItem( "", 16, ReceiptItemDefine.Font_Bold),  new ReceiptItem("", 16, ReceiptItemDefine.Font_Heavy) ),

    FEED        (ReceiptRowType.FEED, new ReceiptItem("",2),  new ReceiptItem() ), // pixel for feed

    FEED_LINE   (ReceiptRowType.FEED, 1,  new ReceiptItem("",20),  new ReceiptItem() ), // pixel for feed
    FEED_4p     (ReceiptRowType.FEED, new ReceiptItem("",4),  new ReceiptItem() ), // pixel for feed
    FEED_8p     (ReceiptRowType.FEED, new ReceiptItem("",8),  new ReceiptItem() ), // pixel for feed
    FEED_12p    (ReceiptRowType.FEED, new ReceiptItem("",12),  new ReceiptItem() ), // pixel for feed
    FEED_16p    (ReceiptRowType.FEED, new ReceiptItem("",16),  new ReceiptItem() ), // pixel for feed
    FEED_20p    (ReceiptRowType.FEED, new ReceiptItem("",20),  new ReceiptItem() ), // pixel for feed
    FEED_24p    (ReceiptRowType.FEED, new ReceiptItem("",24),  new ReceiptItem() ), // pixel for feed

    // settlement start
    SETTL_COMMENT1(ReceiptRowType.STRING, new ReceiptItem(18),  new ReceiptItem(18) ),
    SETTL_DETAIL_TITLE_1(ReceiptRowType.STRING, new ReceiptItem(20),  new ReceiptItem(20) ),
    SETTL_DETAIL_TITLE_2(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    SETTL_DETAIL_TITLE_3(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    SETTL_DETAIL_TITLE_4(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),

    SETTL_DETAIL_LINE_1(ReceiptRowType.STRING, new ReceiptItem("", ReceiptItemDefine.Font_Bold),  new ReceiptItem("", ReceiptItemDefine.Font_Bold) ),
    SETTL_DETAIL_GRAND_TOTAL(ReceiptRowType.STRING, new ReceiptItem("GRAND_TOTAL", 24, ReceiptItemDefine.Font_Bold),  new ReceiptItem() ),
    SETTL_DETAIL_FINISH(ReceiptRowType.STRING, new ReceiptItem("SETTLEMENT CONFIRMED", 24),  new ReceiptItem() ),
    SETTL_DETAIL_LINE_4(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    // settlement end
    VOID_INSTALLMENT_1(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    VOID_INSTALLMENT_2(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    VOID_INSTALLMENT_3(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    VOID_INSTALLMENT_4(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    VOID_INSTALLMENT_5(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),
    VOID_INSTALLMENT_6(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem() ),

    REDEMPTION_1(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem()),
    REDEMPTION_2(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem()),
    REDEMPTION_3(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem()),
    REDEMPTION_4(ReceiptRowType.STRING, new ReceiptItem(),  new ReceiptItem()),

    CANVAS      (ReceiptRowType.IMAGE_RES, new ReceiptItem("",20),  new ReceiptItem() ),    // title will be printed if is not blank

    ;   // DO NOT REMOVE THE ;, add other definition before!


    public ReceiptRowType type;
    public ReceiptItem title;
    public ReceiptItem value;
    /**
     * 1 for paper, 2 for show, 3 for paper and show as the default
     */
    public int printerMode;

    public boolean isForceMultiLines;

    private ReceiptRowType df_type;
    private ReceiptItem df_title;
    private ReceiptItem df_value;
    private boolean df_isForceMultiLines;

    private void set(ReceiptRowType type, ReceiptItem title, ReceiptItem value, boolean isForceMultiLines, int printerMode){
        if( 0 == (title.style & (ReceiptItemDefine.PStyle_align_set) ) ){
            title.style |= ReceiptItemDefine.PStyle_align_left;
        }
        if( 0 == (value.style & (ReceiptItemDefine.PStyle_align_set) ) ){
            value.style |= ReceiptItemDefine.PStyle_align_right;
        }

        this.df_type = type;
        this.df_title = title;
        this.df_value = value;
        this.df_isForceMultiLines = isForceMultiLines ;

        this.type = df_type;
        this.title = df_title;
        this.value = df_value;
        this.isForceMultiLines = this.df_isForceMultiLines;

        this.printerMode = printerMode;
    }

    ReceiptRowsDef(ReceiptRowType type, ReceiptItem title, ReceiptItem value){
        set(type, title, value, false , 3);
    }
    ReceiptRowsDef(ReceiptRowType type, ReceiptItem title, ReceiptItem value, boolean isForceMultiLines ){
        set(type, title,value, isForceMultiLines , 3);
    }
    ReceiptRowsDef(ReceiptRowType type, int printerMode, ReceiptItem title, ReceiptItem value){
        set(type, title, value, false , printerMode);
    }

    public void copy( ReceiptRowsDef printerItem ){
        this.title = new ReceiptItem( printerItem.title);
        this.value = new ReceiptItem( printerItem.value);
        this.type = printerItem.type;
        this.isForceMultiLines = printerItem.isForceMultiLines;
    }

    // restore is used for restore the default value after print
    public void restore(){
        if( type == ReceiptRowType.IMAGE_RES
                || type == ReceiptRowType.IMAGE_BCD
                || type == ReceiptRowType.IMAGE_ASSETS
                || type == ReceiptRowType.IMAGE_STORAGE
            ){
            return;
        }
        type = df_type;
        title = df_title;
        value = df_value;
        isForceMultiLines = df_isForceMultiLines;
    }
    public void setTitle( String title){
        this.title.sValue = title;
    }
    public void set( String value){
        this.value.sValue = value;
    }
    public void set(String title, String value){
        this.title.sValue = title;
        this.value.sValue = value;
    }

    public ReceiptRow toReceiptRows(){
        return new ReceiptRow(this);
    }


}

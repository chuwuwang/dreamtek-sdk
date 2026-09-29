package Utils.canvasReceipt;

/**
 * Created by Simon on 2020/4/1
 */
public class ReceiptRow {
    public ReceiptRowType type;
    public ReceiptItem title;
    public ReceiptItem value;
    /**
     * refer the definition in com/vfi/android/domain/interactor/print/canvas/ReceiptItemDefine.java
     * PStyle_receipt_all
     */
    public int printerMode;

    public boolean isForceMultiLines;
    public ReceiptRow(ReceiptRowsDef r){
        this.type = r.type;
        this.title = new ReceiptItem(r.title);
        this.value = new ReceiptItem(r.value);
        this.printerMode = r.printerMode;
        this.isForceMultiLines = r.isForceMultiLines;
    }
    public ReceiptRow(ReceiptRowType type, ReceiptItem title, ReceiptItem value){
        this.type = type;
        this.title = title;
        this.value = value;
        this.printerMode = ReceiptItemDefine.PStyle_receipt_all;
        this.isForceMultiLines = false;
    }

    public ReceiptRow(String left, String right, boolean isBold){
        this.type = ReceiptRowType.STRING;
        this.title = new ReceiptItem(left);
        if( 0 == (this.title.style & ReceiptItemDefine.PStyle_align_set ) ){
            this.title.style |= ReceiptItemDefine.PStyle_align_left;
        }
        this.value = new ReceiptItem(right);
        if( 0 == (this.value .style & ReceiptItemDefine.PStyle_align_set ) ){
            this.value .style |= ReceiptItemDefine.PStyle_align_right;
        }

        this.printerMode = ReceiptItemDefine.PStyle_receipt_all;
        this.isForceMultiLines = false;
        if( isBold ){
            this.title.fontFile = ReceiptItemDefine.Font_Bold;
            this.value.fontFile = ReceiptItemDefine.Font_Bold;
        }
    }
    public ReceiptRow(String left, int leftStyle, String right, int rightStyle, boolean isBold){
        this.type = ReceiptRowType.STRING;
        this.title = new ReceiptItem(left);
        this.title.style = leftStyle;
        if( 0 == (this.title.style & ReceiptItemDefine.PStyle_align_set ) ){
            this.title.style |= ReceiptItemDefine.PStyle_align_left;

        }
        this.value = new ReceiptItem(right);
        this.value.style = rightStyle;
        if( 0 == (this.value .style & ReceiptItemDefine.PStyle_align_set ) ){
            this.value .style |= ReceiptItemDefine.PStyle_align_right;
        }

        this.printerMode = ReceiptItemDefine.PStyle_receipt_all;
        this.isForceMultiLines = false;
        if( isBold ){
            this.title.fontFile = ReceiptItemDefine.Font_Bold;
            this.value.fontFile = ReceiptItemDefine.Font_Bold;
        }
    }
}

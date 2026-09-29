package Utils.canvasReceipt;

/**
 * Created by Simon on 2019/2/27.
 */

public enum ReceiptRowType {
    STRING,
    IMAGE_ASSETS,
    IMAGE_STORAGE,
    IMAGE_RES,
    LINE,
    FEED,   // the title.size is the pixel for feed
    BARCODE, // the title.size is for the height of barcode
    QRCODE, //  the title.size is for the size of qr rcode
    IMAGE_BCD,
}

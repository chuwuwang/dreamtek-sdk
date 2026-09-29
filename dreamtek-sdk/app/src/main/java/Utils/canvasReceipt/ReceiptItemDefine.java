package Utils.canvasReceipt;

/**
 * Created by Simon on 2019/2/27.
 */

/**
 * Class {@code ReceiptItemDefine } defined the style for printing
 */
public class ReceiptItemDefine {
    // value less than 0x100 means the font size or QR code width
    public static int PStyle_font_size_small = 0;   // 16
    public static int PStyle_font_size_middle = 1; // for 12*24
    public static int PStyle_font_size_big = 2; // 14*28, DH
    public static int PStyle_font_size_large = 3; // 16*32
    public static int PStyle_font_size_large_bold = 4; // 16*32, bold
    public static int PStyle_font_size_hurge = 5; // 24*48

    public static int PStyle_size     = 0x0FFF;
    public static int PStyle_size_set_0     = 0xFFFFF000;

    public static int PStyle_align_left     = 0x1000;
    public static int PStyle_align_center   = 0x2000;
    public static int PStyle_align_right    = 0x4000;
    public static int PStyle_align_append   = 0x8000;   // no new line after it
    public static int PStyle_align_justify_1_of_16 = 0x0100;    // set the width of justify, 1 of 16 is 24 pixel
    public static int PStyle_align_set = PStyle_align_left|PStyle_align_center|PStyle_align_right;
    public static int PStyle_align_remove   = 0xFFFF8FFF;

    public static int PStyle_image_contrast_light  = 0x10000;
    public static int PStyle_image_contrast_normal = 0x20000;
    public static int PStyle_image_contrast_heavy  = 0x40000;

    public static int PStyle_revert         = 0x00100000;
    public static int PStyle_revert_row     = 0x00200000;
    public static int PStyle_blod           = 0x00400000;

    public static int PStyle_receipt_1 = 0x10000000;    // receipt index 1 of 3
    public static int PStyle_receipt_2 = 0x20000000;    // receipt index 2 of 3
    public static int PStyle_receipt_3 = 0x40000000;    // receipt index 3 of 3
    public static int PStyle_receipt_show= 0x80000000;    // receipt for show
    public static int PStyle_only_show = 0x80000000;
    public static int PStyle_only_print = PStyle_receipt_1 | PStyle_receipt_2 | PStyle_receipt_3;
    public static int PStyle_receipt_all = PStyle_receipt_1 | PStyle_receipt_2 | PStyle_receipt_3 | PStyle_receipt_show;

    public static final String Font_default = "/system/fonts/DroidSans.ttf"; // not set will use the system default // DroidSansMono.ttf
//    public static final String Font_default =
//        "/system/fonts/NotoSansArmenian-Regular.ttf";
//         "/system/fonts/Roboto-Medium.ttf";
    public static final String Font_Bold = "/system/fonts/Roboto-Medium.ttf";
    public static final String Font_Heavy = "/system/fonts/DroidSans-Bold.ttf";

}

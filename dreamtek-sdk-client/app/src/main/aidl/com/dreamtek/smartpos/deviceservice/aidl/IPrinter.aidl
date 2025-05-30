package com.dreamtek.smartpos.deviceservice.aidl;

import com.dreamtek.smartpos.deviceservice.aidl.QrCodeContent;
import com.dreamtek.smartpos.deviceservice.aidl.PrinterListener;

/**
 * <p>The printer object to print strings, barcode, QR code, image
 */
interface IPrinter {    
    /**
     * <p> get printer status
     *
	 * @return the status:
	 * <ul>
	 * <li>ERROR_NONE(0x00) - normal</li>
	 * <li>ERROR_PAPERENDED(0xF0) - Paper out</li>
	 * <li>ERROR_NOCONTENT(0xF1) - no content</li>
     * <li>ERROR_HARDERR(0xF2) - printer error</li>
     * <li>ERROR_OVERHEAT(0xF3) - over heat</li>
     * <li>ERROR_NOBM(0xF6) - no black mark</li>
     * <li>ERROR_BUSY(0xF7) - printer is busy</li>
     * <li>ERROR_MOTORERR(0xFB) - moto error</li>
     * <li>ERROR_LOWVOL(0xE1) - battery low</li>
     * <li>ERROR_NOTTF(0xE2) - no ttf</li>
     * <li>ERROR_BITMAP_TOOWIDE(0xE3) - width of bitmap too wide</li>
     * </ul>
     * @
	 */
	int getStatus();
	
    /**
	 * <p> Set the gray level of printing
	 *
	 * @param gray the value from 0 to 7
	 * @
	 */
	void setGray(int gray);
	
	/**
	 * <p> Add text string to print
	 * @param format set text format, the key of format param as follow:
	 * <ul>
     *   <li>{@code int}font</li>
     *   <li>{@code String}fontStyle; sustom font type by passing /xxxx/abc.ttf, default is using /system/fonts/fzzdx.ttf if not set</li>
     *   <li>{@code boolean}bold; {code true}:bold, @{code false}:normal</li>
     *   <li>{@code int}align; {code 0}:left, @{code 1}:center, @{code 2}:right</li>
     *   <li>{@code boolean}newline; {code true}:new line after prin, @{code false}:normal</li>
     *   <li>{@code float}scale_w; (descrption: multiple Width)</li>
     *   <li>{@code float}scale_h; (descrption: multiple Height)</li>
     * </ul>
	 * <p> the key of font format as follow:
	 * <ul>
	 *   <li>0(description:small(size16))
     *   <li>1(description:normal(size24))
     *   <li>2(description:normal_bigger(size24 double height & bold))
     *   <li>3(description:large(size32))
     *   <li>4(description:large_bigger(size32 double height & bold))
     *   <li>5(description:huge(size48))
     *   <li>6(description:normal_wide(size24 double width & bold))
     *   <li>7(description:large_wide(size32 double width & bold)))
	 * </ul>
	 * <p><pre>{@code
     *      Bundle bundle = new Bundle;
     *      bundle.putBoolean("bold", true);
     *      bundle.putBoolean("newline", true);
     *      bundle.putInt("font", 1);
     *      bundle.putInt("align", 1);
     *      bundle.putFloat("scale_w", 1.5f);
     *      bundle.putFloat("scale_h", 1.5f);
     * }
     * </pre>
	 * @param text the text string want to print
	 * @
	 */
	void addText(in Bundle format, String text);

	/**
	 * <p> Add text strings to print.
	 *
	 * @param format set text format, the key of format param as follow:
	 * <ul>
	 *   <li>{@code int}fontSize</li>
	 *   <li>{@code boolean}bold; {code true}:bold, @{code false}:normal</li>
	 *   <li>{@code String}fontStyle</li>
	 * </ul>
	 * <p> the key of fontSize format as follow:
	 * <ul>
	 *   <li>0(description:small(size16))
	 *   <li>1(description:normal(size24))
	 *   <li>2(description:normal_bigger(size24 double height & bold))
	 *   <li>3(description:large(size32))
	 * </ul>
	 * <p> the key of fontStyle format as follow:
     * <ul>
	 *   <li>/xxxx/xx.ttf(absolute path, custom font by user)</li>
	 * </ul>
	 * <p><pre>{@code
	 *      Bundle bundle = new Bundle;
     *      bundle.putBoolean("bold", true);
     *      bundle.putInt("fontSize", 1);
	 * }
	 * </pre>
	 *
	 * @param lString the left justifying String
	 * @param mString the middle(center) justifying String
	 * @param rString the right justifying String
	 * @param mode -
	 {@code 0} : If there is a middle column, the width of the left, middle,
	 and right columns is flexibly assigned based on the middle column.
	 If there is no middle column, the width of the left & right columns is flexibly assigned.
	 {code 1} : the width of the left & right columns is flexibly assigned.
	 {code 2} : the logic same as code 0, but use new SDK print method, recommended
	 * @
	 */
	void addTextInLine(in Bundle format, String lString, String mString, String rString, int mode);
	
    /**
	 * <p> Add barcode to print(CodeType Code128)
	 * @param format set bar format, the key of format param as follow:
	 * <ul>
	 *   <li>align(description: {@code 0}:left, {@code 1}:center, {@code 2}:right)</li>
	 *   <li>height(description: the height of barcode)</li>
	 *   <li>barCodeType, refer to below:</li>
	 *    <pre>
	 *         public enum BarcodeFormat {
     *             AZTEC,(only support center alignment)
     *             CODABAR,
     *             CODE_39,
     *             CODE_93,
     *             CODE_128,
     *             DATA_MATRIX,
     *             EAN_8,
     *             EAN_13,
     *             ITF,
     *             MAXICODE(not support),
     *             PDF_417,
     *             QR_CODE(not support),
     *             RSS_14(not support),
     *             RSS_EXPANDED,
     *             UPC_A,
     *             UPC_E,
     *             UPC_EAN_EXTENSION(not support);
     *        }
	 *        Default: "BarcodeFormat.CODE_128.ordinal()"
	 *   </pre>
	 * </ul>
	 * @param barcode - the barcode string
	 * @
	 */
	void addBarCode(in Bundle format, in String barcode);

    /**
	 * <p> add a QR code to print
	 * @param format set qr code format, the key of format param as follow:
	 * <ul>
	 *   <li>{@code int}offset (description:the offset from the left, if size = 384 then QR code will be center alignment)</li>
	 *   <li>{@code int}expectedHeight (description:the expected height & width of the QR code. The actual size should multiple of the minimun pixel size of QR code)</li>
	 * </ul>
	 * @param qrCode the string of the QR code
	 * @
	 */
	void addQrCode(in Bundle format, String qrCode);

    /**
	 * <p> add multi-QR codes to print
	 *
	 * @param qrCodes {@link QrCodeContent}
	 * @
	 */
	void addQrCodesInLine(in List<QrCodeContent> qrCodes);

	
    /**
	 * <p> Feed the paper
	 *
	 * @param lines lines should > 1 && lines <= 50<BR>The lines should be the actual lines+1 because the current line need be counted.
	 * @
	 */
	void feedLine(int lines);
	
	/**
	 * <p> Start print
	 *
	 * @param listener {@link PrinterListener}the call back listener to tell the print result.
	 * @
	 */
	void startPrint(PrinterListener listener);

	/**
	 * <p> Start print remain the cache
	 *
	 * @param listener {@link PrinterListener}the call back listener to tell the print result.
     * @
     */
    void startSaveCachePrint(PrinterListener listener);

    /**
	 * <p> Set the line space
	 *
	 * @param space - the lines of space : 0~50
	 * @
	 */
    void setLineSpace(int space);

	/**
	 * <p> add some print in emv progress, <b>not end of emv process</b>
	 *
	 * @param listener the call back listener to tell the print result
	 * @
	 */
	void startPrintInEmv(PrinterListener listener);

    /**
	 * <p> clear printer cache
	 *
	 * @return {@code 1} : sucessed; {@code 0} : failed
	 * @
	 */
	int cleanCache();

    /**
	 * <p> Add an image to print, image size & effects should be adjuested before input
	 *
	 * @param format set bitmap format, the key of format param as follow:
	 * <ul>
	 *    <li>{@code int}offset (descriptionthe offset from left)</li>
     * </ul>
	 * @param imageData descriptionthe image buffer.
	 * @
	 */
	void addBmpImage(in Bundle format, in Bitmap image);

	/**
     * <p> Add screen capture to print
     * @param format set screen capture format, the key of format param as follow:
     * <ul>
     *    <li>{@code int}offset (descriptionthe offset from left)</li>
     *    <li>{@code int}width (descriptionthe width want to print.(MAX = 384))</li>
     *    <li>{@code int}height (descriptionthe height want to print)</li>
     * </ul>
     * @
     */
    void addScreenCapture(in Bundle format);
}

package com.bsm.ease2bill.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    // ✅ Generate UPI Payment QR Code (small, for receipts)
    public static String generateUpiPaymentQrBase64(String upiId, String payeeName, BigDecimal amount, int size)
            throws WriterException, IOException {
        
        // Format: upi://pay?pa=UPI_ID&pn=PayeeName&am=Amount&cu=INR&tn=Bill+Reference
        String upiUri = String.format(
            "upi://pay?pa=%s&pn=%s&am=%s&cu=INR&tn=BILL-%s",
            encodeUriComponent(upiId),
            encodeUriComponent(payeeName),
            amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(),
            "REF" // You can replace with actual invoice ID later
        );

        return generateQRCodeBase64(upiUri, size, size);
    }

    // ✅ Generic QR Code Generator (for any data)
    public static String generateQRCodeBase64(String qrData, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); // Minimal margin for small QR

        BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, width, height, hints);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    // Helper: URL-encode URI components
    private static String encodeUriComponent(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8)
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
    }
}
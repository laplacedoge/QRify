package com.laplacedoge.qrify;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    
    private static final int QR_SIZE = 128 * 5;

    Button btnGenerateQrCode;
    EditText etQrCodeEncodingContent;
    ImageView ivGeneratedQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGenerateQrCode = (Button)findViewById(R.id.btnGenerateQrCode);
        etQrCodeEncodingContent = (EditText)findViewById(R.id.etQrCodeEncodingContent);
        ivGeneratedQrCode = (ImageView)findViewById(R.id.ivGeneratedQrCode);

        btnGenerateQrCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String content = etQrCodeEncodingContent.getText().toString();
                Log.d("QR code content", content);
                generateQrCode(content);
            }
        });
    }

    public void generateQrCode(String content) {

        Log.d("QR code content", "asdadasd");
        Map<EncodeHintType, Object> hashMap;
        BitMatrix bitMatrix;
        int[] pixels;
        Bitmap bitmap;

        if(content.length() == 0) {
            return;
        }
        hashMap = new HashMap<EncodeHintType, Object>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hashMap.put(EncodeHintType.MARGIN, new Integer(1));
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE, hashMap);
        } catch (WriterException e) {
            return;
        }
        Log.d("Bitmap", String.format("Width: %d, height: %d", bitMatrix.getWidth(), bitMatrix.getHeight()));
        pixels = new int[QR_SIZE * QR_SIZE];
        for(int y = 0; y < QR_SIZE; y++) {
            for(int x = 0; x < QR_SIZE; x++) {
                pixels[QR_SIZE * y + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
            }
        }
        bitmap = Bitmap.createBitmap(QR_SIZE, QR_SIZE, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, QR_SIZE, 0, 0, QR_SIZE, QR_SIZE);
        ivGeneratedQrCode.setImageBitmap(bitmap);
    }
}






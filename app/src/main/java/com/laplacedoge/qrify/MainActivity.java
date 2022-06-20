package com.laplacedoge.qrify;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

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

    private static final int MIN_QR_CODE_SIZE = 100;
    private static final int MAX_QR_CODE_SIZE = 800;
    private static final int DFT_QR_CODE_SIZE = 128 * 5;

    private static final int MIN_QR_CODE_MARGIN_SIZE = 0;
    private static final int MAX_QR_CODE_MARGIN_SIZE = 4;
    private static final int DFT_QR_CODE_MARGIN_SIZE = 2;
    private static final int[] IDX_QR_CODE_MARGIN_SIZE = {0, 1, 2, 3, 4};

    private static final int MIN_QR_CODE_ERROR_CORRECTION_LEVEL = 0;
    private static final int MAX_QR_CODE_ERROR_CORRECTION_LEVEL = 3;
    private static final int DFT_QR_CODE_ERROR_CORRECTION_LEVEL = 3;
    private static final ErrorCorrectionLevel[] MAP_QR_CODE_ERROR_CORRECTION_LEVEL = {ErrorCorrectionLevel.L, ErrorCorrectionLevel.M, ErrorCorrectionLevel.Q, ErrorCorrectionLevel.H};

    SeekBar sbQrCodeSize;
    SeekBar sbQrCodeMarginSize;
    SeekBar sbQrCodeErrorCorrectionLevel;

    EditText etQrCodeEncodingContent;
    ImageView ivGeneratedQrCode;

    private int qrCodeSize;
    private int qrCodeMarginSize;
    private int qrCodeErrorCorrectionLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    public void setup() {
        qrCodeSize = DFT_QR_CODE_SIZE;
        qrCodeMarginSize = DFT_QR_CODE_MARGIN_SIZE;
        qrCodeErrorCorrectionLevel = DFT_QR_CODE_ERROR_CORRECTION_LEVEL;

        sbQrCodeSize = (SeekBar)findViewById(R.id.sbQrCodeSize);
        sbQrCodeMarginSize = (SeekBar)findViewById(R.id.sbQrCodeMarginSize);
        sbQrCodeErrorCorrectionLevel = (SeekBar)findViewById(R.id.sbQrCodeErrorCorrectionLevel);
        etQrCodeEncodingContent = (EditText)findViewById(R.id.etQrCodeEncodingContent);
        ivGeneratedQrCode = (ImageView)findViewById(R.id.ivGeneratedQrCode);

        sbQrCodeSize.setMax(MAX_QR_CODE_SIZE - MIN_QR_CODE_SIZE);
        sbQrCodeSize.setProgress(DFT_QR_CODE_SIZE - MIN_QR_CODE_SIZE);
        sbQrCodeSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                qrCodeSize = sbQrCodeSize.getProgress() + MIN_QR_CODE_SIZE;
                updateQrCode();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sbQrCodeMarginSize.setMax(MAX_QR_CODE_MARGIN_SIZE);
        sbQrCodeMarginSize.setProgress(DFT_QR_CODE_MARGIN_SIZE);
        sbQrCodeMarginSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                qrCodeMarginSize = sbQrCodeMarginSize.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateQrCode();
            }
        });

        sbQrCodeErrorCorrectionLevel.setMax(MAX_QR_CODE_ERROR_CORRECTION_LEVEL);
        sbQrCodeErrorCorrectionLevel.setProgress(DFT_QR_CODE_ERROR_CORRECTION_LEVEL);
        sbQrCodeErrorCorrectionLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                qrCodeErrorCorrectionLevel = sbQrCodeErrorCorrectionLevel.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateQrCode();
            }
        });

        etQrCodeEncodingContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                updateQrCode();
            }
        });

        updateQrCode();
    }

    public void updateQrCode() {
        String content = etQrCodeEncodingContent.getText().toString();
        if(content.length() > 0) {
            generateQrCode(content);
        }
    }

    public void generateQrCode(String content) {
        Map<EncodeHintType, Object> hashMap;
        BitMatrix bitMatrix;
        int[] pixels;
        Bitmap bitmap;

        hashMap = new HashMap<EncodeHintType, Object>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, MAP_QR_CODE_ERROR_CORRECTION_LEVEL[qrCodeErrorCorrectionLevel]);
        hashMap.put(EncodeHintType.MARGIN, qrCodeMarginSize);
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hashMap);
        } catch (WriterException e) {
            return;
        }
        pixels = new int[qrCodeSize * qrCodeSize];
        for(int y = 0; y < qrCodeSize; y++) {
            for(int x = 0; x < qrCodeSize; x++) {
                pixels[qrCodeSize * y + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
            }
        }
        bitmap = Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, qrCodeSize, 0, 0, qrCodeSize, qrCodeSize);
        ivGeneratedQrCode.setImageBitmap(bitmap);
    }
}






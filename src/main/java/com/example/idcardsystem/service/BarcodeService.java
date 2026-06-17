package com.example.idcardsystem.service;

import com.example.idcardsystem.model.Profile;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
public class BarcodeService {

    public String generateBarcode(Profile profile) throws Exception {
        String folderPath = "src/main/resources/static/barcodes/";
        new File(folderPath).mkdirs();

        String fileName = "barcode_" + profile.getId() + ".png";
        String filePath = folderPath + fileName;

        BitMatrix matrix = new MultiFormatWriter()
                .encode(profile.getRegistrationNumber(), BarcodeFormat.CODE_128, 300, 100);

        MatrixToImageWriter.writeToPath(matrix, "PNG", Path.of(filePath));

        return "/barcodes/" + fileName;
    }
}
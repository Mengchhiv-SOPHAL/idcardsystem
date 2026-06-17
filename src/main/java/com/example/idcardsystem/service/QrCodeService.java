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
public class QrCodeService {

    public String generateQrCode(Profile profile) throws Exception {
        String folderPath = System.getProperty("user.dir")
                + File.separator + "src/main/resources/static/qrcodes/";
        new File(folderPath).mkdirs();

        String data = "UUID: " + profile.getUuid()
                + "\nID: " + profile.getRegistrationNumber()
                + "\nName: " + profile.getFullName()
                + "\nType: " + profile.getType();

        String fileName = "qr_" + profile.getId() + ".png";
        String filePath = folderPath + fileName;

        BitMatrix matrix = new MultiFormatWriter()
                .encode(data, BarcodeFormat.QR_CODE, 200, 200);

        MatrixToImageWriter.writeToPath(matrix, "PNG", Path.of(filePath));

        return "/qrcodes/" + fileName;
    }
}
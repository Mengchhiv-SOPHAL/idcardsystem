package com.example.idcardsystem.service;

import com.example.idcardsystem.model.Profile;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class PdfService {

    public String generatePdf(Profile profile) {
        try {
            String folderPath = System.getProperty("user.dir")
                    + File.separator + "src/main/resources/static/pdfs/";
            new File(folderPath).mkdirs();

            String fileName = "id_card_" + profile.getId() + ".pdf";
            String filePath = folderPath + fileName;

            String html = """
                    <html>
                    <body style='font-family: Arial;'>
                        <div style='width:350px; border:3px solid #222; padding:20px; text-align:center;'>
                            <h2>ID CARD</h2>
                            <p><b>Name:</b> %s</p>
                            <p><b>ID:</b> %s</p>
                            <p><b>Department:</b> %s</p>
                            <p><b>Title:</b> %s</p>
                            <p><b>Type:</b> %s</p>
                        </div>
                    </body>
                    </html>
                    """.formatted(
                    profile.getFullName(),
                    profile.getRegistrationNumber(),
                    profile.getDepartment(),
                    profile.getTitle(),
                    profile.getType()
            );

            HtmlConverter.convertToPdf(html, new FileOutputStream(filePath));

            return "/pdfs/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
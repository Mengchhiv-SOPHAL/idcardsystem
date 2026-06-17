package com.example.idcardsystem.controller;

import com.example.idcardsystem.model.BarcodeType;
import com.example.idcardsystem.model.Profile;
import com.example.idcardsystem.model.ProfileType;
import com.example.idcardsystem.service.PdfService;
import com.example.idcardsystem.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final PdfService pdfService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("profiles", profileService.getAllProfiles());
        return "index";
    }

    @GetMapping("/profiles/new")
    public String newProfileForm(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("types", ProfileType.values());
        model.addAttribute("barcodeTypes", BarcodeType.values());
        return "profile-form";
    }

    @PostMapping("/profiles")
    public String createProfile(@ModelAttribute Profile profile,
                                @RequestParam("photo") MultipartFile photo) {
        profileService.createProfile(profile, photo);
        return "redirect:/";
    }
    @GetMapping("/profiles/batch")
    public String batchGenerate() {

        List<Profile> profiles = profileService.getAllProfiles();

        for(Profile profile : profiles){
            pdfService.generatePdf(profile);
        }

        return "redirect:/";
    }

    @GetMapping("/profiles/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        model.addAttribute("profile", profileService.getProfile(id));
        return "profile-detail";
    }

    @GetMapping("/profiles/edit/{id}")
    public String editProfile(@PathVariable Long id, Model model) {
        model.addAttribute("profile", profileService.getProfile(id));
        model.addAttribute("types", ProfileType.values());
        model.addAttribute("barcodeTypes", BarcodeType.values());
        return "profile-form";
    }

    @PostMapping("/profiles/update/{id}")
    public String updateProfile(@PathVariable Long id,
                                @ModelAttribute Profile profile) {
        profileService.updateProfile(id, profile);
        return "redirect:/";
    }

    @GetMapping("/profiles/delete/{id}")
    public String deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return "redirect:/";
    }

    @GetMapping("/profiles/pdf/{id}")
    public String exportPdf(@PathVariable Long id) {
        Profile profile = profileService.getProfile(id);
        pdfService.generatePdf(profile);
        return "redirect:/profiles/" + id;
    }
    @GetMapping("/profiles/qr/{id}")
public void qrCode(@PathVariable Long id, HttpServletResponse response) throws Exception {
    Profile profile = profileService.getProfile(id);

    String data = "UUID: " + profile.getUuid()
            + "\nID: " + profile.getRegistrationNumber()
            + "\nName: " + profile.getFullName()
            + "\nType: " + profile.getType();

    BitMatrix matrix = new MultiFormatWriter()
            .encode(data, BarcodeFormat.QR_CODE, 200, 200);

    response.setContentType("image/png");
    MatrixToImageWriter.writeToStream(matrix, "PNG", response.getOutputStream());
}

    @GetMapping("/profiles/barcode/{id}")
    public void barcode(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Profile profile = profileService.getProfile(id);

        BitMatrix matrix = new MultiFormatWriter()
                .encode(profile.getRegistrationNumber(), BarcodeFormat.CODE_128, 300, 100);

        response.setContentType("image/png");
        MatrixToImageWriter.writeToStream(matrix, "PNG", response.getOutputStream());
    }

    @GetMapping("/api/profiles")
    @ResponseBody
    public List<Profile> apiProfiles() {
        return profileService.getAllProfiles();
    }

    @PostMapping("/api/profiles")
    @ResponseBody
    public Profile apiCreateProfile(@RequestBody Profile profile) {
        return profileService.createProfile(profile);
    }
}
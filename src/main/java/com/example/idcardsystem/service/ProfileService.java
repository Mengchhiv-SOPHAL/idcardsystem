package com.example.idcardsystem.service;

import com.example.idcardsystem.model.BarcodeType;
import com.example.idcardsystem.model.Profile;
import com.example.idcardsystem.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public Profile createProfile(Profile profile, MultipartFile photo) {
        try {
            profile.setUuid(UUID.randomUUID().toString());
            profile.setRegistrationNumber(generateRegistrationNumber());

            if (profile.getBarcodeType() == null) {
                profile.setBarcodeType(BarcodeType.CODE_128);
            }

            if (photo != null && !photo.isEmpty()) {
                savePhoto(profile, photo);
            }

            return profileRepository.save(profile);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create profile", e);
        }
    }

    public Profile createProfile(Profile profile) {
        return createProfile(profile, null);
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Profile getProfile(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public Profile updateProfile(Long id, Profile updatedProfile) {
        Profile profile = getProfile(id);

        profile.setFullName(updatedProfile.getFullName());
        profile.setDepartment(updatedProfile.getDepartment());
        profile.setTitle(updatedProfile.getTitle());
        profile.setEmail(updatedProfile.getEmail());
        profile.setPhone(updatedProfile.getPhone());
        profile.setBloodGroup(updatedProfile.getBloodGroup());
        profile.setDateOfBirth(updatedProfile.getDateOfBirth());
        profile.setExpiryDate(updatedProfile.getExpiryDate());
        profile.setType(updatedProfile.getType());
        profile.setBarcodeType(updatedProfile.getBarcodeType());

        return profileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    private String generateRegistrationNumber() {
        return Year.now() + "-ID-" + System.currentTimeMillis();
    }

    private void savePhoto(Profile profile, MultipartFile photo) throws Exception {
        String contentType = photo.getContentType();
        String originalName = photo.getOriginalFilename();

        if (contentType == null || originalName == null) {
            throw new RuntimeException("Invalid image file");
        }

        String lowerName = originalName.toLowerCase();

        boolean isValidImage =
                contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                lowerName.endsWith(".jpg") ||
                lowerName.endsWith(".jpeg") ||
                lowerName.endsWith(".png");

        if (!isValidImage) {
            throw new RuntimeException("Only JPG and PNG images are allowed");
        }

        String folderPath = System.getProperty("user.dir")
                + File.separator + "src"
                + File.separator + "main"
                + File.separator + "resources"
                + File.separator + "static"
                + File.separator + "uploads"
                + File.separator;

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String safeFileName = System.currentTimeMillis() + "_" + originalName.replaceAll("\\s+", "_");
        String filePath = folderPath + safeFileName;

        photo.transferTo(new File(filePath));

        profile.setPhotoFileName(safeFileName);
        profile.setPhotoContentType(contentType);
    }

    public String getPhotoUrl(Profile profile) {
        if (profile.getPhotoFileName() == null || profile.getPhotoFileName().isBlank()) {
            return null;
        }
        return "/uploads/" + profile.getPhotoFileName();
    }
}
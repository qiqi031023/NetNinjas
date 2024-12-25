package com.tvpss.controller;

import com.tvpss.model.Achievement;
import com.tvpss.model.Certificate;
import com.tvpss.service.AchievementService;
import com.tvpss.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

import java.util.List;

@Controller
@RequestMapping("/adminstate")
public class StateAdminController {

    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private AchievementService achievementService;
    
    @GetMapping("/dashboard")
    public String showAdminStateDashboard(Model model) {

        return "adminstate/dashboard";
    }

    @GetMapping("/viewCertApplication")
    public String generateCertificate(Model model) {
    	List<Certificate> certificates = certificateService.getAllCertificates();
        model.addAttribute("certificates", certificates);
        model.addAttribute("page", "viewCertApplication");

        return "adminstate/viewCertApplication";
    }
    
    @GetMapping("/generateCertificate")
    public String generateCertificate(@RequestParam("certificateId") String certificateId, Model model) {
        try {
	    	Certificate certificate = certificateService.getCertificateById(certificateId);
	        Achievement achievement = certificateService.getAchievementByCertificateId(certificateId);
	
	        model.addAttribute("certificate", certificate);
	        model.addAttribute("achievement", achievement);
	
	        return "adminstate/generateCertificate";
	    } 
	    	catch (RuntimeException e) {
	        model.addAttribute("error", "Data not found");
	        return "errorPage"; 
	    }
    }
    
    @PostMapping("/generateCertificate")
    public String generateCertificate(
    	@RequestParam(value = "achievementId", required = false) String achievementId,
        @RequestParam(value = "icNumber", required = false) String icNumber,
        @RequestParam(value = "fullName", required = false) String fullName,
        @RequestParam(value = "activityName", required = false) String activityName,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "subCategory", required = false) String subCategory,
        @RequestParam(value = "awardInfo", required = false) String awardInfo,
        @RequestParam(value = "template", required = false) String template,
        @RequestParam(value = "upload-sign", required = false) MultipartFile signature,
        Model model) {
        
        try {
            // Retrieve achievement details if IC number is provided
            Achievement achievement = null;
            if (achievementId != null && !achievementId.isEmpty()) {
                achievement = achievementService.getAchievementByAchievementId(achievementId);
            }

            // If no achievement found, create a temporary one with provided details
            if (achievement == null) {
                achievement = new Achievement();
                achievement.setIcNumber(achievementId);
                achievement.setIcNumber(icNumber);
                achievement.setFullName(fullName);
                achievement.setActivityName(activityName);
                achievement.setCategory(category);
                achievement.setSubCategory(subCategory);
                achievement.setAwardInfo(awardInfo);
            }

            // Add attributes to the model
            model.addAttribute("achievement", achievement);
            model.addAttribute("template", template);
            
            // Handle signature upload if provided
            if (signature != null && !signature.isEmpty()) {
                // Save signature or process it as needed
                String uploadDir = "src/main/webapp/resources/css/signatures/";
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                
                String fileName = System.currentTimeMillis() + "_" + signature.getOriginalFilename();
                File destinationFile = new File(uploadDir + fileName);
                signature.transferTo(destinationFile);
                
                model.addAttribute("signaturePath", "/resources/css/signatures/" + fileName);
            }

            // Return to the same page to display the generated certificate
            return "adminstate/generateCertificate";

        } catch (IOException e) {
            // Log the error
            e.printStackTrace();
            model.addAttribute("error", "Failed to process certificate generation: " + e.getMessage());
            return "adminstate/generateCertificate";
        }
    }
}
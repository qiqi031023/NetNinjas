package com.tvpss.controller;

import com.tvpss.model.School;
import com.tvpss.model.SchoolVersion;
import com.tvpss.service.SchoolService;
import com.tvpss.service.SchoolVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/adminppd")
public class AdminPPDController {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private SchoolVersionService schoolVersionService;

    /**
     * Dashboard for Admin PPD.
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "adminppd/dashboard";
    }

    /**
     * Display School TVPSS Validation list.
     */
    @GetMapping("/schoolValidation")
    public String schoolValidation(Model model) {
        // Retrieve all school versions
        List<SchoolVersion> schoolVersions = schoolVersionService.getAllSchoolVersions();

        // Add school versions to the model
        model.addAttribute("schoolVersions", schoolVersions);

        return "adminppd/schoolValidation"; // Maps to schoolValidation.jsp
    }

    /**
     * Display details of a specific school.
     *
     * @param schoolCode The school code to look up details.
     * @param model      Model to pass data to the view.
     * @return School details page.
     */
    @GetMapping("/schoolDetails")
    public String schoolDetails(@RequestParam("schoolCode") String schoolCode, Model model) {
        // Fetch school details based on schoolCode
        School school = schoolService.getSchoolBySchoolCode(schoolCode);

        // Check if school exists
        if (school == null) {
            model.addAttribute("errorMessage", "School details not found for code: " + schoolCode);
            return "redirect:/adminppd/schoolValidation";
        }

        // Add school details to the model
        model.addAttribute("school", school);

        return "adminppd/schoolDetails"; // View for schoolDetails.jsp
    }



    /**
     * Update the details of a school.
     *
     * @param schoolCode          The school code to update.
     * @param tvpssLogo           Whether the school has uploaded a TVPSS logo.
     * @param studio              Whether the school has a TV studio.
     * @param youtubeUpload       Whether the school uploads to YouTube.
     * @param recordingInSchool   Whether the school supports in-school recording.
     * @param recordingInOutSchool Whether the school supports in and out of school recording.
     * @param redirectAttributes  Attributes for flash messages.
     * @return Redirect to school details page with updates.
     */
    @PostMapping("/updateSchoolDetails")
    public String updateSchoolDetails(
        @RequestParam String schoolCode,
        @RequestParam(required = false) String tvpssLogo,
        @RequestParam(required = false) String studio,
        @RequestParam(required = false) String youtubeUpload,
        @RequestParam(required = false) String recordingInSchool,
        @RequestParam(required = false) String recordingInOutSchool,
        RedirectAttributes redirectAttributes
    ) {
        // Fetch the school object
        School school = schoolService.getSchoolBySchoolCode(schoolCode);

        // Handle case where school is not found
        if (school == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "School not found for code: " + schoolCode);
            return "redirect:/adminppd/schoolValidation";
        }

        // Update school properties based on user input	
        school.setLogoFilename(tvpssLogo != null && tvpssLogo.equalsIgnoreCase("true") ? "Uploaded" : "Not Uploaded");
        school.setStudio(studio != null && studio.equalsIgnoreCase("true") ? "Yes" : "No");
        school.setYoutubeUpload(youtubeUpload != null && youtubeUpload.equalsIgnoreCase("true") ? "Yes" : "No");
        school.setRecordingInSchool(recordingInSchool != null && recordingInSchool.equalsIgnoreCase("true") ? "Yes" : "No");
        school.setRecordingInOutSchool(recordingInOutSchool != null && recordingInOutSchool.equalsIgnoreCase("true") ? "Yes" : "No");

        // Save the updated school details
        schoolService.saveSchool(school);

        // Add success message
        redirectAttributes.addFlashAttribute("successMessage", "School details updated successfully!");

        // Redirect back to school details
        return "redirect:/adminppd/schoolDetails?schoolCode=" + schoolCode;
    }
    /**
     * Redirect to updated school details after update.
     *
     * @param updatedSchool The updated school details.
     * @param model         Model to pass data to the view.
     * @return Updated school details page.
     */
    @GetMapping("/updatedSchoolDetails")
    public String updatedSchoolDetails(@ModelAttribute("updatedSchool") School updatedSchool, Model model) {
        if (updatedSchool == null) {
            model.addAttribute("errorMessage", "No updated school information available.");
            return "redirect:/adminppd/schoolValidation";
        }
        model.addAttribute("updatedSchool", updatedSchool);
        return "adminppd/schoolDetails";
    }
}

package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.model.SiteSetting;
import com.myjourneyblog.MyJourneyBlog.repository.SiteSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final SiteSettingRepository siteSettingRepository;

    @PostMapping("/update-about")
    public String updateAboutSection(@RequestParam("content") String content) {
        SiteSetting setting = SiteSetting.builder()
                .key("home_about_content")
                .value(content)
                .build();

        siteSettingRepository.save(setting);

        // Redirect back to home with a success flag
        return "redirect:/?success=true";
    }
}
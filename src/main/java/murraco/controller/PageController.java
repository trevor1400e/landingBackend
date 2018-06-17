package murraco.controller;

import murraco.dto.EmailDataDTO;
import murraco.model.Email;
import murraco.model.Theme;
import murraco.repository.EmailRepository;
import murraco.repository.ThemeRepository;
import murraco.service.ThemeService;
import murraco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private EmailRepository emailRepository;

    @GetMapping(value = "/{unique}")
    public String pagedata(@PathVariable String unique) {
        Theme aTheme = themeService.findByUniquename(unique);

//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    String name = auth.getName();
//    System.out.println(name);

        aTheme.addView(1);
        themeRepository.save(aTheme);

        return aTheme.getData();
    }

    @PostMapping(value = "/email")
    public String saveEmail(@RequestBody EmailDataDTO emailDataDTO) {
        Theme billNye = themeService.findByUniquename(emailDataDTO.getUniquename());

        if (billNye != null) {
            Email emailTemp = new Email();
            emailTemp.setUniquename(emailDataDTO.getUniquename());
            emailTemp.setEmail(emailDataDTO.getEmail());
            emailTemp.setUsername(billNye.getUsername());

            billNye.addEmail(1);
            themeRepository.save(billNye);
            emailRepository.save(emailTemp);
        } else {
            return "No template page found";
        }
//
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    String name = auth.getName();
//    System.out.println(name);

        return "Was a success yo";
    }

}

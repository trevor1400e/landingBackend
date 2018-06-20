package murraco.controller;

import murraco.dto.EmailDataDTO;
import murraco.model.CollectedEmail;
import murraco.model.Page;
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
        Page page = themeService.findByUniquename(unique);

//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    String name = auth.getName();
//    System.out.println(name);

        page.addView(1);
        themeRepository.save(page);

        return page.getData();
    }

    @PostMapping(value = "/email")
    public String saveEmail(@RequestBody EmailDataDTO emailDataDTO) {
        Page theme = themeService.findByUniquename(emailDataDTO.getUniquename());

        if (theme != null) {
            CollectedEmail emailTemp = new CollectedEmail();
            emailTemp.setPageName(emailDataDTO.getUniquename());
            emailTemp.setEmail(emailDataDTO.getEmail());
            emailTemp.setUsername(theme.getUsername());

            theme.addEmail(1);
            themeRepository.save(theme);
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

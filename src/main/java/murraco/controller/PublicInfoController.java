package murraco.controller;

import murraco.exception.CustomException;
import murraco.model.Page;
import murraco.model.PageView;
import murraco.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

@RestController
@RequestMapping("/public")
public class PublicInfoController {

    @Autowired
    private PageRepository pageRepository;

    @GetMapping("pageData/{pageName}")
    public ResponseEntity getPageData(@PathVariable String pageName, HttpServletRequest req) {
        Page page = pageRepository.findByName(pageName)
                .orElseThrow(() -> new CustomException(
                        "No page found with name " + pageName,
                        HttpStatus.NOT_FOUND
                ));

        // Extract client IP Address from request
        String ipAddress = req.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null)
            ipAddress = req.getRemoteAddr();

        // Create page view to record IP and timestamp
        PageView pageView = new PageView();
        pageView.setIpAddress(ipAddress);

        // Persist the page view
        page.addView(pageView);
        pageRepository.save(page);

        return ResponseEntity.ok(page.getData());
    }

}

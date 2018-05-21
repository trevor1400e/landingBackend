package murraco.controller;

import io.swagger.annotations.*;
import murraco.dto.EmailDataDTO;
import murraco.dto.UserDataDTO;
import murraco.dto.UserLogin;
import murraco.dto.UserResponseDTO;
import murraco.model.Email;
import murraco.model.Theme;
import murraco.model.User;
import murraco.repository.EmailRepository;
import murraco.repository.ThemeRepository;
import murraco.service.ThemeService;
import murraco.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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

  @Autowired
  private ModelMapper modelMapper;

  @GetMapping(value = "/{unique}")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
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
  @ApiOperation(value = "${UserController.email}")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong")})
  public String saveEmail(@ApiParam("Email User") @RequestBody EmailDataDTO emailDataDTO) {

    Theme billNye = themeService.findByUniquename(emailDataDTO.getUniquename());

    if(billNye != null){
      Email emailTemp = new Email();
      emailTemp.setUniquename(emailDataDTO.getUniquename());
      emailTemp.setEmail(emailDataDTO.getEmail());
      emailTemp.setUsername(billNye.getUsername());

      billNye.addEmail(1);
      themeRepository.save(billNye);
      emailRepository.save(emailTemp);
    }else {
      return "No template page found";
    }
//
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    String name = auth.getName();
//    System.out.println(name);

    return "Was a success yo";
  }

}

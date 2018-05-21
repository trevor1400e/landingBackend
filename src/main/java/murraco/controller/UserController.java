package murraco.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import murraco.dto.*;
import murraco.model.Email;
import murraco.model.Role;
import murraco.model.Theme;
import murraco.repository.EmailRepository;
import murraco.repository.ThemeRepository;
import murraco.repository.UserRepository;
import murraco.service.ThemeService;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import murraco.model.User;
import murraco.service.UserService;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ThemeRepository themeRepository;

  @Autowired
  private ThemeService themeService;

  @Autowired
  private EmailRepository emailRepository;

  @Autowired
  private ModelMapper modelMapper;

  @PostMapping("/signin")
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @ApiOperation(value = "${UserController.signin}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 422, message = "Invalid username/password supplied")})
  public String login(@ApiParam("Signin User") @RequestBody UserLogin user) {
    return userService.signin(user.getUsername(), user.getPassword());
  }

  @PostMapping("/signup")
  @ApiOperation(value = "${UserController.signup}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 422, message = "Username is already in use"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
//    public String signup(@ApiParam("Username") @RequestParam String username, //
//            @ApiParam("Password") @RequestParam String password,
//            @ApiParam("Email") @RequestParam String email) {
//      User newUser = new User();
//      newUser.setUsername(username);
//      newUser.setPassword(password);
//      newUser.setEmail(email);
//      newUser.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_CLIENT)));
    return userService.signup(modelMapper.map(user, User.class));
  }

  @DeleteMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.delete}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("Username") @PathVariable String username) {
    userService.delete(username);
    return username;
  }

  @GetMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.search}", response = UserResponseDTO.class)
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
    return modelMapper.map(userService.search(username), UserResponseDTO.class);
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
  @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class)
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
  }

  @GetMapping(value = "/hello")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')  or hasRole('ROLE_PREMIUM')")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String helloworld() {
    return "hello bitch";
  }

  @PostMapping(value = "/theme")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
  @ApiOperation(value = "${UserController.theme}")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong")})
  public boolean saveTheme(@ApiParam("Save Theme") @RequestBody ThemeDataDTO themeDataDTO) {

    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String name = auth.getName();

      Theme tempTheme = new Theme();
      tempTheme.setThemename(themeDataDTO.getThemename());
      tempTheme.setData(themeDataDTO.getData());
      tempTheme.setUniquename(themeDataDTO.getUniquename());
      tempTheme.setUsername(name);
      tempTheme.setEmailcount(0);
      tempTheme.setPageviews(0);

      themeRepository.save(tempTheme);
      return true;
    }catch (Exception e){
        return false;
    }
  }

  @GetMapping(value = "/table")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public List tableData() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String name = auth.getName();

    List<Theme> userThemes = themeService.findByUsername(name);
    List items = new ArrayList();

    for(Theme t : userThemes){
     String omfg = "{\"value\": false, \"name\": \""+t.getUniquename()+"\", \"URL\": \"/"+t.getThemename()+"/"+t.getUniquename()+"\", \"conversions\": \"disabled\", \"impressions\": "+t.getPageviews()+", \"emails\": "+t.getEmailcount()+"}";
      items.add(omfg);
    }

    return items;
  }

  @PostMapping(value = "/upgrade")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
  @ApiOperation(value = "${UserController.upgrade}")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"),
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String upgrade(@ApiParam("User Token") @RequestBody ChargeDataDTO chargeDataDTO) {

    Stripe.apiKey = "sk_test_XqjOE25ia1m5Kp4FRWZ78GR2";

    Map<String, Object> chargeMap = new HashMap<String, Object>();
    chargeMap.put("amount", 1995);
    chargeMap.put("currency", "usd");
    chargeMap.put("source", chargeDataDTO.getChargetoken()); // obtained via Stripe.js

    try {
      Charge charge = Charge.create(chargeMap);
    } catch (StripeException e) {
      e.printStackTrace();
      return "Failed damn";
    }
//TODO: grab user on successfuList emails;l request, set role to PREMIUM and set exp date.
//
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String name = auth.getName();
    User tempUser = userRepository.findByUsername(name);
    tempUser.setPremexp(LocalDate.now().plusMonths(1).toString());
    tempUser.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_PREMIUM)));
    userRepository.save(tempUser);

    return "Was a success";
  }

  @ResponseBody
  @RequestMapping(value ="/txt/{unique}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
  @ApiOperation(value = "${UserController.download}")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"),
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String txtEmail(HttpServletResponse response, @PathVariable String unique){

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String name = auth.getName();
    Theme tempTheme = themeRepository.findByUniquename(unique);
    List<Email> emails;
    if(tempTheme.getUsername().equals(name)){
      emails = emailRepository.findByUniquename(unique);
    }else {
      return "Not your template.";
    }

    String fileName = unique+".txt";
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    String content = "Emails:";
    for(Email s : emails){
      content += "\n"+s.getEmail();
    }
    return content;
  }



}

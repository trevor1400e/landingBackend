package murraco.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import murraco.dto.*;
import murraco.model.Email;
import murraco.model.Role;
import murraco.model.Theme;
import murraco.model.User;
import murraco.repository.EmailRepository;
import murraco.repository.ThemeRepository;
import murraco.repository.UserRepository;
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
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/users")
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
    public String login(@RequestBody UserLogin user) {
        return userService.signin(user.getUsername(), user.getPassword());
    }

    @PostMapping("/signup")
    public String signup(@RequestBody UserDataDTO user) {
        return userService.signup(modelMapper.map(user, User.class));
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable String username) {
        userService.delete(username);
        return username;
    }

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDTO search(@PathVariable String username) {
        return modelMapper.map(userService.search(username), UserResponseDTO.class);
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
    public UserResponseDTO whoami(HttpServletRequest req) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        User userToCheck = userRepository.findByUsername(name);

        if (userToCheck.getCustomerid() != null) {
            try {
                Stripe.apiKey = "sk_test_XqjOE25ia1m5Kp4FRWZ78GR2";

                Customer checkStatus = Customer.retrieve(userToCheck.getCustomerid());
                Subscription checkSubscription = Subscription.retrieve(checkStatus.getSubscriptions().getData().get(0).getId());

                userToCheck.setPremiumstatus(checkSubscription.getStatus());

                userRepository.save(userToCheck);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            userToCheck.setPremiumstatus("unpaid");
            userRepository.save(userToCheck);
        }

        return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    }

    @GetMapping(value = "/hello")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')  or hasRole('ROLE_PREMIUM')")
    public String helloworld() {
        return "hello bitch";
    }

    @PostMapping(value = "/theme")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
    public boolean saveTheme(@RequestBody ThemeDataDTO themeDataDTO) {

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
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping(value = "/table")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
    public List tableData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        List<Theme> userThemes = themeService.findByUsername(name);
        List items = new ArrayList();

        for (Theme t : userThemes) {
            String omfg = "";
            if (t.getEmailcount() != 0 && t.getPageviews() != 0) {
                double roundOff = ((double) t.getEmailcount()) / ((double) t.getPageviews()) * 100f;
                DecimalFormat df = new DecimalFormat("###.#");
                omfg = "{\"value\": false, \"name\": \"" + t.getUniquename() + "\", \"URL\": \"/" + t.getThemename() + "/" + t.getUniquename() + "\", \"conversions\": \"" + df.format(roundOff) + "%\", \"impressions\": " + t.getPageviews() + ", \"emails\": " + t.getEmailcount() + "}";
            } else {
                omfg = "{\"value\": false, \"name\": \"" + t.getUniquename() + "\", \"URL\": \"/" + t.getThemename() + "/" + t.getUniquename() + "\", \"conversions\": \"0%\", \"impressions\": " + t.getPageviews() + ", \"emails\": " + t.getEmailcount() + "}";
            }
            items.add(omfg);
        }

        return items;
    }

    @PostMapping(value = "/upgrade")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
    public String upgrade(@RequestBody ChargeDataDTO chargeDataDTO) {

        Stripe.apiKey = "sk_test_XqjOE25ia1m5Kp4FRWZ78GR2";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User tempUser = userRepository.findByUsername(name);

        if (tempUser.getCustomerid() == null) {

            Map<String, Object> customerParams = new HashMap<String, Object>();

            customerParams.put("description", "Customer for LeadLucky");
            customerParams.put("source", chargeDataDTO.getChargetoken()); // obtained via Stripe.js

            try {
                Customer newCustomer = Customer.create(customerParams);

                Map<String, Object> item = new HashMap<String, Object>();
                item.put("plan", "plan_Cvo76gkcgiAIMh");

                Map<String, Object> items = new HashMap<String, Object>();
                items.put("0", item);

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("customer", newCustomer.getId());
                params.put("items", items);

                Subscription theSub = Subscription.create(params);
                tempUser.setCustomerid(newCustomer.getId());
                tempUser.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_PREMIUM)));
                tempUser.setPremiumstatus(theSub.getStatus());
            } catch (StripeException e) {
                e.printStackTrace();
                return "Failed, bad token?";
            }
        } else {
            //Add update customer here
            return "You're already premium?";
        }
        userRepository.save(tempUser);

        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/txt/{unique}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT') or hasRole('ROLE_PREMIUM')")
    public String txtEmail(HttpServletResponse response, @PathVariable String unique) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Theme tempTheme = themeRepository.findByUniquename(unique);
        List<Email> emails;
        if (tempTheme.getUsername().equals(name)) {
            emails = emailRepository.findByUniquename(unique);
        } else {
            return "Not your template.";
        }

        String fileName = unique + ".txt";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        String content = "Emails:";
        for (Email s : emails) {
            content += "\n" + s.getEmail();
        }
        return content;
    }


}

package ro.siit.tripscatalog.controller;

/**
 * Controller that manages the users profiles actions: create, edit, delete and save.
 * See method description for details.
 *
 * @author Radu Popescu
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ro.siit.tripscatalog.model.Trip;
import ro.siit.tripscatalog.model.User;
import ro.siit.tripscatalog.persistence.TripRepository;
import ro.siit.tripscatalog.persistence.UserRepository;
import ro.siit.tripscatalog.service.FileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private BCryptPasswordEncoder pwdEncoder;
    @Autowired
    private FileService fileService;

    /**
     * createProfile mapping provides an empty user object (with default values) to the web browser.
     *
     * @author Radu Popescu
     */

    @RequestMapping(value = "/user/createProfile", method = RequestMethod.GET)
    public ModelAndView createProfile() {
        ModelAndView model = new ModelAndView("userForm");
        model.addObject("user", new User());
        return model;
    }

    /**
     * editProfile mapping provides the logged user details to the web browser.
     *
     * @author Radu Popescu
     */

    @RequestMapping(value = "/user/editProfile", method = RequestMethod.GET)
    public ModelAndView editProfile() {
        ModelAndView model = new ModelAndView("editUserForm");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUserName);

        model.addObject("user", currentUser);
        return model;
    }

    /**
     * deleteProfile mapping deletes the selected user trips and files.
     * Deletes the user, resets the session and it redirects to login page.
     *
     * @author Radu Popescu
     */

    @RequestMapping(value = "/user/deleteProfile", method = RequestMethod.GET)
    public ModelAndView deleteProfile(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView model = new ModelAndView("/login");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUserName);

        List<Trip> tripList = tripRepository.findByUser(currentUser);
        tripList.forEach(trip -> {
            fileService.deleteResource(trip.getPhoto1_path());
            fileService.deleteResource(trip.getPhoto2_path());
            tripRepository.delete(trip);
        });

        userRepository.delete(currentUser);

        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return model;
    }

    /**
     * saveProfile mapping, in addition to standard object validation (done with annotation), performs a custom validation.
     * Uses regular expression for phone
     * Uses custom password validation (see description for ValidPassword interface and PasswordMatchesValidator)
     * Saves the edited user and it redirects to main page.
     *
     * @author Radu Popescu
     */

    @RequestMapping(value = "/user/saveProfile", method = RequestMethod.POST)
    public ModelAndView saveProfile(@Valid @ModelAttribute User user, BindingResult bindingResult) {

        if (!user.getPassword().equals(user.getConfirm_password())) {
            bindingResult.rejectValue("password", "error.user", "Passwords don't match");
        }

        String regex = "\\d+";
        if (!user.getPhone().startsWith("0") || !user.getPhone().matches(regex)) {
            bindingResult.rejectValue("phone", "error.user", "Phone number must start with 0 and contain only numbers");
        }

        if (bindingResult.hasErrors()) {
            ModelAndView errorModel = new ModelAndView("editUserForm");
            return errorModel;
        }

        ModelAndView model = new ModelAndView("home");

        user.setPassword(pwdEncoder.encode(user.getPassword()));
        userRepository.save(user);

        model.addObject("trips", tripRepository.findByUser(user));

        Trip tripObj = new Trip();

        model.addObject("photo1", tripObj.getPhoto1_path());
        model.addObject("photo2", tripObj.getPhoto2_path());

        model.addObject("tripObj", tripObj);

        return model;
    }

    /**
     * saveNewProfile mapping, in addition to standard object validation (done with annotation), performs a custom validation.
     * Uses regular expression for phone
     * Uses custom password validation (see description for ValidPassword interface and PasswordMatchesValidator)
     * Saves the new user and it redirects to login page.
     *
     * @author Radu Popescu
     */

    @RequestMapping(value = "/user/saveNewProfile", method = RequestMethod.POST)
    public ModelAndView saveNewProfile(@Valid @ModelAttribute User user, BindingResult bindingResult) {

        if (!user.getPassword().equals(user.getConfirm_password())) {
            bindingResult.rejectValue("password", "error.user", "Passwords don't match");
        }

        User userTemp = userRepository.findByUsername(user.getUsername());
        if (userTemp != null) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
        }

        String regex = "\\d+";
        if (!user.getPhone().startsWith("0") || !user.getPhone().matches(regex)) {
            bindingResult.rejectValue("phone", "error.user", "Phone number must start with 0 and contain only numbers");
        }

        if (bindingResult.hasErrors()) {
            ModelAndView errorModel = new ModelAndView("userForm");
            return errorModel;
        }

        ModelAndView model = new ModelAndView("login");

        user.setPassword(pwdEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return model;
    }
}
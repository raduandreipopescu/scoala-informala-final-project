package ro.siit.tripscatalog.controller;

/**
 * Controller that displays the main page of the application.
 * Method viewTrips loads the trips collection for the logged user and displays the selected trip details.
 *
 * @author Radu Popescu
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ro.siit.tripscatalog.model.Trip;
import ro.siit.tripscatalog.model.User;
import ro.siit.tripscatalog.persistence.TripRepository;
import ro.siit.tripscatalog.persistence.UserRepository;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView viewTrips(@RequestParam(name = "id", required = false) Long id) {
        ModelAndView model = new ModelAndView("home");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUserName);

        model.addObject("trips", tripRepository.findByUser(currentUser));

        Trip tripObj = new Trip();

        if (id != null) {
            Optional<Trip> tripOptional = tripRepository.findById(id);
            if (tripOptional.isPresent()) {
                tripObj = tripOptional.get();
            }
        }

        model.addObject("photo1", tripObj.getPhoto1_path());
        model.addObject("photo2", tripObj.getPhoto2_path());

        model.addObject("tripObj", tripObj);

        return model;
    }


}

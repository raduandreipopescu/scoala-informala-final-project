package ro.siit.tripscatalog.controller;

/**
 * Controller that manages the trips actions: create, edit, delete and save.
 * Create, edit, delete use the same mapping and selection is made by the "action" attribute.
 * See method description for details.
 *
 * @author Radu Popescu
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ro.siit.tripscatalog.model.Trip;
import ro.siit.tripscatalog.model.User;
import ro.siit.tripscatalog.persistence.TripRepository;
import ro.siit.tripscatalog.persistence.UserRepository;
import ro.siit.tripscatalog.service.FileService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class TripController {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileController fileController;

    /**
     * createTrip mapping provides an empty trip object (with default values) to the web browser.
     *
     * @author Radu Popescu
     */

    @RequestMapping(value = "/home", method = RequestMethod.GET, params = "action=createTrip")
    public ModelAndView tripForm() {
        ModelAndView model = new ModelAndView("tripForm");
        Trip trip = new Trip();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUserName);
        if (currentUser != null) {
            trip.setUser(currentUser);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        trip.setDate_from(LocalDate.parse(LocalDate.now().format(formatter)));
        trip.setDate_to(LocalDate.parse(LocalDate.now().format(formatter)));

        model.addObject("trip", trip);
        return model;
    }

    /**
     * editTrip mapping provides the selected trip object to the web browser.
     *
     * @author Radu Popescu
     */

    @RequestMapping(value = "/home", method = RequestMethod.GET, params = "action=editTrip")
    public ModelAndView editTrip(@RequestParam("id") Long id) {
        ModelAndView model = new ModelAndView("tripForm");

        Optional<Trip> tripOptional = tripRepository.findById(id);

        Trip tripTmp = new Trip();
        if (tripOptional.isPresent()) {
            tripTmp = tripOptional.get();
        }
        model.addObject("trip", tripTmp);
        return model;
    }

    /**
     * deleteTrip mapping deletes the selected trip object and the associated images.
     * It redirects the user to main page.
     *
     * @author Radu Popescu
     */

    @RequestMapping(value = "/home", method = RequestMethod.GET, params = "action=deleteTrip")
    public ModelAndView deleteTrip(@RequestParam("id") Long id) {
        ModelAndView model = new ModelAndView("home");

        Optional<Trip> tripOptional = tripRepository.findById(id);

        if (tripOptional.isPresent()) {
            Trip tripTmp = tripOptional.get();
            fileService.deleteResource(tripTmp.getPhoto1_path());
            fileService.deleteResource(tripTmp.getPhoto2_path());
            tripRepository.delete(tripTmp);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUserName);

        model.addObject("trips", tripRepository.findByUser(currentUser));

        Trip tripObj = new Trip();
        model.addObject("tripObj", tripObj);
        model.addObject("photo1", tripObj.getPhoto1_path());
        model.addObject("photo2", tripObj.getPhoto2_path());

        return model;
    }

    /**
     * saveTrip mapping, in addition to standard object validation (done with annotation), performs a custom validation,
     * saves the object and it redirects the user to main page.
     *
     * @author Radu Popescu
     */

    @RequestMapping(value = "/trip/saveTrip", method = RequestMethod.POST)
    public ModelAndView saveTrip(@RequestParam("photo1") MultipartFile photo1,
                                 @RequestParam("photo2") MultipartFile photo2,
                                 @Valid @ModelAttribute Trip trip, BindingResult bindingResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUserName);

        if (photo1.isEmpty() && (trip.getId() == null)) {
            bindingResult.rejectValue("photo1_path", "error.trip", "Please upload photo 1");
        }

        if (photo2.isEmpty() && (trip.getId() == null)) {
            bindingResult.rejectValue("photo2_path", "error.trip", "Please upload photo 2");
        }

        if (trip.getDate_to().isBefore(trip.getDate_from())) {
            bindingResult.rejectValue("date_to", "error.trip", "Please enter valid dates");
        }

        List<Trip> tripList = tripRepository.findByUser(currentUser);
        tripList.forEach(tripFromList -> {
            if (trip.getTrip_name().equalsIgnoreCase(tripFromList.getTrip_name()) && trip.getId() != tripFromList.getId()) {
                bindingResult.rejectValue("trip_name", "error.trip", "Trip name already exists");
            }
        });

        if (bindingResult.hasErrors()) {
            ModelAndView errorModel = new ModelAndView("tripForm");
            return errorModel;
        }

        ModelAndView model = new ModelAndView("home");

        Trip tripFromDb = new Trip();

        if (trip.getId() != null) {
            Optional<Trip> tripOptional = tripRepository.findById(trip.getId());
            if (tripOptional.isPresent()) {
                tripFromDb = tripOptional.get();
            }
        }

        if (trip.getId() == null || (trip.getId() != null && !photo1.isEmpty())) {
                trip.setPhoto1_path(UUID.randomUUID().toString());
        } else {
            trip.setPhoto1_path(tripFromDb.getPhoto1_path());
        }

        if (trip.getId() == null || (trip.getId() != null && !photo2.isEmpty())) {
            trip.setPhoto2_path(UUID.randomUUID().toString());
        } else {
            trip.setPhoto2_path(tripFromDb.getPhoto2_path());
        }

        tripFromDb = tripRepository.save(trip);

        tripList = tripRepository.findByUser(currentUser);

        if (trip.getId() == null || (trip.getId() != null && !photo1.isEmpty())) {
            fileService.store(tripFromDb.getPhoto1_path(), photo1);
        }
        if (trip.getId() == null || (trip.getId() != null && !photo2.isEmpty())) {
            fileService.store(tripFromDb.getPhoto2_path(), photo2);
        }

        model.addObject("tripObj", tripFromDb);
        model.addObject("trips", tripList);
        model.addObject("selectedTrip", tripFromDb.getId());
        model.addObject("photo1", tripFromDb.getPhoto1_path());
        model.addObject("photo2", tripFromDb.getPhoto2_path());

        return model;
    }
}
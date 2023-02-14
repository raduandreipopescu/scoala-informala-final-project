package ro.siit.tripscatalog.config;

/**
 * Class WebMvcConfig implements WebMvcConfigurer in order to link mappings with views.
 *
 * @author Radu Popescu
 *
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/login").setViewName("logout");
        registry.addViewController("/login").setViewName("login");
    }
}

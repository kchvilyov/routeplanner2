package us.crimean.maps;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {
    @GetMapping("/routeplanner")
    public String routeplanner(Model model) {
        return "routeplanner";
    }

    @GetMapping("/help-routeplanner")
    public String helpRouteplanner(Model model) {
        return "help-routeplanner";
    }

    @GetMapping("/about-routeplanner")
    public String aboutRouteplanner(Model model) {
        return "about-routeplanner";
    }
}

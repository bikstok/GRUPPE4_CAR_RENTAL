package org.example.gruppe4_car_rental.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    //Index er forside/login siden
    @GetMapping("/")
    public String index() {
        return "index";
    }
}

//@RequestParam fanger disse værdier og gør dem tilgængelige i din Java-metode.
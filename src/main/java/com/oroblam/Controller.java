package com.oroblam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by magnecch on 29/01/2017.
 */
@RestController
public class Controller {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
}

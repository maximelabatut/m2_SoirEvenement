package gateway.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FragmentsController {

    @GetMapping("/includes/header")
    public String getHeader() {
        return "/includes/header.html";
    }

    @GetMapping("/includes/headerLogged")
    public String getHeaderLogged() {
        return "/includes/headerLogged.html";
    }

    @GetMapping("/includes/head")
    public String getHead() {
        return "/includes/head.html";
    }

    @GetMapping("/includes/footer")
    public String getFooter() {
        return "/includes/footer.html";
    }
}
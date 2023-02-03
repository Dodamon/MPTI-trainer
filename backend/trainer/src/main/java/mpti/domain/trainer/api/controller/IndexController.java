package mpti.domain.trainer.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
public class IndexController {
    @GetMapping("")
    public String checkDuplicateId() {
        return "<h1>Hello Trainer Server Main Page</h1>";
    }
}

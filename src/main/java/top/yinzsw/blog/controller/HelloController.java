package top.yinzsw.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 *
 * @author yinzsW
 * @since 22/12/15
 */
@RestController
@RequestMapping("hello")
public class HelloController {
    @GetMapping
    public String hello() {
        return "hello";
    }
}

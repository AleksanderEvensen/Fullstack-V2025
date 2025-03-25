package edu.ntnu.fullstack.amazoom.controller

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

// Order LOWEST_PRECEDENCE to make sure this controller is the last to be called
@Controller
@Order(Ordered.LOWEST_PRECEDENCE)
class SpaController {
    @RequestMapping(value = ["/{path:[^\\.]*}"])
    fun redirect(): String {
        return "forward:/"
    }

}
package com.kodigo.alltodo_api.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.BAD_REQUEST.value()) {
                System.out.println("400");
                return "error-400";
            }
             if(statusCode == HttpStatus.FORBIDDEN.value()) {
                 System.out.println("403");
                return "error-403";
            }
             if(statusCode == HttpStatus.NOT_FOUND.value()) {
                 System.out.println("404");
                return "error-404";
            }
             if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                 System.out.println("500");
                return "error-500";
            }
             if(statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                 System.out.println("503");
                return "error-503";
            }
        }
        return "error";
    }
}

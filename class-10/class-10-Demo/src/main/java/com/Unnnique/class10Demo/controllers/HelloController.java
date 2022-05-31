package com.Unnnique.class10Demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
@RequestMapping("/hello")
public class HelloController {

    // a home route
    // rest the request method
    // EITHER: set a response body, or use a MODEL!!! cannot do both
    @RequestMapping(value = "/", method = RequestMethod.GET)

    public String getTest(){
        return "splashPage";
    }

    @GetMapping("/hi")
    public String getHi(){

        return "hello";
    }

    // route with URL params (NOT QUERY PARAMS)
    @GetMapping("/sayhello/{name}")
    @ResponseBody
    public String sayHello(@PathVariable String name){
        String upperName = name.toUpperCase(Locale.ROOT);
        return "Hello " + upperName;

    }

    // route with params using model
    @GetMapping("/model/{name}")
    public String modelMe(@PathVariable String name, Model model){

        MyModel myModel = new MyModel();
        myModel.name = name;
        myModel.number = 3;

        model.addAttribute("info", myModel);
        // This is not what u think, it is the route to the template!!!
        return "friends/newFriend";
    }





}

package ro.unibuc.hello.controller;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.hello.dto.Greeting;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.HelloWorldService;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HelloWorldController {

    @Autowired
    private HelloWorldService helloWorldService;

    @Autowired
    MeterRegistry metricsRegistry;

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello-world")
    @Timed(value = "hello.greeting.time", description = "Time taken to return greeting")
    @Counted(value = "hello.greeting.count", description = "Times greeting was returned")
    @ResponseBody
    public Greeting sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
        metricsRegistry.counter("my_non_aop_metric", "endpoint", "hello").increment(counter.incrementAndGet());
        return helloWorldService.hello(name);
    }

    @GetMapping("/info")
    @ResponseBody
    @Timed(value = "hello.info.time", description = "Time taken to return info")
    @Counted(value = "hello.info.count", description = "Times info was returned")
    public Greeting info(@RequestParam(name="title", required=false, defaultValue="Overview") String title) throws EntityNotFoundException {
        return helloWorldService.buildGreetingFromInfo(title);
    }

}

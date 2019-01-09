package com.os.enaio.test.web.rest;

import com.os.enaio.test.services.EnaioService;
import com.os.services.blue.error.ErrorTypeResolver;
import com.os.services.blue.error.exceptions.EnaioServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by cschulze on 15.03.2017.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    private final static Logger LOGGER = LoggerFactory.getLogger(EnaioService.class);

    @Autowired
    private EnaioService enaioService;

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String showDemoResponse() throws EnaioServiceException {
        return "Hello World, I am the test-service.";
    }

    @RequestMapping(value = "error", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object showErrorMessage() throws EnaioServiceException {
        throw new EnaioServiceException(ErrorTypeResolver.resolve("DEMO_ERROR"));
    }

    /// Reading a path variable and a query parameter (which do nothing, only for showcasing)
    @RequestMapping(value = "check-license/{lic}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String checkLic(@PathVariable("lic") String lic, @RequestParam(value = "test", required = false) Boolean testParam) throws Exception {
        LOGGER.info("Test Query Param is " + testParam);

        return enaioService.checkLicense(lic);
    }

    /// Assuming a Post with a JSON Array containing all licenses to check
    @RequestMapping(value = "check-license", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String[] checkLics(@RequestBody String[] requestBodyData) throws Exception {
        return enaioService.checkLicenses(requestBodyData);
    }
}

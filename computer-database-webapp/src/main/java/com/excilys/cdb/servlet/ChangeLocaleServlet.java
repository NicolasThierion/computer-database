package com.excilys.cdb.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.servlet.ViewConfig.LocaleChanger.Get;
/**
 * Servlet implementation to handle dashboard page.
 */
@Controller
@RequestMapping(value = ViewConfig.LocaleChanger.MAPPING)
public class ChangeLocaleServlet {

    /* ***
     * ATTRIBUTES
     */

    /* ***
     * PUBLIC SERVLET METHODS
     */
    /**
     * Redirect to supplied page.
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView doChangeLocaleAndRedirect(@RequestParam(value = Get.LANG, required = true) String locale,
            @RequestParam(value = Get.REDIRECT, defaultValue = ViewConfig.Dashboard.MAPPING) String url) {
        final ModelAndView mv = new ModelAndView("redirect:" + url);

        return mv;
    }
}

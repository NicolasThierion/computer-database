package com.excilys.cdb.servlets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;

/**
 * Servlet implementation to handle 'add computer' page.
 */
@Controller
public class AddComputerServlet {

    private static final Logger LOG          = LoggerFactory.getLogger(AddComputerServlet.class);

    /* ***
     * ATTRIBUTES
     */
    @Autowired
    private IComputerService mComputerService;
    @Autowired
    private ICompanyService  mComanyService;

    /* ***
     * CONTROLLER'S PUBLIC METHODS
     */

    @RequestMapping(value = ViewConfig.AddComputer.MAPPING, method = RequestMethod.GET)
    public ModelAndView gotoAdd() {
        LOG.info("gotoAdd()");

        // fetch company list for <select>
        final List<Company> companies = mComanyService.listByName();
        final Page<Company> companiesPage = new Page<Company>(companies);

        final ModelAndView mv = new ModelAndView(ViewConfig.AddComputer.MAPPING);

        // set attributes & redirect to jsp.
        mv.addObject(ViewConfig.AddComputer.Set.COMPANIES_PAGE_BEAN, companiesPage);
        return mv;
    }

    @RequestMapping(value = ViewConfig.AddComputer.MAPPING, method = RequestMethod.POST)
    public ModelAndView doAdd(@ModelAttribute("computerForm") ComputerDto computerDto,
            BindingResult bindingResult, Model model) {
        LOG.info("doAdd(" + computerDto + ", " + bindingResult + ", " + model + ")");

        if (bindingResult.hasErrors()) {
            // TODO
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }
        final Computer computer = computerDto.toComputer();
        mComputerService.add(computer);
        return new ModelAndView(ViewConfig.Dashboard.MAPPING);
    }
}

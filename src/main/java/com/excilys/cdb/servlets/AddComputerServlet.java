package com.excilys.cdb.servlets;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.CompanyDto;
import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.servlets.ViewConfig.AddComputer.Get;
import com.excilys.cdb.servlets.ViewConfig.AddComputer.Set;
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
        final List<CompanyDto> companyDtos = new LinkedList<CompanyDto>();
        mComanyService.listByName().stream().forEach(s -> companyDtos.add(CompanyDto.fromCompany(s)));


        final ModelAndView mv = new ModelAndView(ViewConfig.AddComputer.MAPPING);

        // set attributes & redirect to jsp.
        mv.addObject(Set.COMPANY_DTO_LIST, companyDtos);
        // set an empty computerDto to hold form fields from POST.
        mv.addObject(Set.COMPUTER_DTO, new ComputerDto());
        return mv;
    }

    @RequestMapping(value = ViewConfig.AddComputer.MAPPING, method = RequestMethod.POST)
    public ModelAndView doAdd(@ModelAttribute(Get.COMPUTER_DTO) ComputerDto computerDto,
            BindingResult bindingResult) {
        LOG.info("doAdd(" + computerDto + ", " + bindingResult + ")");

        if (bindingResult.hasErrors()) {
            // TODO
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }
        final Computer computer = computerDto.toComputer();
        mComputerService.add(computer);
        return new ModelAndView("redirect:" + ViewConfig.Dashboard.MAPPING);
    }
}

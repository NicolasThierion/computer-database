package com.excilys.cdb.servlets;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.servlets.ViewConfig.EditComputer.Get;
import com.excilys.cdb.servlets.ViewConfig.EditComputer.Set;

/**
 * Servlet implementation to handle 'edit computer' page.
 */
@Controller
@RequestMapping(value = ViewConfig.EditComputer.MAPPING)
public class EditComputerServlet {

    /* ***
     * ATTRIBUTES
     */
    @Autowired
    private IComputerService mComputerService;
    @Autowired
    private ICompanyService  mComanyService;

    /* ***
     * PRIVATE METHODS
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView doUpdate(@RequestParam(value = Get.COMPUTER_ID, required = true) Long computerId,
            @RequestParam(value = Get.COMPUTER_NAME, required = true) String computerName,
            @RequestParam(value = Get.COMPUTER_RELEASE, defaultValue = "") String computerRelease,
            @RequestParam(value = Get.COMPUTER_DISCONT, defaultValue = "") String computerDiscont,
            @RequestParam(value = Get.COMPANY_ID, defaultValue = "") Long companyId) {
        final Computer computer = new ComputerDto(computerId, computerName, computerRelease, computerDiscont,
                companyId).toComputer();
        mComputerService.update(computer);
        final ModelAndView mv = new ModelAndView("redirect:" + ViewConfig.EditComputer.MAPPING);
        mv.addObject(Get.COMPUTER_ID, computerId);
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView gotoEdit(
            @RequestParam(value = Get.COMPUTER_ID, required = true) final Long computerId) {

        // create a new computer from computerId.
        final ComputerDto computer = ComputerDto.fromComputer(mComputerService.retrieve(computerId));

        // fetch company list for <select>
        final List<Company> companies = mComanyService.listByName();
        final Page<Company> companiesPage = new Page<Company>(companies);

        final ModelAndView mv = new ModelAndView(ViewConfig.EditComputer.MAPPING);

        // set attributes & redirect to jsp.
        mv.addObject(Set.COMPUTER_BEAN, computer);
        mv.addObject(Set.COMPANIES_PAGE_BEAN, companiesPage);
        return mv;
    }
}

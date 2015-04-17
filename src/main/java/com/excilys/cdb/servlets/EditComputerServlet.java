package com.excilys.cdb.servlets;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.CompanyDto;
import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
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
    /**
     * do the update & redirect to dashboard.
     *
     * @param computerDto
     * @param bindingResult
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView doUpdate(@ModelAttribute(Get.COMPUTER_DTO) ComputerDto computerDto,
            BindingResult bindingResult) {
        final Computer computer = computerDto.toComputer();
        mComputerService.update(computer);
        final ModelAndView mv = new ModelAndView("redirect:" + ViewConfig.Dashboard.MAPPING);
        mv.addObject(Get.COMPUTER_ID, computerDto.getId());
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView gotoEdit(
            @RequestParam(value = Get.COMPUTER_ID, required = true) final Long computerId) {

        // create a new computer from computerId.
        final ComputerDto computerDto = ComputerDto.fromComputer(mComputerService.retrieve(computerId));

        // fetch company list for <select>
        final List<CompanyDto> companyDtos = new LinkedList<CompanyDto>();
        mComanyService.listByName().stream().forEach(s -> companyDtos.add(CompanyDto.fromCompany(s)));

        final ModelAndView mv = new ModelAndView(ViewConfig.EditComputer.MAPPING);

        // set attributes & redirect to jsp.
        mv.addObject(Set.COMPUTER_DTO, computerDto);
        mv.addObject(Set.COMPANY_DTO_LIST, companyDtos);
        return mv;
    }
}

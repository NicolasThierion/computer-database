package com.excilys.cdb.servlet;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
import com.excilys.cdb.servlet.ViewConfig.EditComputer.Get;
import com.excilys.cdb.servlet.ViewConfig.EditComputer.Set;
import com.excilys.cdb.validator.ComputerValidator;

/**
 * Servlet implementation to handle 'edit computer' page. Handle
 * "Add computer url" ({@link #ViewConfig.AddComputer.MAPPING}) & "Edit computer url" (
 * {@link #ViewConfig.EditComputer.MAPPING}).
 */
@Controller
@RequestMapping()
public class EditComputerServlet {

    /* ***
     * ATTRIBUTES
     */
    @Autowired
    private IComputerService mComputerService;
    @Autowired
    private ICompanyService  mCompanyService;

    @Autowired
    private ComputerValidator mComputerValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(mComputerValidator);
    }

    /**
     * Go to "Add computer" JSP.
     *
     * @return
     */
    @RequestMapping(value = ViewConfig.AddComputer.MAPPING, method = RequestMethod.GET)
    public ModelAndView gotoAdd() {

        // fetch company list for <select>
        final List<CompanyDto> companyDtos = mGetCompanyDtoList();

        final ModelAndView mv = new ModelAndView(ViewConfig.AddComputer.MAPPING);

        // set attributes & redirect to jsp.
        mv.addObject(ViewConfig.AddComputer.Set.COMPANY_DTO_LIST, companyDtos);
        // set an empty computerDto to hold form fields from POST.
        mv.addObject(ViewConfig.AddComputer.Set.COMPUTER_DTO, new ComputerDto());
        return mv;
    }

    @RequestMapping(value = ViewConfig.AddComputer.MAPPING, method = RequestMethod.POST)
    public ModelAndView doAdd(@Valid @ModelAttribute(Get.COMPUTER_DTO) ComputerDto computerDto,
            BindingResult bindingResult) {
        ModelAndView mv;
        if (bindingResult.hasErrors()) {
            // some errors... Let validator fill error placeholders in JSP
            // then keep editing this computer on this page.
            mv = new ModelAndView(ViewConfig.AddComputer.MAPPING);

            // fetch company list for <select>
            final List<CompanyDto> companyDtos = mGetCompanyDtoList();

            // set attributes & redirect to jsp.
            mv.addObject(ViewConfig.AddComputer.Set.COMPUTER_DTO, computerDto);
            mv.addObject(ViewConfig.AddComputer.Set.COMPANY_DTO_LIST, companyDtos);
        } else {
            mv = new ModelAndView("redirect:" + ViewConfig.Dashboard.MAPPING);
            final Computer computer = computerDto.toComputer();
            mComputerService.add(computer);
        }

        return mv;
    }

    /**
     * do the update & redirect to dashboard.
     *
     * @param computerDto
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = ViewConfig.EditComputer.MAPPING, method = RequestMethod.POST)
    public ModelAndView doUpdate(@Valid @ModelAttribute(Get.COMPUTER_DTO) ComputerDto computerDto,
            BindingResult bindingResult) {

        ModelAndView mv;
        if (bindingResult.hasErrors()) {
            // some errors... Let validator fill error placeholders in JSP
            // then keep editing this computer on this page.
            mv = new ModelAndView(ViewConfig.EditComputer.MAPPING);

            // fetch company list for <select>
            final List<CompanyDto> companyDtos = mGetCompanyDtoList();

            // set attributes & redirect to jsp.
            mv.addObject(Set.COMPUTER_DTO, computerDto);
            mv.addObject(Set.COMPANY_DTO_LIST, companyDtos);
        } else {
            // no errors : update this computer...
            final Computer computer = computerDto.toComputer();
            mComputerService.update(computer);

            // then redirect to dashboard.
            mv = new ModelAndView("redirect:" + ViewConfig.Dashboard.MAPPING);
        }
        return mv;
    }

    @RequestMapping(value = ViewConfig.EditComputer.MAPPING, method = RequestMethod.GET)
    public ModelAndView gotoEdit(
            @RequestParam(value = Get.COMPUTER_ID, required = true) final Long computerId) {

        // create a new computer from computerId.
        final ComputerDto computerDto = ComputerDto.fromComputer(mComputerService.retrieve(computerId));

        // fetch company list for <select>
        final List<CompanyDto> companyDtos = mGetCompanyDtoList();

        final ModelAndView mv = new ModelAndView(ViewConfig.EditComputer.MAPPING);

        // set attributes & redirect to jsp.
        mv.addObject(Set.COMPUTER_DTO, computerDto);
        mv.addObject(Set.COMPANY_DTO_LIST, companyDtos);
        return mv;
    }


    private List<CompanyDto> mGetCompanyDtoList() {
        final List<CompanyDto> companyDtos = new LinkedList<CompanyDto>();
        mCompanyService.listByName().stream().forEach(s -> companyDtos.add(CompanyDto.fromCompany(s)));
        return companyDtos;
    }
}

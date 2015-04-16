package com.excilys.cdb.servlets;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.servlets.ViewConfig.Dashboard;
import com.excilys.cdb.servlets.ViewConfig.Dashboard.Get;
import com.excilys.cdb.servlets.ViewConfig.Dashboard.Set;
/**
 * Servlet implementation to handle dashboard page.
 */
@Controller
@RequestMapping(value = ViewConfig.Dashboard.MAPPING)
public class DashboardServlet {

    /* ***
     * ATTRIBUTES
     */
    @Autowired
    private IComputerService mComputerService;

    /**
     * Redirect to dashBoard.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView gotoDashboard(
            @RequestParam(value = Get.PAGE_SIZE, defaultValue = ""
            + Dashboard.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = Get.SEARCH, defaultValue = "") String queryName,
            @RequestParam(value = Get.PAGE_OFFSET, defaultValue = "0") int offset) {
        List<Computer> computers = new LinkedList<Computer>();

        // count results & store them in a Page<Computer>
        final int totalResults = mComputerService.getCount(queryName);

        if (totalResults > 0) {
            // select all computer in page range, & ensure offset is not too
            // far.
            computers = mComputerService.listLikeName(offset, pageSize, queryName);
            if (offset > totalResults) {
                offset = totalResults;
            }
        }

        final List<ComputerDto> dtos = ComputerDto.fromComputers(computers);
        final Page<ComputerDto> page = new Page<ComputerDto>(dtos, offset, totalResults, queryName);
        page.setSize(pageSize);

        final ModelAndView mv = new ModelAndView(ViewConfig.Dashboard.MAPPING);

        // set result page & send redirect.
        mv.addObject(Set.PAGE_BEAN, page);
        return mv;
    }
}

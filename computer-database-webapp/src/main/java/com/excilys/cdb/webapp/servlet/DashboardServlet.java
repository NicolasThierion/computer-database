package com.excilys.cdb.webapp.servlet;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.webapp.servlet.ViewConfig.Dashboard;
import com.excilys.cdb.webapp.servlet.ViewConfig.Dashboard.Get;
import com.excilys.cdb.webapp.servlet.ViewConfig.Dashboard.Set;
/**
 * Servlet implementation to handle dashboard page.
 */
@Controller
public class DashboardServlet {

    /* ***
     * ATTRIBUTES
     */
    @Autowired
    private IComputerService mComputerService;

    /* ***
     * PUBLIC SERVLET METHODS
     */
    /**
     * Redirect to dashBoard.
     */
    @RequestMapping(value = ViewConfig.Dashboard.MAPPING, method = RequestMethod.GET)
    public ModelAndView gotoDashboard(@ModelAttribute(Get.PAGE_BEAN) Page<?> page) {
        if (page.getSearch() == null) {
            page.setSearch("");
        }
        if (page.getSize() <= 0) {
            page.setSize(Dashboard.DEFAULT_PAGE_SIZE);
        }
        return doSearch(page);
    }

    /**
     * Search computer.
     */
    @RequestMapping(value = ViewConfig.SearchComputer.MAPPING, method = RequestMethod.GET)
    public ModelAndView doSearch(@ModelAttribute(Get.PAGE_BEAN) Page<?> page) {

        List<Computer> computers = new LinkedList<Computer>();

        final String queryName = page.getSearch();
        // redirect to dashboard if no query supplied
        if (queryName == null) {
            return gotoDashboard(page);
        }

        int offset = page.getOffset();
        final int pageSize = page.getSize();

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
        page = new Page<ComputerDto>(dtos, offset, totalResults, queryName);
        page.setSize(pageSize);

        final ModelAndView mv = new ModelAndView(ViewConfig.Dashboard.MAPPING);

        // set result page & send redirect.
        mv.addObject(Set.PAGE_BEAN, page);
        return mv;
    }

    /**
     * Delete the given computer & redirect to dashBoard.
     */
    @RequestMapping(
            value = {ViewConfig.DeleteComputer.MAPPING, ViewConfig.SearchComputer.MAPPING},
            method = RequestMethod.POST)
    public ModelAndView doDeleteComputer(
            @RequestParam(value = ViewConfig.DeleteComputer.Get.COMPUTER_IDS, required = true) long[] computerIds,
            @ModelAttribute(Get.PAGE_BEAN) Page<?> page) {

        mComputerService.delete(computerIds);

        // we will redirect to dashboard. As dashboard is handled by GET, it
        // cannot get attributes from this POST context. We have to pass
        // attributes through url manually.
        final String url = new StringBuilder().append(ViewConfig.Dashboard.MAPPING)
                .append("?")
                .append(page.toUrlArgs()).toString();
        final ModelAndView mv = new ModelAndView("redirect:" + url);
        mv.addObject(Set.PAGE_BEAN, page);

        // retirect to dashboard
        return mv;
    }
}

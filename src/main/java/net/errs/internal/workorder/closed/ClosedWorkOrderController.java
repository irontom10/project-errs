package net.errs.internal.workorder.closed;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Controller
@RequestMapping("/api/v1/workorders")
public class ClosedWorkOrderController {

    private final ClosedWorkOrderService service;

    public ClosedWorkOrderController(ClosedWorkOrderService service) {
        this.service = service;
    }

    private List<ClosedWorkOrderDto> find(int page, int limit) {
        if (page < 1 || limit < 1 || limit > 500) {
            throw new ResponseStatusException(BAD_REQUEST, "page must be >= 1 and limit must be 1-500");
        }
        return service.list(page, limit);
    }

    @GetMapping(value = "/closed", headers = "HX-Request=true", produces = MediaType.TEXT_HTML_VALUE)
    public String html(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "50") int limit,
                       Model model) {
        model.addAttribute("orders", find(page, limit));
        return "internal/pages/workorder/closed/rows :: rows";
    }

    @ResponseBody
    @GetMapping(value = "/closed", headers = "!HX-Request", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClosedWorkOrderDto> json(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "50") int limit) {
        return find(page, limit);
    }
}

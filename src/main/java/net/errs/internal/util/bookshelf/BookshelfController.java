package net.errs.internal.util.bookshelf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/internal/util/books")
public class BookshelfController {

    private final BookshelfService bookshelfService;

    public BookshelfController(BookshelfService bookshelfService) {
        this.bookshelfService = bookshelfService;
    }

    @GetMapping
    public String browseBooks(@RequestParam(defaultValue = "") String path,
                              @RequestParam(name = "q", required = false) String query,
                              Model model) throws Exception {
        boolean hasQuery = query != null && !query.isBlank();

        model.addAttribute("currentPath", path);
        model.addAttribute("parentPath", bookshelfService.parentPath(path));
        model.addAttribute("items", bookshelfService.listFolder(path));
        model.addAttribute("searchQuery", hasQuery ? query : "");
        model.addAttribute("searchResults", hasQuery ? bookshelfService.search(query) : List.of());
        return "internal/pages/util/bookshelf/menu";
    }
}

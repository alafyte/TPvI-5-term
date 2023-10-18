package by.belstu.lab01.controller;

import by.belstu.lab01.forms.AlbumForm;
import by.belstu.lab01.forms.DeleteAlbumForm;
import by.belstu.lab01.forms.EditAlbumForm;
import by.belstu.lab01.model.Album;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping
public class AlbumController {
    private static final List<Album> albums = new ArrayList<Album>();

    static {
        albums.add(new Album("The Dark Side of the Moon", "Pink Floyd"));
        albums.add(new Album("Back in Black", "AC/DC"));
    }

    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @GetMapping(value = {"/", "/index"})
    public ModelAndView index(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        model.addAttribute("message", message);
        log.info("/index");
        return modelAndView;
    }

    @GetMapping(value = {"/all"})
    public ModelAndView personList(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("album_list");
        model.addAttribute("albums", albums);
        log.info("/all GET");
        return modelAndView;
    }

    @GetMapping(value = {"/add"})
    public ModelAndView showAddPersonPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("add_album");
        AlbumForm albumForm = new AlbumForm();
        model.addAttribute("album_form", albumForm);
        log.info("/add GET");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView savePerson(Model model, @ModelAttribute("album_form") AlbumForm albumForm) {
        ModelAndView modelAndView = new ModelAndView();

        log.info("/add POST");

        modelAndView.setViewName("album_list");
        String title = albumForm.getTitle();
        String author = albumForm.getAuthor();
        if (title != null && title.length() > 0 && author != null && author.length() > 0) {
            for (Album album : albums) {
                if (album.getTitle().equals(title)) {
                    modelAndView.setViewName("add_album");
                    errorMessage = "There's already an album with this title";
                    model.addAttribute("errorMessage", errorMessage);
                    return modelAndView;
                }
            }
            Album newAlbum = new Album(title, author);
            albums.add(newAlbum);
            model.addAttribute("albums", albums);
            return modelAndView;
        } else {
            errorMessage = "All fields are required";
        }
        modelAndView.setViewName("add_album");
        model.addAttribute("errorMessage", errorMessage);
        return modelAndView;
    }

    @GetMapping(value = {"/delete"})
    public ModelAndView showDelAlbumPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("del_album");
        DeleteAlbumForm albumForm = new DeleteAlbumForm();
        model.addAttribute("album_form", albumForm);
        log.info("/delete GET");
        return modelAndView;
    }

    @PostMapping(value = {"/delete"})
    public ModelAndView delBook(Model model,
                                @ModelAttribute("album_form") DeleteAlbumForm albumForm) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("album_list");
        String title = albumForm.getTitle();
        log.info("/delete POST");

        if (title != null && title.length() > 0) {
            int index = 0;
            boolean found = false;
            for (Album album : albums) {
                if (album.getTitle().equalsIgnoreCase(title)) {
                    albums.remove(index);
                    found = true;
                    break;
                }
                index++;
            }
            if (!found) {
                errorMessage = "Such album not found";
            } else {
                model.addAttribute("albums", albums);
                return modelAndView;
            }
        } else {
            errorMessage = "All fields are required";
        }
        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("del_album");
        return modelAndView;
    }

    @GetMapping(value = {"/edit"})
    public ModelAndView showEditAlbumPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("edit_album");
        EditAlbumForm albumForm = new EditAlbumForm();
        model.addAttribute("album_form", albumForm);
        log.info("/edit GET");
        return modelAndView;
    }

    @PostMapping(value = {"/edit"})
    public ModelAndView updateAlbum(Model model,
                                    @ModelAttribute("album_form") EditAlbumForm albumForm) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("album_list");
        String title = albumForm.getTitle();
        String newTitle = albumForm.getNewTitle();
        String newAuthor = albumForm.getNewAuthor();
        log.info("/edit POST");

        if (title != null && title.length() > 0
                && newTitle != null && newTitle.length() > 0
                && newAuthor != null && newAuthor.length() > 0) {
            boolean found = false;
            for (Album album : albums) {
                if (album.getTitle().equals(newTitle)) {
                    errorMessage = "There's already an album with such title";
                    model.addAttribute("errorMessage", errorMessage);
                    modelAndView.setViewName("edit_album");
                    return modelAndView;
                }
            }
            for (Album album : albums) {
                if (album.getTitle().equals(title)) {
                    album.setTitle(newTitle);
                    album.setAuthor(newAuthor);
                    found = true;
                }
            }
            if (!found) {
                errorMessage = "Such album not found";
            } else {
                model.addAttribute("albums", albums);
                return modelAndView;
            }
        } else {
            errorMessage = "All fields are required";
        }

        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("edit_album");
        return modelAndView;
    }
}

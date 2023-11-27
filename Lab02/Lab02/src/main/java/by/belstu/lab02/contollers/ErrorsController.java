package by.belstu.lab02.contollers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorsController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView modelAndView = new ModelAndView();
        String errorMessage = "Ошибка";
        String errorCode = "Произошла ошибка";
        String additionalInfo = "Попытайтесь перезагрузить страницу или свяжитесь с администратором";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value() || statusCode == HttpStatus.FORBIDDEN.value()) {
                errorCode = "404";
                errorMessage = "Страница не найдена.";
                additionalInfo = "Страница, которую вы ищете, не существует.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorCode = "500";
                errorMessage = "Ошибка сервера.";
                additionalInfo = "Попытайтесь перезагрузить страницу или свяжитесь с администратором";
            }
        }
        modelAndView.addObject("errorCode", errorCode);
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.addObject("additionalInfo", additionalInfo);
        modelAndView.setViewName("error");
        return modelAndView;
    }
}

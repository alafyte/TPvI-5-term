package by.belstu.lab02.contollers;


import by.belstu.lab02.dto.AuthDto;
import by.belstu.lab02.dto.LoginResponseDto;
import by.belstu.lab02.dto.RegisterWorkerRequest;
import by.belstu.lab02.jwt.JwtGenerator;
import by.belstu.lab02.models.Role;
import by.belstu.lab02.models.Roles;
import by.belstu.lab02.models.User;
import by.belstu.lab02.models.Worker;
import by.belstu.lab02.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserServices userServices;

    @Autowired
    RoleServices roleServices;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private LocalValidatorFactoryBean validator;


    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private WorkerServices workerServices;

    @GetMapping(value = {"/"})
    public ModelAndView index(Model model) {
        UserDetailsImpl auth_user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userServices.findByLogin(auth_user.getUsername()).get();
        Role role = user.getRoles();

        ModelAndView modelAndView = new ModelAndView();

        if (role.getName().equals(Roles.ADMIN)) {
            model.addAttribute("user", user);
            modelAndView.setViewName("indexAdmin");
        } else if (role.getName().equals(Roles.WORKER)) {
            model.addAttribute("user", user);
            modelAndView.setViewName("indexWorker");
        }

        log.info("/ GET");
        return modelAndView;
    }

    @GetMapping("/register-worker")
    public ModelAndView RegUser(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        List<Worker> workers = workerServices.findAll();
        modelAndView.addObject("workers", workers);
        modelAndView.setViewName("RegisterWorker");

        log.info("/register-worker GET");
        return modelAndView;
    }


    @PostMapping("/register-worker")
    public ResponseEntity<?> registerUser(@RequestBody RegisterWorkerRequest registerWorkerRequest) {
        try {
            Set<ConstraintViolation<RegisterWorkerRequest>> violations = validator.validate(registerWorkerRequest);

            if (!violations.isEmpty()) {
                List<String> errorMessages = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
                return new ResponseEntity<>(errorMessages, HttpStatus.OK);
            }

            User user = new User(
                    registerWorkerRequest.getLogin(),
                    passwordEncoder.encode(registerWorkerRequest.getPassword())
            );
            if (userServices.findByWorkerId(registerWorkerRequest.getId()).isPresent()) {
                user.setId(userServices.findByWorkerId(registerWorkerRequest.getId()).get().getId());
            }

            user.setRoles(roleServices.findByName(Roles.WORKER).get());
            user.setWorker(workerServices.findById(registerWorkerRequest.getId()));
            userServices.saveUser(user);

            emailSenderService.sendSimpleEmail(user.getWorker().getEmail(), "Регистрация", "Вы были успешно зарегистрированы в приложении");
            log.info("/register-worker POST");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Произошла ошибка при регистрации работника");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
    }

    @GetMapping("/login")
    public ModelAndView authUser(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");

        log.info("/login GET");
        return modelAndView;
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody AuthDto adminAuthDto, HttpServletResponse response) {

        try {
            Set<ConstraintViolation<AuthDto>> violations = validator.validate(adminAuthDto);

            if (!violations.isEmpty()) {
                List<String> errorMessages = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
                return new ResponseEntity<>(errorMessages, HttpStatus.OK);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(adminAuthDto.getLogin(), adminAuthDto.getPassword()));
            User user = userServices.findByLogin(adminAuthDto.getLogin()).orElseThrow();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication, user.getRoles().toString());
            response.addCookie(new Cookie("java_token", token));

            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setSuccess(true);
            responseDto.setMessage("login successful !!");
            responseDto.setUser(user.getLogin(), user.getId());

            log.info("/login POST");
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Неверный логин или пароль");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
    }

    @GetMapping("/view-users")
    public ModelAndView viewUsers(Model model) {
        List<User> users = userServices.getUsers();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ViewUsers");
        modelAndView.addObject("users", users);

        log.info("/view-users GET");
        return modelAndView;
    }

}
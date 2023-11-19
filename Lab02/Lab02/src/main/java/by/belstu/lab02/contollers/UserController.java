package by.belstu.lab02.contollers;


import by.belstu.lab02.dto.AuthDto;
import by.belstu.lab02.dto.LoginResponseDto;
import by.belstu.lab02.forms.UserForm;
import by.belstu.lab02.jwt.JwtGenerator;
import by.belstu.lab02.models.Role;
import by.belstu.lab02.models.Roles;
import by.belstu.lab02.models.User;
import by.belstu.lab02.models.Worker;
import by.belstu.lab02.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private UserDetailsServiceImpl customUserDetailsService;
    private final EmailSenderService emailSenderService;

    private final WorkerServices workerServices;

    public UserController(EmailSenderService emailSenderService, WorkerServices workerServices) {
        this.emailSenderService = emailSenderService;
        this.workerServices = workerServices;
    }

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
            modelAndView.setViewName("indexWorker");
        }
        return modelAndView;
    }

    @PostMapping("/register-worker")
    public ModelAndView registerUser(Model model, @ModelAttribute("signUpForm") UserForm signUpRequest) {

        ModelAndView modelAndView = new ModelAndView("indexWorker");
        ModelAndView modelAndView2 = new ModelAndView("signup");
        Worker worker = null;
        try {
            worker = workerServices.findById(signUpRequest.getWorker_id());
        } catch (Exception e) {
            modelAndView2.addObject("message", "Такого пользователя не существует не существует");
            return modelAndView2;
        }
        if (worker != null) {
            if (worker.getPosition().equals("ADMIN")) {
                signUpRequest.setRole("ADMIN");
            } else signUpRequest.setRole("WORKER");
        } else signUpRequest.setRole("GUEST");
        if (userServices.existsByLogin(signUpRequest.getLogin())) {
            model.addAttribute("errorMessage", "Такого рабочего не существует");
            return modelAndView2;
        }
        try {
            User user = new User(
                    signUpRequest.getLogin(),
                    passwordEncoder.encode(signUpRequest.getPassword()));
            user.setWorker(worker);
            Set<Role> roles = new HashSet<>();


            switch (signUpRequest.getRole()) {
                case "ADMIN" -> {
                    Role adminRole = roleServices
                            .findByName(Roles.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                    roles.add(adminRole);
                }
                case "WORKER" -> {
                    Role modRole = roleServices
                            .findByName(Roles.WORKER)
                            .orElseThrow(() -> new RuntimeException("Error, Role WORKER is not found"));
                    roles.add(modRole);
                }
                default -> {
                    Role userRole = roleServices
                            .findByName(Roles.GUEST)
                            .orElseThrow(() -> new RuntimeException("Error, Role GUEST is not found"));
                    roles.add(userRole);
                }
            }
            user.setRoles(roles.stream().findFirst().get());

            userServices.saveUsers(user);
            return modelAndView;
        } catch (Exception err) {
            modelAndView2.addObject("errorMessage", err.getMessage());
            return modelAndView2;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> userLogin(@RequestBody AuthDto adminAuthDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminAuthDto.getLogin(), adminAuthDto.getPassword()));
        User user = userServices.findByLogin(adminAuthDto.getLogin()).orElseThrow();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication, user.getRoles().toString());
        response.addCookie(new Cookie("token", token));

        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("login successful !!");
        responseDto.setUser(user.getLogin(), user.getId());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/login")
    public ModelAndView authUser(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/register-worker")
    public ModelAndView RegUser(Model model) {
        UserForm signUpForm = new UserForm();
        model.addAttribute("signUpForm", signUpForm);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signup");
        return modelAndView;
    }


}
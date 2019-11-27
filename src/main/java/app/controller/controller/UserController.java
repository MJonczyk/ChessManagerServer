package app.controller.controller;

import app.controller.service.CmUserDetailsService;
import app.controller.service.UserService;
import app.model.dto.UserDTO;
import app.model.entity.User;
import app.model.request.LoginRequest;
import app.model.response.JwtTokenAuthResponse;
import app.model.response.RegistrationResponse;
import app.security.JwtTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final CmUserDetailsService userDetailsService;
    private final UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    UserController(UserService userService, AuthenticationManager authenticationManager,
                   JwtTokenGenerator jwtTokenGenerator, CmUserDetailsService userDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest authenticationRequest) throws Exception {
        logger.info("signIn: /login");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenGenerator.generateToken(userDetails);

        return ResponseEntity.ok(new JwtTokenAuthResponse(token));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> addUser(@RequestBody UserDTO newUser) throws Exception {
        logger.info("addUser: /register");
        User user = userService.save(newUser);
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegistrationResponse());
        return ResponseEntity.ok(new RegistrationResponse(user.getLogin()));
    }


//    @PostMapping("/login")
//    public ResponseEntity signIn(@Valid @RequestBody Login loginData) {
//
//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginData.getLogin(),
//                        loginData.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        String jwt = tokenGenerator.generateToken(auth);
//        return ResponseEntity.ok(new JwtTokenAuth(jwt));
//    }
//
//    @PostMapping("/registration")
//    public ResponseEntity register(@Valid @RequestBody Registration registrationData) {
//        if (userRepository.findUserByLogin(registrationData.getLogin()) != null)
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//
//        if (userRepository.findUserByEmail(registrationData.getEmail()) != null)
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//
//        User user = new User(registrationData.getLogin(), registrationData.getPassword(), registrationData.getEmail());
//        user.setPassword(passwordEncoder.encode(registrationData.getPassword()));
//        Role role = roleRepository.findByName("user");
//        user.setRoles(Collections.singleton(role));
//        logger.info(user.getLogin());
//        logger.info(user.getPassword());
//
//        userRepository.save(user);
//        Long id = userRepository.findUserByLogin(user.getLogin()).getId();
//        URI resourceUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{id}").buildAndExpand(id)
//                .toUri();
//
//        return ResponseEntity.created(resourceUri).body(user);
//    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public List<User> getAll() {
        logger.info("getAll: /users");
        return userService.getAll();
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/users/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        logger.info("delete: /users/id");
        userService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}

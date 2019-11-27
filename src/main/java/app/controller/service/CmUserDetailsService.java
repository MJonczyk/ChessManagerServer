package app.controller.service;

import app.model.entity.Role;
import app.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CmUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(CmUserDetailsService.class);

    public CmUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        app.model.entity.User user = userRepository.findUserByLogin(username).orElse(null);

        if (user != null) {
            Set<Role> roles = user.getRoles();
            List<SimpleGrantedAuthority> authorities = roles.stream().map(role ->
                    (new SimpleGrantedAuthority(role.getName()))).collect(Collectors.toList());
            logger.info("loadbyUsername init");
            for (SimpleGrantedAuthority a: authorities)
                logger.info(a.getAuthority());
            return new User(user.getLogin(), user.getPassword(),
                    authorities);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

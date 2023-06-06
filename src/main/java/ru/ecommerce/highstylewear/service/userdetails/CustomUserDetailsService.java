package ru.ecommerce.highstylewear.service.userdetails;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ecommerce.highstylewear.constants.UserRolesConstants;
import ru.ecommerce.highstylewear.model.User;
import ru.ecommerce.highstylewear.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.ecommerce.highstylewear.constants.UserRolesConstants.ADMIN;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Value("${spring.security.user.name}")
    private String adminUserName;
    @Value("${spring.security.user.password}")
    private String adminPassword;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("--------------------------------" + username);
        if (username.equals(adminUserName)) {
            return new CustomUserDetails(null, username, adminPassword, List.of(new SimpleGrantedAuthority("ROLE_" + ADMIN)));
        }
        else {
            User user = userRepository.findUserByLogin(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            //ROLE_USER, ROLE_LIBRARIAN
            authorities.add(new SimpleGrantedAuthority(user.getRole().getId() == 1L ? "ROLE_" + UserRolesConstants.USER :
                    "ROLE_" + UserRolesConstants.STUFF));
            return new CustomUserDetails(user.getId().longValue(), username, user.getPassword(), authorities);
        }
    }
}

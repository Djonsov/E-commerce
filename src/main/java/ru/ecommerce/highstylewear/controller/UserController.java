package ru.ecommerce.highstylewear.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.ecommerce.highstylewear.config.jwt.JWTTokenUtil;
import ru.ecommerce.highstylewear.constants.Errors;
import ru.ecommerce.highstylewear.dto.LoginDTO;
import ru.ecommerce.highstylewear.dto.RegisterDTO;
import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.exception.EmailRegisteredException;
import ru.ecommerce.highstylewear.exception.LoginRegisteredException;
import ru.ecommerce.highstylewear.model.User;
import ru.ecommerce.highstylewear.service.UserService;
import ru.ecommerce.highstylewear.service.userdetails.CustomUserDetails;
import ru.ecommerce.highstylewear.service.userdetails.CustomUserDetailsService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ru.ecommerce.highstylewear.constants.Errors.REST.*;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями магазина")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController extends GenericController<User, UserDTO> {
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTTokenUtil jwtTokenUtil;
    private final UserService userService;

    public UserController(UserService userService, CustomUserDetailsService customUserDetailsService, JWTTokenUtil jwtTokenUtil, UserService userService1) {
        super(userService);
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService1;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody LoginDTO loginDTO) {
        log.info("-----------------LOGINDTO------------->" + loginDTO.getLogin());
        Map<String, Object> response = new HashMap<>();
        UserDetails foundUser = customUserDetailsService.loadUserByUsername(loginDTO.getLogin());
        log.info("foundUser {}", foundUser.toString());
        if (!userService.checkPassword(loginDTO.getPassword(), foundUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка авторизации! \n Неверный логин или пароль");
        }
        String token = jwtTokenUtil.generateToken(foundUser);
        response.put("token", token);
        response.put("username", foundUser.getUsername());
        response.put("role", foundUser.getAuthorities());
        if (!foundUser.getUsername().equals("admin")) {
            response.put("id", userService.getUserByLogin(foundUser.getUsername()).getId());
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @PostMapping("/registration")
//    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
//        try {
//            if (userService.getUserByLogin(dto.getLogin()) != null)
//                throw new LoginRegisteredException();
//            else {
//                if (userService.getUserByEmail(dto.getEmail()) != null)
//                    throw new EmailRegisteredException();
//                else
//                    return ResponseEntity.ok().body(userService.register(dto));
//            }
//        } catch (EmailRegisteredException e) {
//            log.error(e.getMessage()  + "(" + dto.getEmail() + ")");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(EMAIL_REGISTERED_ERROR_MESSAGE + "(" + dto.getEmail() + ")");
//        } catch (LoginRegisteredException e) {
//            log.error(e.getMessage() + "(" + dto.getLogin() + ")");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LOGIN_REGISTERED_ERROR_MESSAGE + "(" + dto.getLogin() + ")");
//        }
//
//    }

    @CrossOrigin
    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) throws Exception {

        if (userService.getUserByLogin(dto.getLogin()) != null)
            throw new LoginRegisteredException();
        else {
            if (userService.getUserByEmail(dto.getEmail()) != null)
                throw new EmailRegisteredException();
            else
                return ResponseEntity.ok().body(userService.register(dto));
        }
    }

    @PostMapping("/create-stuff")
    public ResponseEntity<?> createStuff(@RequestBody RegisterDTO newEntity) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.createStuff(newEntity));
    }


    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.deleteSoft(id);
    }

    @PostMapping("/remember-password")
    public ResponseEntity<?> rememberPassword(@RequestBody UserDTO userDTO) {//адрес электронной почты
        userDTO = userService.getUserByEmail(userDTO.getEmail());
        if (Objects.isNull(userDTO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким адресом электронной почты не зарегистрирован");
        } else {
            userService.sendChangePasswordEmail(userDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Вышлено письмо с дальнейшими инструкциями для сброса пароля");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@PathParam(value = "uuid") String uuid, @RequestBody UserDTO userDTO) {
        userService.changePassword(uuid, userDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body("Пароль успешно изменен");
    }

    @PostMapping("/change-password/user")
    public ResponseEntity<?> changeUserPassword(@PathParam(value = "uuid") String uuid, @RequestBody UserDTO userDTO) {
        userService.changePassword(uuid, userDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body("Пароль успешно изменен");
    }

    @PostMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable Long id) {
        userService.restore(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getById(id));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDTO updatedUser) throws AuthException, EmailRegisteredException {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!id.equals(customUserDetails.getUserId())) {
            throw new AuthException(HttpStatus.FORBIDDEN + ": " + Errors.Users.USER_FORBIDDEN_ERROR);
        }
        UserDTO user = userService.getById(id);
        if (userService.checkEmailForRegistration(updatedUser.getEmail())) {
            user.setName(updatedUser.getName());
            user.setSurName(updatedUser.getSurName());
            user.setPatronim(updatedUser.getPatronim());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());

        } else {
            throw new EmailRegisteredException();
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(user));
    }

}

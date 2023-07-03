package ru.ecommerce.highstylewear.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ecommerce.highstylewear.constants.Errors;
import ru.ecommerce.highstylewear.constants.MailConstants;
import ru.ecommerce.highstylewear.dto.RegisterDTO;
import ru.ecommerce.highstylewear.dto.RoleDTO;
import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.mapper.GenericMapper;
import ru.ecommerce.highstylewear.model.User;
import ru.ecommerce.highstylewear.repository.GenericRepository;
import ru.ecommerce.highstylewear.repository.RoleRepository;
import ru.ecommerce.highstylewear.repository.UserRepository;
import ru.ecommerce.highstylewear.service.userdetails.CustomUserDetails;
import ru.ecommerce.highstylewear.utils.MailUtils;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class UserService extends GenericService<User, UserDTO> {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JavaMailSender javaMailSender;

    private final RoleRepository roleRepository;

    public UserService(GenericRepository<User> repository, GenericMapper<User, UserDTO> mapper, BCryptPasswordEncoder bCryptPasswordEncoder, JavaMailSender javaMailSender, RoleRepository roleRepository) {
        super(repository, mapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTO create(UserDTO newObject) {

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        newObject.setRole(roleDTO);
        newObject.setCreatedWhen(LocalDateTime.now());

        return mapper.toDTO((repository).save(mapper.toEntity(newObject)));
    }


    public UserDTO getUserByLogin(final String login) {
        return mapper.toDTO(((UserRepository) repository).findUserByLogin(login));
    }

    public UserDTO getUserByEmail(final String email) {
        return mapper.toDTO(((UserRepository) repository).findUserByEmail(email));
    }

    public boolean checkPassword(String password, UserDetails foundUser) {
        return bCryptPasswordEncoder.matches(password, foundUser.getPassword());
    }

    public UserDTO register(RegisterDTO dto)  {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setSurName(dto.getSurName());
        user.setPatronim(dto.getPatronim());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setBirthDate(dto.getBirthDate());
        user.setAddress(dto.getAddress());
        user.setCreatedWhen(LocalDateTime.now());
        user.setRole(roleRepository.findRoleById(1L));
        user.setCreatedBy("REGISTRATION");

        return mapper.toDTO(repository.save((user)));
    }

    public UserDTO createStuff(RegisterDTO newObject) {
        User user = new User();
        user.setLogin(newObject.getLogin());
        user.setPassword(bCryptPasswordEncoder.encode(newObject.getPassword()));
        user.setName(newObject.getName());
        user.setSurName(newObject.getSurName());
        user.setPatronim(newObject.getPatronim());
        user.setEmail(newObject.getEmail());
        user.setPhone(newObject.getPhone());
        user.setBirthDate(newObject.getBirthDate());
        user.setAddress(newObject.getAddress());
        user.setCreatedWhen(LocalDateTime.now());
        user.setRole(roleRepository.findRoleById(2L));
        user.setCreatedBy("REGISTRATION by " + SecurityContextHolder.getContext().getAuthentication().getName());
        user.setIsDeleted(false);
        return mapper.toDTO((repository).save(user));
    }

    public void sendChangePasswordEmail(final UserDTO userDTO) {
        UUID uuid = UUID.randomUUID();
        userDTO.setChangePasswordToken(uuid.toString());
        update(userDTO);
        SimpleMailMessage mailMessage = MailUtils.crateMailMessage(userDTO.getEmail(),
                "djekadjonsov@yandex.ru",
                MailConstants.MAIL_SUBJECT_FOR_REMEMBER_PASSWORD,
                MailConstants.MAIL_MESSAGE_FOR_REMEMBER_PASSWORD + uuid);
        javaMailSender.send(mailMessage);
        log.info("---MAIL SEND---");
    }

    public void changePassword(final String uuid,
                               final String password) {
        UserDTO userDTO = mapper.toDTO(((UserRepository) repository).findUserByChangePasswordToken(uuid));
        userDTO.setChangePasswordToken(null);
        userDTO.setPassword(bCryptPasswordEncoder.encode(password));
        update(userDTO);
    }

    public boolean checkEmailForRegistration(String email){
        return !Objects.isNull(getUserByEmail(email));
    }

    public boolean checkForAccess(Long userId){
        return userId.equals(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
    }


}

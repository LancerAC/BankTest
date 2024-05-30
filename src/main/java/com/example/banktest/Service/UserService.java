package com.example.banktest.Service;

import com.example.banktest.Entity.BankAccount;
import com.example.banktest.Entity.User;
import com.example.banktest.Repository.UserRepository;
import com.example.banktest.Web.Dto.SearchCriteriaDTO;
import com.example.banktest.Web.Dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@Log4j
public class UserService implements UserDetailsService {

    private static final String PHONE_REGEX = "^(\\+7|8)\\d{10}$";

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;


    public void createUser(UserDTO userDto, BankAccount bankAccount) {

        Date date = Date.valueOf(userDto.getBirthDate());

        User user = User.builder()
                .userName(userDto.getUserName())
                .password(encoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .email(userDto.getEmail())
                .bankAccount(bankAccount)
                .firstName(userDto.getFirstName())
                .middleName(userDto.getMiddleName())
                .lastName(userDto.getLastName())
                .birthDate(date)
                .build();

        if (userRepository.findByUserName(userDto.getUserName()) != null
        || userRepository.findByPhoneNumber(userDto.getPhoneNumber()) != null
        || userRepository.findByEmail(userDto.getEmail()) != null
        || !validatePhoneNumber(userDto.getPhoneNumber())
        || !isValidIsoDate(userDto.getBirthDate())){
            log.debug("Can't create user");
            throw new RuntimeException();
        } else{
            userRepository.save(user);
            log.info("User created");
        }
    }

    private static boolean isValidIsoDate(String dateStr) {
        try {
            DateTimeFormatter.ISO_DATE.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean validatePhoneNumber(String phoneNumber) {
            Pattern pattern = Pattern.compile(PHONE_REGEX);
            Matcher matcher = pattern.matcher(phoneNumber);
            return matcher.matches();

    }

    public List<User> searchClients(SearchCriteriaDTO searchCriteria, Pageable pageable) {
        return userRepository.findBySearchCriteria(
                parseDate(searchCriteria.getBirthDate()).toString(),
                searchCriteria.getPhoneNumber(),
                searchCriteria.getFirstName() != null ? searchCriteria.getFirstName() + "%" : null,
                searchCriteria.getMiddleName() != null ? searchCriteria.getMiddleName() + "%" : null,
                searchCriteria.getLastName() != null ? searchCriteria.getLastName() + "%" : null,
                searchCriteria.getEmail(),
                pageable
        );
    }

    private static Date parseDate(LocalDate date) {
        if(date == null){
            return new Date(1, 01, 01);
        } else {
            return Date.valueOf(date);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}

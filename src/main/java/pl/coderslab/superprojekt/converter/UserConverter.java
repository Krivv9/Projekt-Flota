package pl.coderslab.superprojekt.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.coderslab.superprojekt.model.User;
import pl.coderslab.superprojekt.repository.UserRepository;

public class UserConverter implements Converter<String, User> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User convert(String source) {
        Long id = Long.parseLong(source);
        return userRepository.findUserById(id);
    }

}
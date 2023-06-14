package kg.kubatbekov.university_cms.service;

import kg.kubatbekov.university_cms.model.UserEntity;
import kg.kubatbekov.university_cms.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Repository
public class UserEntityDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserEntityDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("The user '%s' is not found!", username));
        }

        return new User(
                user.get().getUsername(),
                user.get().getPassword(),
                mapRolesToAuthorities(user.get().getRoles()));
    }

    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(String roles) {
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}

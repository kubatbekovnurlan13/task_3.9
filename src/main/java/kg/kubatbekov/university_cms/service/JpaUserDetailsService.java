package kg.kubatbekov.university_cms.service;

import kg.kubatbekov.university_cms.repository.UserRepository;
import kg.kubatbekov.university_cms.security.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository
                .findUserByUsername(username)
                .map(SecurityUser::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username not found: " + username));
    }
}

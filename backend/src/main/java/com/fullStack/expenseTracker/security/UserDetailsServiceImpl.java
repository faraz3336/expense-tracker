package com.fullStack.expenseTracker.security;

import com.fullStack.expenseTracker.models.User;
import com.fullStack.expenseTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by email={}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        log.info("User found email={} enabled={} rolesCount={}",
                user.getEmail(), user.isEnabled(), user.getRoles() == null ? 0 : user.getRoles().size());

        return UserDetailsImpl.build(user);
    }

}

package aleksa.radovic.TaskManagementSystem_BE.service;

import aleksa.radovic.TaskManagementSystem_BE.model.Role;
import aleksa.radovic.TaskManagementSystem_BE.model.User;
import aleksa.radovic.TaskManagementSystem_BE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getRole() == null){
            user.setRole(Role.ROLE_USER);
        }
        user.setEnabled(true);
        userRepository.save(user);
        return user;
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }else {
            return "failed login";
        }
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        return userRepository.save(existingUser);
    }
}

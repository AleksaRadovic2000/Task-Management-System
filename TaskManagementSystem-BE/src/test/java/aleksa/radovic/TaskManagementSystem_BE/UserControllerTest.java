package aleksa.radovic.TaskManagementSystem_BE;

import aleksa.radovic.TaskManagementSystem_BE.model.Role;
import aleksa.radovic.TaskManagementSystem_BE.model.User;
import aleksa.radovic.TaskManagementSystem_BE.repository.UserRepository;
import aleksa.radovic.TaskManagementSystem_BE.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    private String adminToken;
    private String userToken;

    private long updatedUserId;

    @BeforeAll
    public void setUp() {
        userRepository.deleteAll();
        User newUser = new User();
        newUser.setUsername("User1");
        newUser.setPassword("password1");
        newUser.setRole(Role.ROLE_USER);

        userRepository.save(newUser);

        User user = userRepository.findByUsername("User1");
        updatedUserId = user.getId();

        System.out.println(updatedUserId);

        adminToken = jwtService.generateToken("admin");
        userToken = jwtService.generateToken("user");
    }

    @Test
    @Order(1)
    public void testRegister() throws Exception {
        String jsonUser = """
                    {
                    "username": "user",
                    "password": "password"
                    }
                """;

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    @Order(2)
    public void testRegisterAdmin() throws Exception {
        String jsonUser = """
                    {
                    "username": "admin",
                    "password": "password",
                    "role" : "ROLE_ADMIN"
                    }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    @Order(3)
    public void testLogin() throws Exception {
        String jsonUser = """
                    {
                    "username": "user",
                    "password": "password"
                    }
                """;

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("eyJ")));

    }
    @Test
    public void testGetAllUsersAsAdmin() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(Matchers.greaterThanOrEqualTo(0))));
    }

    @Test
    public void testGetAllUsersAsUser() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateUserAsAdmin() throws Exception {
        String jsonUser = """
                    {
                    "username": "updatedUser",
                    "password": "updatedPassword"
                    }
                """;

        mockMvc.perform(put("/users/{id}", updatedUserId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    public void testUpdateUserAsUser() throws Exception {
        String jsonUser = """
                    {
                    "username": "user",
                    "password": "password"
                    }
                """;

        mockMvc.perform(put("/users/1")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isForbidden());
    }
}

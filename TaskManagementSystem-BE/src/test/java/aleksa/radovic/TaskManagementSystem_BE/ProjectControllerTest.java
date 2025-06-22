package aleksa.radovic.TaskManagementSystem_BE;


import aleksa.radovic.TaskManagementSystem_BE.model.Project;
import aleksa.radovic.TaskManagementSystem_BE.repository.ProjectRepository;
import aleksa.radovic.TaskManagementSystem_BE.service.JwtService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private JwtService jwtService;

    private String adminToken;
    private String userToken;

    private Long projectId;

    @BeforeAll
    public void setUp() {
        Project project = new Project();
        project.setName("Project 1");
        project.setDescription("description 1");
        project = projectRepository.save(project);

        projectId = project.getId();

        adminToken = jwtService.generateToken("admin");
        userToken = jwtService.generateToken("user");
    }

    @Test
    public void testCreateProjectAsAdmin() throws Exception {
        String jsonProject = """
                    {
                            "name": "New Project",
                            "description": "This is a description of the new project."
                          }
                """;

        mockMvc.perform(post("/projects")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProject))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    public void testCreateProjectWithoutToken() throws Exception {
        String jsonProject = """
                    {
                            "name": "New Project - without token",
                            "description": "This is a description of the new project."
                          }
                """;

        mockMvc.perform(post("/projects")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProject))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAllProjects() throws Exception {
        mockMvc.perform(get("/projects")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(Matchers.greaterThanOrEqualTo(0))));
    }

    @Test
    public void testUpdateProjectAsAdmin() throws Exception {
        String jsonProject = """
                    {
                            "name": "Updated Project",
                            "description": "Updated description"
                          }
                """;
        mockMvc.perform(put("/projects/{id}", projectId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProject))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project"));
    }

    @Test
    public void testUpdateProjectAsUser() throws Exception {
        String jsonProject = """
                    {
                            "name": "Updated Project",
                            "description": "Updated description"
                          }
                """;

        mockMvc.perform(put("/projects/{id}", projectId)
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProject))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteProjectAsUser() throws Exception {
        mockMvc.perform(delete("/projects/{id}", projectId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteProjectAsAdmin() throws Exception {
        mockMvc.perform(delete("/projects/{id}", projectId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }
}

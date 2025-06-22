package aleksa.radovic.TaskManagementSystem_BE;

import aleksa.radovic.TaskManagementSystem_BE.model.Task;
import aleksa.radovic.TaskManagementSystem_BE.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;
    private Long taskId;

    private String userToken;

    @BeforeAll
    public void setUp() throws Exception {
        userToken = jwtService.generateToken("user");
        String jsonTask = """
                {
                  "title": "Implement authentication",
                  "description": "Develop and integrate user authentication using Spring Security and JWT.",
                  "status": "IN_PROGRESS",
                  "priority": "HIGH"
                }
                                
                """;
        String response = mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTask))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pattern pattern = Pattern.compile("\"id\":(\\d+)");
        Matcher matcher = pattern.matcher(response);

        String id = "";
        if (matcher.find()) {
            id = matcher.group(1);
            System.out.println(id);
        }

        taskId = Long.valueOf(id);
    }


    @Test
    public void testCreateTask() throws Exception {
        String jsonTask = """
                {
                  "title": "Task1",
                  "description": "Description1",
                  "status": "IN_PROGRESS",
                  "priority": "HIGH"
                }
                                
                """;

        mockMvc.perform(post("/tasks")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTask))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task1"));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(Matchers.greaterThanOrEqualTo(1))));
    }

    @Test
    public void testGetAllTasksWithFilters() throws Exception {
        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + userToken)
                        .param("status", "IN_PROGRESS")
                        .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(Matchers.greaterThanOrEqualTo(1))));
    }

    @Test
    public void testUpdateTask() throws Exception {
        String jsonTask = """
                {
                  "title": "Updated Task",
                  "description": "Updated description",
                  "status": "COMPLETED",
                  "priority": "LOW"
                }
                                
                """;

        mockMvc.perform(put("/tasks/{id}", taskId)
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTask))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.priority").value("LOW"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", taskId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNoContent());
    }
}

package aleksa.radovic.TaskManagementSystem_BE.service;

import aleksa.radovic.TaskManagementSystem_BE.model.Project;
import aleksa.radovic.TaskManagementSystem_BE.model.User;
import aleksa.radovic.TaskManagementSystem_BE.repository.ProjectRepository;
import aleksa.radovic.TaskManagementSystem_BE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    public Project createProject(Project project, String token) {
        String username = jwtService.extractUserName(token);
        User currentUser = userRepository.findByUsername(username);
        project.setCreatedBy(currentUser);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project updateProject(Long id, Project project) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Project does not exist"));
        existingProject.setDescription(project.getDescription());
        existingProject.setName(project.getName());

        return projectRepository.save(existingProject);
    }

    public void deleteProject(Long id) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Project does not exist"));

        projectRepository.delete(existingProject);
    }
}

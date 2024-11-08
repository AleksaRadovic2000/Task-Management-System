INSERT INTO users (username, password, role, enabled)
VALUES
    ('admin', 'hashed_password_for_admin', 'ROLE_ADMIN', true),
    ('user', 'hashed_password_for_user', 'ROLE_USER', true);

INSERT INTO projects (name, description, created_by)
VALUES
    ('Projekt A', 'Opis projekta A', 1),
    ('Projekt B', 'Opis projekta B', 1);

INSERT INTO tasks (title, description, status, priority, assigned_to, project_id)
VALUES
    ('Task 1', 'Opis zadatka 1', 'NEW', 'HIGH', 2, 1),
    ('Task 2', 'Opis zadatka 2', 'IN_PROGRESS', 'MEDIUM', 2, 1),
    ('Task 3', 'Opis zadatka 3', 'COMPLETED', 'LOW', 2, 2);

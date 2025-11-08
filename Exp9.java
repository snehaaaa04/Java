public interface UserRepository extends JpaRepository<User, Long> { }
package com.example.springcrud.controller;

import com.example.springcrud.model.User;
import com.example.springcrud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository repo;

    // READ (Display all users)
    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listUsers", repo.findAll());
        return "index";
    }

    // CREATE (Show add form)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "add_user";
    }

    // SAVE new user
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        repo.save(user);
        return "redirect:/";
    }

    // UPDATE (Show edit form)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = repo.findById(id).get();
        model.addAttribute("user", user);
        return "edit_user";
    }

    // DELETE user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/";
    }
}
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>User List</title>
</head>
<body>
    <h2>User Management System</h2>
    <a href="/add">Add New User</a>
    <table border="1" cellpadding="10">
        <tr>
            <th>ID</th><th>Name</th><th>Email</th><th>City</th><th>Actions</th>
        </tr>
        <tr th:each="user : ${listUsers}">
            <td th:text="${user.id}"></td>
            <td th:text="${user.name}"></td>
            <td th:text="${user.email}"></td>
            <td th:text="${user.city}"></td>
            <td>
                <a th:href="@{/edit/{id}(id=${user.id})}">Edit</a> |
                <a th:href="@{/delete/{id}(id=${user.id})}">Delete</a>
            </td>
        </tr>
    </table>
</body>
</html>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Add User</title>
</head>
<body>
    <h2>Add New User</h2>
    <form th:action="@{/save}" th:object="${user}" method="post">
        Name: <input type="text" th:field="*{name}" required/><br>
        Email: <input type="email" th:field="*{email}" required/><br>
        City: <input type="text" th:field="*{city}" required/><br>
        <button type="submit">Save</button>
    </form>
</body>
</html>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Edit User</title>
</head>
<body>
    <h2>Edit User</h2>
    <form th:action="@{/save}" th:object="${user}" method="post">
        <input type="hidden" th:field="*{id}"/>
        Name: <input type="text" th:field="*{name}" required/><br>
        Email: <input type="email" th:field="*{email}" required/><br>
        City: <input type="text" th:field="*{city}" required/><br>
        <button type="submit">Update</button>
    </form>
</body>
</html>
package com.example.springcrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCrudAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCrudAppApplication.class, args);
    }
}

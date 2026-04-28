package com.todo.app.controller;

// REMOVED: Spring @Controller with Model + Thymeleaf view names
// Reason: quarkus-spring-web does NOT support plain @Controller — only @RestController.
// The view-rendering endpoints (returning HTML via Thymeleaf + Model.addAttribute) are
// migrated to Quarkus REST + Qute @CheckedTemplate (see below).
// The JSON REST endpoints (@ResponseBody + @RequestBody) are kept as @RestController-style
// JAX-RS endpoints.

import java.net.URI;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.springframework.data.domain.Page;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import com.todo.app.entity.Task;
import com.todo.app.service.TaskService;

@Path("/")
public class TaskController {

    @Inject
    TaskService taskService;

    // Qute checked templates — files must live under
    // src/main/resources/templates/TaskController/{name}.html
    @CheckedTemplate(requireTypeSafeExpressions = false)
    public static class Templates {
        public static native TemplateInstance home(
                Task task,
                boolean noTasks,
                int currentPage,
                int totalPages,
                long totalItems,
                List<Task> tasks);

        public static native TemplateInstance error();
    }

    @GET
    @Path("/error")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance viewErrorPage() {
        return Templates.error();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response viewIndexPage() {
        return Response.seeOther(URI.create("/home")).build();
    }

    @GET
    @Path("/home")
    @Produces(MediaType.TEXT_HTML)
    public Object viewHome() {
        return findPaginated(1);
    }

    @GET
    @Path("/home/{pageNo}")
    @Produces(MediaType.TEXT_HTML)
    public Object findPaginated(@PathParam("pageNo") int pageNo) {
        int pageSize = 6;

        Page<Task> page = taskService.getAllTasksPage(pageNo, pageSize);
        List<Task> tasks = page.getContent();

        if (tasks.isEmpty()) {
            if (pageNo > 1) {
                return Response.seeOther(URI.create("/home/" + (pageNo - 1))).build();
            }
            return Templates.home(new Task(), true, 1, 0, 0, List.of());
        }

        if (pageNo > page.getTotalPages()) {
            return Response.seeOther(URI.create("/home/" + page.getTotalPages())).build();
        }
        if (pageNo < 1) {
            return Response.seeOther(URI.create("/home")).build();
        }

        return Templates.home(
                new Task(),
                false,
                pageNo,
                page.getTotalPages(),
                page.getTotalElements(),
                tasks);
    }

    // Create task using AJAX request (JSON in / JSON out)
    @POST
    @Path("/home")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTask(Task task) {
        taskService.addTask(task);
        List<Task> tasks = taskService.getAllTasks();
        return Response.ok(tasks).build();
    }

    // Delete task using AJAX request
    @DELETE
    @Path("/home/{taskId}")
    public Response deleteTask(@PathParam("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return Response.ok().build();
    }

}

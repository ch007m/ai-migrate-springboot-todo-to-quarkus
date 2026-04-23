package com.todo.app.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.todo.app.entity.Task;
import com.todo.app.service.TaskService;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Controller
public class TaskController {

    @Inject
    TaskService taskService;

    @CheckedTemplate(requireTypeSafeExpressions = false)
    public static class Templates {
        public static native TemplateInstance home(Task task, List<Task> tasks, boolean noTasks, int currentPage,
                int totalPages, long totalItems, List<Integer> pageNumbers);

        public static native TemplateInstance error();
    }

    @GET
    @Path("error")
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
    @Path("home")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance viewHome() {
        return renderPage(1);
    }

    @GET
    @Path("home/{pageNo}")
    @Produces(MediaType.TEXT_HTML)
    public Object findPaginated(@PathParam("pageNo") int pageNo) {
        if (pageNo < 1) {
            return Response.seeOther(URI.create("/home")).build();
        }

        int pageSize = 6;
        Page<Task> page = taskService.getAllTasksPage(pageNo, pageSize);
        List<Task> tasks = page.getContent();

        if (tasks.isEmpty()) {
            if (pageNo > 1) {
                return Response.seeOther(URI.create("/home/" + (pageNo - 1))).build();
            }
            return Templates.home(new Task(), List.of(), true, 1, 0, 0, List.of());
        }

        if (pageNo > page.getTotalPages()) {
            return Response.seeOther(URI.create("/home/" + page.getTotalPages())).build();
        }

        return Templates.home(
                new Task(),
                tasks,
                false,
                pageNo,
                page.getTotalPages(),
                page.getTotalElements(),
                IntStream.rangeClosed(1, page.getTotalPages()).boxed().collect(Collectors.toList()));
    }

    @POST
    @Path("home")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTask(Task task) {
        taskService.addTask(task);
        List<Task> tasks = taskService.getAllTasks();
        return Response.ok(tasks).build();
    }

    @DELETE
    @Path("home/{taskId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTask(@PathParam("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return Response.ok().build();
    }

    private TemplateInstance renderPage(int pageNo) {
        Object result = findPaginated(pageNo);
        if (result instanceof TemplateInstance templateInstance) {
            return templateInstance;
        }
        return Templates.home(new Task(), List.of(), true, 1, 0, 0, List.of());
    }
}

// Made with Bob

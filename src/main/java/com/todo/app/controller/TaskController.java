package com.todo.app.controller;

import java.net.URI;
import java.util.List;

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
public class TaskController {

    @Inject
    TaskService taskService;

    @CheckedTemplate(requireTypeSafeExpressions = false)
    public static class Templates {
        public static native TemplateInstance home(Task task, List<Task> tasks,
                boolean noTasks, int currentPage, int totalPages, long totalItems);
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
        return findPaginated(1);
    }

    @GET
    @Path("home/{pageNo}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance findPaginated(@PathParam("pageNo") int pageNo) {
        int pageSize = 6;
        long totalItems = taskService.countTasks();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        if (totalItems == 0) {
            return Templates.home(new Task(), List.of(), true, 1, 0, 0);
        }

        if (pageNo > totalPages) {
            pageNo = totalPages;
        }
        if (pageNo < 1) {
            pageNo = 1;
        }

        List<Task> tasks = taskService.getTasksPage(pageNo, pageSize);
        return Templates.home(new Task(), tasks, false, pageNo, totalPages, totalItems);
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
    public Response deleteTask(@PathParam("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return Response.ok().build();
    }
}

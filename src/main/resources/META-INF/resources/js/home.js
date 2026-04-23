$(document).ready(function () {
  $("#taskForm").submit(function (event) {
    event.preventDefault();

    const title = $("#title").val();
    const description = $("#description").val();
    const dueDate = $("#dueDate").val();

    $.ajax({
      type: "POST",
      url: "/home",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify({ title: title, description: description, dueDate: dueDate }),
      success: function () {
        $.ajax({
          type: "GET",
          url: window.location.href,
          contentType: "text/html; charset=UTF-8",
          success: function (response) {
            confirm("Task created successfully!");
            $("body").html(response);
          },
          error: function (error) {
            console.error("Error getting tasks: " + error.responseText);
          }
        });
      },
      error: function (error) {
        console.error("Error creating task: " + error.responseText);
      }
    });

    $("#title").val("");
    $("#description").val("");
    $("#dueDate").val("");
  });
});

// Made with Bob

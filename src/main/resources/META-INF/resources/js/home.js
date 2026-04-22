$(document).ready(function () {
  // Handle form submission
  $("#taskForm").submit(function (event) {
    event.preventDefault();

    // Get form data
    let title = $("#title").val();
    let description = $("#description").val();
    let dueDate = $("#dueDate").val();

    // Send a POST request to the server
    $.ajax({
      type: "POST",
      url: "/home",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify({ 'title': title, 'description': description, 'dueDate': dueDate }),
      success: function (response) {
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
        })
      },
      error: function (error) {
        console.error("Error creating task: " + error.responseText);
      }
    });

    // Clear form fields
    $("#title").val("");
    $("#description").val("");
    $("#dueDate").val("");
  });
});

function process() {
    $('#get').click(function () {
        $.ajax({
            type: "GET",
            cache: false,
            url: 'list',
            data: "",
            success: function (response) {
                var html = "<br>" +
                    "<h2>Results:</h2>" +
                    "<table border='2'>";
                $.each(response.data, function (i) {
                    html = html + "<tr>"
                        + "<td>" + response.data[i].firstName + "</td>"
                        + "<td>" + response.data[i].lastName + "</td>"
                        + "<td>" + response.data[i].email + "</td>"
                        + "</tr>";
                });
                html = html + "</table>";
                $('#container').html(html);
            }
        });
    });

    $('#post').click(function () {
        if (!$("#firstName").val() || !$("#lastName").val() || !$("#email").val()) {
            alert("Enter your data!");
        } else {
            $.ajax({
                type: "POST",
                cache: false,
                url: 'list',
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify({
                    firstName: $("#firstName").val(),
                    lastName: $("#lastName").val(),
                    email: $("#email").val()
                }),
                success: function (response) {
                    $('#get').click();
                }
            });
        }

    });
}
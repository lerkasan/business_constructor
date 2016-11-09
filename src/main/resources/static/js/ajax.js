function process() {

    $('#post').click(function () {
        if (!$("#email").val() || !$("#password").val() || !$("#passwordConfirm").val()) {
            alert("Enter your data!");
        } else {
            $.ajax({
                       type: "POST",
                       cache: false,
                       url: 'register',
                       dataType: "json",
                       contentType: 'application/json',
                       data: JSON.stringify({
                                                email: $("#email").val(),
                                                password: $("#password").val(),
                                                passwordConfirm: $("#passwordConfirm").val()
                                            }),
                       beforeSend: function (xhr, settings) {
                           if (settings.type == 'POST' || settings.type == 'PUT' || settings.type
                                                                                    == 'DELETE') {
                               function getCookie(name) {
                                   var cookieValue = null;
                                   if (document.cookie && document.cookie != '') {
                                       var cookies = document.cookie.split(';');
                                       for (var i = 0; i < cookies.length; i++) {
                                           var cookie = jQuery.trim(cookies[i]);
                                           // Does this cookie string begin with the name we want?
                                           if (cookie.substring(0, name.length + 1) == (name
                                                                                        + '=')) {
                                               cookieValue =
                                                   decodeURIComponent(
                                                       cookie.substring(name.length + 1));
                                               break;
                                           }
                                       }
                                   }
                                   return cookieValue;
                               }

                               if (!(/^http:.*/.test(settings.url) || /^https:.*/.test(
                                       settings.url))) {
                                   // Only send the token to relative URLs i.e. locally.
                                   xhr.setRequestHeader("X-XSRF-TOKEN", getCookie('XSRF-TOKEN'));
                               }
                           }
                       },
                       success: function (response) {
                           if (response.status == 201) {
                               alert("You’re successfully registered");
                           }
                           if (response.status == 400) {
                               alert(response.data.errors.defaultmessage);
                           }
                       }
                   });
        }

    });
}
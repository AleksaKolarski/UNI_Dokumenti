$(document).ready(function(e){
  localStorage.removeItem('jwt');

  $('#id_page_title').text(translation('Login'));

  var login_username;
  var login_password;
  var login_dugme;
  var login_log;

  $('#id_p_login').text(translation('Login'));
  $('#id_p_username').text(translation('Username:'));
  $('#id_p_password').text(translation('Password:'));
  $('#id_login_register').text(translation('Register'));

  login_username = $("#id_input_username");
  login_password = $("#id_input_password");
  login_dugme = $("#id_button_login");
  login_log = $("#id_login_log_field");

  login_dugme.text(translation('Login'));

  add_validation_text(login_username, 5, 10);
  add_validation_text(login_password, 5, 20);

  login_dugme.on('click', function(event){
    var username = login_username.val();
    var password = login_password.val();

    customAjax({
      method: "POST",
      url: 'auth/login',
      data: {'username': username, 'password': password},
      success: function(jwt, status, xhr){
        if(xhr.status == 200){
            // dobar login, idi na pocetnu stranu
            localStorage.setItem('jwt', jwt);
            window.location.href = "/index.html";
        }
      },
      error: function(xhr, status, error){
        login_log.text(translation('Wrong credentials'));
      }
    });
  });
});
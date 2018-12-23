$(document).ready(function(e){
  localStorage.removeItem('jwt');

  var login_username;
  var login_password;
  var login_dugme;
  var login_log;

  login_username = $("#id_input_username");
  login_password = $("#id_input_password");
  login_dugme = $("#id_button_login");
  login_log = $("#id_login_log_field");

  add_validation_text(login_username, 5, 10);
  add_validation_text(login_password, 5, 10);

  $(login_dugme).on('click', function(event){
    var username = login_username.val();
    var password = login_password.val();
    //$.post('auth/login', {'username': username, 'password': password}, function(jwt, status){
    customAjax("POST", 'auth/login', {'username': username, 'password': password}, function(jwt, status, xhr){
      if(xhr.status == 200){
          // dobar login, idi na pocetnu stranu
          localStorage.setItem('jwt', jwt);
          window.location.href = "/index.html";
      }
    }, function(xhr, status){
      console.log("Server returned " + xhr.status + "; status is " + status);
      login_log.text("Pogresni podaci");
    });
  });
});
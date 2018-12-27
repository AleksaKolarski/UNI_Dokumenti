$(document).ready(function(e){
  localStorage.removeItem("jwt");

  var register_firstname;
  var register_lastname;
  var register_username;
  var register_password1;
  var register_password2;
  var register_dugme;
  var register_log;

  register_firstname = $("#id_input_firstname");
  register_lastname = $("#id_input_lastname");
  register_username = $("#id_input_username");
  register_password1 = $("#id_input_password1");
  register_password2 = $("#id_input_password2");
  register_dugme = $("#id_button_register");
  register_log = $("#id_register_log_field");

  add_validation_text(register_firstname, 5, 30);
  add_validation_text(register_lastname, 5, 30);
  add_validation_text(register_username, 5, 10);
  add_validation_password_match(register_password1, register_password2, 5, 20);

  $(register_dugme).on('click', function(event){
    var firstname = register_firstname.val();
    var lastname = register_lastname.val();
    var username = register_username.val();
    var password1 = register_password1.val();
    var password2 = register_password2.val();

    if(check_text(register_firstname, 5, 30)){
      if(check_text(register_lastname, 5, 30)){
        if(check_text(register_username, 5, 10)){
          if(check_password_match(register_password1, register_password2, 5, 20)){
            $.ajax({
              type: "POST", 
              data: JSON.stringify({'firstname': firstname, 'lastname': lastname, 'username': username, 'password': password1}),
              url: "user/register",
              processData: false,
              contentType: "application/json"
            });
            /*
            customAjax("POST", 'user/register', JSON.stringify({'firstname': firstname, 'lastname': lastname,'username': username,'password': password1}), function(data, status, xhr){
              // success
              if(xhr.status == 201){
                window.location.href = "/login.html";
              }
            }, function(xhr, status){
              register_log.text("Greska pri registraciji");
            });
            */
          }
          else
            register_log.text("Obe lozinke se moraju poklapati i biti odgovarajuce duzine (5 - 20)");
        }
        else
          register_log.text("Korisnicko ime nije odgovarajuce duzine (5 - 10)");
      }
      else
        register_log.text("Prezime nije odgovarajuce duzine (5 - 30)");
    }
    else
      register_log.text("Ime nije odgovarajuce duzine (5 - 30)");
  });
});
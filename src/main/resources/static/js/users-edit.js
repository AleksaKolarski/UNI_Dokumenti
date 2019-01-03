

// pls uradi skroz opet al kako treba
// svaki tip menjanja mora imati svoj html zbog jednostavnosti
// ROLE_USER je uvek aktivirana, nema potrebe za checkbox


var changeType; // register/edit/delete/password
var changeRole; // admin/default (admin->users.html/default->login.html)
var userId;
var edit_user_firstname;
var edit_user_lastname;
var edit_user_username;
var edit_user_password1;
var edit_user_password2;
var edit_user_roles_div;
var edit_user_role_user;
var edit_user_role_admin;
var edit_user_dugme;
var edit_user_log;

$(document).ready(function (e) {
  var searchParams;

  searchParams = new URLSearchParams(window.location.search);
  changeType = searchParams.get('changeType');
  userId = searchParams.get('userId');
  changeRole = searchParams.get('changeRole');

  edit_user_firstname = $('#id_input_firstname');
  edit_user_lastname = $('#id_input_lastname');
  edit_user_username = $('#id_input_username');
  edit_user_password1 = $('#id_input_password1');
  edit_user_password2 = $('#id_input_password2');
  edit_user_roles_div = $('#id_div_roles');
  edit_user_role_user = $('#id_checkbox_role_user');
  edit_user_role_admin = $('#id_checkbox_role_admin');
  edit_user_dugme = $('#id_button_edit_user');
  edit_user_log = $('#id_edit_user_log_field');

  if (changeType == 'delete') {
    edit_user_firstname.prop('disabled', true);
    edit_user_lastname.prop('disabled', true);
    edit_user_username.prop('disabled', true);
    $('.class_checkbox_roles').prop('disabled', true);
    $('.class_div_input_password').hide();
  }
  else if (changeType == 'password') {
    $('.class_div_input').hide();
    edit_user_roles_div.hide();
    add_validation_password_match(edit_user_password1, edit_user_password2, 5, 20);
  }
  else {
    add_validation_text(edit_user_firstname, 5, 30);
    add_validation_text(edit_user_lastname, 5, 30);
    add_validation_text(edit_user_username, 5, 10);
    $('.class_div_input_password').hide();
  }

  if (changeType != 'register') {
    customAjax({
      method: 'GET',
      url: 'user/getById',
      data: { 'userId': userId },
      success: function (user, status, xhr) {
        if (user != null) {
          gui_show_user(user);
        }
      }
    });
  }

  if(changeType == 'edit' && changeRole == 'admin'){
    customAjax({
      method: 'GET',
      url: 'role/getAllByUserId',
      data: { 'userId': userId },
      success: function(roles, status, xhr){
        roles.forEach(role => {
          if(role.name == 'ROLE_USER'){
            edit_user_role_user.prop('checked', true);
          }
          if(role.name == 'ROLE_ADMIN'){
            edit_user_role_admin.prop('checked', true);
          }
        });
      }
    });
  }

  if(changeRole == 'default'){
    $('#id_a_cancel').attr("href", "login.html");
    edit_user_roles_div.hide();
  }
  else{
    $('#id_a_cancel').attr("href", "users.html");
  }

  initBasedOnChangeType(changeType);
});

function gui_show_user(user) {
  edit_user_firstname.val(user.firstname);
  edit_user_lastname.val(user.lastname);
  edit_user_username.val(user.username);
}

function initBasedOnChangeType(changeType) {
  var title;
  var p;

  title = $('title');
  p = $('#form_p');

  title.text('User ' + changeType);
  p.text('User ' + changeType);
  edit_user_dugme.html(changeType);

  edit_user_dugme.on('click', function (event) {
    var firstname = edit_user_firstname.val();
    var lastname = edit_user_lastname.val();
    var username = edit_user_username.val();
    var password = edit_user_password1.val();

    if (changeType == 'delete' || check_text(edit_user_firstname, 5, 30)) {
      if (changeType == 'delete' || check_text(edit_user_lastname, 5, 30)) {
        if (changeType == 'delete' || check_text(edit_user_username, 5, 10)) {
          if (changeType == 'delete' || check_password_match(edit_user_password1, edit_user_password2, 5, 20)) {
            var method;
            switch (changeType) {
              case 'register':
                method = 'POST';
                break;
              case 'edit':
              case 'password':
                method = 'PUT';
                break;
              case 'delete':
                method = 'DELETE';
                break;
            }
            customAjax({
              method: method,
              url: 'user/' + changeType,
              data: JSON.stringify({ 'id': userId, 'firstname': firstname, 'lastname': lastname, 'username': username, 'password': password }),
              processData: false,
              contentType: 'application/json',
              success: function (data, status, xhr) {
                if (changeType == 'register' && changeRole == 'default' || changeType == 'password' && changeRole == 'default') {
                  window.location.href = 'login.html';
                }
                else {
                  window.location.href = 'users.html';
                }
              },
              error: function (xhr, status, error) {
                if (xhr.status == 409) {
                  edit_user_log.text('Korisnik vec postoji');
                }
                else {
                  edit_user_log.text('Error when doing ' + changeType);
                }
              }
            });
          }
          else
            edit_user_log.text('Obe lozinke se moraju poklapati i biti odgovarajuce duzine (5 - 20)');
        }
        else
          edit_user_log.text('Korisnicko ime nije odgovarajuce duzine (5 - 10)');
      }
      else
        edit_user_log.text('Prezime nije odgovarajuce duzine (5 - 30)');
    }
    else
      edit_user_log.text('Ime nije odgovarajuce duzine (5 - 30)');
  });
}
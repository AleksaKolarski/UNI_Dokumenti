
var changeType; // register/edit/delete/password
var changeRole; // admin/user/visitor
var userId;
var edit_user_div_input_root;
var edit_user_form_p;
var edit_user_firstname;
var edit_user_lastname;
var edit_user_username;
var edit_user_password1;
var edit_user_password2;
var edit_user_role_admin;
var edit_user_dugme;
var edit_user_cancel;
var edit_user_log;

$(document).ready(function (e) {

  init_search_params();

  init_form();

});

function init_search_params() {
  var searchParams;
  searchParams = new URLSearchParams(window.location.search);
  changeType = searchParams.get('changeType');
  userId = searchParams.get('userId');
  changeRole = searchParams.get('changeRole');
}

function init_form() {
  edit_user_div_input_root = $('#id_div_input_fields');
  edit_user_form_p = $('#id_form_p');
  edit_user_dugme = $('#id_button_edit_user');
  edit_user_cancel = $('#id_a_cancel');
  edit_user_log = $('#id_edit_user_log_field');

  if (changeType == 'password') {
    init_form_password();
  }
  else if (changeType == 'edit') {
    init_form_edit();
  }
  else if (changeType == 'delete') {
    init_form_delete();
  }
  else if (changeType == 'register') {
    init_form_register();
  }
}

// CHANGE PASSWORD
function init_form_password() {
  edit_user_form_p.text('Change password');
  var html;
  html =  '<div class="class_div_input_password">' +
            '<p>Password:</p>' +
            '<input id="id_input_password1" type="password">' +
          '</div>' +
          '<div class="class_div_input_password">' +
            '<p>Repeat password:</p>' +
            '<input id="id_input_password2" type="password">' +
          '</div>';
  edit_user_div_input_root.html(html);
  edit_user_dugme.html('Confirm');
  edit_user_cancel.attr('href', ((changeRole == 'admin')?'users.html':'index.html'));
  

  edit_user_password1 = $('#id_input_password1');
  edit_user_password2 = $('#id_input_password2');

  add_validation_password_match(edit_user_password1, edit_user_password2, 5, 20);

  edit_user_dugme.on('click', function(event){
    if(check_password_match(edit_user_password1, edit_user_password2, 5, 20)){
      customAjax({
        method: 'PUT',
        url: 'user/change-password',
        data: {'userId': userId, 'password': edit_user_password1.val()},
        success: function(data, status, xhr){
          window.location.href = ((changeRole == 'admin')?'users.html':'login.html');
        },
        error: function(xhr, status, error){
          edit_user_log.text('Greska pri promeni sifre');
        }
      });
    }
    else
      edit_user_log.text('Obe lozinke se moraju poklapati i biti odgovarajuce duzine (5 - 20)');
  });
}

// EDIT USER
function init_form_edit(){
  edit_user_form_p.text('Edit user'); 
  var html;
  html =  '<div class="class_div_input">' + 
            '<p>Firstname:</p>' + 
            '<input id="id_input_firstname" type="text">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Lastname:</p>' + 
            '<input id="id_input_lastname" type="text">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Username:</p>' + 
            '<input id="id_input_username" type="text">' + 
          '</div>';
  if(changeRole == 'admin'){
    html += '<div class="class_div_input">' + 
              '<p>Admin</p>' + 
              '<input id="id_checkbox_role_admin" type="checkbox">' + 
            '</div>';    
  }
  edit_user_div_input_root.html(html);
  edit_user_dugme.html('Confirm');
  edit_user_cancel.attr('href', ((changeRole == 'admin')?'users.html':'index.html'));

  edit_user_firstname = $('#id_input_firstname');
  edit_user_lastname = $('#id_input_lastname');
  edit_user_username = $('#id_input_username');
  edit_user_role_admin = $('#id_checkbox_role_admin');

  add_validation_text(edit_user_firstname, 5, 30);
  add_validation_text(edit_user_lastname, 5, 30);
  add_validation_text(edit_user_username, 5, 10);

  form_disable(true);

  // get user to fill input fields
  customAjax({
    method: 'GET',
    url: 'user/getById',
    data: { 'userId': userId },
    success: function(user, status, xhr){
      edit_user_firstname.val(user.firstname);
      edit_user_lastname.val(user.lastname);
      edit_user_username.val(user.username);
      edit_user_role_admin.prop('checked', user.isAdmin);
      form_disable(false);
    }
  });

  edit_user_dugme.on('click', function(event){
    if(check_text(edit_user_firstname, 5, 30)){
      if(check_text(edit_user_lastname, 5, 30)){
        if(check_text(edit_user_username, 5, 10)){

          var firstname = edit_user_firstname.val();
          var lastname = edit_user_lastname.val();
          var username = edit_user_username.val();
          var isAdmin = edit_user_role_admin.is(':checked');

          customAjax({
            method: 'PUT',
            url: 'user/edit',
            data: JSON.stringify({  'id': userId, 
                                    'firstname': firstname, 
                                    'lastname': lastname, 
                                    'username': username, 
                                    'isAdmin':  isAdmin}),
            processData: false,
            contentType: 'application/json',
            success: function(data, status, xhr){
              window.location.href = ((changeRole == 'admin')?'users.html':'index.html');
            },
            error: function(xhr, status, error){
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
          edit_user_log.text('Korisnicko ime nije odgovarajuce duzine (5 - 10)');
      }
      else
        edit_user_log.text('Prezime nije odgovarajuce duzine (5 - 30)');
    }
    else
      edit_user_log.text('Ime nije odgovarajuce duzine (5 - 30)');
  });
}

// DELETE USER
function init_form_delete(){
  edit_user_form_p.text('Delete user');
  var html;
  html = '<div class="class_div_input">' + 
            '<p>Firstname:</p>' + 
            '<input id="id_input_firstname" type="text" disabled="true">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Lastname:</p>' + 
            '<input id="id_input_lastname" type="text" disabled="true">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Username:</p>' + 
            '<input id="id_input_username" type="text" disabled="true">' + 
          '</div>';
  if(changeRole == 'admin'){
    html += '<div class="class_div_input">' + 
              '<p>Admin</p>' + 
              '<input id="id_checkbox_role_admin" type="checkbox" disabled="true">' + 
            '</div>';
  }
  edit_user_div_input_root.html(html);
  edit_user_dugme.html('Confirm');
  edit_user_cancel.attr('href', ((changeRole == 'admin')?'users.html':'index.html'));

  edit_user_firstname = $('#id_input_firstname');
  edit_user_lastname = $('#id_input_lastname');
  edit_user_username = $('#id_input_username');
  edit_user_role_admin = $('#id_checkbox_role_admin');

  edit_user_dugme.attr('disabled', true);

  customAjax({
    method: 'GET',
    url: 'user/getById',
    data: { 'userId': userId },
    success: function(user, status, xhr){
      edit_user_firstname.val(user.firstname);
      edit_user_lastname.val(user.lastname);
      edit_user_username.val(user.username);
      edit_user_role_admin.prop('checked', user.isAdmin);
      edit_user_dugme.attr('disabled', false);
    }
  });

  edit_user_dugme.on('click', function(event){
    customAjax({
      method: 'DELETE',
      url: 'user/delete',
      data: { 'userId': userId },
      success: function(data, status, xhr){
        if(changeRole == 'admin'){
          window.location.href = 'users.html';
        }
        else{
          window.location.href = 'login.html';
        }
      },
      error: function(xhr, status, error){
        edit_user_log.text('Greska pri brisanju korisnika');
      }
    });
  });
}

// REGISTER USER
function init_form_register(){
  edit_user_form_p.text('Register user');
  var html;
  html = '<div class="class_div_input">' + 
            '<p>Firstname:</p>' + 
            '<input id="id_input_firstname" type="text">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Lastname:</p>' + 
            '<input id="id_input_lastname" type="text">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Username:</p>' + 
            '<input id="id_input_username" type="text"">' + 
          '</div>' + 
          '<div class="class_div_input_password">' +
            '<p>Password:</p>' +
            '<input id="id_input_password1" type="password">' +
          '</div>' +
          '<div class="class_div_input_password">' +
            '<p>Repeat password:</p>' +
            '<input id="id_input_password2" type="password">' +
          '</div>';
  if(changeRole == 'admin'){
    html += '<div class="class_div_input">' + 
              '<p>Admin</p>' + 
              '<input id="id_checkbox_role_admin" type="checkbox">' + 
            '</div>';
  }
  edit_user_div_input_root.html(html);
  edit_user_dugme.html('Register');
  edit_user_cancel.attr('href', ((changeRole == 'admin')?'users.html':'login.html'));

  edit_user_firstname = $('#id_input_firstname');
  edit_user_lastname = $('#id_input_lastname');
  edit_user_username = $('#id_input_username');
  edit_user_password1 = $('#id_input_password1');
  edit_user_password2 = $('#id_input_password2');
  edit_user_role_admin = $('#id_checkbox_role_admin');

  add_validation_text(edit_user_firstname, 5, 30);
  add_validation_text(edit_user_lastname, 5, 30);
  add_validation_text(edit_user_username, 5, 10);
  add_validation_password_match(edit_user_password1, edit_user_password2, 5, 20);

  edit_user_dugme.on('click', function(event){
    if(check_text(edit_user_firstname, 5, 30)){
      if(check_text(edit_user_lastname, 5, 30)){
        if(check_text(edit_user_username, 5, 10)){
          if(check_password_match(edit_user_password1, edit_user_password2, 5, 20)){
            var firstname = edit_user_firstname.val();
            var lastname = edit_user_lastname.val();
            var username = edit_user_username.val();
            var password = edit_user_password1.val();
            var isAdmin = edit_user_role_admin.is(':checked');
  
            customAjax({
              method: 'POST',
              url: 'user/register',
              data: JSON.stringify({ 'id': userId, 'firstname': firstname, 'lastname': lastname, 'username': username, 'password': password, 'isAdmin':  isAdmin}),
              processData: false,
              contentType: 'application/json',
              success: function(data, status, xhr){
                window.location.href = ((changeRole == 'admin')?'users.html':'login.html');
              },
              error: function(xhr, status, error){
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

function form_disable(state){
  edit_user_firstname.attr('disabled', state);
  edit_user_lastname.attr('disabled', state);
  edit_user_username.attr('disabled', state);
  edit_user_role_admin.attr('disabled', state);
  edit_user_dugme.attr('disabled', state);
}
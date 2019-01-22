/*
**
**
**
*/

$(document).ready(function (e) {
  var navigation;

  var categories;
  var books;
  var search;

  header = $('#id_header');

  header.append('<div id="id_header_div_language">' + 
                  '<a id="id_header_language_en" href="#"><p>English</p></a>' +
                  '<a id="id_header_language_rs" href="#"><p>Srpski</p></a>' +  
                '</div>');
  $('#id_header_language_en').on('click', function(event){
    translation_init_param('en');
  });

  $('#id_header_language_rs').on('click', function(event){
    translation_init_param('rs');
  });

  navigation = $('#id_header_navigation');

  $('#id_h3').text(translation('This service stores and enables the search of electronic documents.'));

  navigation.append('<button id="id_button_categories" type="button">' + translation('Categories') + '</button>');
  navigation.append('<button id="id_button_books" type="button">' + translation('Books') + '</button>');
  navigation.append('<button id="id_button_search" type="button">' + translation('Search') + '</button>');

  categories = $('#id_button_categories');
  books = $('#id_button_books');
  search = $('#id_button_search');

  categories.on('click', function(event){
    window.location.href = '/categories.html';
  });

  books.on('click', function(event){
    window.location.href = '/books.html';
  });

  search.on('click', function(event){
    window.location.href = '/search.html';
  })

  customAjax({
    method: 'GET',
    url: 'user/currentUser',
    success: function (user, status, xhr) {
      // ispisi negde ime ulogovanog korisnika
      $('#id_h5_logged_in_user').text(translation('Logged in user:') + user.firstname + ' ' + user.lastname + ' (' + user.username + ')');

      var users;
      var edit_profile;
      var edit_password;
      var delete_profile;
      var logout;

      if (user.isAdmin == true) {
        navigation.append('<button id="id_button_users" type="button">' + translation('Users') + '</button>');
        users = $('#id_button_users');
        users.on('click', function (event) {
          window.location.href = "/users.html";
        });
      }
      
      navigation.append('<button id="id_button_profile" type="button">' + translation('Edit profile') + '</button>');
      navigation.append('<button id="id_button_password" type="button">' + translation('Edit password') + '</button>');
      navigation.append('<button id="id_button_delete" type="button">' + translation('Delete profile') + '</button>');
      navigation.append('<button id="id_button_logout" type="button">' + translation('Logout') + '</button>');

      edit_profile = $('#id_button_profile');
      edit_password = $('#id_button_password');
      delete_profile = $('#id_button_delete');
      logout = $('#id_button_logout');      

      edit_profile.on('click', function(event){
        window.location.href = '/users-edit.html?changeType=edit&userId=' + user.id + '&changeRole=user';
      });

      edit_password.on('click', function(event){
        window.location.href = '/users-edit.html?changeType=password&userId=' + user.id + '&changeRole=user';
      });

      delete_profile.on('click', function(event){
        window.location.href = '/users-edit.html?changeType=delete&userId=' + user.id + '&changeRole=user';
      });

      logout.on('click', function (event) {
        localStorage.removeItem('jwt');
        window.location.href = '/login.html';
      });
    },
    error: function(){
      var login;
      var register;

      navigation.append('<button id="id_button_login" type="button">' + translation('Login') + '</button>');
      navigation.append('<button id="id_button_register" type="button">' + translation('Register') + '</button>');

      login = $('#id_button_login');
      register = $('#id_button_register');

      login.on('click', function(event){
        window.location.href = '/login.html';
      });

      register.on('click', function(event){
        window.location.href = '/users-edit.html?changeType=register&changeRole=visitor';
      });
    }
  });
});
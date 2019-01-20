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
  navigation = $('#id_header_navigation');

  navigation.append('<button id="id_button_categories" type="button">Kategorije</button>');
  navigation.append('<button id="id_button_books" type="button">Knjige</button>');
  navigation.append('<button id="id_button_search" type="button">Pretraga</button>');

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
      $('#id_h5_logged_in_user').text('Ulogovani korisnik: ' + user.firstname + ' ' + user.lastname + ' (' + user.username + ')');

      var users;
      var edit_profile;
      var edit_password;
      var delete_profile;
      var logout;

      if (user.isAdmin == true) {
        navigation.append('<button id="id_button_users" type="button">Korisnici</button>');
        users = $('#id_button_users');
        users.on('click', function (event) {
          window.location.href = "/users.html";
        });
      }
      
      navigation.append('<button id="id_button_profile" type="button">Edit profile</button>');
      navigation.append('<button id="id_button_password" type="button">Change password</button>');
      navigation.append('<button id="id_button_delete" type="button">Delete profile</button>');
      navigation.append('<button id="id_button_logout" type="button">Logout</button>');

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

      navigation.append('<button id="id_button_login" type="button">Login</button>');
      navigation.append('<button id="id_button_register" type="button">Register</button>');

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
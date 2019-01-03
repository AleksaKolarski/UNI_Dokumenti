/*
**
**
**
*/

$(document).ready(function (e) {
  var navigation;

  header = $('#id_header');
  navigation = $('#id_header_navigation');

  // ucitavamo trenutno ulogovanog korisnika
  customAjax({
    method: 'GET',
    url: 'user/currentUser',
    success: function (user, status, xhr) {

      // ucitavamo njegove role
      customAjax({
        method: 'GET',
        url: 'role/getAllByUserId',
        data: { 'userId': user.id },
        success: function (roles, status, xhr) {
          var categories;
          var books;
          var search;
          var users;
          var logout;

          navigation.append('<button id="id_button_categories" type="button">Kategorije</button>');
          navigation.append('<button id="id_button_books" type="button">Knjige</button>');
          navigation.append('<button id="id_button_search" type="button">Pretraga</button>');

          roles.forEach(role => {
            if (role.name == 'ROLE_ADMIN') {
              navigation.append('<button id="id_button_users" type="button">Korisnici</button>');
              users = $('#id_button_users');
              users.on('click', function (event) {
                window.location.href = "/users.html";
              });
            }
          });
          navigation.append('<button id="id_button_logout" type="button">Logout</button>');

          categories = $('#id_button_categories');
          books = $('#id_button_books');
          search = $('#id_button_search');
          logout = $('#id_button_logout');

          logout.on('click', function (event) {
            localStorage.removeItem("jwt");
            window.location.href = "/login.html";
          });
        }
      });
    }
  });
});
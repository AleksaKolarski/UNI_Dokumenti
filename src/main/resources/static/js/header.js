/*
**
**
**
*/

$(document).ready(function(e){
  var header;
  var navigation;

  header = $('#id_header');
  navigation = $('#id_header_navigation');

  customAjax("GET", "auth/current-user", null, function(data, status, xhr){
    console.log(data);
    data.roles.forEach(role => {
      if(role.name == 'ROLE_ADMIN'){
        navigation.append('<button type="button">Korisnici</button>');
      }
    });
  },
  function(xhr, status){
    
  });

});
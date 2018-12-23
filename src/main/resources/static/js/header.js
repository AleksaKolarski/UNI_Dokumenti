$(document).ready(function(e){
  var header;

  header = $('#id_header');

  customAjax("GET", "auth/current-user", null, function(data, status, xhr){
    console.log(data);
  },
  function(xhr, status){
    
  });

});
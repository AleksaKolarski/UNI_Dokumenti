/*
**
**
**
**
*/

$(document).ready(function (e) {

  $('#id_page_title').text(translation('Users'));

  var table;

  table = $('#id_table_users');

  customAjax({
    method: "GET",
    url: "user/all",
    success: function (data, status, xhr) {
      var html = '';
      html += '<tr id="id_tr_header">' + 
                '<th>ID</th>' + 
                '<th>'+ translation('Firstname') +'</th>' + 
                '<th>'+ translation('Lastname') +'</th>' + 
                '<th>'+ translation('Username') +'</th>' + 
                '<th>'+ translation('Admin') +'</th>' + 
                '<th>' + 
                  '<a href="users-edit.html?changeType=register&changeRole=admin">' + 
                    '<button type="button">'+ translation('Add new') +'</button>' + 
                  '</a>' + 
                '</th>' + 
              '</tr>';
      data.forEach(user => {
        html += '<tr class="class_tr_user">' + 
                  '<th>' + user.id + '</th>' + 
                  '<th>' + user.firstname + '</th>' + 
                  '<th>' + user.lastname + '</th>' + 
                  '<th>' + user.username + '</th>' + 
                  '<th>' + user.isAdmin + '</th>' + 
                  '<th>' +
                    '<a href="users-edit.html?changeType=password&userId=' + user.id + '&changeRole=admin"><button type="button">'+ translation('Password') +'</button></a>' +
                    '<a href="users-edit.html?changeType=edit&userId=' + user.id + '&changeRole=admin"><button type="button">'+ translation('Edit') +'</button></a>' +
                    '<a href="users-edit.html?changeType=delete&userId=' + user.id + '&changeRole=admin"><button type="button">'+ translation('Delete') +'</button></a>' +
                  '</th>' + 
                '</tr>';
      });
      table.append(html);
    }
  });
});
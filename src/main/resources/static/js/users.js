/*
**
**
**
**
*/

$(document).ready(function (e) {
  var table;

  table = $('#id_table_users');

  customAjax({
    method: "GET",
    url: "user/all",
    success: function (data, status, xhr) {
      var html = '';
      html += '<tr>' + 
                '<th>ID</th>' + 
                '<th>Firstname</th>' + 
                '<th>Lastname</th>' + 
                '<th>Username</th>' + 
                '<th>Admin</th>' + 
                '<th>' + 
                  '<a href="users-edit.html?changeType=register&changeRole=admin">' + 
                    '<button type="button">Add new user</button>' + 
                  '</a>' + 
                '</th>' + 
              '</tr>';
      data.forEach(user => {
        html += '<tr data-userId="' + user.id + '" class="class_tr_user">' + 
                  '<th>' + user.id + '</th>' + 
                  '<th>' + user.firstname + '</th>' + 
                  '<th>' + user.lastname + '</th>' + 
                  '<th>' + user.username + '</th>' + 
                  '<th>' + user.isAdmin + '</th>' + 
                  '<th>' +
                    '<a href="users-edit.html?changeType=password&userId=' + user.id + '&changeRole=admin"><button type="button">Password</button></a>' +
                    '<a href="users-edit.html?changeType=edit&userId=' + user.id + '&changeRole=admin"><button type="button">Edit</button></a>' +
                    '<a href="users-edit.html?changeType=delete&userId=' + user.id + '&changeRole=admin"><button type="button">Delete</button></a>' +
                  '</th>' + 
                '</tr>';
      });
      table.append(html);
    }
  });
});
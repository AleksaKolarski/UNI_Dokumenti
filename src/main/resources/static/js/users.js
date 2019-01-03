/*
**
**
**
**
*/

$(document).ready(function(e){
  var table;

  table = $('#id_table_users');

  customAjax({
    method: "GET",
    url: "user/all",
    success: function(data, status, xhr){
      data.forEach(user => {
        gui_add_div_user(table, user);
      });
    }
  });
});

function gui_add_div_user(where, user){
  var html = '';
  html += '<tr data-userId="' + user.id + '" class="class_tr_user">';
  html += '<th>' + user.id + '</th>';
  html += '<th>' + user.firstname + '</th>';
  html += '<th>' + user.lastname + '</th>';
  html += '<th>' + user.username + '</th>';
  html += '<th id="id_th_user_roles_' + user.id + '"></th>';
  html += '<th>' +
            '<a href="users-edit.html?changeType=password&userId=' + user.id + '&changeRole=admin"><button type="button">Password</button></a>' + 
            '<a href="users-edit.html?changeType=edit&userId=' + user.id + '&changeRole=admin"><button type="button">Edit</button></a>' + 
            '<a href="users-edit.html?changeType=delete&userId=' + user.id + '"><button type="button">Delete</button></a>' + 
          '</th>';
  html += '</tr>';
  where.append(html);

  var th_user_roles;
  th_user_roles = $('#id_th_user_roles_' + user.id);
  customAjax({
    method: 'GET',
    url: 'role/getAllByUserId',
    data: {'userId': user.id},
    success: function(roles, status, xhr){
      var roleText = '';
      roles.forEach(role => {
        roleText += role.name + ' ';
      });
      th_user_roles.html(roleText);
    }
  });
}
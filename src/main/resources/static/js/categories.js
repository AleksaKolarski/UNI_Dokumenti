
var admin = false;
var table;

$(document).ready(function (e) {
  
  table = $('#id_table_categories');

  customAjax({
    method: 'GET',
    url: 'role/current-user-roles',
    success: function(roles, status, xhr){
      roles.forEach(role => {
        if(role.name == 'ROLE_ADMIN'){
          admin = true;
        }
      });
    },
    complete: function(){
      render_table();
    }
  });
});

function render_table(){
  customAjax({
    method: 'GET',
    url: 'category/all',
    success: function (categories, status, xhr) {
      var html = '';
      html += '<tr id="id_tr_header">' + 
                '<th>ID</th>' + 
                '<th>Name</th>';
      if(admin){
        html += '<th>' + 
                  '<a href="categories-edit.html?changeType=create">' + 
                    '<button type="button">Add new category</button>' + 
                  '</a>' + 
                '</th>';
      }
      html += '</tr>';
      categories.forEach(category => {
        html += '<tr class="class_tr_category">' + 
                  '<th>' + category.id + '</th>' + 
                  '<th>' + category.name + '</th>';
        if(admin){
          html += '<th>' +
                    '<a href="categories-edit.html?changeType=edit&categoryId=' + category.id + '"><button type="button">Edit</button></a>' +
                    '<a href="categories-edit.html?changeType=delete&categoryId=' + category.id + '"><button type="button">Delete</button></a>' +
                  '</th>';
        } 
        html += '</tr>';
      });
      table.append(html);
    }
  });
}
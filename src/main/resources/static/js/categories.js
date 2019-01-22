
var admin = false;
var table;

$(document).ready(function (e) {

  $('#id_page_title').text(translation('Categories'));
  
  table = $('#id_table_categories');

  customAjax({
    method: 'GET',
    url: 'user/currentUser',
    success: function(user, status, xhr){
        admin = user.isAdmin;
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
                '<th>'+ translation('Name') +'</th>';
      if(admin){
        html += '<th>' + 
                  '<a href="categories-edit.html?changeType=create">' + 
                    '<button type="button">'+ translation('Add new category') +'</button>' + 
                  '</a>' + 
                '</th>';
      }
      html += '</tr>';
      categories.forEach(category => {
        html += '<tr class="class_tr_category">' + 
                  '<th>' + category.id + '</th>' + 
                  '<th>' + 
                    '<a href="books.html?filterCategory=' + category.name + '&sortDirection=ASC">' + 
                      category.name + 
                    '</a>' + 
                  '</th>';
        if(admin){
          html += '<th>' +
                    '<a href="categories-edit.html?changeType=edit&categoryId=' + category.id + '"><button type="button">'+ translation('Edit') +'</button></a>' +
                    '<a href="categories-edit.html?changeType=delete&categoryId=' + category.id + '"><button type="button">'+ translation('Delete') +'</button></a>' +
                  '</th>';
        } 
        html += '</tr>';
      });
      table.append(html);
    }
  });
}
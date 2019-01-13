
var admin = false;
var table;

$(document).ready(function (e) {
  
  table = $('#id_table_books');

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
    url: 'ebook/all',
    success: function (books, status, xhr) {
      var html = '';
      html += '<tr id="id_tr_header">' + 
                '<th>ID</th>' + 
                '<th>Title</th>' + 
                '<th>Author</th>' + 
                '<th>Keywords</th>' + 
                '<th>Publication year</th>' + 
                '<th>Document name</th>' + 
                '<th>Filename</th>' + 
                '<th>mime</th>' + 
                '<th>Language</th>' + 
                '<th>Category</th>' + 
                '<th>Uploader</th>';
      if(admin){
        html += '<th>' + 
                  '<a href="books-edit.html?changeType=create">' + 
                    '<button type="button">Add new book</button>' + 
                  '</a>' + 
                '</th>';
      }
      html += '</tr>';
      books.forEach(book => {
        html += '<tr class="class_tr_book">' + 
                  '<th>' + book.id + '</th>' + 
                  '<th>' + book.title + '</th>' + 
                  '<th>' + book.author + '</th>' + 
                  '<th>' + book.keywords + '</th>' + 
                  '<th>' + book.publicationYear + '</th>' + 
                  '<th>' + book.documentName + '</th>' + 
                  '<th>' + book.filename + '</th>' + 
                  '<th>' + book.mime + '</th>' + 
                  '<th>' + book.languageName + '</th>' + 
                  '<th>' + book.categoryName + '</th>' + 
                  '<th>' + book.uploaderUsername + '</th>';
        if(admin){
          html += '<th>' +
                    '<a href="books-edit.html?changeType=edit&ebookId=' + book.id + '"><button type="button">Edit</button></a>' +
                    '<a href="books-edit.html?changeType=delete&ebookId=' + book.id + '"><button type="button">Delete</button></a>' +
                  '</th>';
        } 
        html += '</tr>';
      });
      table.append(html);
    }
  });
}
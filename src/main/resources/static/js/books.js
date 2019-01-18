
var admin = false;
var table;

var filterCategory;
var sortDirection;

var link_sort_asc;
var link_sort_desc;

$(document).ready(function (e) {

  init_search_params();
  
  table = $('#id_table_books');

  link_sort_asc = $('#id_sort_asc');
  link_sort_desc = $('#id_sort_desc');

  link_sort_asc.on('click', function(event){
    window.location.href = 'books.html?filterCategory=' + (filterCategory?filterCategory:'') + '&sortDirection=ASC';
  });

  link_sort_desc.on('click', function(event){
    window.location.href = 'books.html?filterCategory=' + (filterCategory?filterCategory:'') + '&sortDirection=DESC';
  })

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

function init_search_params() {
  var searchParams;
  searchParams = new URLSearchParams(window.location.search);
  filterCategory = searchParams.get('filterCategory');
  sortDirection = searchParams.get('sortDirection');
}

function render_table(){
  customAjax({
    method: 'GET',
    url: 'ebook/all',
    data: { 'filterCategory': filterCategory, 'sortDirection': sortDirection }, 
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
                '<th>Uploader</th>' + 
                '<th>Download</th>';
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
                  '<th>' + book.uploaderUsername + '</th>' + 
                  '<th><a id="id_link_download_' + book.id + '" href="#">Download</a></th>';
        if(admin){
          html += '<th>' +
                    '<a href="books-edit.html?changeType=edit&ebookId=' + book.id + '"><button type="button">Edit</button></a>' +
                    '<a href="books-edit.html?changeType=delete&ebookId=' + book.id + '"><button type="button">Delete</button></a>' +
                  '</th>';
        } 
        html += '</tr>';
      });
      table.append(html);
      books.forEach(book => {
        $('#id_link_download_' + book.id).on('click', function(event){
          customAjax({
            method: 'GET',
            url: 'file/generate-token/' + book.documentName,
            success: function(token, status, xhr){
              window.location.href = 'file/download/' + token;
            }
          });
        });
      });
    }
  });
}

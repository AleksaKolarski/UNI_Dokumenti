var user;
var admin = false;
var table;

var filterCategory;
var sortDirection;

var link_sort_asc;
var link_sort_desc;

$(document).ready(function (e) {

  $('#id_page_title').text(translation('Books'));

  init_search_params();
  
  table = $('#id_table_books');

  $('#id_a_sort').text(translation('Sort'))
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
    url: 'user/currentUser',
    success: function(userDTO, status, xhr){
      admin = userDTO.isAdmin;
      user = userDTO;
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
                '<th>' + translation('Title') + '</th>' + 
                '<th>' + translation('Author') + '</th>' + 
                '<th>' + translation('Keywords') + '</th>' + 
                '<th>' + translation('Publication year') + '</th>' + 
                '<th>' + translation('Document name') + '</th>' + 
                '<th>' + translation('Filename') + '</th>' + 
                '<th>mime</th>' + 
                '<th>' + translation('Language') + '</th>' + 
                '<th>' + translation('Category') + '</th>' + 
                '<th>' + translation('Uploader') + '</th>' + 
                '<th>' + translation('Download') + '</th>';
      if(admin){
        html += '<th>' + 
                  '<a href="books-edit.html?changeType=create">' + 
                    '<button type="button">' + translation('Add new book') + '</button>' + 
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
        html += '<th>';
        if(user != null){
          if(book.categoryName == user.categoryName || admin == true || user.categoryName == null){
            html += '<a id="id_link_download_' + book.id + '" href="#">' + translation('Download') + '</a>';
          }
        }
        else{
          html += translation('Register first');
        }
        html += '</th>';
        if(admin){
          html += '<th>' +
                    '<a href="books-edit.html?changeType=edit&ebookId=' + book.id + '"><button type="button">' + translation('Edit') + '</button></a>' +
                    '<a href="books-edit.html?changeType=delete&ebookId=' + book.id + '"><button type="button">' + translation('Delete') + '</button></a>' +
                  '</th>';
        } 
        html += '</tr>';
      });
      table.append(html);
      books.forEach(book => {
        $('#id_link_download_' + book.id).on('click', function(event){
          customAjax({
            method: 'GET',
            url: 'file/generate-token/' + book.filename,
            success: function(token, status, xhr){
              window.location.href = 'file/download/' + token;
            }
          });
        });
      });
    }
  });
}





$(document).ready(function (e) {
  var table;

  table = $('#id_table_books');

  customAjax({
    method: "GET",
    url: "book/all",
    success: function (books, status, xhr) {
      var html = '';
      html += '<tr>' + 
                '<th>ID</th>' + 
                '<th>Title</th>' + 
                '<th>Author</th>' + 
                '<th>Keywords</th>' + 
                '<th>Publication year</th>' + 
                '<th>Filename</th>' + 
                '<th>mime</th>' + 
                '<th>Language</th>' + 
                '<th>Category</th>' + 
                '<th>Uploader</th>' + 
                '<th>' + 
                  '<a href="books-edit.html?">' + 
                    '<button type="button">Add new book</button>' + 
                  '</a>' + 
                '</th>' + 
              '</tr>';
        books.forEach(book => {
        html += '<tr data-bookId="' + book.id + '" class="class_tr_book">' + 
                  '<th>' + book.id + '</th>' + 
                  '<th>' + book.title + '</th>' + 
                  '<th>' + book.author + '</th>' + 
                  '<th>' + book.keywords + '</th>' + 
                  '<th>' + book.publicationYear + '</th>' + 
                  '<th>' + book.filename + '</th>' + 
                  '<th>' + book.mime + '</th>' + 
                  '<th>' + book.languageName + '</th>' + 
                  '<th>' + book.categoryName + '</th>' + 
                  '<th>' + book.uploaderUsername + '</th>' + 
                  '<th>' +
                    '<a href="books-edit.html?"><button type="button">Edit</button></a>' +
                    '<a href="books-edit.html?"><button type="button">Delete</button></a>' +
                  '</th>' + 
                '</tr>';
      });
      table.append(html);
    }
  });
});
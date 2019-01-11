
var changeType; // create/edit/delete
var bookId;
var edit_book_div_input_root;
var edit_book_form_p;

var edit_book_choose_upload;
var edit_book_button_upload;
var edit_book_title;
var edit_book_author;
var edit_book_keywords;
var edit_book_publication_year;
var edit_book_language;
var edit_book_category;

var edit_book_dugme;
var edit_book_cancel;
var edit_book_log;

$(document).ready(function (e) {

  init_search_params();

  init_form();

});

function init_search_params() {
  var searchParams;
  searchParams = new URLSearchParams(window.location.search);
  changeType = searchParams.get('changeType');
  bookId = searchParams.get('bookId');
}

function init_form() {
  edit_book_div_input_root = $('#id_div_input_fields');
  edit_book_form_p = $('#id_form_p');
  edit_book_dugme = $('#id_button_edit_book');
  edit_book_cancel = $('#id_a_cancel');
  edit_book_log = $('#id_edit_book_log_field');

  if (changeType == 'create') {
    init_form_create();
  }
  else if (changeType == 'edit') {
    init_form_edit();
  }
  else if (changeType == 'delete') {
    init_form_delete();
  }
}

function init_form_create(){
  edit_book_form_p.text('Upload new book');
  var html;
  html =  '<div id="id_div_file_chooser">' + 
            '<input id="id_button_file_chooser" type="file" name="doc" value="Choose file"/>' + 
            '<div>' + 
              '<input id="id_button_upload" type="button" value="Upload">' +
              '<img id="id_gif_loading" src="loading.gif">' +  
            '</div>' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Title:</p>' + 
            '<input id="id_input_title" type="text">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Author:</p>' + 
            '<input id="id_input_author" type="text">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Keywords:</p>' + 
            '<input id="id_input_keywords" type="text">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Publication year:</p>' + 
            '<input id="id_input_publication_year" type="number" min="0" max="2099" step="1" value="2019">' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Language:</p>' + 
            '<select id="id_input_language">' + 
            '</select>' + 
          '</div>' + 
          '<div class="class_div_input">' + 
            '<p>Category:</p>' + 
            '<select id="id_input_category">' + 
            '</select>' + 
          '</div>';
  edit_book_div_input_root.html(html);
  edit_book_dugme.html('Confirm');

  edit_book_choose_upload = $('#id_button_file_chooser');
  edit_book_button_upload = $('#id_button_upload');

  edit_book_title = $('#id_input_title');
  edit_book_author = $('#id_input_author');
  edit_book_keywords = $('#id_input_keywords');
  edit_book_publication_year = $('#id_input_publication_year');
  edit_book_language = $('#id_input_language');
  edit_book_category = $('#id_input_category');

  form_disable(true);

  add_validation_text(edit_book_title, 5, 80);
  add_validation_text(edit_book_author, 5, 120);
  add_validation_text(edit_book_keywords, 0, 120);
  add_validation_number(edit_book_publication_year, 0, 2099);

  // dobavljanje svih jezika
  customAjax({
    method: 'GET',
    url: 'language/all',
    success: function(languages, status, xhr){
      languages.forEach(language => {
        edit_book_language.append('<option value="' + language.name + '">' + language.name + '</option>');
      });
    }
  });

  // dobavljanje svih kategorija
  customAjax({
    method: 'GET',
    url: 'category/all',
    success: function(categories, status, xhr){
      categories.forEach(category => {
        edit_book_category.append('<option value="' + category.name + '">' + category.name + '</option>');
      });
    }
  });

  edit_book_button_upload.on('click', function(event){
    var file = edit_book_choose_upload.prop('files')[0];
    var data = new FormData();
    data.append('doc', file);
    console.log(file);
    customAjax({
      method: 'POST',
      url: 'upload/',
      data: data,
      cache: false, 
      contentType: false, 
      processData: false, 
      success: function(data, status, xhr){
        console.log('Uspesno poslat fajl');
      }
    });
  });
}

function init_form_edit(){

}

function init_form_delete(){

}

function form_disable(state){
  edit_book_title.attr("disabled", state);
  edit_book_author.attr("disabled", state);
  edit_book_keywords.attr("disabled", state);
  edit_book_publication_year.attr("disabled", state);
  edit_book_language.attr("disabled", state);
  edit_book_category.attr("disabled", state);
  edit_book_dugme.attr("disabled", state);
}
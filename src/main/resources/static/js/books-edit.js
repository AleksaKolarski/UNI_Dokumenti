
var changeType; // create/edit/delete
var bookId;
var edit_book_div_input_root;
var edit_book_form_p;

var edit_book_choose_upload;
var edit_book_button_upload;
var edit_book_gif;
var edit_book_title;
var edit_book_author;
var edit_book_keywords;
var edit_book_publication_year;
var edit_book_language;
var edit_book_category;

var book_filename;
var book_documentName;
var book_mime;

var edit_book_dugme;
var edit_book_log;
var edit_book_cancel;

var form_disable_counter = 0;
var load_book_counter = 0;

var html_template =   '<div class="class_div_input">' + 
                        '<p>' + translation('Title') + ':</p>' + 
                        '<input id="id_input_title" type="text">' + 
                      '</div>' + 
                      '<div class="class_div_input">' + 
                        '<p>' + translation('Author') + ':</p>' + 
                        '<input id="id_input_author" type="text">' + 
                      '</div>' + 
                      '<div class="class_div_input">' + 
                        '<p>' + translation('Keywords') + ':</p>' + 
                        '<input id="id_input_keywords" type="text">' + 
                      '</div>' + 
                      '<div class="class_div_input">' + 
                        '<p>' + translation('Publication year') + ':</p>' + 
                        '<input id="id_input_publication_year" type="number" min="0" max="2099" step="1" value="2019">' + 
                      '</div>' + 
                      '<div class="class_div_input">' + 
                        '<p>' + translation('Language') + ':</p>' + 
                        '<select id="id_input_language">' + 
                        '</select>' + 
                      '</div>' + 
                      '<div class="class_div_input">' + 
                        '<p>' + translation('Category') + ':</p>' + 
                        '<select id="id_input_category">' + 
                        '</select>' + 
                      '</div>';

$(document).ready(function (e) {

  init_search_params();

  init_form();

});

function init_search_params() {
  var searchParams;
  searchParams = new URLSearchParams(window.location.search);
  changeType = searchParams.get('changeType');
  bookId = searchParams.get('ebookId');
}

function init_form() {
  edit_book_div_input_root = $('#id_div_input_fields');
  edit_book_form_p = $('#id_form_p');
  edit_book_dugme = $('#id_button_edit_book');
  edit_book_log = $('#id_edit_book_log_field');
  edit_book_cancel = $('#id_a_cancel');

  edit_book_cancel.html(translation('Cancel'));

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
  $('#id_page_title').text(translation('Upload new book'));
  edit_book_form_p.text(translation('Upload new book'));
  var html;
  html =  '<div id="id_div_file_chooser">' + 
            '<input id="id_button_file_chooser" type="file" name="doc"/>' + 
            '<div>' + 
              '<input id="id_button_upload" type="button" value="' + translation('Upload') + '">' +
              '<img id="id_gif_loading" src="img/loading.gif" style="visibility: hidden;">' +  
            '</div>' + 
          '</div>' + 
          html_template;
  edit_book_div_input_root.html(html);
  edit_book_dugme.html(translation('Confirm'));

  edit_book_choose_upload = $('#id_button_file_chooser');
  edit_book_button_upload = $('#id_button_upload');
  edit_book_gif = $('#id_gif_loading');

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
      form_disable(false);
    },
    error: function(xhr, status, error){
      edit_book_log.text(translate('Error while getting all languages'));
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
      form_disable(false);
    },
    error: function(xhr, status, error){
      edit_book_log.text(translation('Error while getting all categories'));
    }
  });

  edit_book_button_upload.on('click', function(event){
    // show gif
    edit_book_gif.css('visibility', 'visible');
    
    var file = edit_book_choose_upload.prop('files')[0];
    var data = new FormData();
    data.append('doc', file);
    customAjax({
      method: 'POST', 
      url: 'file/upload/', 
      data: data, 
      cache: false, 
      contentType: false, 
      processData: false, 
      success: function(metadata, status, xhr){

        edit_book_title.val(metadata.title);
        edit_book_author.val(metadata.author);
        edit_book_keywords.val(metadata.keywords);
        edit_book_publication_year.val(metadata.publicationYear);
        book_filename = metadata.filename;
        book_documentName = metadata.documentName;
        book_mime = metadata.mime;
        form_disable(false);

        edit_book_button_upload.attr("disabled", true);
        edit_book_choose_upload.attr("disabled", true);
      },
      complete: function(xhr, status){
        edit_book_gif.css('visibility', 'hidden');
      },
      error: function(xhr, status, error){
        edit_book_log.text(translation('Error while uploading document'));
      }
    });
  });

  edit_book_dugme.on('click', function(event){
    if(check_text(edit_book_title, 5, 80)){
      if(check_text(edit_book_author, 5, 120)){
        if(check_text(edit_book_keywords, 0, 120)){
          if(check_number(edit_book_publication_year, 0, 2099)){

            var title = edit_book_title.val();
            var author = edit_book_author.val();
            var keywords = edit_book_keywords.val();
            var publicationYear = edit_book_publication_year.val();
            var languageName = edit_book_language.val();
            var categoryName = edit_book_category.val();

            customAjax({
              method: 'POST',
              url: 'ebook/create',
              data: JSON.stringify({  'title': title, 
                                      'author': author, 
                                      'keywords': keywords, 
                                      'publicationYear': publicationYear, 
                                      'filename': book_filename, 
                                      'mime': book_mime, 
                                      'documentName': book_documentName, 
                                      'languageName': languageName, 
                                      'categoryName': categoryName }),
              processData: false,
              contentType: 'application/json',
              success: function(data, status, xhr){
                window.location.href = 'books.html';
              },
              error: function(xhr, status, error){
                edit_book_log.text(translation('Error while creating document'));
              }
            });
          }
          else{
            edit_book_log.text(translation('Publication year not valid'));
          }
        }
        else{
          edit_book_log.text(translation('Keyword length must be less than 120'));
        }
      }
      else{
        edit_book_log.text(translation('Author length not valid (5-120)'));
      }
    }
    else{
      edit_book_log.text(translation('Title length not valid (5-80)'));
    }
  });
}

function init_form_edit(){
  $('#id_page_title').text(translation('Edit book'));
  edit_book_form_p.text(translation('Edit book'));
  var html;
  html = html_template;
  edit_book_div_input_root.html(html);
  edit_book_dugme.html(translation('Confirm'));

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
        edit_book_language.append('<option id="id_option_language_' + language.name + '" value="' + language.name + '">' + language.name + '</option>');
      });
      form_disable(false);
      load_book();
    },
    error: function(xhr, status, error){
      edit_book_log.text(translate('Error while getting all languages'));
    }
  });

  // dobavljanje svih kategorija
  customAjax({
    method: 'GET',
    url: 'category/all',
    success: function(categories, status, xhr){
      categories.forEach(category => {
        edit_book_category.append('<option id="id_option_category_' + category.name + '" value="' + category.name + '">' + category.name + '</option>');
      });
      form_disable(false);
      load_book();
    },
    error: function(xhr, status, error){
      edit_book_log.text(translate('Error while getting all categories'));
    }
  });

  edit_book_dugme.on('click', function(event){
    if(check_text(edit_book_title, 5, 80)){
      if(check_text(edit_book_author, 5, 120)){
        if(check_text(edit_book_keywords, 0, 120)){
          if(check_number(edit_book_publication_year, 0, 2099)){

            var title = edit_book_title.val();
            var author = edit_book_author.val();
            var keywords = edit_book_keywords.val();
            var publicationYear = edit_book_publication_year.val();
            var languageName = edit_book_language.val();
            var categoryName = edit_book_category.val();

            customAjax({
              method: 'PUT',
              url: 'ebook/edit',
              data: JSON.stringify({  'id': bookId, 
                                      'title': title, 
                                      'author': author, 
                                      'keywords': keywords, 
                                      'publicationYear': publicationYear, 
                                      'filename': book_filename, 
                                      'documentName': book_documentName, 
                                      'languageName': languageName, 
                                      'categoryName': categoryName }),
              processData: false,
              contentType: 'application/json',
              success: function(data, status, xhr){
                window.location.href = 'books.html';
              },
              error: function(xhr, status, error){
                edit_book_log.text(translation('Error while editing document'));
              }
            });
          }
          else{
            edit_book_log.text(translation('Publication year not valid'));
          }
        }
        else{
          edit_book_log.text(translation('Keyword length must be less than 120'));
        }
      }
      else{
        edit_book_log.text(translation('Author length not valid (5-120)'));
      }
    }
    else{
      edit_book_log.text(translation('Title length not valid (5-80)'));
    }
  });
}

function init_form_delete(){
  $('#id_page_title').text(translation('Delete book'));
  edit_book_form_p.text(translation('Delete book'));
  var html;
  html = html_template;
  edit_book_div_input_root.html(html);
  edit_book_dugme.html(translation('Confirm'));

  edit_book_title = $('#id_input_title');
  edit_book_author = $('#id_input_author');
  edit_book_keywords = $('#id_input_keywords');
  edit_book_publication_year = $('#id_input_publication_year');
  edit_book_language = $('#id_input_language');
  edit_book_category = $('#id_input_category');

  form_disable(true);

  customAjax({
      method: 'GET',
      url: 'ebook/getById',
      data: { 'ebookId': bookId },
      success: function(ebook, status, xhr){
        edit_book_title.val(ebook.title);
        edit_book_author.val(ebook.author);
        edit_book_keywords.val(ebook.keywords);
        edit_book_publication_year.val(ebook.publicationYear);
        book_filename = ebook.filename;
        book_documentName = ebook.documentName;  
        edit_book_language.append('<option value="' + ebook.languageName + '">' + ebook.languageName + '</option>');
        edit_book_category.append('<option value="' + ebook.categoryName + '">' + ebook.categoryName + '</option>');
        edit_book_dugme.attr('disabled', false);
      },
      error: function(xhr, status, error){
        edit_book_log.text(translation('Error while getting document information'));
      }
    });

  edit_book_dugme.on('click', function(event){
    customAjax({
      method: 'DELETE',
      url: 'ebook/delete',
      data: { 'ebookId': bookId },
      success: function(data, status, xhr){
        window.location.href = 'books.html';
      },
      error: function(xhr, status, error){
        edit_book_log.text(translation('Error while deleting document'));
      }
    });
  });
}

function form_disable(state){
  if(state == false){
    form_disable_counter++;
  }
  if(state == true || form_disable_counter == 3){
    edit_book_title.attr('disabled', state);
    edit_book_author.attr('disabled', state);
    edit_book_keywords.attr('disabled', state);
    edit_book_publication_year.attr('disabled', state);
    edit_book_language.attr('disabled', state);
    edit_book_category.attr('disabled', state);
    edit_book_dugme.attr('disabled', state);
  }
}

function load_book(){
  load_book_counter++;
  if(load_book_counter == 2){
    customAjax({
      method: 'GET',
      url: 'ebook/getById',
      data: { 'ebookId': bookId },
      success: function(ebook, status, xhr){
        edit_book_title.val(ebook.title);
        edit_book_author.val(ebook.author);
        edit_book_keywords.val(ebook.keywords);
        edit_book_publication_year.val(ebook.publicationYear);
        book_filename = ebook.filename;
        book_documentName = ebook.documentName;
        edit_book_language.val(ebook.languageName).change();
        edit_book_category.val(ebook.categoryName).change();
        form_disable(false);
      },
      error: function(xhr, status, error){
        edit_book_log.text(translation('Error while getting document information'));
      }
    });
  }
}
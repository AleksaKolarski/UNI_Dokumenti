var changeType; // create/edit/delete
var categoryId;
var edit_category_div_input_root;
var edit_category_form_p;

var edit_category_name;

var edit_category_dugme;
var edit_category_log;

$(document).ready(function(e){

  init_search_params();

  init_form();
});

function init_search_params() {
  var searchParams;
  searchParams = new URLSearchParams(window.location.search);
  changeType = searchParams.get('changeType');
  categoryId = searchParams.get('categoryId');
}

function init_form() {
  edit_category_div_input_root = $('#id_div_input_fields');
  edit_category_form_p = $('#id_form_p');
  edit_category_dugme = $('#id_button_edit_category');
  edit_category_log = $('#id_edit_category_log_field');

  $('#id_a_cancel').text(translation('Cancel'));

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
  $('#id_page_title').text(translation('Create new category'));
  edit_category_form_p.text(translation('Create new category'));
  var html;
  html =  '<div class="class_div_input">' + 
            '<p>'+ translation('Name') +':</p>' + 
            '<input id="id_input_name" type="text">' + 
          '</div>';
  edit_category_div_input_root.html(html);
  edit_category_dugme.html(translation('Confirm'));

  edit_category_name = $('#id_input_name');

  add_validation_text(edit_category_name, 5, 30);

  edit_category_dugme.on('click', function(event){
    if(check_text(edit_category_name, 5, 30)){
      var name = edit_category_name.val();
      customAjax({
        method: 'POST',
        url: 'category/create',
        data: JSON.stringify({ 'name': name }),
        contentType: 'application/json',
        success: function(data, status, xhr){
          window.location.href = 'categories.html';
        },
        error: function(xhr, status, error){
          edit_category_log.text(translation('Error while creating category'));
        }
      });
    }
    else{
      edit_category_log.text(translation('Category name length not valid (5-30)'));
    }
  });
}

function init_form_edit(){
  $('#id_page_title').text(translation('Edit category'));
  edit_category_form_p.text(translation('Edit category'));
  var html;
  html =  '<div class="class_div_input">' + 
            '<p>'+ translation('Name') +':</p>' + 
            '<input id="id_input_name" type="text">' + 
          '</div>';
  edit_category_div_input_root.html(html);
  edit_category_dugme.html(translation('Confirm'));

  edit_category_name = $('#id_input_name');

  add_validation_text(edit_category_name, 5, 30);

  edit_category_dugme.attr('disabled', true);
  edit_category_name.attr('disabled', true);
  customAjax({
    method: 'GET',
    url: 'category/getById',
    data: { 'categoryId': categoryId }, 
    success: function(category, status, xhr){
      edit_category_name.val(category.name);
      edit_category_dugme.attr('disabled', false);
      edit_category_name.attr('disabled', false);
    },
    error: function(xhr, status, error){
      edit_category_log.text(translation('Error while getting category info'));
    }
  });

  edit_category_dugme.on('click', function(event){
    if(check_text(edit_category_name, 5, 30)){
      var name = edit_category_name.val();
      customAjax({
        method: 'PUT',
        url: 'category/edit',
        data: JSON.stringify({ 'id': categoryId, 'name': name }),
        contentType: 'application/json',
        success: function(data, status, xhr){
          window.location.href = 'categories.html';
        },
        error: function(xhr, status, error){
          edit_category_log.text('Greska pri menjanju kategorije');
        }
      });
    }
    else{
      edit_category_log.text(translation('Category name length not valid (5-30)'));
    }
  });
}

function init_form_delete(){
  $('#id_page_title').text(translation('Delete category'));
  edit_category_form_p.text(translation('Delete category'));
  var html;
  html =  '<div class="class_div_input">' + 
            '<p>'+ translation('Name') +':</p>' + 
            '<input id="id_input_name" type="text">' + 
          '</div>';
  edit_category_div_input_root.html(html);
  edit_category_dugme.html(translation('Confirm'));

  edit_category_name = $('#id_input_name');


  edit_category_dugme.attr('disabled', true);
  edit_category_name.attr('disabled', true);
  customAjax({
    method: 'GET',
    url: 'category/getById',
    data: { 'categoryId': categoryId }, 
    success: function(category, status, xhr){
      edit_category_name.val(category.name);
      edit_category_dugme.attr('disabled', false);
    },
    error: function(xhr, status, error){
      edit_category_log.text(translation('Error while getting category info'));
    }
  });

  edit_category_dugme.on('click', function(event){
    customAjax({
      method: 'DELETE',
      url: 'category/delete',
      data: { 'categoryId': categoryId },
      success: function(data, status, xhr){
        window.location.href = 'categories.html';
      },
      error: function(xhr, status, error){
        edit_category_log.text(translation('Error while deleting category'));
      }
    });
  });
}
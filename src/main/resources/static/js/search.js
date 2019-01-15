
var searchType;
var id_form_search;

// normal search
var normalSearchTarget;


$(document).ready(function (e) {

  id_form_search = $('#id_form_search');

  init_search_params();
  
  init_search_form();

});

function init_search_params() {
  var searchParams;
  searchParams = new URLSearchParams(window.location.search);
  searchType = searchParams.get('searchType');

  // normal search
  normalSearchTarget = searchParams.get('normalSearchTarget');

}


function init_search_form(){
  switch (searchType) {
    case 'Normal':
      init_search_form_normal();
      break;
    case 'BooleanQuery':
      init_search_form_boolean();
      break;
    case 'PhrazeQuery':
      init_search_form_phraze();
      break;
    case 'FuzzyQuery':
      init_search_form_fuzzy();
      break;
    default:
      init_search_form_normal();
      break;
  }
}

function init_search_form_normal(){
  var html =  '<input id="id_input_search" type="text">' +
              '<select>' + 
                '<option id="" value="Normal">Normal</option>' + 
                '<option id="" value="BooleanQuery">BooleanQuery</option>' + 
                '<option id="" value="PhrazeQuery">PhrazeQuery</option>' + 
                '<option id="" value="FuzzyQuery">FuzzyQuery</option>' + 
              '</select>' + 
              '<input id="id_button_search" type="button" value="Search"></input>' + 
              '<div id="id_div_radio">' + 
                '<input id="id_checkbox_search_title" name="radio_normal" type="radio" value="Title" ' + (normalSearchTarget=='Title'?'checked':'') + '> Title' + 
                '<input id="id_checkbox_search_author" name="radio_normal" type="radio" value="Author" ' + (normalSearchTarget=='Author'?'checked':'') + '> Author' + 
                '<input id="id_checkbox_search_keywords" name="radio_normal" type="radio" value="Keywords" ' + (normalSearchTarget=='Keywords'?'checked':'') + '> Keywords' + 
                '<input id="id_checkbox_search_content" name="radio_normal" type="radio" value="Content" ' + (normalSearchTarget=='Content'?'checked':'') + '> Content' + 
                '<input id="id_checkbox_search_language" name="radio_normal" type="radio" value="Language" ' + (normalSearchTarget=='Language'?'checked':'') + '> Language' + 
              '</div>';
  id_form_search.html(html);

  console.log($('input[name=radio_normal]:checked', id_form_search).val());
}
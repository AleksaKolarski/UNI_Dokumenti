
var searchType;
var id_form_search;

var searchTarget1;
var searchParam1;
var searchTarget2;
var searchParam2;
var booleanOperation;


$(document).ready(function (e) {

  id_form_search = $('#id_form_search');

  init_search_params();
  
  init_search_form();

  fill_search_results();
});

function init_search_params() {
  var searchParams;
  searchParams = new URLSearchParams(window.location.search);
  searchType = searchParams.get('searchType');
  if(searchType == null){
    searchType = 'Term';
  }

  // normal search
  searchTarget1 = searchParams.get('searchTarget1');
  if(searchTarget1 == null){
    searchTarget1 = 'title';
  }
  searchParam1 = searchParams.get('searchParam1');
  if(searchParam1 == null){
    searchParam1 = '';
  }

  searchTarget2 = searchParams.get('searchTarget2');
  if(searchTarget2 == null){
    searchTarget2 = 'title';
  }
  searchParam2 = searchParams.get('searchParam2');
  if(searchParam2 == null){
    searchParam2 = '';
  }
  booleanOperation = searchParams.get('booleanOperation');
  if(booleanOperation == null){
    booleanOperation = '';
  }
}

function init_search_form(){
  var html =  '<input id="id_input_search" type="text">' +
              '<select id="id_select_search_type">' + 
                '<option value="Term">TermQuery</option>' + 
                '<option value="Boolean">BooleanQuery</option>' + 
                '<option value="Phrase">PhraseQuery</option>' + 
                '<option value="Fuzzy">FuzzyQuery</option>' + 
              '</select>' + 
              '<button id="id_button_search" type="button">Search</button>' + 
              '<div id="id_div_radio">' + 
                '<input id="id_checkbox_search_title" name="radio_normal" type="radio" value="title" ' + (searchTarget1=='title'?'checked':'') + '> Title' + 
                '<input id="id_checkbox_search_author" name="radio_normal" type="radio" value="author" ' + (searchTarget1=='author'?'checked':'') + '> Author' + 
                '<input id="id_checkbox_search_keywords" name="radio_normal" type="radio" value="keyword" ' + (searchTarget1=='keyword'?'checked':'') + '> Keywords' + 
                '<input id="id_checkbox_search_content" name="radio_normal" type="radio" value="text" ' + (searchTarget1=='text'?'checked':'') + '> Content' + 
                '<input id="id_checkbox_search_language" name="radio_normal" type="radio" value="language" ' + (searchTarget1=='language'?'checked':'') + '> Language' + 
              '</div>';
  id_form_search.html(html);

  $('#id_select_search_type').val(searchType);
  $('#id_input_search').val(searchParam1);

  $('#id_button_search').on('click', function(event){
    search();
  });
}

function search(){  
  searchType = $('#id_select_search_type').val();
  searchParam1 = $('#id_input_search').val();
  searchTarget1 = $('input[name=radio_normal]:checked', id_form_search).val();
  window.location.href = 'search.html?searchType='+ searchType +'&searchTarget1='+ searchTarget1 +'&searchParam1=' + searchParam1 + '&searchTarget2=' + searchTarget2 + '&searchParam2='+ searchParam2 + '&booleanOperation=' + booleanOperation;
}

function fill_search_results(){
  customAjax({
    method: 'GET',
    url: 'search/search',
    data: { 'searchType': searchType, 'searchTarget1': searchTarget1, 'searchParam1': searchParam1, 'searchTarget2': searchTarget2, 'searchParam2': searchParam2, 'booleanOperation': booleanOperation },
    success: function(results, status, xhr){
      var div_search_results = $('#id_div_search_results');
      var html;
      results.forEach(result => {
        var ebook = result.ebookDTO;
        html =  '<div class="class_div_search_result">' +
                  '<p class="class_div_search_result_title">'+ ebook.title +'</p>' +
                  '<p class="class_div_search_result_author">'+ ebook.author +'</p>' +
                  '<p class="class_div_search_result_publication_year">('+ ebook.publicationYear +',</p>' +
                  '<p class="class_div_search_result_language_name">'+ ebook.languageName +',</p>' +
                  '<p class="class_div_search_result_category_name">'+ ebook.categoryName +')</p>' +
                  '<p class="class_div_search_result_highlight">...'+ result.highlight +'...</p>' +
                  '<a id="id_link_download_' + ebook.id + '" href="#"><p class="class_div_search_result_document_name">'+ ebook.documentName +'</p></a>' +
                '</div>';
        div_search_results.append(html);
        $('#id_link_download_' + ebook.id).on('click', function(event){
          customAjax({
            method: 'GET',
            url: 'file/generate-token/' + ebook.documentName,
            success: function(token, status, xhr){
              window.location.href = 'file/download/' + token;
            }
          });
        });
      });
    }
  });
}
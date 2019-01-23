
var searchType;
var id_form_search;

var searchTarget1;
var searchParam1;
var searchTarget2;
var searchParam2;
var booleanOperation;

var user;


$(document).ready(function (e) {

  $('#id_page_title').text(translation('Search'));

  id_form_search = $('#id_form_search');

  init_search_params();
  
  init_search_form();

  fill_search_results();
});

function init_search_params() {
  var searchParams;
  searchParams = new URLSearchParams(window.location.search);
  searchType = searchParams.get('searchType');
  if(searchType == null || searchType == ''){
    searchType = 'Term';
  }
  searchTarget1 = searchParams.get('searchTarget1');
  if(searchTarget1 == null || searchTarget1 == ''){
    searchTarget1 = 'title';
  }
  searchParam1 = searchParams.get('searchParam1');
  if(searchParam1 == null){
    searchParam1 = '';
  }
  searchTarget2 = searchParams.get('searchTarget2');
  if(searchTarget2 == null || searchTarget2 == ''){
    searchTarget2 = 'title';
  }
  searchParam2 = searchParams.get('searchParam2');
  if(searchParam2 == null){
    searchParam2 = '';
  }
  booleanOperation = searchParams.get('booleanOperation');
  if(booleanOperation == null || booleanOperation == ''){
    booleanOperation = 'AND';
  }
}

function init_search_form(){
  var html =  '<div>' + 
                '<input id="id_input_search" type="text">' +
                '<select id="id_select_search_target">' +
                  '<option value="title">'+ translation('Title') +'</option>' + 
                  '<option value="author">'+ translation('Author') +'</option>' + 
                  '<option value="keyword">'+ translation('Keyword') +'</option>' + 
                  '<option value="text">'+ translation('Content') +'</option>' + 
                  '<option value="language">'+ translation('Language') +'</option>' + 
                '</select>' + 
              '</div>'; 

  if(searchType == 'Boolean'){
    html += '<select id="id_select_boolean">' + 
              '<option value="AND">'+ translation('AND') +'</option>' + 
              '<option value="OR">'+ translation('OR') +'</option>' + 
              '<option value="NOT">'+ translation('NOT') +'</option>' + 
            '</select>' +        
            '<div>' + 
              '<input id="id_input_search2" type="text">' +
              '<select id="id_select_search_target2">' +
                '<option value="title">'+ translation('Title') +'</option>' + 
                '<option value="author">'+ translation('Author') +'</option>' + 
                '<option value="keyword">'+ translation('Keyword') +'</option>' + 
                '<option value="text">'+ translation('Content') +'</option>' + 
                '<option value="language">'+ translation('Language') +'</option>' + 
              '</select>' +
            '</div>';
  }

  html += '<select id="id_select_search_type">' + 
            '<option value="Term">TermQuery</option>' + 
            '<option value="Boolean">BooleanQuery</option>' + 
            '<option value="Phrase">PhraseQuery</option>' + 
            '<option value="Fuzzy">FuzzyQuery</option>' + 
          '</select>' + 
          '<button id="id_button_form_search" type="button">'+ translation('Search') +'</button>';
  id_form_search.html(html);

  $('#id_select_search_type').val(searchType);
  $('#id_select_search_target').val(searchTarget1);
  $('#id_select_search_target2').val(searchTarget2);
  $('#id_input_search').val(searchParam1);
  $('#id_input_search2').val(searchParam2);
  $('#id_select_boolean').val(booleanOperation);

  $('#id_select_search_type').on('change', function(event){
    searchType = this.value;
    search();
  });

  $('#id_button_form_search').on('click', function(event){
    search();
  });
}

function search(){
  if(searchType == 'Boolean'){
    searchTarget2 = $("#id_select_search_target2").val();
    if(searchTarget2 == undefined){
      searchTarget2 = '';
    }
    searchParam2 = $("#id_input_search2").val();
    if(searchParam2 == undefined){
      searchParam2 = '';
    }
    booleanOperation = $("#id_select_boolean").val();
    if(booleanOperation == undefined){
      booleanOperation = '';
    }
  }
  searchType = $('#id_select_search_type').val();
  searchTarget1 = $('#id_select_search_target').val();
  searchParam1 = $('#id_input_search').val();
  window.location.href = 'search.html?searchType='+ searchType +'&searchTarget1='+ searchTarget1 +'&searchParam1=' + searchParam1 + '&searchTarget2=' + searchTarget2 + '&searchParam2='+ searchParam2 + '&booleanOperation=' + booleanOperation;
}

function fill_search_results(){

  var admin = false;
  var userCategory;

  customAjax({
    method: 'GET',
    url: 'user/currentUser',
    success: function(userDTO, status, xhr){
      user = userDTO;
      admin = userDTO.isAdmin;
      userCategory = userDTO.categoryName;
    },
    complete: function(){
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
                      '<p class="class_div_search_result_highlight">...'+ result.highlight +'...</p>';
            if(user != null){
              if(admin == true || userCategory == ebook.categoryName || userCategory == null){
                html += '<a id="id_link_download_' + ebook.id + '" href="#"><p class="class_div_search_result_document_name">'+ ebook.documentName +'</p></a>';
              }
            }
            if(user == null){
              html += translation('Register to download');
            }
            html += '</div>';
            div_search_results.append(html);
            $('#id_link_download_' + ebook.id).on('click', function(event){
              customAjax({
                method: 'GET',
                url: 'file/generate-token/' + ebook.filename,
                success: function(token, status, xhr){
                  window.location.href = 'file/download/' + token;
                }
              });
            });
          });
        }
      });
    }
  });
}
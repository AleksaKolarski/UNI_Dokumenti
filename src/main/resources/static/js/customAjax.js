/* Custom jquery ajax calls that forward jwt token from localstorage:
**
** Author: Aleksa Kolarski (SF 27/2016)
** 2018
*/

function customAjax(params){
  var jwt = localStorage.getItem('jwt');
  $.ajax({
    async: params.async,
    cache: params.cache,
    complete: function(xhr, status){
      console.log(params.url + ': Server returned ' + xhr.status + '; status is ' + status);
    },
    contentType: params.contentType,
    data: params.data,
    dataType: params.dataType,
    error: params.error,
    method: params.method,
    mimeType: params.mimeType,
    processData: params.processData,
    success: params.success,
    url: params.url,
    beforeSend: function (xhr) {
      if(jwt != null){
        xhr.setRequestHeader("Authorization", 'Bearer '+ jwt)
      }
    }
  });
}
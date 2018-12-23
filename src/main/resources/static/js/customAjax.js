/* Custom jquery ajax calls that forward jwt token from localstorage:
**
** Author: Aleksa Kolarski (SF 27/2016)
** 2018
*/

function customAjax(requestType, requestUrl, dataParam, successParam, completeParam){
  customAjax(requestType, requestUrl, dataParam, successParam, completeParam, null);
}

function customAjax(requestType, requestUrl, dataParam, successParam, completeParam, responseDataType){
  var jwt = localStorage.getItem('jwt');
  $.ajax({
    data: dataParam,
    dataType: responseDataType,
    success: successParam,
    complete: completeParam,
    type: requestType,
    url: requestUrl,
    beforeSend: function (xhr) {
      if(jwt != null){
        xhr.setRequestHeader("Authorization", 'Bearer '+ jwt)
      }
    }
  });
}
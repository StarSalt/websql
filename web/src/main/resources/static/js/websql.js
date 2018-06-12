/**
 *
 * Created by Administrator on 2017/9/14.
 */


function postjson(url, param, callback) {
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(param),
        dataType: "json",
        contentType: "application/json",
        success: function (result) {
            console.log(result.code + "----" + result.message);
            callback(result);
        },
        error: function (xmlrequest) {
            console.log(xmlrequest.responseText);
        }
    });
}

function post(url, param, callback) {
    $.ajax({
        type: "POST",
        url: url,
        data: param,
        success: function (result) {
            console.log(result.code + "----" + result.message);
            callback(result);
        },
        error: function (xmlrequest) {
            console.log(xmlrequest.responseText);
        }
    });
}

function get(url, param, callback) {
    $.ajax({
        type: "GET",
        url: url,
        data: param,
        success: function (result) {
            console.log(result.code + "----" + result.message);
            callback(result);
        },
        error: function (xmlrequest) {
            console.log(xmlrequest.responseText);
        }
    });
}

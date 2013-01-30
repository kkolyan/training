//var ServerControl = {
//    fireRequest: function(s) {
//        alert(s);
//    }
//};

function init() {
    foreachElement("input", function(input) {
        input.onclick = function () {
            this.focus();
            this.select();
        }
    });
    foreachElement("form", function(form) {
       form.onsubmit = function() {
           return onFormSubmit(this);
       }
    });
    foreachElement("a", function(link) {
       link.onclick = function() {
           return onLinkFollow(this);
       }
    });
}

function onFormSubmit(form) {
    var data = "";
    for (var i = 0; i < form.elements.length; i++) {
        var name = form.elements[i].name;
        if (name) {
            var value = form.elements[i].value;
            value = encodeURIComponent(value);
            if (i != 0) {
                data += "&"
            }
            data += name +"="+value;
        }
    }

    var action = removeHostFromUrl(form.action);
    var raw = "POST "+action+" HTTP/1.1\n";
    raw += "Host: "+window.location.host+"\n";
    raw += "Content-Length: "+data.length+"\n";
    raw += "Content-Type: application/x-www-form-urlencoded\n";
    raw += "Referer: "+window.location+"\n";
    raw += "\n";
    raw += data;

    return tryDirectRequest(raw);
}

function onLinkFollow(link) {
    var location = removeHostFromUrl(link.href);
    var raw = "GET "+location+" HTTP/1.1\n";
    raw += "\n";

    return tryDirectRequest(raw);
}

function tryDirectRequest(raw) {
    try {
        ServerControl.fireRequest(raw);
        return false;
    } catch (e) {
    }
    return true;
}

function removeHostFromUrl(uri) {
    uri = uri.split("://", 2)[1];
    uri = uri.substr(uri.indexOf("/"));
    return uri;
}

function foreachElement(tagName, callback) {
    var elements = document.getElementsByTagName(tagName);
    for (var i = 0; i < elements.length; i ++) {
        var input = elements[i];
        callback(input);
    }
}
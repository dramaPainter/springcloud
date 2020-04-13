document.write("<link rel='stylesheet' href='/file/vue/element-ui.css'/>");
document.write("<script src='/file/vue/vue.js'></script>");
document.write("<script src='/file/vue/axios.js'></script>");
document.write("<script src='/file/vue/element-ui.js'></script>");

var doFailedCallback = function (callback) {
	if (callback != undefined || callback != null) {
		callback();
	}
};

function loadData(method, url, param, succeedCallback, failedCallback) {
	axios({method: method, url: url, data: param})
		.then(function (r) {
			if (r.status != 200) {
				alert(r.statusText);
				doFailedCallback(failedCallback);
			} else if (r.request.responseURL.indexOf(url) == -1) {
				location.href = r.request.responseURL;
			} else if (r.data.code < 0) {
				alert(r.data.message);
				doFailedCallback(failedCallback);
			} else {
				succeedCallback(r.data);
			}
		})
		.catch(function (reason) {
			alert(reason);
			doFailedCallback(failedCallback);
		});
}
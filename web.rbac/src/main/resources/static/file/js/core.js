class Enum {
    add(field, name, value) {
        this[field] = {name, value};
        return this;
    }

    getValue(name) {
        if (name === undefined || name === null) {
            return null;
        }

        for (let i in this) {
            let e = this[i];
            if (e && e.name === name) {
                return e.value;
            }
        }

        return null;
    }

    getName(value) {
        if (value === undefined || value === null) {
            return "";
        }

        for (let i in this) {
            let e = this[i];
            if (e && e.value === value) {
                return e.name;
            }
        }

        return "";
    }
}

var enums = {};
enums.status = new Enum()
    .add('ENABLE', '启用', 1)
    .add('DISABLE', '冻结', 2);


function loadScript(url, callback) {
    if (url.indexOf(".css") === -1) {
        let script = document.createElement('script');
        script.src = url;
        callback = callback || function () {
        };
        script.onload = script.onreadystatechange = function () {
            if (!this.readyState || 'loaded' === this.readyState || 'complete' === this.readyState) {
                callback();
                this.onload = this.onreadystatechange = null;
                this.parentNode.removeChild(this);
            }
        }
        document.body.appendChild(script);
    } else {
        document.write("<link rel='stylesheet' href='" + url + "'/>");
        callback();
    }
}

var doFailedCallback = function (callback) {
    if (callback !== undefined) {
        callback();
    }
};

function loadData(method, url, param, succeedCallback, failedCallback) {
    axios({method: method, url: url, data: param})
        .then(function (r) {
            if (r.status !== 200) {
                alert(r.statusText);
                doFailedCallback(failedCallback({code: -r.status, message: r.statusText}));
            } else if (r.request.responseURL.indexOf(url) === -1) {
                location.href = r.request.responseURL;
            } else if (r.data.code < 0) {
                doFailedCallback(failedCallback(r.data));
            } else {
                succeedCallback(r.data);
            }
        })
        .catch(function (reason) {
            doFailedCallback(failedCallback({code: -1, message: reason}));
        });
}

/*
loadScript("/file/vue/element/element-ui.css", "css");
loadScript("/file/css/core.css", "css");
loadScript("/file/vue/element/vue.js");
loadScript("/file/vue/element/axios.js");
loadScript("/file/vue/element/element-ui.js");
*/

function appLoad(fun) {
    loadScript("https://unpkg.com/element-ui/lib/theme-chalk/index.css", function () {
        loadScript("/file/css/core.css", function () {
            loadScript("https://unpkg.com/vue", function () {
                loadScript("https://unpkg.com/element-ui", function () {
                    loadScript("https://unpkg.com/axios/dist/axios.min.js", function () {
                        fun();
                    });
                });
            });
        });
    });
}



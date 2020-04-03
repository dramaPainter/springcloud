var stomp;
var loopTitle = 0;
var selectedUser = 0;
var selectedAssistant = -1;

var form = new Vue({
	el: '.form',
	data: {
		menu: [{
			text: '刷新对话',
			icon: 'iconfont i-refresh'
		}, {
			text: '查看资料',
			icon: 'iconfont icon-css-class'
		}, {
			text: '删除对话',
			icon: 'iconfont i-red'
		}],
		users: [],
		formData: new FormData(),
		status: "断开",
		btnSendClass: "btnGray",
		btnSendDisable: true,
		msgCount: 0,
		msgCountClass: "msgCount",
		message: "",
		shortcut_display: false,
		shortcut: [],
		response: []
	},
	methods: {
		connect: connect,
		send: send,
		select: select,
		keyEvent: keyEvent,
		doAction: doAction,
		upload: upload,
		showUpload: showUpload
	}
});

function keyEvent(key) {
	switch (key) {
		case "up":
			keyUpDown(true);
			break;
		case "down":
			keyUpDown(false);
			break;
		case "esc":
			form.shortcut_display = false;
			form.shortcut.splice(0, form.shortcut.length);
			break;
		case "enter":
			if (selectedAssistant > -1 && form.shortcut.length > 0) {
				form.message = form.shortcut[selectedAssistant].message;
				form.shortcut_display = false;
				form.shortcut.splice(0, form.shortcut.length);
				selectedAssistant = -1;
			}
			break;
		case "space":
			axios({
				method: 'post', url: '/chat/searchAssistant?key=' + encodeURIComponent(form.message)
			}).then(function (r) {
				if (r.status != 200) {
					alert(r.statusText);
					return;
				}
				form.shortcut = r.data;
			}).catch(function (reason) {
				alert(reason.response.data.message);
			});
			break;
		default:
			break;
	}
	return false;
}

function keyUpDown(up) {
	var length = $(".shortcut")[0].children.length;
	if (length == 0) {
		selectedAssistant = -1;
	} else if (selectedAssistant <= 0 && up) {
		selectedAssistant = 0;
	} else if (selectedAssistant >= length - 1 && !up) {
		selectedAssistant = length - 1;
	} else {
		if (up) {
			selectedAssistant--;
		} else {
			selectedAssistant++;
		}
	}

	if (selectedAssistant == -1) return;
	var node = $(".shortcut")[0].children;
	for (var i = 0; i < length; i++) node[i].className = "";
	node[selectedAssistant].className = "selected";
}

function resolveEvent() {
	select();
}

function doAction(index) {
	switch (index[0]) {
		case 0:
			reload(selectedUser);
			break;
		case 1:
			window.open("/login/detail?userid=" + selectedUser, "_blank");
			break;
		case 2:
			if (confirm("确认要删除？")) remove(selectedUser);
			break;
		default:
			alert('未知菜单');
			break
	}
}

function showUpload() {
	if (selectedUser > 0) {
		$('#upload').click();
	} else {
		alert("请先选择要上传图片的玩家。");
	}
}

function upload() {
	let file = form.$refs.inputer.files[0];
	$("#upload").value = "";
	form.formData.delete("file");
	form.formData.delete("receiver");
	form.formData.append("receiver", selectedUser);
	canvasResize(file, {
		crop: false, quality: 0.3, rotate: 0, callback(src) {
			form.formData.append("file", src);
			axios({
				method: 'post', url: "/upload/image", data: form.formData,
				headers: {'Content-Type': 'multipart/form-data;charset=utf-8'}
			}).then(function (r) {
				if (r.data.code < 0) {
					alert(r.data.message);
				}
			}).catch(function (reason) {
				alert(reason.response.data.message);
			});
		}
	});
}

function select() {
	removeHighlight();
	selectedUser = parseInt(event.target.lastChild.nodeValue);

	for (var i = 0; i < form.response.length; i++) {
		form.response[i].display = false;
	}
	var response = form.response.findId("userid", selectedUser);
	if (response != null) response.display = true;
	var user = form.users.findId("id", selectedUser);
	if (user != null) user.class = "selected";
	scrollToBottom(selectedUser);
	recount();
	form.btnSendClass = "";
	form.btnSendDisable = false;
}

function removeHighlight() {
	if (selectedUser > 0) {
		var user = form.users.findId("id", selectedUser);
		if (user != null) user.class = "";
	}
}

function remove(userid) {
	form.users.removeIf("id", userid);
	form.response.removeIf("userid", userid);
	if (userid == selectedUser) selectedUser = 0;
	recount();
	form.btnSendClass = "btnGray";
	form.btnSendDisable = true;
}

function reload(userid) {
	axios({
		method: 'get', url: '/chat/message?userid=' + userid
	}).then(function (r) {
		if (r.status != 200) {
			alert(r.statusText);
			return;
		}

		let obj = form.response.findId("userid", userid);
		if (obj != null) obj.data = r.data;
		form.btnSendClass = "";
		form.btnSendDisable = false;
		scrollToBottom(userid);
	}).catch(function (reason) {
		alert(reason.response.data.message);
	});
}

function send() {
	if (selectedUser == 0) {
		alert("请先选择要加载聊天记录的玩家。");
		return;
	} else if (form.message.length == 0) {
		alert("不能发送空消息。");
		return;
	}

	keyEvent("esc");
	form.btnSendClass = "btnGray";
	axios({
		method: 'post', url: '/chat/send?receiver=' + selectedUser + '&body=' + encodeURIComponent(form.message)
	}).then(function (r) {
		form.btnSendClass = "";
		if (r.status == 200) {
			form.message = "";
		} else {
			alert(r.statusText);
		}
	}).catch(function (reason) {
		form.btnSendClass = "";
		alert(reason.response.data.message);
	});
}

function connect() {
	if (form.status == "断开") {
		stomp = Stomp.over(new SockJS('/websocket'));
		stomp.debug = function () {
		};
		stomp.connect({
			username: $("#username").value
		}, function (success) {
			form.status = "已连接";
			form.msgCount = 0;
			stomp.subscribe('/kefu/' + $("#username").value, function (r) {
				var model = JSON.parse(r.body);
				if (model.kefu) {
					var obj = form.response.findId("userid", model.receiver);
					if (obj == null) {
						var display = model.receiver == selectedUser;
						form.response.push({"userid": model.receiver, "display": display, "data": [model]})
					} else {
						obj.data.push(model);
						for (var i = 0; i < obj.data.length; i++) {
							if (!obj.data[i].kefu) {
								obj.data[i].view = true;
							}
						}
						scrollToBottom(model.receiver);
					}
				} else {
					if (model.body == undefined) {
						var obj = form.response.findId("userid", model.receiver);
						if (obj != null) {
							for (var i = 0; i < obj.data.length; i++) {
								if (obj.data[i].kefu) obj.data[i].view = true;
							}
						}
					} else {
						var obj = form.response.findId("userid", model.sender);
						if (obj == null) {
							var display = model.sender == selectedUser;
							form.response.push({"userid": model.sender, "display": display, "data": [model]})
						} else {
							obj.data.push(model);
							for (var i = 0; i < obj.data.length; i++) {
								if (obj.data[i].kefu) {
									obj.data[i].view = true;
								}
							}
						}
						var css = selectedUser == model.sender ? "selected" : "highlight";
						form.users.removeIf("id", model.sender);
						form.users = [{"id": model.sender, "class": css}].concat(form.users);
						recount();
						scrollToBottom(model.sender);
					}
				}
			});
			console.log("聊天已接入：" + success);
		}, function (error) {
			form.status = "断开";
			form.msgCount = 0;

			if (error.headers) {
				alert(error.headers.message);
			} else {
				alert(error);
			}
		});
	} else {
		if (stomp != null) {
			stomp.disconnect();
		}
		form.status = "断开";
		form.msgCount = 0;
		console.log('已断开连接。');
	}
}

function scrollToBottom(userid) {
	setTimeout(function () {
		var msgs = $(".msg");
		for (var i = 0; i < msgs.length; i++) {
			if (msgs[i].getAttribute("itemid") == userid) {
				msgs[i].scrollTo(0, msgs[i].scrollHeight);
				break;
			}
		}
	}, 100);
}

function recount() {
	var count = 0;
	for (var i = 0; i < form.users.length; i++) {
		if (form.users[i].class == "highlight") count++;
	}
	form.msgCount = count;
	form.msgCountClass = count > 0 ? "msgCount highlight" : "msgCount";
	if (count == 0) {
		clearInterval(loopTitle);
		loopTitle = 0;
		document.title = "";
	} else if (loopTitle == 0 && count > 0) {
		var msg = "您有新的消息，请注意查收。";
		loopTitle = setInterval(function () {
			msg = msg.substring(1) + msg.substring(0, 1);
			document.title = msg;
		}, 1000)
	}
}
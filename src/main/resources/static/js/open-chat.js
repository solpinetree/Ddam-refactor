$("#chatinput").keypress(function (event) {
    if (event.which == 13) {
        $('#chatbtn').click();
        return false;
    }
});

$('#chatbtn').click(function () {
    send('CHAT');
});

const id = document.getElementById("senderId").value;
const username = document.getElementById("senderName").value;

function list() {

    $.ajax({
        url: '/crews/chats',
        type: 'get',
        async: true,
        success: function (data) {
            var a = '';
            $.each(data, function (key, value) {
                var time = moment(value.createdAt).format('MM/DD HH:mm')

                if (value.sender.id == id) { // 보낸건 오른쪽에
                    a += '<div class="sasa" align="right">';
                    a += '<span><b>' + value.sender.nickname + '</b></span><br>';
                    a += '<span style="color:black;">' + value.message + '</span><br>';
                    a += '<span style="font-size:3px;">' + time + '</span>';
                    a += '</div>';
                } else { // 받은건 왼쪽
                    a += '<div class="hmhm" align:"left">';
                    a += '<span><b>' + value.sender.nickname + '</b></span><br>';
                    a += '<span style="color:black;">' + value.message + '</span><br>';
                    a += '<span style="font-size:3px;">' + time + '</span>';
                    a += '<br /></div>';
                }
            });
            $('.msg_view').html(a);
            $('#msgDiv').scrollTop($('#msgDiv')[0].scrollHeight);
        }
    });
}

$('#msgDiv').scrollTop($('#msgDiv')[0].scrollHeight);	// 페이지  리로딩 되었을 때 스크롤을 맨 밑을 보여줌
const websocket = new WebSocket("ws://localhost:8080/open/chat");
websocket.onmessage = onMessage;
websocket.onopen = onOpen;
websocket.onclose = onClose;

function onClose(evt) {
    console.log("close event : " + evt);
}

function onOpen(evt) {
    console.log("open event : " + evt);
    list();
    send('ENTER');
}

function onMessage(msg) {
    let data = JSON.parse(msg.data);

    let senderId = data.senderId;
    let message = data.message;
    let senderName = data.senderName;
    let time = moment(new Date()).format('MM/DD HH:mm');


    let a = '';
    if (senderId == id) { // 보낸건 오른쪽에
        a += '<div class="sasa" align="right">';
        a += '<span><b>' + senderName + '</b></span><br>';
        a += '<span style="color:black;">' + message + '</span><br>';
        a += '<span style="font-size:3px;">' + time + '</span>';
        a += '</div>';
    } else { // 받은건 왼쪽
        a += '<div class="hmhm" align:"left">';
        a += '<span><b>' + senderName + '</b></span><br>';
        a += '<span style="color:black;">' + message + '</span><br>';
        a += '<span style="font-size:3px;">' + time + '</span>';
        a += '<br /></div>';
    }

    $('#msgDiv').append(a);
    $('#msgDiv').scrollTop($('#msgDiv')[0].scrollHeight);
}

function send(type) {
    var message = document.getElementById("msg").value;

    if ((type == 'CHAT' && message != "") || type == 'ENTER') {
        let msg = {
            'senderId': id,
            'senderName': username,
            'type': type,
            'message': message
        }

        if (message != null) {
            websocket.send(JSON.stringify(msg));	// websocket handler로 전송(서버로 전송)
        }
        document.getElementById("msg").value = '';
    }
}
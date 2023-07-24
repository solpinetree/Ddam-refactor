// 알림 수
function notiCount(){

    var userId = $("#userId").val();

    $.ajax({
        url: "/notifications/count?userId="+userId,
        type: "GET",
        success: function (res) {
            $("#notiNum").html(res);
        },
    });
}

// 알림 삭제 버튼을 누르면 . .
function removeNoti(notiId){
    $.ajax({
        url: "/deleteNoti",
        type: "POST",
        data: {
            "notiId":notiId
        },
        success: function (res) {
            notiCount();
            notilist();
        },
    });
}


/* Notifications 버튼 클릭*/
$('#Notifications').click(function() {

    $('#notificationModal').modal(); //id가 "notificationModal"인 모달창을 열어준다.
    $('.modal-title').text("알림"); //modal 의 header 부분에 "크루 멤버"라는 값을 넣어준다.
    notilist();
});


function notilist() {

    var userId = $("#userId").val();

    $.ajax({
        url: '/notifications/' + userId,
        type: 'post',
        async : true,
        data : {'userId' : userId},
        success : function(data) {
            var a = '<table class="modal_table">';
            $.each(data, function(key, value){
                var date = moment(value.writetime).format('YYYY-MM-DD HH:mm');
                a+='<tr><td id="modal_userID">'+ date +'</td>';
                a+='<td id="llll">'+value.noti+'</td>';
                a+='<td id="modal_userFollow">';
                a+='<button onclick="removeNoti('+value.id+')" class="btn btn-outline-primary">알림 삭제</button>';
                a+='</td></tr>';
            });
            a += '</table>';
            $('.notiModalBody').html(a);
        }
    });
}


$(document).ready(function(){
    notiCount();
    // setInterval(notiCount,1000);	// 처음 시작했을 때 실행되도록
    // setInterval(notilist, 4000);
});
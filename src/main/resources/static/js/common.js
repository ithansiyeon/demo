$(document).ready(function(){
	authorityMenu();

	$(document).on("click", "li.has_sub > a", function(e) {
		e.preventDefault();
		var $parentLi = $(this).parent();
		var $subMenu = $parentLi.find(" > ul");

		if (!$parentLi.hasClass("on")) {
			$subMenu.stop().slideDown(200);
			$parentLi.addClass("on");
			$parentLi.siblings().removeClass("on").find("ul").stop().slideUp(200);
		} else {
			$subMenu.stop().slideUp(200);
			$parentLi.removeClass("on");
		}
	});

	// cm_list
	$(".cm_list > div > a").click(function(){
		var submenu = $(this).next("div.hide_view");
		if( submenu.is(":visible") ){
			submenu.removeClass("open");
		}else{
			submenu.addClass("open");
		}
	});

	// 댓글
	$(".cm_re_info > button").click(function(){
		var submenu = $(this).parent().next("div.hide_view");
		if( submenu.is(":visible") ){
			submenu.removeClass("open");
		}else{
			submenu.addClass("open");
		}
	});

	// 첨부파일
	$(".file_input input[type='file']").on('change',function(){
		var fileName = $(this).val().split('/').pop().split('\\').pop();
		$(this).parent().siblings("input[type='text']").val(fileName);
	});
	// 파일업로드 미리보기
	$('.file_upload input[type=file]').change(function(event) {
		var tmppath = URL.createObjectURL(event.target.files[0]);
		$(this).parent('label').parent('.file_upload').parent('.file_preview').find("img").attr('src',tmppath);
	});
});

// 레이어 팝업(기본)
function layerPop(popName){
	var $layer = $("#"+ popName);
	$layer.fadeIn(500).css('display', 'inline-block').wrap( '<div class="overlay_t"></div>');
	$('body').css('overflow','hidden');
}
function layerPopClose(){
	$(".popLayer").hide().unwrap( '');
	$('body').css('overflow','auto');
	$(".popLayer video").each(function() { this.pause(); this.load(); });
}
function layerPopClose2(popName){
	$("#"+ popName).hide().unwrap( '');
	$('body').css('overflow','auto');
}

// 클릭시 새창 팝업 띄우기
function popup_win(str,id,w,h,scrollchk){
	var popupX = (document.body.offsetWidth / 2) - w/3;
	// 만들 팝업창 좌우 크기의 1/2 만큼 보정값으로 빼주었음
	var popupY = (window.screen.height / 2) - h/2;
	var pop = window.open(str,id,"width="+w+",height="+h+",scrollbars="+scrollchk+ " left="+popupX,", top="+popupY+",resize=no,location=no ");
	pop.focus();
}

function ajaxCmm(type, url, dataType, param, contentType, callback) {
	if(dataType == '') {
		dataType = 'json';
	}
	if(contentType == '') {
		contentType = 'application/x-www-form-urlencoded; charset=utf-8';
	}
	$.ajax({
		type: type,
		url: url,
		data: param,
		contentType: contentType,
		dataType: dataType,
		success: function(data, status, xr) {
			return callback(data);
		},
		error: function(xhr, status, error) {
			console.log(error);
			var msg = JSON.parse(xhr.responseText);
			console.log(msg);
			alert(msg['message']);
			location.reload();
		}
	});
}

function ajaxUpload(type, url, param, callback) {
	$.ajax({
		type: type,
		url: url,
		data: param,
		contentType: false,
		enctype: 'multipart/form-data',
		processData: false,
		success: function(data, status, xr) {
			return callback(data);
		},
		error: function(xhr, status, error) {
			console.log(error);
		}
	});
}

function authorityMenu() {
	ajaxCmm('get', `/menu/authorityMenuList`, 'text', '', '', function (data) {
		$(".sideMenuTable").replaceWith(data);
	});
}
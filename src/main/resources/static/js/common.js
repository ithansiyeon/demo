$(document).ready(function(){
	/* lnb */
	(function($){

		var lnbUI = {
			click : function (target, speed) {
				var _self = this,
					$target = $(target);
				_self.speed = speed || 300;

				$target.each(function(){
					if(findChildren($(this))) {
						return;
					}
					$(this).addClass('noDepth');
				});

				function findChildren(obj) {
					return obj.find('> ul').length > 0;
				}

				$target.on('click','a', function(e){
					e.stopPropagation();
					var $this = $(this),
						$depthTarget = $this.next(),
						$siblings = $this.parent().siblings();

					$this.parent('li').find('ul li').removeClass('on');
					$siblings.removeClass('on');
					$siblings.find('ul').slideUp(250);

					if($depthTarget.css('display') == 'none') {
						_self.activeOn($this);
						$depthTarget.slideDown(_self.speed);
					} else {
						$depthTarget.slideUp(_self.speed);
						_self.activeOff($this);
					}

				})

			},
			activeOff : function($target) {
				$target.parent().removeClass('on');
			},
			activeOn : function($target) {
				$target.parent().addClass('on');
			}
		};

		// Call lnbUI
		$(function(){
			lnbUI.click('#lnb li', 300)
		});

	}(jQuery));

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
			alert(msg['message'].split(":")[1]);
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
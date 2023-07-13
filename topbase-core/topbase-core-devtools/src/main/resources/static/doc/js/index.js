var module1 = (function(mod) {　　　
	mod.indexFun = function() {
		var domain = window.location.protocol + '//' + document.location.host;
		$('.index-input').val(domain);
		$('.jump-link').click(function() {
			sessionStorage.baseUrl = $('.index-input').val();
			window.location.href = '/static/doc/doc.html';
		})
		var baseUrl = sessionStorage.baseUrl || '';
		$.ajax({
			type: "get",
			url: baseUrl + "/devdoc/config",
			async: true,
			success: function(data) {
				var result = data.result;
				$('.center').eq(0).html(result.fullName);
				$('.data-data').eq(0).html(result.balancedDataCenterId);
				$('.data-data').eq(1).html(result.balancedWorkerId);
				$('.data-data').eq(2).html(result.localIpByNetcard);
				$('.data-data').eq(3).html(result.activeUsers);
				var tableStr = '<tbody>' +
					'<tr>' +
					'<th>序号</th>' +
					'<th>键</th>' +
					'<th>配置信息</th>' +
					'</tr>';
				var index = 1;
				for(var key in data.result.props) {
					tableStr += '<tr>' +
						'<td align="center" style="width:50px">' + index + '</td>' +
						'<td align="left" style="width:200px">' + key + '</td>' +
						'<td align="left" style="word-wrap:break-word;word-break:break-all;">' +  data.result.props[key] + '</td>' +
						'</tr>';
					index++;
				}
				tableStr += '</tbody>';
				$('.code-table1').html(tableStr);
			},
			error: function(error) {
				console.error('请求错误')
			}
		});
	}

	mod.docFun = function() {
		var baseUrl = sessionStorage.baseUrl || '';
		$.ajax({
			type: "get",
			url: baseUrl + "/devdoc/config",
			async: true,
			success: function(data) {
				var result = data.result;
				$('.docs-header').html(result.fullName);
				$('.max-body-text-size').html(result.maxBodyTextSize);
				$('.docs-footer').html(result.fullName);
			},
			error: function(error) {
				console.error('请求错误')
			}
		});
		$.ajax({
			type: "get",
			url: baseUrl + "/devdoc/listkeys",
			async: true,
			success: function(data) {
				var tableStr = '<table class="doc-table" border="1">';
				for(var i = 0; i < data.result.length; i++) {
					var item = data.result[i];
					tableStr += '<tr>' +
						'<td align="center">说明</td>' +
						'<td align="center">' +
						item.remark +
						'</td>' +
						'<td align="center">类型</td>' +
						'<td align="center">' +
						item.type +
						'</td>' +
						'</tr>' +
						'<tr>' +
						'<td align="right">accessid</td>' +
						'<td class="id-text" id="accessid' + i + '" colspan="2">' +
						item.accessId +
						'</td>' +
						'<td><input class="copy-id-btn" id="copyaccessid' + i + '" type="button" value="复制" /></td>' +
						'</tr>' +
						'<tr>' +
						'<td align="right">accesskey</td>' +
						'<td class="key-text" id="accesskey' + i + '" colspan="2">' +
						item.accessKey +
						'</td>' +
						'<td><input class="copy-key-btn" id="copyaccesskey' + i + '" type="button" value="复制" /></td>' +
						'</tr>'
				}
				tableStr += '</table>';
				$('.doc-table-wrapper').html(tableStr);
				var index = 0;
				$('.id-text').each(function () {
					var copyaccessid = '#copyaccessid' + index;
					var accessid = '#accessid' + index;
					var copyaccesskey = '#copyaccesskey' + index;
					var accesskey = '#accesskey' + index;
					new Clipboard(copyaccessid, {
						text: function() {
							return $(accessid).text();
						}
					}).on('success', function(e) {
						$(copyaccessid).attr('value', "已复制");
						$(copyaccessid).attr('disabled', "true");
					}).on('error', function(e) {
						alert("复制失败");
					});
					new Clipboard(copyaccesskey, {
						text: function() {
							return $(accesskey).text();
						}
					}).on('success', function(e) {
						$(copyaccesskey).attr('value', "已复制");
						$(copyaccesskey).attr('disabled', "true");
					}).on('error', function(e) {
						alert("复制失败");
					});
					index++;
				})
			},
			error: function(error) {
				console.error('请求错误')
			}
		});
	}

	mod.reponseCodeFun = function() {
		var baseUrl = sessionStorage.baseUrl || '';
		var language = $("#select").val();
		getResponseCode(language);
		$('#select').change(function(e) {
			console.log($(this).val())
			language = $(this).val();
			getResponseCode(language);
		})

		function getResponseCode(language) {
			$.ajax({
				type: "get",
				url: baseUrl + "/devdoc/resp/"+language,
				async: true,
				headers: {
				},
				success: function(data) {
					var tableStr = '<tbody>' +
					'<tr>' +
					'<th>序号</th>' +
					'<th>编号</th>' +
					'<th>信息</th>' +
					'</tr>';
				var index = 1;
				for(var key in data.result) {
					tableStr += '<tr>' +
						'<td align="center">' + index + '</td>' +
						'<td align="center">' + key + '</td>' +
						'<td align="left">' + data.result[key] + '</td>' +
						'</tr>';
					index++;
				}
				tableStr += '</tbody>';
				$('.code-table').html(tableStr);
				window.scrollTo(0,0);
				},
				error: function(error) {
					console.error('请求错误')
				}
			});
		}
	}
	
	mod.dataDictionaryFun = function() {
		var baseUrl = sessionStorage.baseUrl || '';
		$.ajax({
			type: "get",
			url: baseUrl + "/devdoc/data/dictionary/false",
			async: true,
			headers: {
			},
			success: function(data) {
				var tableStr = '<tbody>' +
				'<tr>' +
				'<th>序号</th>' +
				'<th>字典码</th>' +
				'<th>{"编号":"值"}</th>' +
				'</tr>';
			var index = 1;
			for(var key in data.result) {
				tableStr += '<tr>' +
					'<td align="center">' + index + '</td>' +
					'<td align="center">' + key + '</td>' +
					'<td align="left"><pre>' + syntaxHighlight(data.result[key]) + '</pre></td>' +
					'</tr>';
				index++;
			}
			tableStr += '</tbody>';
			$('.code-table').html(tableStr);
			window.scrollTo(0,0);
			},
			error: function(error) {
				console.error('请求错误')
			}
		});
	}

	mod.testToolFun = function() {
		var baseUrl = sessionStorage.baseUrl || '';
		$('#submit-btn').click(function() {
			var content = $('#requestContent').val();
			var contentBase64Str = Base64.encode(content);
			var obj = {
				accessKey: $('#accessKey').val(),
				content: contentBase64Str
			}
			var paramStr = JSON.stringify(obj);
			var paramBase64Str = Base64.encode(paramStr);

			$.ajax({
				type: "post",
				url: baseUrl + "/devdoc/sign",
				contentType: "application/json",
				data: paramBase64Str,
				async: true,
				success: function(data) {
					var result = data.result;
					$('.copysignaturem').html(result.signature);
					$('.copysignatureb').html(result.signatureUrl);
					$('.copyrequestContentEncode').html(contentBase64Str);
					$('.copysignaturemm').html(result.signatureBase64);
					$('.copysignature').html(result.signatureBase64Url);
					$('.copy-btn').attr('value', "复制");
					$('.copy-btn').attr('disabled', false);
					new Clipboard('#copysignaturem', {
						text: function() {
							return $('.copysignaturem').text();
						}
					}).on('success', function(e) {
						$('#copysignaturem').attr('value', "已复制");
						$('#copysignaturem').attr('disabled', "true");
					}).on('error', function(e) {
						alert("复制失败");
					});

					new Clipboard('#copysignatureb', {
						text: function() {
							return $('.copysignatureb').text();
						}
					}).on('success', function(e) {
						$('#copysignatureb').attr('value', "已复制");
						$('#copysignatureb').attr('disabled', "true");
					}).on('error', function(e) {
						alert("复制失败");
					});

					new Clipboard('#copysignaturemm', {
						text: function() {
							return $('.copysignaturemm').text();
						}
					}).on('success', function(e) {
						$('#copysignaturemm').attr('value', "已复制");
						$('#copysignaturemm').attr('disabled', "true");
					}).on('error', function(e) {
						alert("复制失败");
					});

					new Clipboard('#copysignature', {
						text: function() {
							return $('.copysignature').text();
						}
					}).on('success', function(e) {
						$('#copysignature').attr('value', "已复制");
						$('#copysignature').attr('disabled', "true");
					}).on('error', function(e) {
						alert("复制失败");
					});

					new Clipboard('#copyrequestContentEncode', {
						text: function() {
							return $('.copyrequestContentEncode').text();
						}
					}).on('success', function(e) {
						$('#copyrequestContentEncode').attr('value', "已复制");
						$('#copyrequestContentEncode').attr('disabled', "true");
					}).on('error', function(e) {
						alert("复制失败");
					});
				},
				error: function(error) {
					console.error('请求错误')
				}
			});
		})
		var Base64 = {
			// private property
			_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
			// public method for encoding
			encode : function (input) {
			    var output = "";
			    var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
			    var i = 0;
			    input = Base64._utf8_encode(input);
			    while (i < input.length) {
			        chr1 = input.charCodeAt(i++);
			        chr2 = input.charCodeAt(i++);
			        chr3 = input.charCodeAt(i++);
			        enc1 = chr1 >> 2;
			        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			        enc4 = chr3 & 63;
			        if (isNaN(chr2)) {
			            enc3 = enc4 = 64;
			        } else if (isNaN(chr3)) {
			            enc4 = 64;
			        }
			        output = output +
			        this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
			        this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
			    }
			    return output;
			},
			// private method for UTF-8 encoding
			_utf8_encode : function (string) {
			    string = string.replace(/\r\n/g,"\n");
			    var utftext = "";
			    for (var n = 0; n < string.length; n++) {
			        var c = string.charCodeAt(n);
			        if (c < 128) {
			            utftext += String.fromCharCode(c);
			        }
			        else if((c > 127) && (c < 2048)) {
			            utftext += String.fromCharCode((c >> 6) | 192);
			            utftext += String.fromCharCode((c & 63) | 128);
			        }
			        else {
			            utftext += String.fromCharCode((c >> 12) | 224);
			            utftext += String.fromCharCode(((c >> 6) & 63) | 128);
			            utftext += String.fromCharCode((c & 63) | 128);
			        }
			    }
			    return utftext;
			},
		}
	}　　　　
	return mod;　　
})(window.module1 || {});

function syntaxHighlight(json) {
    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}

function f() {
	js.ua.UserAgent.TRIDENT = true;

	if (js.ua.Engine.TRIDENT) {
		alert(js.ua.Message.ALERT);
	}
	
	return js.ua.Style.TRIDENT ? 'styleFloat' : 'cssFloat';
}

$legacy(js.ua.Layout.TV || js.ua.TypeFace.ARIAL, function() {

});
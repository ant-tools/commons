$package("js.tools.scanner.test");

$include("js.format.Number");
$include("js.net.RMI.Loop");

js.tools.scanner.test.DependencySample = function(node) {
	$assert(js.lang.Types.isUndefined(this._method), "js.tools.scanner.test.DependencySample#DependencySample", "Method should be undefined.")
	$assert(typeof this._comparator === "undefined", "js.tools.scanner.test.DependencySample#DependencySample", "Comparator should be undefined.")
	$assert(node.nodeName.toLowerCase() === "a", "js.tools.scanner.test.DependencySample#DependencySample", "Node is not an anchor.");
	this._method = js.net.Method.POST;
	this._comparator = js.lang.Strings.startsWith("query");
	js.net.XHR.TIMEOUT = 1000;
};

js.tools.scanner.test.DependencySample._log = LogFactory.getLogger("js.tools.scanner.test.DependencySample");

js.tools.scanner.test.DependencySample.init = function() {
	$assert(type in js.event.Types, "js.event.DomEvents#addListener", "Unrecognized event type #%s.", type);
	var uuid = js.util.UUID();
	var xhr = new js.net.XHR();
	if (js.lang.Types.isKindOf(js.format.FullDateTime, js.format.AbstractDateTime)) {
		var l = js.ua.Regional.language;
		js.ua.System._getErrorMessage(l);
	}
};

js.tools.scanner.test.DependencySample.currency = new js.format.Currency();
js.tools.scanner.test.DependencySample.date = js.format.ShortDateTime();

js.tools.scanner.test.DependencySample.prototype = {
	_log : js.lang.LogFactory.getLogger("js.tools.scanner.test.DependencySample"),

	METHOD : js.net.Method.HEAD,
	STATE : js.net.ReadyState.DONE,
	TIMEOUT : new js.util.Timeout(),

	method : function() {
		function callback() {
			js.util.Timer(1000, function() {
				var rmi = new js.net.RMI();
			});
		}
	},

	toString : function() {
		return "js.tools.scanner.test.DependencySample";
	}
};
$extends(js.tools.scanner.test.DependencySample, js.lang.Object);
$init(js.tools.scanner.test.DependencySample);

$legacy(js.ua.Engine.TRIDENT, function() {
	js.tools.scanner.test.DependencySample.prototype.method = function() {
		var n = js.util.Rand(10);
		this.method = js.lang.NOP;
	}
});

js.tools.scanner.test.Exception = function(node, el) {
	node[js.dom.Node._BACK_REF] = el;
};
$extends(js.tools.scanner.test.Exception, Error);

js.hood.IndexPage = function() {
	this.description = new js.widget.Description(R.string.widget_description);
	js.ua.System.alert(1234, R.string.bad_user_name, object);
	var message = R.string.bad_user_password;
	this._welcome = R.string.welcome;
	throw R.string.throw_message;
	throw new js.lang.Exception(R.string.missing_component, viewName);
	return message + ' ' + R.string.error_message;
	return R.string.return_message;
	var icon = R.image.test_icon;
};

var xbAttachEvent = function(obj, evt, handler) {
	return false;
}
var xbGetEventElement = function(evt) {
	return null;
}
var xbFireEvent = function(obj, evtype, evt) {
	return false;
}
if(window.addEventListener) {
	xbAttachEvent = function(obj, evt, handler) {
		obj.addEventListener(evt, handler, false);
		return true;
	}
	xbFireEvent = function(obj, evtype, evt) {
		return obj.dispatchEvent(evt);
	}
	xbGetEventElement = function(evt) {
		var elem = null;
		if(evt) elem = (evt.target.nodeType==3) ? evt.target.parentNode:evt.target;
		return elem;
	}
} else if(window.attachEvent) {
	xbAttachEvent = function(obj, evt, handler) {
		return obj.attachEvent("on" + evt, handler);
	}
	xbFireEvent = function(obj, evtype, evt) {
		return obj.fireEvent("on" + evtype);
	}
	xbGetEventElement = function(evt) {
		var elem = null;
		evt = evt ? evt : (window.event ? window.event : null);
		if(evt) elem = evt.srcElement;
		return elem;
	}
} else {
	xbAttachEvent = function(obj, evt, handler) {
		obj["on" + evt] = handler;
		return true;
	}
	xbFireEvent = function(obj, evtype, evt) {
		obj["on" + evtype]();
		return true;
	}
}
function xbPreventDefault(evt) {
	if(!evt && window.event) evt = window.event;
	if(evt.preventDefault) evt.preventDefault();
	else evt.returnValue = false;
	return false;
}

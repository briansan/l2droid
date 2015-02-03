// load modules
var http = require('http'), url = require('url'), querystring = require('querystring');
var port = 8124;

// echo message
function echo (request, response) {
	var data = '';
  	var echo, msg;
	request.on('data', function (chunk)
	{ 
		data += chunk;
	});

	request.on('end', function ()
  	{
		var decoded = querystring.parse(data);
		echo = decoded.msg;

		// content header
		response.writeHead(200, {'content-type': 'application/json'});
		// content body as JSON and signal complete
		msg = "Echo response: " + echo;
		console.log(msg);
		response.write(JSON.stringify({ response: msg }) + '\n');
		response.end();
	});
}

// process request
function processRequest(request, response) {
    var uri = url.parse(request.url).pathname;
    if (uri == '/echo') {
	console.log("Handled request: " + request.url);
        echo(request, response);
    } else {
        console.log("Unhandled request: " + request.url);
	response.writeHead(404, {'Content-Type': 'text/plain'});
	response.end();
	request.connection.destroy();
    }
}

// create server listening on defined port
http.createServer(processRequest).listen(port);

console.log("HTTP JSON Echo Server running on port " + port);



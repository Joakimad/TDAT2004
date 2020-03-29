'use strict';

const net = require('net');
const sha1 = require("crypto-js/sha1");
const base64 = require('crypto-js/enc-base64');

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer(connection => {
    connection.on('data', () => {
        let content = `<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
  </head>
  <body>
    WebSocket test page
    <script>
      let ws = new WebSocket('ws://localhost:3001');
      ws.onmessage = event => alert('Message from server: ' + event.data);
      ws.onopen = () => ws.send('hello');
    </script>
  </body>
</html>
`;
        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
    });
});

httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
});

// WebSocket server
let clients = [];
const wsServer = net.createServer(connection => {
    console.log('Client connected');

    connection.on('data', data => {
        if (data.toString().includes("HTTP/1.1")) {  // Handshake
            if (clients.indexOf(connection) === -1) {
                clients.push(connection);
            }

            let headers = data.toString().split("\n");
            let key = "";
            headers.map(header => {
                if (header.indexOf("Sec-WebSocket-Key:") > -1) {
                    let indexStart = header.indexOf("Key:");
                    key = header.substring(indexStart + 4).trim();
                }
            });

            let hash = sha1(key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11");
            let serverKey = base64.stringify(hash);

            let returnValue = "HTTP/1.1 101 Switching Protocols\r\n" +
                "Upgrade: websocket\r\n" +
                "Connection: Upgrade\r\n" +
                "Sec-WebSocket-Accept: " + serverKey.trim() + "\r\n" +
                "\r\n";
            connection.write(returnValue);
        }
        else { // Send data to others and self
            let parsedData = parse(data);
            for (let i = 0; i < clients.length; i++) { // Write to others
                if (!clients[i].destroyed && clients[i] !== connection) {
                    clients[i].write(frameData(parsedData));
                } else if (clients[i] === connection) { //Write to self
                    clients[i].write(frameData("Success!"));
                }
            }
        }
    });

    // Close connection
    connection.on('end', () => {
        console.log('Client disconnected');
        clients = clients.filter(e => e !== connection);
        connection.end();
    });
});

wsServer.on('error', error => {
    console.error('Error!\n', error);
});

wsServer.listen(3001, () => {
    console.log('WebSocket server listening on port 3001');
});

function parse(data) {
    let bytes = data;
    let length = bytes[1] & 127;
    let maskStart = 2;
    let dataStart = maskStart + 4;

    let parsedData = "";

    for (let i = dataStart; i < dataStart + length; i++) {
        let byte = bytes[i] ^ bytes[maskStart + ((i - dataStart)) % 4];
        parsedData += String.fromCharCode(byte);
    }
    return parsedData;
}

function frameData(str) {
    let type = 0x81;
    let frame = [type];
    let length = str.length;

    if (length > 127) {
        throw new Error("Max 127 bytes");
    }
    let maskLength = 0b00000000 | length;
    frame.push(maskLength);

    let data = Buffer.from(str);
    for (let i = 0; i < data.length; i++) {
        frame.push(data[i]);
    }
    return Buffer.from(frame);
}
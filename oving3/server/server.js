const express = require("express");
const bodyParser = require('body-parser');
const app = express();
const {exec} = require("child_process");
const cors = require("cors");
const fs = require('fs');
app.use(bodyParser.json());
app.use(bodyParser.json({type: '*/*'}));
app.use(cors());

app.post('/run/node', (req, res) => {
    console.log('Running code');
    let code = req.body.input;
    writeAndRunFile(code, res);
});

function writeAndRunFile(code, res) {
    let codeResult = [];
    fs.writeFileSync('./codeFromWebpage.js', code);

    let script = exec("node codeFromWebpage.js", (error, stdout, stderr) => {
        if (error) {
            codeResult.push(error.message.toString());
            return;
        }
        if (stderr) {
            codeResult.push(stderr.toString());
            return;
        }
        codeResult.push(stdout);
        console.log(stdout);
    });
    script.on('close', () => {
        console.log("Finished");
        res.send(codeResult);
    });
}

app.listen(8080, () => {
    console.log('Server running on port 8080');
});
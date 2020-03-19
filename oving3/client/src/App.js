import React, {useState} from 'react';
import axios from 'axios';
import AceEditor from "react-ace";
import "ace-builds/src-noconflict/mode-python";

function App() {
    const [output, setOutput] = useState([]);

    function runCode(code) {
        axios.post('http://localhost:9001/', {input: code})
            .then(res => setOutput(res.data))
            .catch(err => console.log(err));
    }

    return (
        <div>
            <Input codeRunner={runCode}/>
            <Output text={output}/>
        </div>
    );
}

function Input({codeRunner}) {
    let code = "";

    function runCode() {
        codeRunner(code);
    }

    function onChangeHandler(value) {
        code = value;
    }

    return (
        <div>
            <AceEditor
                focus={true}
                onChange={value => onChangeHandler(value)}
            />
            <button onClick={runCode}>Run code</button>
        </div>
    )
}

function Output({text}) {
    return (
        <div>
            {text.map(output => output)}
        </div>
    )
}

export default App;
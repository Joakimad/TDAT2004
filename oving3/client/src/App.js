import React, {useState} from 'react';
import axios from 'axios';

function App() {

    const [output, setOutput] = useState([]);

    function runCode(code) {
        return axios.post('http://localhost:9001/run/node', {input: code})
            .then(res => setOutput(res.data))
            .catch(rej => console.log(rej));
    }

    return (
        <div className="App">
            <div className="wrapper">
                <Input codeRunner={runCode}/>
                {output.map(output => output)}
            </div>
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
            <textarea
                cols={280}
                rows={5}
                onChange={value => onChangeHandler(value)}
            />
            <button className="btn margin-top-20" onClick={runCode}>Run code</button>
        </div>
    )
}

export default App;
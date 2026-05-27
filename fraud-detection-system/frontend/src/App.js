import React from 'react';

function App() {
  return (
    <div style={{ margin: '2rem', fontFamily: 'Arial, sans-serif' }}>
      <h1>Online Banking Fraud Detection</h1>
      <p>Welcome to the project scaffold. Use this frontend to call the API Gateway.</p>
      <ul>
        <li>Auth service: /auth/login</li>
        <li>Transaction service: /transaction/transfer</li>
        <li>Fraud engine: /fraud/evaluate</li>
      </ul>
    </div>
  );
}

export default App;

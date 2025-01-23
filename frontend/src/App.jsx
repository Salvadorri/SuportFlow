import React from 'react';
import AppRoutes from './routes/index.js'; // ou './routes/index' se você nomeou o arquivo como index.jsx

import './styles/App.css';

function App() {
  return (
    <div className="app">
      <AppRoutes />
    </div>
  );
}

export default App;
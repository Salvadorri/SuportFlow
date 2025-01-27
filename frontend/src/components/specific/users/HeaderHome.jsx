import React from 'react';

const HeaderHome = () => {
  return (
    <div className="headerhome">
      <div className="headerhome-brand">
        <img src="./assets/logo.png" alt="SupportFlowAI Logo" className="logo" />
        SupportFlowAI
      </div>
      <div className="headerhome-links">
        <a href="#sobre">Sobre</a>
        <a href="#recursos">Recursos</a>
        <a href="#precos">Contato</a>
      </div>
      <button className="login-button">Login / Signup</button>
    </div>
  );
}

export default HeaderHome;
import React from 'react';

const Contatos = () => {
  return (
    <div className="contatos-container">
      <h2>Entre em Contato</h2>
      <div className="contato-info">
        <p>
          <span className="contato-label">Telefone:</span>
          <a href="tel:+1234567890">+123 456 7890</a>
        </p>
        <p>
          <span className="contato-label">Email:</span>
          <a href="mailto:contato@empresa.com">contato@suportflowai.com</a>
        </p>
      </div>
    </div>
  );
};

export default Contatos;
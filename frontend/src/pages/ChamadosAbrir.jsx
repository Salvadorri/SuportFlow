
import "../styles/tailwind.css";

import React from "react";

const CriarChamado = () => {
  return (
    <div className="flex h-screen">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white p-5">
        <h1 className="text-2xl font-bold mb-6">Support Flow.AI</h1>
        <nav>
          <ul>
            <li className="mb-4 font-semibold">Chamados</li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Dashboard</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Meus Chamados</a></li>
          </ul>
        </nav>
      </aside>
      
      {/* Main Content */}
      <main className="flex-1 p-6">
        <h2 className="text-3xl font-semibold mb-6">Criar Chamado</h2>
        
        <div className="bg-white p-6 rounded-lg shadow max-w-4xl mx-auto">
          <form>
            <div className="mb-4">
              <label className="block text-gray-700">Título do Chamado *</label>
              <input type="text" className="w-full p-2 border rounded" placeholder="Digite o título" required />
            </div>

            <div className="mb-4">
              <label className="block text-gray-700">Descrição *</label>
              <textarea className="w-full p-2 border rounded" rows="4" placeholder="Descreva o problema" required></textarea>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-gray-700">Prioridade *</label>
                <select className="w-full p-2 border rounded">
                  <option>Baixa</option>
                  <option>Média</option>
                  <option>Alta</option>
                </select>
              </div>
              <div>
                <label className="block text-gray-700">Categoria *</label>
                <select className="w-full p-2 border rounded">
                  <option>Suporte Técnico</option>
                  <option>Financeiro</option>
                  <option>Outro</option>
                </select>
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
              <div>
                <label className="block text-gray-700">Atribuir para *</label>
                <input type="text" className="w-full p-2 border rounded" placeholder="Nome do atendente" required />
              </div>
              <div>
                <label className="block text-gray-700">Data de Vencimento *</label>
                <input type="date" className="w-full p-2 border rounded" required />
              </div>
            </div>
            
            <div className="flex gap-4 mt-6">
              <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">Salvar</button>
              <button type="button" className="bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-700">Cancelar</button>
            </div>

            <p className="text-gray-500 text-sm mt-2">* Campos obrigatórios</p>
          </form>
        </div>
      </main>
    </div>
  );
};

export default CriarChamado;

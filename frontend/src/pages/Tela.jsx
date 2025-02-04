import React from "react";
import "C:/Users/jvdal/OneDrive/Área de Trabalho/Desenvolvimento/SuportFlow/frontend/src/styles/tailwind.css";

export default function Tela() {
  return (
    <div className="flex flex-col h-screen">
      {/* Header */}
      <header className="bg-gray-800 text-white py-4 px-6 text-center text-xl font-semibold">
        SupportFlowAI
      </header>

      <div className="flex flex-1">
        {/* Sidebar */}
        <aside className="w-64 bg-gray-900 text-white p-6 flex-shrink-0">
          <h1 className="text-xl font-bold mb-6">Home</h1>
          <nav className="space-y-4">
            <div>
              <h2 className="text-gray-400">Cadastrar/Editar:</h2>
              <ul className="ml-2 space-y-2">
                <li>Empresas</li>
                <li>Gerente</li>
                <li>Funcionários</li>
                <li>Cliente Final</li>
              </ul>
            </div>
            <div>
              <h2 className="text-gray-400">Produtos & Serviços Empresas:</h2>
              <ul className="ml-2 space-y-2">
                <li>Cadastrar</li>
                <li>Editar</li>
              </ul>
            </div>
            <div>
              <h2 className="text-gray-400">Chamados:</h2>
              <ul className="ml-2 space-y-2">
                <li>Atribuir Chamado</li>
                <li>Feedbacks</li>
                <li>Abrir/Editar</li>
                <li>Priorizar</li>
                <li>Histórico</li>
                <li>Abertos/Finalizados cliente view</li>
              </ul>
            </div>
            <div>
              <h2 className="text-gray-400">Relatórios:</h2>
              <ul className="ml-2 space-y-2">
                <li>Feedbacks</li>
              </ul>
            </div>
            <div>
              <h2 className="text-gray-400">Chat AI:</h2>
              <ul className="ml-2 space-y-2">
                <li>Perguntar</li>
                <li>Conversas</li>
                <li>Histórico Chat AI</li>
              </ul>
            </div>
          </nav>
        </aside>

        {/* Main Content */}
        <main className="flex-1 bg-gray-100 p-10 overflow-auto">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="bg-white shadow-lg p-6 rounded-xl text-center">
              <h3 className="text-lg font-bold">Produtos e Serviços</h3>
              <p className="text-gray-600">Gerencie seus produtos e serviços.</p>
            </div>
            <div className="bg-white shadow-lg p-6 rounded-xl text-center">
              <h3 className="text-lg font-bold">Funcionários</h3>
              <p className="text-gray-600">Crie, edite ou exclua logins dos funcionários.</p>
            </div>
            <div className="bg-white shadow-lg p-6 rounded-xl text-center">
              <h3 className="text-lg font-bold">Relatórios</h3>
              <p className="text-gray-600">Acompanhe o desempenho e histórico de interações.</p>
            </div>
          </div>

          <div className="mt-8 flex flex-wrap justify-center gap-4">
            <button className="bg-blue-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-blue-600 transition">
              + Criar Produto ou Serviço
            </button>
            <button className="bg-blue-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-blue-600 transition">
              + Adicionar Funcionário
            </button>
            <button className="bg-blue-500 text-white px-6 py-3 rounded-lg shadow-md hover:bg-blue-600 transition">
              + Configurar Prioridades
            </button>
          </div>
        </main>
      </div>

      {/* Footer */}
      <footer className="bg-gray-800 text-white text-center py-3 text-sm">
        © 2025 SupportFlow - Todos os direitos reservados.
      </footer>
    </div>
  );
}

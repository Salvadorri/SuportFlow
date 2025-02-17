import React, { useState } from 'react';

const index = () => {
  // Sample data with added dates
  const initialTickets = [
    { 
      id: "#101", 
      title: "Erro no login", 
      status: "Em andamento", 
      priority: "Alta", 
      color: "text-yellow-500",
      date: "2025-02-06"
    },
    { 
      id: "#102", 
      title: "Problema de pagamento", 
      status: "Resolvido", 
      priority: "Média", 
      color: "text-green-500",
      date: "2025-02-05"
    },
    { 
      id: "#103", 
      title: "Erro na integração", 
      status: "Em andamento", 
      priority: "Alta", 
      color: "text-yellow-500",
      date: "2025-02-06"
    }
  ];

  const [tickets, setTickets] = useState(initialTickets);
  const [sortConfig, setSortConfig] = useState({ key: 'date', direction: 'desc' });

  // Sorting function
  const sortTickets = (key) => {
    let direction = 'asc';
    if (sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });

    const sortedTickets = [...tickets].sort((a, b) => {
      if (key === 'priority') {
        // Sort by priority (Alta > Média > Baixa)
        const priorityOrder = { 'Alta': 3, 'Média': 2, 'Baixa': 1 };
        return direction === 'asc' 
          ? priorityOrder[a[key]] - priorityOrder[b[key]]
          : priorityOrder[b[key]] - priorityOrder[a[key]];
      } else {
        // Sort by date or other fields
        return direction === 'asc'
          ? a[key].localeCompare(b[key])
          : b[key].localeCompare(a[key]);
      }
    });

    setTickets(sortedTickets);
  };

  return (
    <div className="flex h-screen">
      {/* Sidebar remains the same */}
      <aside className="w-64 bg-gray-900 text-white p-5">
        <h1 className="text-2xl font-bold mb-6">Support Flow.AI</h1>
        <nav>
          <ul>
            <li className="mb-4 font-semibold">Chamados:</li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Abrir/Editar</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Priorizar</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Histórico</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Feedbacks</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Atribuir para atendente</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Chat Cliente</a></li>
          </ul>
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-6">
        <h2 className="text-3xl font-semibold mb-6">Dashboard de Chamados</h2>

        {/* Cards section remains the same */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
          {["Novos Chamados", "Em Andamento", "Histórico"].map((title, index) => (
            <div key={index} className="bg-white p-6 rounded-lg shadow">
              <h3 className="text-lg font-semibold">{title}</h3>
              <p className="text-gray-600 mb-4">
                {index === 0 ? "Gerencie os novos tickets" : index === 1 ? "Acompanhe chamados ativos" : "Visualize chamados fechados"}
              </p>
              <button className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                {index === 0 ? "+ Criar Novo Chamado" : index === 1 ? "Ver Em Andamento" : "Ver Histórico"}
              </button>
            </div>
          ))}
        </div>

        {/* Statistics section remains the same */}
        <div className="bg-white p-6 rounded-lg shadow mb-6">
          <h3 className="text-lg font-semibold mb-4">Estatísticas Chamados</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {["Chamados Abertos", "Em Andamento", "Resolvidos Hoje"].map((stat, index) => (
              <div key={index} className="p-4 bg-gray-100 rounded">
                <h4 className="text-gray-600">{stat}</h4>
                <p className="text-2xl font-bold">{[24, 16, 8][index]}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Modified Tickets List */}
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-lg font-semibold mb-4">Lista de Chamados</h3>
          <table className="w-full border-collapse border border-gray-200">
            <thead>
              <tr className="bg-gray-100">
                <th className="border p-2">ID</th>
                <th className="border p-2">Título</th>
                <th className="border p-2">Status</th>
                <th 
                  className="border p-2 cursor-pointer hover:bg-gray-200"
                  onClick={() => sortTickets('priority')}
                >
                  Prioridade {sortConfig.key === 'priority' && (sortConfig.direction === 'asc' ? '↑' : '↓')}
                </th>
                <th 
                  className="border p-2 cursor-pointer hover:bg-gray-200"
                  onClick={() => sortTickets('date')}
                >
                  Data de Abertura {sortConfig.key === 'date' && (sortConfig.direction === 'asc' ? '↑' : '↓')}
                </th>
              </tr>
            </thead>
            <tbody>
              {tickets.map((ticket, index) => (
                <tr key={index}>
                  <td className="border p-2"> 
                    <button className="text-blue-600 hover:underline Chamado-button">{ticket.id}</button>
                  </td>
                  <td className="border p-2">
                    <button className="text-blue-600 hover:underline Chamado-button">{ticket.title}</button>
                  </td>
                  <td className={`border p-2 ${ticket.color}`}>{ticket.status}</td>
                  <td className="border p-2 text-orange-500">{ticket.priority}</td>
                  <td className="border p-2">{new Date(ticket.date).toLocaleDateString('pt-BR')}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </main>
    </div>
  );
};

export default index;
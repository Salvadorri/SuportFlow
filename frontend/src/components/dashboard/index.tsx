// src/components/dashboard/chamados.tsx
import React, { useState } from "react";
import { Link } from "@tanstack/react-router";

// Best Practice: Define an interface for your Ticket data
interface Ticket {
  id: string;
  title: string;
  status: string;
  priority: string;
  date: string;
  color: string;
}

const Chamados: React.FC = () => {
  // Sample data with added dates (using the Ticket interface)
  const initialTickets: Ticket[] = [
    {
      id: "#101",
      title: "Erro no login",
      status: "Em andamento",
      priority: "Alta",
      color: "text-yellow-500",
      date: "2025-02-06",
    },
    {
      id: "#102",
      title: "Problema de pagamento",
      status: "Resolvido",
      priority: "Média",
      color: "text-green-500",
      date: "2025-02-05",
    },
    {
      id: "#103",
      title: "Erro na integração",
      status: "Em andamento",
      priority: "Alta",
      color: "text-yellow-500",
      date: "2025-02-06",
    },
  ];

  const [tickets, setTickets] = useState<Ticket[]>(initialTickets);
  const [sortConfig, setSortConfig] = useState<{ key: keyof Ticket | null; direction: 'asc' | 'desc' }>({ key: 'date', direction: 'desc' }); // Initialize with null or a valid key


  const menuItems = [
    { label: "Abrir Chamado", href: "/criar-chamado" },
    { label: "Editar/Priorizar/Atribuir", href: "/editar-chamado" },
    { label: "Histórico Chamados", href: "/chamados-historico" },
    { label: "Chat Clientes", href: "/meuschatsclientes" },
    { label: "Chat", href: "/chatchamado" }

  ];

  const cardItems = [
    {
      title: "Novos Chamados",
      description: "Gerencie os novos tickets",
      buttonText: "+ Criar Novo Chamado",
      href: "/criar-chamado"
    },
    {
      title: "Em Andamento",
      description: "Acompanhe chamados ativos",
      buttonText: "Ver Em Andamento",
      href: "#" //  Keep as # or replace with a suitable route if needed
    },
    {
      title: "Histórico",
      description: "Visualize chamados fechados",
      buttonText: "Ver Histórico",
      href: "/chamados-historico"
    },
  ];

  const statItems = [
    { label: "Chamados Abertos", value: 24 },
    { label: "Em Andamento", value: 16 },
    { label: "Resolvidos Hoje", value: 8 },
  ];


  // Sorting function (with keyof Ticket for type safety)
  const sortTickets = (key: keyof Ticket) => {
    let direction: 'asc' | 'desc' = "asc";  // Explicitly type direction
    if (sortConfig.key === key && sortConfig.direction === "asc") {
      direction = "desc";
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
        // Sort by date or other fields (using localeCompare for strings)
        // Check if the values are strings before calling localeCompare
        const aValue = a[key];
        const bValue = b[key];

        if (typeof aValue === 'string' && typeof bValue === 'string') {
            return direction === 'asc'
              ? aValue.localeCompare(bValue)
              : bValue.localeCompare(aValue);
        } else {
          // Handle cases where values might not be strings (optional, depends on your data)
            return 0; // Or some other default comparison
        }
      }
    });

    setTickets(sortedTickets);
  };


  return (
    <div className="flex h-screen">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white p-5">
        <h1 className="text-2xl font-bold mb-6">Support Flow.AI</h1>
        <nav>
          <ul>
            <li className="mb-4 font-semibold"> Chamados:</li>
            {menuItems.map((item) => (
              <li
                key={item.label}
                className="mb-2 hover:bg-gray-700 p-2 rounded"
              >
                <Link to={item.href} className="text-blue-400 block w-full h-full">
                  {item.label}
                </Link>
              </li>
            ))}
          </ul>
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-6">
        <h2 className="text-3xl font-semibold mb-6">Dashboard de Chamados</h2>

        {/* Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
          {cardItems.map((card) => (
            <div key={card.title} className="bg-white p-6 rounded-lg shadow">
              <h3 className="text-lg font-semibold">{card.title}</h3>
              <p className="text-gray-600 mb-4">{card.description}</p>
              <Link to={card.href}>
                <button className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                  {card.buttonText}
                </button>
              </Link>
            </div>
          ))}
        </div>

        {/* Estatísticas */}
        <div className="bg-white p-6 rounded-lg shadow mb-6">
          <h3 className="text-lg font-semibold mb-4">
            Estatísticas de Chamados
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {statItems.map((stat) => (
              <div key={stat.label} className="p-4 bg-gray-100 rounded">
                <h4 className="text-gray-600">{stat.label}</h4>
                <p className="text-2xl font-bold">{stat.value}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Tabela de Chamados */}
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
              {tickets.map((ticket) => (
                <tr key={ticket.id}>
                  <td className="border p-2">
                    <Link to={`/chamado/${ticket.id}`}><button className="text-blue-600 hover:underline Chamado-button">{ticket.id}</button></Link>
                  </td>
                  <td className="border p-2">
                    <Link to={`/chamado/${ticket.id}`}><button className="text-blue-600 hover:underline Chamado-button">{ticket.title}</button></Link>
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

export default Chamados;
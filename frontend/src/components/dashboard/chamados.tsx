import React from "react";

const Chamados: React.FC = () => {
  const menuItems = [
    { label: "Abrir/Editar", href: "#" },
    { label: "Priorizar", href: "#" },
    { label: "Histórico", href: "#" },
    { label: "Feedbacks", href: "#" },
    { label: "Atribuir para atendente", href: "#" },
  ];

  const cardItems = [
    {
      title: "Novos Chamados",
      description: "Gerencie os novos tickets",
      buttonText: "+ Criar Novo Chamado",
    },
    {
      title: "Em Andamento",
      description: "Acompanhe chamados ativos",
      buttonText: "Ver Em Andamento",
    },
    {
      title: "Histórico",
      description: "Visualize chamados fechados",
      buttonText: "Ver Histórico",
    },
  ];

  const statItems = [
    { label: "Chamados Abertos", value: 24 },
    { label: "Em Andamento", value: 16 },
    { label: "Resolvidos Hoje", value: 8 },
  ];

  const ticketItems = [
    {
      id: "#101",
      title: "Erro no login",
      status: "Em andamento",
      priority: "Alta",
      color: "text-yellow-500",
    },
    {
      id: "#102",
      title: "Problema de pagamento",
      status: "Resolvido",
      priority: "Média",
      color: "text-green-500",
    },
  ];

  return (
    <div className="flex h-screen">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white p-5">
        <h1 className="text-0xl font-bold mb-6">Support Flow.AI</h1>
        <nav>
          <ul>
            <li className="mb-4 font-semibold"> Chamados:</li>
            {menuItems.map((item) => (
              <li
                key={item.label}
                className="mb-2 hover:bg-gray-700 p-2 rounded"
              >
                <a href={item.href}>{item.label}</a>
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
              <button className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                {card.buttonText}
              </button>
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
                {["ID", "Título", "Status", "Prioridade", "Ações"].map(
                  (header) => (
                    <th key={header} className="border p-2">
                      {header}
                    </th>
                  )
                )}
              </tr>
            </thead>
            <tbody>
              {ticketItems.map((ticket) => (
                <tr key={ticket.id}>
                  <td className="border p-2">{ticket.id}</td>
                  <td className="border p-2">{ticket.title}</td>
                  <td className={`border p-2 ${ticket.color}`}>
                    {ticket.status}
                  </td>
                  <td className="border p-2 text-orange-500">
                    {ticket.priority}
                  </td>
                  <td className="border p-2">
                    <button className="text-blue-600">Ver</button>
                  </td>
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

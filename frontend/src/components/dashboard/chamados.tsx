const Chamados = () => {
  return (
    <div className="flex h-screen">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white p-5">
        <h1 className="text-0xl font-bold mb-6">Support Flow.AI</h1>
        <nav>
          <ul>
            <li className="mb-4 font-semibold"> Chamados:</li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Abrir/Editar</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Priorizar</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Histórico</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Feedbacks</a></li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#">Atribuir para atendente</a></li>
          </ul>
        </nav>
      </aside>
      {/* Main Content */}
      <main className="flex-1 p-6">
        <h2 className="text-3xl font-semibold mb-6">Dashboard de Chamados</h2>
        {/* Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
          {["Novos Chamados", "Em Andamento", "Histórico"].map((title, index) => (
            <div key={index} className="bg-white p-6 rounded-lg shadow">
              <h3 className="text-lg font-semibold">{title}</h3>
              <p className="text-gray-600 mb-4">{index === 0 ? "Gerencie os novos tickets" : index === 1 ? "Acompanhe chamados ativos" : "Visualize chamados fechados"}</p>
              <button className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                {index === 0 ? "+ Criar Novo Chamado" : index === 1 ? "Ver Em Andamento" : "Ver Histórico"}
              </button>
            </div>
          ))}
        </div>
        {/* Estatísticas */}
        <div className="bg-white p-6 rounded-lg shadow mb-6">
          <h3 className="text-lg font-semibold mb-4">Estatísticas de Chamados</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {["Chamados Abertos", "Em Andamento", "Resolvidos Hoje"].map((stat, index) => (
              <div key={index} className="p-4 bg-gray-100 rounded">
                <h4 className="text-gray-600">{stat}</h4>
                <p className="text-2xl font-bold">{[24, 16, 8][index]}</p>
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
                {["ID", "Título", "Status", "Prioridade", "Ações"].map((header, index) => (
                  <th key={index} className="border p-2">{header}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {[{ id: "#101", title: "Erro no login", status: "Em andamento", priority: "Alta", color: "text-yellow-500" },
                { id: "#102", title: "Problema de pagamento", status: "Resolvido", priority: "Média", color: "text-green-500" }]
                .map((ticket, index) => (
                  <tr key={index}>
                    <td className="border p-2">{ticket.id}</td>
                    <td className="border p-2">{ticket.title}</td>
                    <td className={`border p-2 ${ticket.color}`}>{ticket.status}</td>
                    <td className="border p-2 text-orange-500">{ticket.priority}</td>
                    <td className="border p-2"><button className="text-blue-600">Ver</button></td>
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

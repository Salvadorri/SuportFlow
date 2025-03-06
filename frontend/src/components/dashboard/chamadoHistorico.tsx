import { useState } from "react";
import { Link } from "@tanstack/react-router";

export default function HistoricoChamados() {
  const [chamados, setChamados] = useState([
    { id: 101, cliente: "Empresa A", titulo: "Erro no login", categoria: "Bug", status: "Resolvido", prioridade: "Alta", abertura: "05/02/2025", fechamento: "06/02/2025" },
    { id: 102, cliente: "Empresa B", titulo: "Problema de pagamento", categoria: "Financeiro", status: "Atrasado", prioridade: "Média", abertura: "04/02/2025", fechamento: "05/02/2025" },
    { id: 103, cliente: "Empresa C", titulo: "Bug na interface", categoria: "Bug", status: "Resolvido", prioridade: "Baixa", abertura: "03/02/2025", fechamento: "04/02/2025" },
  ]);

  const [sortKey, setSortKey] = useState<string | null>(null);
  const [sortOrder, setSortOrder] = useState("asc");
  const [searchTerm, setSearchTerm] = useState("");

  const prioridadeOrdenacao: { [key: string]: number } = { "Alta": 3, "Média": 2, "Baixa": 1 };

  const handleSort = (key: string) => {
    const order = sortKey === key && sortOrder === "asc" ? "desc" : "asc";
    setSortKey(key);
    setSortOrder(order);

    const sortedChamados = [...chamados].sort((a, b) => {
      let valA = key === "prioridade" ? prioridadeOrdenacao[a[key]] : a[key];
      let valB = key === "prioridade" ? prioridadeOrdenacao[b[key]] : b[key];
      
      if (valA < valB) return order === "asc" ? -1 : 1;
      if (valA > valB) return order === "asc" ? 1 : -1;
      return 0;
    });

    setChamados(sortedChamados);
  };

  const filteredChamados = chamados.filter(chamado => 
    chamado.cliente.toLowerCase().includes(searchTerm.toLowerCase()) ||
    chamado.titulo.toLowerCase().includes(searchTerm.toLowerCase()) ||
    chamado.categoria.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <div className="w-64 bg-gray-900 text-white p-6">
        <h2 className="text-xl font-bold mb-4">Support Flow.AI</h2>
        <nav>
          <ul>
          <li className="mb-2 hover:bg-gray-700 p-2 rounded">
  <Link to="/dashboard" className="text-blue-400 block w-full h-full">
    Dashboard
  </Link>
</li>
<li className="mb-2 hover:bg-gray-700 p-2 rounded"><a href="#" className="text-blue-400">Meus Chamados</a>
            </li>
          </ul>
        </nav>
      </div>
      {/* Content */}
      <div className="p-6 bg-gray-100 flex-1">
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-2xl font-bold">Histórico de Chamados</h1>
          <input
            type="text"
            placeholder="Pesquisar..."
            className="p-2 border rounded"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div className="bg-white shadow-md rounded-lg overflow-hidden">
          <table className="w-full border-collapse">
            <thead>
              <tr className="bg-gray-200 text-left">
                <th className="p-3 cursor-pointer" onClick={() => handleSort("id")}>ID</th>
                <th className="p-3 cursor-pointer" onClick={() => handleSort("cliente")}>Cliente</th>
                <th className="p-3">Título</th>
                <th className="p-3 cursor-pointer" onClick={() => handleSort("categoria")}>Categoria</th>
                <th className="p-3 cursor-pointer" onClick={() => handleSort("status")}>Status</th>
                <th className="p-3 cursor-pointer" onClick={() => handleSort("prioridade")}>Prioridade</th>
                <th className="p-3 cursor-pointer" onClick={() => handleSort("abertura")}>Data de Abertura</th>
                <th className="p-3 cursor-pointer" onClick={() => handleSort("fechamento")}>Data de Fechamento</th>
              </tr>
            </thead>
            <tbody>
              {filteredChamados.map((chamado) => (
                <tr key={chamado.id} className="border-b hover:bg-gray-100">
                  <td className="p-3 text-blue-600 cursor-pointer">#{chamado.id}</td>
                  <td className="p-3">{chamado.cliente}</td>
                  <td className="p-3 text-blue-600 cursor-pointer">{chamado.titulo}</td>
                  <td className="p-3">{chamado.categoria}</td>
                  <td className="p-3 text-green-600">{chamado.status}</td>
                  <td className={`p-3 ${chamado.prioridade === "Alta" ? "text-red-600" : chamado.prioridade === "Média" ? "text-yellow-600" : "text-green-600"}`}>{chamado.prioridade}</td>
                  <td className="p-3">{chamado.abertura}</td>
                  <td className="p-3">{chamado.fechamento}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
import { useState } from "react";
import { Link } from "@tanstack/react-router";
import logo from "../../assets/logo.png";

const menuItems = [
  { label: "Abrir Chamado", href: "/criar-chamado" },
  { label: "Histórico Chamados", href: "/chamados-historico" },
  { label: "Chat", href: "/chatchamadodash" }
];

export default function HistoricoChamados() {
  const [chamados, setChamados] = useState([
    { 
      id: 101, 
      cliente: "Empresa A", 
      titulo: "Erro no login", 
      descricao: "Usuário não consegue acessar o sistema após atualização",
      categoria: "Bug", 
      status: "Em Andamento", 
      prioridade: "Alta", 
      abertura: "05/02/2025", 
      fechamento: "", 
      dataVencimento: "12/02/2025",
      atribuidoPara: "João Silva",
      historico: [
        { data: "05/02/2025 14:30", autor: "Sistema", mensagem: "Chamado criado" },
        { data: "05/02/2025 15:45", autor: "João Silva", mensagem: "Iniciada análise do problema" },
        { data: "06/02/2025 10:20", autor: "João Silva", mensagem: "Identificada causa do erro" }
      ]
    },
    { 
      id: 102, 
      cliente: "Empresa B", 
      titulo: "Problema de pagamento", 
      descricao: "Cliente reportou falha no processamento de pagamento por cartão",
      categoria: "Financeiro", 
      status: "Atrasado", 
      prioridade: "Média", 
      abertura: "04/02/2025", 
      fechamento: "", 
      dataVencimento: "11/02/2025",
      atribuidoPara: "Maria Santos",
      historico: [
        { data: "04/02/2025 09:15", autor: "Sistema", mensagem: "Chamado criado" },
        { data: "04/02/2025 10:30", autor: "Maria Santos", mensagem: "Contatado cliente para mais informações" }
      ]
    },
    { 
      id: 103, 
      cliente: "Empresa C", 
      titulo: "Bug na interface", 
      descricao: "Botões da interface não respondem em navegadores Firefox",
      categoria: "Bug", 
      status: "Resolvido", 
      prioridade: "Baixa", 
      abertura: "03/02/2025", 
      fechamento: "04/02/2025", 
      dataVencimento: "10/02/2025",
      atribuidoPara: "Pedro Costa",
      historico: [
        { data: "03/02/2025 13:10", autor: "Sistema", mensagem: "Chamado criado" },
        { data: "03/02/2025 14:25", autor: "Pedro Costa", mensagem: "Problema identificado" },
        { data: "04/02/2025 09:45", autor: "Pedro Costa", mensagem: "Correção implementada e testada" },
        { data: "04/02/2025 10:30", autor: "Sistema", mensagem: "Chamado resolvido" }
      ]
    },
  ]);

  const [sortKey, setSortKey] = useState<string | null>(null);
  const [sortOrder, setSortOrder] = useState("asc");
  const [searchTerm, setSearchTerm] = useState("");
  const [chamadoSelecionado, setChamadoSelecionado] = useState<number | null>(null);
  const [editMode, setEditMode] = useState(false);
  const [novoComentario, setNovoComentario] = useState("");

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

  const handleVisualizarChamado = (id: number) => {
    setChamadoSelecionado(id);
    setEditMode(false);
  };

  const handleVoltar = () => {
    setChamadoSelecionado(null);
    setEditMode(false);
  };

  const handleSave = () => {
    setEditMode(false);
    // Aqui poderia ter uma lógica para salvar as alterações em um servidor
  };

  const handleAddComentario = () => {
    if (novoComentario.trim() && chamadoSelecionado !== null) {
      const novoHistorico = {
        data: new Date().toLocaleString(),
        autor: "Usuário Atual",
        mensagem: novoComentario
      };
      
      setChamados(prevChamados => 
        prevChamados.map(chamado => 
          chamado.id === chamadoSelecionado 
            ? {...chamado, historico: [...chamado.historico, novoHistorico]} 
            : chamado
        )
      );
      
      setNovoComentario("");
    }
  };

  const atualizarChamado = (id: number, dados: any) => {
    setChamados(prevChamados => 
      prevChamados.map(chamado => 
        chamado.id === id ? {...chamado, ...dados} : chamado
      )
    );
  };

  const chamadoAtual = chamados.find(chamado => chamado.id === chamadoSelecionado);

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white p-5">
        <div className="flex items-center mb-6">
          <img src={logo} alt="SupportFlow Logo" className="h-14 w-auto mr-2"/>
          <h1 className="text-2xl font-bold">SupportFlow</h1>
        </div>
        <nav>
          <ul>
            {menuItems.map((item) => (
              <li
                key={item.label}
                className="mb-2 hover:bg-green-700 p-2 rounded"
              >
                <Link to={item.href} className="text-white hover:text-white block w-full h-full">
                  {item.label}
                </Link>
              </li>
            ))}
          </ul>
        </nav>
      </aside>
      
      {/* Content */}
      <div className="p-6 bg-gray-100 flex-1">
        {chamadoSelecionado === null ? (
          // Lista de chamados
          <>
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
                      <td className="p-3 text-blue-600 cursor-pointer" onClick={() => handleVisualizarChamado(chamado.id)}>
                        #{chamado.id}
                      </td>
                      <td className="p-3">{chamado.cliente}</td>
                      <td className="p-3 text-blue-600 cursor-pointer" onClick={() => handleVisualizarChamado(chamado.id)}>
                        {chamado.titulo}
                      </td>
                      <td className="p-3">{chamado.categoria}</td>
                      <td className={`p-3 ${
                        chamado.status === "Resolvido" ? "text-green-600" : 
                        chamado.status === "Atrasado" ? "text-red-600" : 
                        "text-yellow-600"
                      }`}>
                        {chamado.status}
                      </td>
                      <td className={`p-3 ${
                        chamado.prioridade === "Alta" ? "text-red-600" : 
                        chamado.prioridade === "Média" ? "text-yellow-600" : 
                        "text-green-600"
                      }`}>
                        {chamado.prioridade}
                      </td>
                      <td className="p-3">{chamado.abertura}</td>
                      <td className="p-3">{chamado.fechamento}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        ) : (
          // Visualização detalhada do chamado
          <div className="bg-white shadow-md rounded-lg p-6">
            {/* Header */}
            <div className="flex justify-between items-center mb-6">
              <div className="flex items-center">
                <button 
                  onClick={handleVoltar}
                  className="mr-4 bg-gray-200 hover:bg-gray-300 p-2 rounded"
                >
                  ← Voltar
                </button>
                <h1 className="text-2xl font-bold">Chamado #{chamadoAtual?.id}</h1>
              </div>
              <div className="space-x-2">
                {editMode ? (
                  <>
                    <button 
                      onClick={handleSave}
                      className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                    >
                      Salvar
                    </button>
                    <button 
                      onClick={() => setEditMode(false)}
                      className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                    >
                      Cancelar
                    </button>
                  </>
                ) : (
                  <button 
                    onClick={() => setEditMode(true)}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                  >
                    Editar
                  </button>
                )}
              </div>
            </div>

            {/* Main Info */}
            {chamadoAtual && (
              <>
                <div className="grid grid-cols-2 gap-6 mb-6">
                  <div>
                    <div className="mb-4">
                      <label className="block text-sm font-medium text-gray-700 mb-1">Título</label>
                      {editMode ? (
                        <input
                          type="text"
                          value={chamadoAtual.titulo}
                          onChange={(e) => atualizarChamado(chamadoAtual.id, {titulo: e.target.value})}
                          className="w-full p-2 border rounded"
                        />
                      ) : (
                        <p className="text-gray-900">{chamadoAtual.titulo}</p>
                      )}
                    </div>

                    <div className="mb-4">
                      <label className="block text-sm font-medium text-gray-700 mb-1">Cliente</label>
                      <p className="text-gray-900">{chamadoAtual.cliente}</p>
                    </div>

                    <div className="mb-4">
                      <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
                      {editMode ? (
                        <select 
                          value={chamadoAtual.status}
                          onChange={(e) => atualizarChamado(chamadoAtual.id, {status: e.target.value})}
                          className="w-full p-2 border rounded"
                        >
                          <option>Novo</option>
                          <option>Em Andamento</option>
                          <option>Aguardando Cliente</option>
                          <option>Atrasado</option>
                          <option>Resolvido</option>
                          <option>Fechado</option>
                        </select>
                      ) : (
                        <p className={`text-gray-900 ${
                          chamadoAtual.status === "Resolvido" ? "text-green-600" : 
                          chamadoAtual.status === "Atrasado" ? "text-red-600" : 
                          "text-yellow-600"
                        }`}>
                          {chamadoAtual.status}
                        </p>
                      )}
                    </div>
                  </div>

                  <div>
                    <div className="mb-4">
                      <label className="block text-sm font-medium text-gray-700 mb-1">Prioridade</label>
                      {editMode ? (
                        <select 
                          value={chamadoAtual.prioridade}
                          onChange={(e) => atualizarChamado(chamadoAtual.id, {prioridade: e.target.value})}
                          className="w-full p-2 border rounded"
                        >
                          <option>Baixa</option>
                          <option>Média</option>
                          <option>Alta</option>
                        </select>
                      ) : (
                        <p className={`text-gray-900 ${
                          chamadoAtual.prioridade === "Alta" ? "text-red-600" : 
                          chamadoAtual.prioridade === "Média" ? "text-yellow-600" : 
                          "text-green-600"
                        }`}>
                          {chamadoAtual.prioridade}
                        </p>
                      )}
                    </div>

                    <div className="mb-4">
                      <label className="block text-sm font-medium text-gray-700 mb-1">Categoria</label>
                      {editMode ? (
                        <select 
                          value={chamadoAtual.categoria}
                          onChange={(e) => atualizarChamado(chamadoAtual.id, {categoria: e.target.value})}
                          className="w-full p-2 border rounded"
                        >
                          <option>Bug</option>
                          <option>Suporte Técnico</option>
                          <option>Financeiro</option>
                          <option>Dúvida</option>
                        </select>
                      ) : (
                        <p className="text-gray-900">{chamadoAtual.categoria}</p>
                      )}
                    </div>

                    <div className="mb-4">
                      <label className="block text-sm font-medium text-gray-700 mb-1">Atribuído para</label>
                      {editMode ? (
                        <input
                          type="text"
                          value={chamadoAtual.atribuidoPara}
                          onChange={(e) => atualizarChamado(chamadoAtual.id, {atribuidoPara: e.target.value})}
                          className="w-full p-2 border rounded"
                        />
                      ) : (
                        <p className="text-gray-900">{chamadoAtual.atribuidoPara}</p>
                      )}
                    </div>
                  </div>
                </div>

                {/* Descrição */}
                <div className="mb-6">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
                  {editMode ? (
                    <textarea
                      value={chamadoAtual.descricao}
                      onChange={(e) => atualizarChamado(chamadoAtual.id, {descricao: e.target.value})}
                      className="w-full p-2 border rounded h-32"
                    />
                  ) : (
                    <p className="text-gray-900 whitespace-pre-wrap">{chamadoAtual.descricao}</p>
                  )}
                </div>

                {/* Histórico e Comentários */}
                <div className="mt-8">
                  <h2 className="text-xl font-bold mb-4">Histórico de Resolução</h2>
                  <div className="space-y-4 mb-4">
                    {chamadoAtual.historico.map((item, index) => (
                      <div key={index} className="bg-gray-50 p-4 rounded">
                        <div className="flex justify-between text-sm text-gray-500 mb-1">
                          <span>{item.autor}</span>
                          <span>{item.data}</span>
                        </div>
                        <p className="text-gray-900">{item.mensagem}</p>
                      </div>
                    ))}
                  </div>

                  {/* Novo Comentário */}
                  <div className="mt-4">
                    <textarea
                      value={novoComentario}
                      onChange={(e) => setNovoComentario(e.target.value)}
                      placeholder="Adicione um comentário..."
                      className="w-full p-2 border rounded h-24 mb-2"
                    />
                    <button
                      onClick={handleAddComentario}
                      className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                    >
                      Adicionar Comentário
                    </button>
                  </div>
                </div>
              </>
            )}
          </div>
        )}
      </div>
    </div>
  );
}